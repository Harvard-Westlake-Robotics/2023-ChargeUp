package frc.robot.Drive.Auto;

import frc.robot.Drive.Auto.Movements.Movement;
import frc.robot.Drive.Components.GearShifter;
import frc.robot.Util.AngleMath;
import frc.robot.Util.Container;
import frc.robot.Util.DeSpam;
import frc.robot.Util.Pair;
import frc.robot.Util.Promise;
import frc.robot.Util.ScaleInput;
import frc.robot.Util.Tickable;
import frc.robot.Core.Scheduler;
import frc.robot.Devices.Imu;

public class AutonomousDrive implements Tickable {
    DriveSidePD left;
    DriveSidePD right;
    GearShifter shifter;
    Scheduler scheduler;
    Imu imu;

    public AutonomousDrive(Scheduler scheduler, DriveSidePD left, DriveSidePD right, GearShifter shifter, Imu imu) {
        this.left = left;
        this.right = right;
        this.shifter = shifter;
        this.imu = imu;
        this.scheduler = scheduler;
    }

    Movement movement;
    double secondsIntoMovement;
    double debt; // seconds
    Pair<Double> initialTargets;
    Tickable tickable;

    public void setMovement(Movement movement) {
        this.secondsIntoMovement = 0;
        this.debt = 0;
        initialTargets = new Pair<Double>(left.getTarget(), right.getTarget());
        if (movement == null) {
            this.movement = null;
        } else {
            this.movement = movement;
        }
    }

    public void tick(double dTime) {
        if (movement != null) { // runs the active `Movement`
            var currentCorrections = new Pair<Double>(left.getCorrection(shifter.getState()),
                    right.getCorrection(shifter.getState()));
            if (secondsIntoMovement + dTime > movement.getDuration()) {
                var netMovement = movement.getTotalDistance();
                left.setTarget(initialTargets.left + netMovement.left);
                right.setTarget(initialTargets.right + netMovement.right);
                movement = null;
            }
            // only skips to next tick if the robot can debt and the robot is currently
            // applying the maximum correction
            else if (!(movement.canDebt(dTime)
                    && Math.max(
                            Math.abs(currentCorrections.left),
                            Math.abs(currentCorrections.right)) > 100)) {
                secondsIntoMovement += dTime;
                var dTarget = movement.sample(secondsIntoMovement).map((Double e) -> e * dTime);
                left.incrementTarget(dTarget.left);
                right.incrementTarget(dTarget.right);
            }
        }
        { // Ticks DriveSides
          // scales the corrections to ([100, -100], [100, -100])
            Pair<Double> voltages = ScaleInput.normalize(
                    new Pair<Double>(left.getCorrection(shifter.getState()), right.getCorrection(shifter.getState())));
            left.setPercentVoltage(voltages.left);
            right.setPercentVoltage(voltages.right);
        }
    }

    DeSpam dSpam = new DeSpam(0.8);

    public Promise goFor(double inches, double velocity, double max_acc) {
        var distSoFar = new Container<Double>(0.0);
        var prom = new Promise();
        var currVelocity = new Container<Double>(0.0);
        var cancel = scheduler.registerTick((dTime) -> {
            if (Math.abs(distSoFar.val) < (Math.pow(velocity, 2) / (2 * max_acc)))
                currVelocity.val += dTime * max_acc;

            double decelStart = Math.abs(inches) - (Math.pow(velocity, 2) / (2 * max_acc));
            if (Math.abs(distSoFar.val) > decelStart + 0.1)
                currVelocity.val -= max_acc * dTime;

            if (currVelocity.val < 0)
                currVelocity.val = 0.0;

            if (Math.abs(inches) <= Math.abs(distSoFar.val)) {
                prom.resolve();
            }
            double distTick = dTime * currVelocity.val * Math.signum(inches);

            dSpam.exec(() -> {
                System.out.println(
                        "currvel: " + currVelocity.val + " decelstart: " + decelStart + " distSoFar: " + distSoFar.val);
            });

            left.incrementTarget(distTick);
            right.incrementTarget(distTick);
            distSoFar.val += distTick;
        });
        prom.then(() -> {
            cancel.run();
        });
        return prom;
    }

    public Promise turnFor(double degrees, double velocity, double max_acc) {
        var distSoFar = new Container<Double>(0.0);
        double startDeg = imu.getRotation() + (left.error);
        var prom = new Promise();
        var cancel = scheduler.registerTick((dTime) -> { 
            if (Math.abs(AngleMath.getDelta(startDeg, imu.getRotation() + ((Math.abs(left.error) + Math.abs(right.error)) / (24.0 * Math.PI / 360.0)) * Math.signum(degrees))) > Math.abs(degrees)) {
                prom.resolve();
            }
            double distTick = dTime * velocity * Math.signum(degrees);

            // dSpam.exec(() -> {
            // System.out.println(
            // "currvel: " + currVelocity.val + " decelstart: " + decelStart + " distSoFar:
            // " + distSoFar.val);
            // });

            left.incrementTarget(distTick);
            right.incrementTarget(-distTick);
            distSoFar.val += distTick;
        });
        prom.then(() -> {
            cancel.run();
        });
        return prom;
    }

    public String toString() {
        return "left: " + left + "\nright: " + right;
    }
}