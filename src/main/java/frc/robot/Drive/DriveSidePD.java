package frc.robot.Drive;

import frc.robot.Util.PDController;

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
        this.driveSide.resetEncoders();

        this.target = driveSide.getPositionInInches();
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
        resetPID(null);
        driveSide.resetEncoders();
        target = driveSide.getPositionInInches();
    }

    public void incrementTarget(double amount) {
        target += amount;
    }

    // ! remove these after debugging
    public double error;
    public double correct;
    // ! </>
    
    public String toString() {
        return "error: " + error + " current: " + driveSide.getInchesSinceLastShift() + " target: " + target + " correction: " + correct;
    }

    /**
     * Uses the pid controllers to set the motor voltages based on their distance
     * from their targets
     * 
     * @param fac a factor that the motor voltages are multiplied by if they need to
     * @return If the voltages sent to the motor is
     */
    public Double tick(Double fac) {
        if (fac == null)
            fac = 1.0;
        double error = target - driveSide.getPositionInInches();
        this.error = error;
        double correction;
        if (driveSide.getIsLowGear()) {
            highGearController.reset();
            correction = lowGearController.tick(error);
        } else {
            lowGearController.reset();
            correction = highGearController.tick(error);
        }
        if (Math.abs(correction) > 100.0) {
            driveSide.setPower((correction > 0) ? 100 : -100);
            return correction / 100;
        }
        this.correct = correction;
        driveSide.setPower(correction * fac);
        return null;
    }
}
