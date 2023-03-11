package frc.robot.Arm.Components;

import frc.robot.Settings;
import frc.robot.Devices.Encoder;
import frc.robot.Devices.Motor.SparkMax;

// motor group
public class ArmAngler {
    SparkMax arm1;
    SparkMax arm2;
    Encoder encoder;

    public ArmAngler(SparkMax arm1, SparkMax arm2, Encoder armEncoder) {
        this.arm1 = arm1;
        this.arm2 = arm2;
        this.encoder = armEncoder;

        arm1.setBrake(false);
        arm2.setBrake(false);
    }

    public void setBrake(boolean brake) {
        arm1.setBrake(brake);
        arm2.setBrake(brake);
    }

    public void setVoltage(double voltage) {
        arm1.setVoltage(voltage);
        arm2.setVoltage(voltage);
    }

    public double getRevs() {
        if (Settings.armBeingBadMode) {
            return 0;
        }
        return encoder.getRevs();
    }

    public double getDegrees()
    {
        return getRevs() * 360;
    }

    public void zero() {
        encoder.reset();
    }
}