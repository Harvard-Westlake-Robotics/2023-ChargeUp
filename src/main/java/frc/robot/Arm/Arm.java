package frc.robot.Arm;

import frc.robot.Motor.TalonSRX;

// this is the subsystem
public class Arm {
    // Chain:
    // 60t - output
    // 15t - input

    // Motor:
    // 8t input
    // 64t output

    private ArmAngler armAngler;
    private TalonSRX extender;

    public Arm(ArmAngler armAngler, TalonSRX extender) {
        this.armAngler = armAngler;
        this.extender = extender;
    }

    public void setPower(double percent) {
        if (Math.abs(percent) > 100.0)
            throw new Error("The fuck how do you expect me to send over 100% voltage to the motor you dumbass");
        double voltage = percent * (12.0 / 100.0);
        armAngler.setVoltage(voltage);
    }

    //! 
    public double getEncoderPosition() {
        return armAngler.getPosition();
    }

    // public double getArmPosition() {
    //     return getEncoderPosition() / 256;
    // }

}
