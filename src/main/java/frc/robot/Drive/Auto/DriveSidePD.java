package frc.robot.Drive.Auto;

import frc.robot.Drive.Components.DriveSide;
import frc.robot.Util.PDController;
import frc.robot.Util.Round;

public class DriveSidePD {
    private DriveSide driveSide;
    private PDController lowGearController;
    private PDController highGearController;
    private double target; // inches

    public DriveSidePD(DriveSide driveSide, PDController lowGearController, PDController highGearController) {
        this.lowGearController = lowGearController.clone();
        this.highGearController = highGearController.clone();
        this.lowGearController.reset();
        this.highGearController.reset();

        this.driveSide = driveSide;
        this.driveSide.resetEncoder();

        this.target = driveSide.getPositionInches();
    }

    /**
     * Resets the PID controllers if they haven't been ticked for a while
     * 
     * @param resetHighGear false to reset the low gear and null to reset both
     */
    private void resetPID(Boolean resetHighGear) {
        if (resetHighGear == null) {
            resetPID(true);
            resetPID(false);
        } else {
            if (resetHighGear)
                highGearController.reset();
            else
                lowGearController.reset();
        }
    }

    /**
     * resets the PID controllers, their targets, and the drive's motor encoders
     */
    public void reset() {
        driveSide.resetEncoder();
        target = driveSide.getPositionInches();
        resetPID(null);
    }

    public void incrementTarget(double amount) {
        target += amount;
    }

    // ! strictly for debugging {
    public double errorb;
    public double correct;

    public String toString() {
        return "error: " + Round.rd(errorb) + " correction: " + Round.rd(correct) + " (" + Round.rd(target) + "-"
                + Round.rd(driveSide.getPositionInches())
                + ")";
    }
    // ! }

    public double getCorrection(boolean isLowGear) {
        double error = target - driveSide.getPositionInches();
        this.errorb = error;
        double correction;
        if (isLowGear) {
            highGearController.reset();
            correction = lowGearController.solve(error);
        } else {
            lowGearController.reset();
            correction = highGearController.solve(error);
        }
        return correction;
    }

    public void setPercentVoltage(double percent) {
        driveSide.setPower(percent);
    }

    // !!! DANGER ZONE !!!
    public double getTarget() {
        return target;
    }

    /**
     * You might actually kill someone if you use this method.
     * Be careful :salute:
     * @param target
     */
    public void setTarget(double target) {
        this.target = target;
    }
}
