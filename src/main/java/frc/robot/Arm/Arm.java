package frc.robot.Arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// this is the subsystem
public class Arm extends SubsystemBase {
    // chain:
    // 60t - output
    // 15t - input

    // motor:
    // 8t input
    // 64t output

    private WPI_TalonSRX arm1 ;
    private WPI_TalonSRX arm2 ;

    public Arm (WPI_TalonSRX arm1, WPI_TalonSRX arm2)
    {
        this.arm1 = arm1;
        this.arm2 = arm2;
    }

    public void moveAngle (double angle)
    {
        
    }


    // public void periodic ()
    // {
    //     // This method will be called once per scheduler run


    // }

    public void stop()
    {
        arm1.stopMotor();
        arm2.stopMotor();
    }

}
