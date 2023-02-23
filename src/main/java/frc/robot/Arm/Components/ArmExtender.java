package frc.robot.Arm.Components;

import frc.robot.Motor.TalonSRX;

// this is the subsystem
public class ArmExtender {
    // Chain:
    // 60t - output
    // 15t - input

    // Motor:
    // 8t input
    // 64t output

    private TalonSRX extender;

    public ArmExtender(TalonSRX extender) {
        this.extender = extender;
    }

    public void setPower(double percent) {
        if (Math.abs(percent) > 100.0)
            throw new Error("The fuck how do you expect me to send over 100% voltage to the motor you dumbass");
        extender.setPercentVoltage(percent);
    }
    
    // convert encoder val to length
    public double getLength() // 4096 ticks per rev
    {
        // ??:?? gear ratio ; 18/35 sprocket ratio ; 2" wheel diameter ; 2 inch of
        // height per 1 inch of string ; min arm length 35"
        return (extender.getPosition() * 14 / 60 * 18 / 35 * 2 * Math.PI * 2 + 35); // ! assumptions were made here
    }
}
