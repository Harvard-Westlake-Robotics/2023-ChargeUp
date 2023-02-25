package frc.robot.Arm.Components;

import frc.robot.Motor.SparkMax ;

import frc.robot.Arm.ArmConstants;

// motor group
public class ArmAngler{
    SparkMax arm1;
    SparkMax arm2;

    //! must be confirmed
    int[] scorePresets =     {0,       45,   80,  100} ; // degree //! must be confirmed & convert to encoder
                            //default, high, mid, low
    int[] intakePresets =    {0,       45,      120} ; // degree //! must be confirmed & convert to encoder
                            //default, plat, ground


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
    public double encoderToDegree (double encoder)
    {
        // 14:60 gear ratio ; 15:60 sprocket ratio
        return encoder * (14 / 60) * (15 / 60); // ! assumptions were made here
    }
    public double degreeToEncoder (double degree)
    {
        // 60:15 gear ratio ; 60:14 sprocket ratio
        return degree * (60 / 15) * (60 / 14) ;
    }
    
    double min, max;
    public void manualJoystickMove(double voltage) {
        // if(getPosition() <= max && getPosition() >= min) {
        //     setVoltage(voltage);
        // }
        // else {
        //     setVoltage(0);
        // }
    }


    // pid for movement
    // calculated as 0 degrees --> 240 degrees
    // for humans measured as -120 degrees --> 120 degrees
    double angleMax = ArmConstants.ROTATE_ABS_MAX ; // 120 degrees

    double error ;
    double currentPos = 0 ; // encoder
    double pConstant = 10 ; //! need tune

    private void anglerPID (double targetDegree) // degree
    {
        currentPos = getPosition() ; // encoder
        double targetEncoder = degreeToEncoder(targetDegree) ;
    }

}