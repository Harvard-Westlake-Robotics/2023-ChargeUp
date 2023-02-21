package frc.robot.Arm;

import frc.robot.Motor.SparkMax ;

public class ArmAngler{
    SparkMax arm1;
    SparkMax arm2;

    public ArmAngler(SparkMax arm1, SparkMax arm2){
        this.arm1 = arm1;
        this.arm2 = arm2;
    }

    public void setArmsVoltage(double voltage){
        arm1.setVoltage (voltage);
        arm2.setVoltage (voltage);
    }




}