package frc.robot.Intake;

import frc.robot.Motor.SparkMax;

public class Intake {
    private SparkMax left;
    private SparkMax right;

    public Intake(SparkMax left, SparkMax right) {
        this.left = left;
        this.right = right;
    }

    public void setVoltage (double voltage)
    {
        left.setVoltage(voltage);
        right.setVoltage(voltage);
    }
}
