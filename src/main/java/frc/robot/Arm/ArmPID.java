package frc.robot.Arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmPID {

    private WPI_TalonSRX arm1;
    private WPI_TalonSRX arm2;

    public ArmPID (WPI_TalonSRX arm1, WPI_TalonSRX arm2)
    {
        this.arm1 = arm1;
        this.arm2 = arm2;
    }

    
}