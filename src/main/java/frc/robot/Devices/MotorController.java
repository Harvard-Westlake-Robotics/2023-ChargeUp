package frc.robot.Devices;

public abstract class MotorController {
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

    protected abstract void uSetVoltage(double voltage);

    protected abstract double uGetRevs();
    
    public double getRevs() {
        return isReversed ? -uGetRevs() : uGetRevs();
    }

    public double getDegrees() {
        return getRevs() * 360.0;
    }

    public double getRadians() {
        return getRevs() * 2.0 * Math.PI;
    }

    public abstract void stop();

    public abstract void resetEncoder();

    public void setVoltage(double volts) {
        if (Math.abs(volts) > 12.0)
            throw new Error("Illegal voltage");
        uSetVoltage(isReversed ? -volts : volts);
    }

    public void setPercentVoltage(double percent) {
        setVoltage(percent * (12.0 / 100.0));
    }

    public void tick(double dTime) {

    }
}