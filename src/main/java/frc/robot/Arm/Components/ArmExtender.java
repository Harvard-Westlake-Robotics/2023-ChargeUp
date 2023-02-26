package frc.robot.Arm.Components;

import frc.robot.Motor.LimitSwitch;
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
    LimitSwitch overExtending;
    LimitSwitch reverseExtending;

    public ArmExtender(TalonSRX extender) {
        this.extender = extender;
        // this.overExtending = overExtending;
        // this.reverseExtending = reverseExtending;
    }

    public boolean isOverExtended() {
        return overExtending.get();
    }
    public boolean isReverseExtended() {
        return reverseExtending.get();
    }

    
    public void setPower(double percent) {
        // if (percent > 0) {
        //     if (overExtending.get()) {
        //         // We are going up and top limit is tripped so stop
        //         extender.setVoltage(0);
        //         return;
        //     }
        // } else {
        //     if (reverseExtending.get()) {
        //         // We are going down and bottom limit is tripped so stop
        //         extender.setVoltage(0);
        //         return;
        //     }
        // }
        if (Math.abs(percent) > 100.0)
            throw new Error("The fuck how do you expect me to send over 100% voltage to the motor you dumbass");
        extender.setPercentVoltage(percent);
    }
    

    public double getPosition(){
        return extender.getPosition();
    }
    public double getLength ()
    {
        return encoderToLength(getPosition()) ;
    }
    // convert encoder val to length
    public double encoderToLength(double position) // 4096 ticks per rev
    {
        // ??:?? gear ratio ; 18/35 sprocket ratio ; 2" wheel diameter ; 2 inch of
        // height per 1 inch of string ; min arm length 35"
        return (position * 14 / 60 * 18 / 35 * 2 * Math.PI * 2 + 35); // ! assumptions were made here
    }
}
