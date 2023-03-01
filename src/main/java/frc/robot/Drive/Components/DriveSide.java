package frc.robot.Drive.Components;

import frc.robot.Motor.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;

public class DriveSide {
    private TalonSRX one;
    private TalonSRX two;
    private TalonSRX three;

    private Encoder encoder;

    double revsAtLastShift = 0;
    double inchesAtLastShift = 0;

    public DriveSide(TalonSRX one, TalonSRX two, TalonSRX three, GearShifter shifter, Encoder encoder) {
        this.one = one;
        this.two = two;
        this.three = three;

        this.encoder = encoder;
        resetEncoder();
    }

    public double getPositionInches() {
        return encoder.getDistance() * (6 * Math.PI);
    }

    public void resetEncoder() {
        encoder.reset();
    }

    public void stop() {
        for (TalonSRX motor : new TalonSRX[] { one, two, three }) {
            motor.stop();
        }
    }

    public void setPower(double percentage) {
        if (Math.abs(percentage) > 100.0)
            throw new Error("power too high: " + percentage);
        double voltage = percentage * (12.0 / 100.0);
        for (TalonSRX motor : new TalonSRX[] { one, two, three }) {
            motor.setVoltage(voltage);
        }
    }
}
