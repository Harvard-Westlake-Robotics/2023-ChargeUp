package frc.robot.Drive.Components;

import frc.robot.Devices.Encoder;
import frc.robot.Devices.Motor.Falcon;

public class DriveSide {
    private Falcon one;
    private Falcon two;
    private Falcon three;

    private Encoder encoder;

    double revsAtLastShift = 0;
    double inchesAtLastShift = 0;

    public DriveSide(Falcon one, Falcon two, Falcon three, GearShifter shifter, Encoder encoder) {
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

    public void stop() {
        for (Falcon motor : new Falcon[] { one, two, three }) {
            motor.stop();
        }
    }

    public void setPower(double percentage) {
        if (Math.abs(percentage) > 100.0)
            throw new Error("power too high: " + percentage);
        double voltage = percentage * (12.0 / 100.0);
        for (Falcon motor : new Falcon[] { one, two, three }) {
            motor.setVoltage(voltage);
        }
    }
}
