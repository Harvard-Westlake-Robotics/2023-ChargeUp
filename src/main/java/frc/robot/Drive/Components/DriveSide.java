package frc.robot.Drive.Components;

import frc.robot.Devices.Encoder;
import frc.robot.Devices.MotorController;

public class DriveSide {
    private MotorController one;
    private MotorController two;
    private MotorController three;

    private Encoder encoder;

    double revsAtLastShift = 0;
    double inchesAtLastShift = 0;

    public DriveSide(MotorController one, MotorController two, MotorController three, GearShifter shifter, Encoder encoder) {
        this.one = one;
        this.two = two;
        this.three = three;

        this.encoder = encoder;
        resetEncoder();
    }

    public double getPositionInches() {
        return encoder.getRevs() * (6 * Math.PI);
    }

    public void resetEncoder() {
        encoder.reset();
    }

    public void setPower(double percentage) {
        if (Math.abs(percentage) > 100.0)
            throw new Error("power too high: " + percentage);
        double voltage = percentage * (12.0 / 100.0);
        for (MotorController motor : new MotorController[] { one, two, three }) {
            motor.setVoltage(voltage);
        }
    }
}
