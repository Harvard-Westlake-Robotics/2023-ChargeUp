package frc.robot.Intake;

import frc.robot.Devices.Motor.SparkMax;

public class Intake {
    private SparkMax left;
    private SparkMax right;

    double voltage = 0;

    public Intake(SparkMax left, SparkMax right) {
        this.left = left;
        this.right = right;

        left.setBrake(true);
        right.setBrake(true);

        left.setCurrentLimit(20, 100);
        right.setCurrentLimit(20, 100);
    }

    public void setVoltage (double voltage)
    {
        left.setVoltage(voltage);
        right.setVoltage(voltage);
        this.voltage = voltage;
    }

    public double getVoltage()
    {
        return voltage;
    }
}
