package frc.robot.Arm;

import frc.robot.Motor.TalonSRX;

// this is the subsystem
public class ArmSwivel {
    // Chain:
    // 60t - output
    // 15t - input

    // Motor:
    // 8t input
    // 64t output

    private TalonSRX one;
    private TalonSRX two;

    public ArmSwivel(TalonSRX one, TalonSRX two) {
        this.one = one;
        this.two = two;

    }

    public void setPower(double percent) {
        if (Math.abs(percent) > 100.0)
            throw new Error("The fuck how do you expect me to send over 100% voltage to the motor you dumbass");
        double voltage = percent * (12.0 / 100.0);
        one.setVoltage(voltage);
        two.setVoltage(voltage);
    }

    public void stop() {
        for (TalonSRX motor : new TalonSRX[] { one, two }) {
            motor.stop();
        }
    }

    //! 
    public double getEncoderPosition() {
        return one.getPosition();
    }

    public double getArmPosition() {
        return getEncoderPosition() / 256;
    }
}
