package frc.robot.Intake;

import frc.robot.Motor.SparkMax;

public class Intake {
    private SparkMax left;
    private SparkMax right;

    double voltage = 0;

    public Intake(SparkMax left, SparkMax right) {
        this.left = left;
        this.right = right;
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
