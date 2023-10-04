package frc.robot.Drive.Auto;

import frc.robot.Drive.Components.GearShifter;
import frc.robot.Util.AngleMath;
import frc.robot.Util.Container;
import frc.robot.Util.Promise;
import frc.robot.Core.Scheduler;
import frc.robot.Devices.Imu;

public class AutonomousDrive {
    DriveSidePD left;
    DriveSidePD right;
    GearShifter shifter;
    Scheduler scheduler;
    Imu imu;

    private void ensureInitTurnTarget() {
        if (turn_target == null)
            turn_target = imu.getRotation();
    }

    public AutonomousDrive(Scheduler scheduler, DriveSidePD left, DriveSidePD right, GearShifter shifter, Imu imu) {
        this.left = left;
        this.right = right;
        this.shifter = shifter;
        this.imu = imu;
        this.scheduler = scheduler;
    }

    public Promise goFor(double inches, double velocity, double max_acc) {
        ensureInitTurnTarget();
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

            left.incrementTarget(distTick);
            right.incrementTarget(distTick);
            distSoFar.val += distTick;
        });
        prom.then(() -> {
            cancel.run();
        });
        return prom;
    }

    Double turn_target = null;

    public Promise turnFor(double degrees, double velocity, double max_acc) {
        ensureInitTurnTarget();
        var distSoFar = new Container<Double>(0.0);
        double startDeg = turn_target;
        var prom = new Promise();
        var cancel = scheduler.registerTick((dTime) -> {
            var unrealizedCorrect = (left.error - right.error) / (26.0 * Math.PI / 360.0);
            var prospective_rotation = unrealizedCorrect + imu.getRotation();
            System.out.println("imu: " + imu.getRotation());
            System.out.println("u correct: " + unrealizedCorrect);
            System.out.println("rot: " + prospective_rotation);
            if (Math.abs(AngleMath.getDelta(startDeg, prospective_rotation)) > Math.abs(degrees)) {
                prom.resolve();
            }
            double distTick = dTime * velocity * Math.signum(degrees);

            left.incrementTarget(distTick);
            right.incrementTarget(-distTick);
            distSoFar.val += distTick;
        });
        prom.then(() -> {
            turn_target += degrees;
            cancel.run();
        });
        return prom;
    }

    public String toString() {
        return "left: " + left + "\nright: " + right;
    }
}