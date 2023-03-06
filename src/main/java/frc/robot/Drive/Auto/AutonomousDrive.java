package frc.robot.Drive.Auto;

import frc.robot.Drive.Auto.Movements.Movement;
import frc.robot.Drive.Components.GearShifter;
import frc.robot.Util.Pair;
import frc.robot.Util.ScaleInput;
import frc.robot.Util.Tickable;

public class AutonomousDrive implements Tickable {
    DriveSidePD left;
    DriveSidePD right;
    GearShifter shifter;

    public AutonomousDrive(DriveSidePD left, DriveSidePD right, GearShifter shifter) {
        this.left = left;
        this.right = right;
        this.shifter = shifter;
    }

    Movement movement;
    double secondsIntoMovement;
    double debt; // seconds
    Pair<Double> initialTargets;

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
            var currentCorrections = new Pair<Double>(left.getCorrection(shifter.getState()), right.getCorrection(shifter.getState()));
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
            Pair<Double> voltages = ScaleInput.normalize(new Pair<Double>(left.getCorrection(shifter.getState()), right.getCorrection(shifter.getState())));
            left.setPercentVoltage(voltages.left);
            right.setPercentVoltage(voltages.right);
        }
    }

    public String toString() {
        return "left: " + left + "\nright: " + right;
    }
}