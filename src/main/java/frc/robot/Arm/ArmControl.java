package frc.robot.Arm ;

import frc.robot.Motor.SparkMax ;

import frc.robot.Arm.ArmConstants ;
import frc.robot.Arm.ArmCalculator ;
import edu.wpi.first.wpilibj.Joystick ;


// joystick controls
// keep robot legal while in match

public class ArmControl
{
    private Joystick joystick ;
    private SparkMax armMotor1 ;
    private SparkMax armMotor2 ;

    // arm limits (changing)
    // will be updated based on changes in arm length / pos
    public double currentLengthMax ;
    public double currentRotateMax ;


    public ArmControl (Joystick joystick, SparkMax armMotor1, SparkMax armMotor2)
    {
        this.joystick = joystick ;
        this.armMotor1 = armMotor1 ;
        this.armMotor2 = armMotor2 ;
    }

    public void ControlArmAngle (double joystick.get)
    {

    }

}