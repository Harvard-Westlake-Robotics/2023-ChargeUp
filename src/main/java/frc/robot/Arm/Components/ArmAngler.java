package frc.robot.Arm.Components;

import frc.robot.Motor.SparkMax ;

import frc.robot.Arm.ArmConstants;

// motor group
public class ArmAngler{
    SparkMax arm1;
    SparkMax arm2;

    public ArmAngler(SparkMax arm1, SparkMax arm2){
        this.arm1 = arm1;
        this.arm2 = arm2;
    }

    public void setVoltage(double voltage){
        arm1.setVoltage (voltage);
        arm2.setVoltage (voltage);
    }

    public double getPosition(){
        return arm1.getPosition();
    }


    // pid for movement
    // calculated as 0 degrees --> 240 degrees
    // for humans measured as -120 degrees --> 120 degrees
    double target = ArmConstants.ROTATE_ABS_MAX ; // 120 degrees

    double error ;
    double currentPos = 0 ;
    double tick ;



}