package frc.robot.Arm.Components;

import frc.robot.Settings;
import frc.robot.Devices.MotorController;

// this is the subsystem
public class ArmExtender {
    // Chain:
    // 60t - output
    // 15t - input

    // Motor:
    // 8t input
    // 64t output

    private MotorController extender1;
    private MotorController extender2;

    public ArmExtender(MotorController extender1, MotorController extender2) {
        this.extender1 = extender1;
        this.extender2 = extender2;
    }

    private void setPV(double pv) {
        extender1.setPercentVoltage(pv);
        extender2.setPercentVoltage(pv);
    }

    public void setPower(double percent) {
       
        //System.out.println(percent);
        setPV(percent);
    }

    public void reset() {
        extender1.resetEncoder();
        extender2.resetEncoder();
    }

    // convert encoder val to length
    public double getExtension() { // 4096 ticks per rev

        if (Settings.armBeingBadMode) {
            return 5;
        }
        // 14:60 gear ratio ; 12:17 sprocket ratio ; 2" wheel diameter
        // 1" height per 1 inch of string ; min arm length 35"
        return (extender1.getRevs() * (14.0 / 60.0) * (12.0 / 17.0) * 2.0 * Math.PI); // ! assumptions were made here
    }
}
