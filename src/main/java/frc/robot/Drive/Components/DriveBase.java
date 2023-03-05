package frc.robot.Drive.Components;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveBase {
    
    private MotorControllerGroup leftDrive;
    private MotorControllerGroup rightDrive;
    private DifferentialDrive robotDrive;
    private Encoder leftEncoder;
    private Encoder rightEncoder;


    public DriveBase() {
        var leftFront = new WPI_TalonFX(5);
        var leftBack = new WPI_TalonFX(4);
        var leftTop = new WPI_TalonFX(3);
        leftTop.setInverted(true) ;
        leftFront.setNeutralMode(NeutralMode.Coast);
        leftBack.setNeutralMode(NeutralMode.Coast);
        leftTop.setNeutralMode(NeutralMode.Coast);
        leftDrive = new MotorControllerGroup(leftFront, leftBack, leftTop);
        this.leftEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k1X);
        leftEncoder.setDistancePerPulse((Math.PI * 6.0) / 1024.0); // 6 in wheels, 1024 ticks per rev

        var rightFront = new WPI_TalonFX(2);
        var rightBack = new WPI_TalonFX(0);
        var rightTop = new WPI_TalonFX(1);
        rightFront.setInverted(true);
        rightBack.setInverted(true);
        rightFront.setNeutralMode(NeutralMode.Coast);
        rightBack.setNeutralMode(NeutralMode.Coast);
        rightTop.setNeutralMode(NeutralMode.Coast);
        rightDrive = new MotorControllerGroup(rightFront, rightBack, rightTop);
        this.rightEncoder = new Encoder(2, 3, true, Encoder.EncodingType.k1X);
        rightEncoder.setDistancePerPulse((Math.PI * 6.0) / 1024.0); // 6 in wheels, 1024 ticks per rev
    
        robotDrive = new DifferentialDrive(leftDrive, rightDrive);
    }

    public double getPositionInches() {
        return leftEncoder.getDistance();
    }

    public void resetEncoder() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void stop() {
        leftDrive.stopMotor();
        rightDrive.stopMotor();
    }

    public void setVoltage(double leftVoltage, double rightVoltage) {
        robotDrive.tankDrive(leftVoltage/12, rightVoltage/12);
    }
}
