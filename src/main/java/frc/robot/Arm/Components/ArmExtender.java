package frc.robot.Arm.Components;

import frc.robot.Devices.MotorController;
import frc.robot.Devices.Motor.SparkMax;

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

    public ArmExtender(SparkMax extender1, SparkMax extender2) {
        this.extender1 = extender1;
        this.extender2 = extender2;

        extender1.setCurrentLimit(140, 140);
        extender2.setCurrentLimit(140, 140);
    }

    private void setPV(double pv) {
        extender1.setPercentVoltage(pv);
        extender2.setPercentVoltage(pv);
    }

    public void setPower(double percent) {
        setPV(percent);
    }

    public void reset() {
        extender1.resetEncoder();
        extender2.resetEncoder();
    }

    // convert encoder val to length
    public double getExtension() { // 4096 ticks per rev
        // 14:60 gear ratio ; 12:17 sprocket ratio ; 2" wheel diameter
        // 2" height per 1 inch of string
        return (extender1.getRevs() * (14.0 / 60.0) * (12.0 / 17.0) * (2.16 * Math.PI)) * 2;
    }
}
