package frc.robot.Arm.Components;


// motor group
public class ArmAngler {
    SparkMax arm1;
    SparkMax arm2;
    Encoder encoder;

    public ArmAngler(SparkMax arm1, SparkMax arm2, Encoder armEncoder) {
        this.arm1 = arm1;
        this.arm2 = arm2;
        this.encoder = armEncoder;

        arm1.setBrake(true);
        arm2.setBrake(true);
    }

    public void setBrake(boolean brake) {
        arm1.setBrake(brake);
        arm2.setBrake(brake);
    }

    public void setVoltage(double voltage) {
        arm1.setVoltage(voltage);
        arm2.setVoltage(voltage);
    }

    public double getPosition() {
        return encoder.getRevs();
    }

    public void zero() {
        encoder.reset();
    }
}