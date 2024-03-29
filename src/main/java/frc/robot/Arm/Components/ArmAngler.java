package frc.robot.Arm.Components;

import frc.robot.Devices.Encoder;
import frc.robot.Devices.Motor.SparkMax;
import frc.robot.Util.DeSpam;

// motor group
public class ArmAngler {
    SparkMax arm1;
    SparkMax arm2;
    Encoder encoder;

    public ArmAngler(SparkMax arm1, SparkMax arm2, Encoder armEncoder) {
        this.arm1 = arm1;
        this.arm2 = arm2;
        this.encoder = armEncoder;

        arm1.setCurrentLimit(65, 120);
        arm2.setCurrentLimit(65, 120);
    }

    public void setBrake(boolean brake) {
        arm1.setBrake(brake);
        arm2.setBrake(brake);
    }

    public void setVoltage(double voltage) {
        arm1.setVoltageSafe(voltage);
        arm2.setVoltageSafe(voltage);
    }

    DeSpam dSpam = new DeSpam(0.2);

    public double getRevs() {
        dSpam.exec(() -> {
            System.out.println(encoder.getRevs());

        });
        return encoder.getRevs() - 0.025;
    }

    public double getDegrees() {
        return getRevs() * 360.0;
    }

    public void zero() {
        encoder.reset();
    }
}