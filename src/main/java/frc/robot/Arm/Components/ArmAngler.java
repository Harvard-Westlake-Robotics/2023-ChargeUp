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
    public double encoderToAngle ()
    {
        // 14:60 gear ratio ; 12:60 sprocket ratio
        return getPosition() * (60 / 14) * (60 / 15); // ! assumptions were made here
    }
    
    double min, max;
    public void manualJoystickMove(double voltage) {
        if(getPosition() <= max && getPosition() >= min) {
            setVoltage(voltage);
        }
        else {
            setVoltage(0);
        }
    }


    // pid for movement
    // calculated as 0 degrees --> 240 degrees
    // for humans measured as -120 degrees --> 120 degrees
    double angleMax = ArmConstants.ROTATE_ABS_MAX ; // 120 degrees

    double error ;
    double currentPos = 0 ;
    double tick ;


}