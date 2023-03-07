package frc.robot.Devices;

import frc.robot.Core.TimedHardware;

public abstract class MotorController extends TimedHardware {
    Double maxSlew;
    boolean isReversed;

    public MotorController(boolean isReversed, double maxSlew) {
        super();

        this.maxSlew = maxSlew;
        this.isReversed = isReversed;
    }

    public MotorController(boolean isReversed) {
        super();

        this.isReversed = isReversed;
        maxSlew = null;
    }

    protected abstract void setVolt(double voltage);

    protected abstract double getRev();
    
    public double getRevs() {
        return isReversed ? -getRev() : getRev();
    }

    public abstract void resetEncoder();

    public void setVoltage(double volts) {
        if (Math.abs(volts) > 12.0)
            throw new Error("Illegal voltage");
        setVolt(isReversed ? -volts : volts);
    }

    public void setPercentVoltage(double percent) {
        setVoltage(percent * (12.0 / 100.0));
    }

    public void tick(double dTime) {

    }
}