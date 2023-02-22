package frc.robot.Arm ;

import frc.robot.Motor.SparkMax ;
import frc.robot.Motor.TalonSRX;
import frc.robot.Arm.ArmAngler;

import frc.robot.Arm.ArmConstants ;
import frc.robot.Arm.ArmCalculator ;


// joystick controls
// keep robot legal while in match

public class ArmControl
{

    private ArmAngler armAngler ;
    private TalonSRX extender ;

    // arm limits (changing)
    // will be updated based on changes in arm length / pos
    public double currentLengthMax ; // based on rotation
    public double currentRotateMax ; // based on length
    // current length / pos
    public double currentLength ;
    public double currentAngle ;

    public ArmControl (SparkMax armMotor1, SparkMax armMotor2, TalonSRX extender)
    {
        this.armAngler = new ArmAngler (armMotor1, armMotor2) ;
        this.extender = extender ;
    }


    // convert encoder val to length
    public static double convertPosToLength(double pos) // 4096 ticks per rev
    {
        // ??:?? gear ratio ; 18/35 sprocket ratio ; 2" wheel diameter ; 2 inch of
        // height per 1 inch of string ; min arm length 35"
        return (pos * 14 / 60 * 18 / 35 * 2 * Math.PI * 2 + 35); // ! assumptions were made here
    }

    // convert encoder vals to angle
    public static double convertPosToAngle(double pos) // counts in whole revolutions
    {
        // 14:60 gear ratio ; 12:60 sprocket ratio
        double absAngle = pos * (60 / 14) * (60 / 15); // ! assumptions were made here

        // facing forwards
        if (absAngle >= 0)
            return absAngle;

        // facing backwards
        return -absAngle;
    }


    
    // raise/lower arm
    public void controlArmAngle (double joystickPos)
    {
        // calculate new arm limits
        currentLengthMax = ArmCalculator.maxLength (currentRotateMax) ;

        // check if arm is within limits
        if (currentLength > currentLengthMax)
        {
            // move arm to max length

        }

        armAngler.move(joystickPos);
    }

}