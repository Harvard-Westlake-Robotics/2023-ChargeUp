package frc.robot.Drive.Components;

import frc.robot.Devices.Encoder;
import frc.robot.Devices.MotorController;
import frc.robot.Devices.Motor.SparkMax;

public class DriveSide {
    private SparkMax front;
    private SparkMax back;
    private SparkMax top;

    private Encoder encoder;

    double revsAtLastShift = 0;
    double inchesAtLastShift = 0;

    public DriveSide(SparkMax front, SparkMax back, SparkMax top, Encoder encoder) {
        this.front = front;
        this.back = back;
        this.top = top;

        this.encoder = encoder;
        resetEncoder();
    }

    public void setCurrentLimit(int stall, int free) {
        for (SparkMax motor : new SparkMax[] { front, back, top }) {
            motor.setCurrentLimit(stall, free);
        }
    }

    public double getPositionInches() {
        return encoder.getRevs() * (6 * Math.PI);
    }

    public void resetEncoder() {
        encoder.reset();
    }

    public void setPower(double percentage) {
        if (Math.abs(percentage) > 100.0)
            percentage = percentage > 0 ? 100 : -100;
        double voltage = percentage * (12.0 / 100.0);
        for (MotorController motor : new MotorController[] { front, back, top }) {
            motor.setVoltage(voltage);
        }
    }

    public void setBrake(boolean brake) {
        for (SparkMax motor : new SparkMax[] { front, back, top }) {
            motor.setBrake(brake);
        }
    }

    public boolean isMotorBraking() {
        boolean braking = false;
        for (SparkMax motor : new SparkMax[] { front, back, top }) {
            if (motor.isBraking())
                braking = true;
        }
        return braking;
    }

    public double[] getTemps() {
        return new double[] { front.getTemp(), back.getTemp(), top.getTemp() };
    }
}
