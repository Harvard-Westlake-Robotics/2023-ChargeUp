package frc.robot.Arm.Components;

import frc.robot.Devices.LimitSwitch;
import frc.robot.Devices.Falcon;

// this is the subsystem
public class ArmExtender {
    // Chain:
    // 60t - output
    // 15t - input

    // Motor:
    // 8t input
    // 64t output

    private Falcon extender;
    public LimitSwitch overExtending;
    public LimitSwitch overRetracting;

    public ArmExtender(Falcon extender, LimitSwitch overExtending, LimitSwitch overRetracting) {
        this.extender = extender;
        this.overExtending = overExtending;
        this.overRetracting = overRetracting;
    }

    public boolean isOverExtended() {
        return overExtending.get();
    }

    public boolean isReverseExtended() {
        return overRetracting.get();
    }

    public void setPower(double percent) {
        // VIRTUAL LIMITER LIMIT SWITCH IS SHIT
        if (percent > 0) {
            if (getExtension() > 43) {
                // We are going up and top limit is tripped so stop
                extender.setVoltage(0);
                return;
            }
        } else {
            if (getExtension() < 1) {
                // We are going down and bottom limit is tripped so stop
                extender.setVoltage(0);
                return;
            }
        }
        if (Math.abs(percent) > 100.0)
            throw new Error("The fuck how do you expect me to send over 100% voltage to the motor you dumbass");
        extender.setPercentVoltage(percent);
    }

    public double getExtension() {
        return encoderToLength(extender.getPosition());
    }

    public void reset() {
        extender.resetEncoder();
    }

    // convert encoder val to length
    // encoder val is 0 to 50
    public double encoderToLength(double encoderPosition) // 4096 ticks per rev
    {
        // ??:?? gear ratio ; 18/35 sprocket ratio ; 2" wheel diameter ; 2 inch of
        // height per 1 inch of string ; min arm length 35"
        return (encoderPosition * (14.0 / 60.0) * (18.0 / 35.0) * 2.0 * Math.PI * 2.0); // ! assumptions were made here
    }
}
