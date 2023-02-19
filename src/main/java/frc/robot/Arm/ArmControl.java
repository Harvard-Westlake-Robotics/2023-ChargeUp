package frc.robot.Arm ;

import frc.robot.Motor.SparkMax ;
import frc.robot.Motor.TalonSRX;
import frc.robot.Arm.ArmConstants ;

import javax.swing.plaf.basic.BasicTabbedPaneUI.TabSelectionHandler;

import frc.robot.Arm.ArmCalculator ;


// joystick controls
// keep robot legal while in match

public class ArmControl
{

    private SparkMax armMotor1 ;
    private SparkMax armMotor2 ;
    private TalonSRX extender ;

    // arm limits (changing)
    // will be updated based on changes in arm length / pos
    public double currentLengthMax ;
    public double currentRotateMax ;


    public ArmControl (SparkMax armMotor1, SparkMax armMotor2, TalonSRX extender)
    {
        this.armMotor1 = armMotor1 ;
        this.armMotor2 = armMotor2 ;
        this.extender = extender ;
    }

    public void ControlArmAngle (double joystickPos)
    {
        extender.setVoltage(joystickPos);
    }

}