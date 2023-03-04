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

        arm1.setBrake(true);
        arm2.setBrake(true);
    }

    public void setVoltage(double voltage) {
        arm1.setVoltage(voltage);
        arm2.setVoltage(voltage);
    }

    public void resetEncoder ()
    {
        encoder.reset();
    }

    public double getPosition() {
        return encoder.get();
    }

    public double getAngle ()
    {
        return posToAngle (getPosition()) ;
    }
    public double posToAngle (double encoderPos)
    {
        return (-encoderPos / 1024 * 180) ;
    }
}