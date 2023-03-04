package frc.robot.Arm.Components;

import frc.robot.Motor.SparkMax;
import edu.wpi.first.wpilibj.Encoder;

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

    public void setVoltage(double voltage) {
        arm1.setVoltage(voltage);
        arm2.setVoltage(voltage);
    }

    public double getPosition() {
        return encoder.get();
    }
}