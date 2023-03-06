package frc.robot.Drive.Components;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; 


// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveBase extends SubsystemBase {
    
    private MotorControllerGroup leftDrive;
    private MotorControllerGroup rightDrive;
    private DifferentialDrive robotDrive;
    private Encoder leftEncoder;
    private Encoder rightEncoder;

    private double [] ypr;
    private PigeonIMU imu;
    private PigeonIMU.GeneralStatus generalStatus;
    private int imuErrorCode;

    public DriveBase() {
        ypr = new double [3];
        generalStatus = new PigeonIMU.GeneralStatus();
        imu = new PigeonIMU(18);

        var leftFront = new WPI_TalonFX(5);
        var leftBack = new WPI_TalonFX(4);
        var leftTop = new WPI_TalonFX(3);
        leftTop.setInverted(true);
        leftFront.setNeutralMode(NeutralMode.Coast);
        leftBack.setNeutralMode(NeutralMode.Coast);
        leftTop.setNeutralMode(NeutralMode.Coast);
        leftDrive = new MotorControllerGroup(leftFront, leftBack, leftTop);
        this.leftEncoder = new Encoder(2, 3, true, Encoder.EncodingType.k1X);
        leftEncoder.setDistancePerPulse((Math.PI * 6.0) / 1024.0); // 6 in wheels, 1024 ticks per rev

        var rightFront = new WPI_TalonFX(2);
        var rightBack = new WPI_TalonFX(0);
        var rightTop = new WPI_TalonFX(1);
        rightTop.setInverted(true);
        rightFront.setNeutralMode(NeutralMode.Coast);
        rightBack.setNeutralMode(NeutralMode.Coast);
        rightTop.setNeutralMode(NeutralMode.Coast);
        rightDrive = new MotorControllerGroup(rightFront, rightBack, rightTop);
        this.rightEncoder = new Encoder(0, 1, true, Encoder.EncodingType.k1X);
        rightEncoder.setDistancePerPulse((Math.PI * 6.0) / 1024.0); // 6 in wheels, 1024 ticks per rev
    
        robotDrive = new DifferentialDrive(leftDrive, rightDrive);
    }

    public double getLeftDist() {
        return leftEncoder.getDistance();
    }
    public double getRightDist()
    {
        return rightEncoder.getDistance();
    }
    public double getLeftPos()
    {
        return leftEncoder.get();
    }
    public double getRightPos()
    {
        return rightEncoder.get();
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
    public void driveRobot (double left, double right)
    {
        robotDrive.tankDrive(left, right);
    }

    @Override
    public void periodic ()
    {
        // Drive
        // System.out.println ("E") ;
        /*double leftPos = getLeftPos() ;
        SmartDashboard.putNumber("Left Pos", leftPos);
        double rightPos = getRightPos();
        SmartDashboard.putNumber("Right Pos", rightPos);
        double leftDist = getLeftDist();
        SmartDashboard.putNumber("Left Dist", leftDist);
        double rightDist = getRightDist();
        SmartDashboard.putNumber("Right Dist", rightDist);*/
        
        imuErrorCode = imu.getGeneralStatus(generalStatus).value;
        imu.getYawPitchRoll(ypr);

        SmartDashboard.putNumber("IMU Yaw", ypr[0]);
        SmartDashboard.putNumber("IMU Health", imuErrorCode);

        SmartDashboard.putNumber("Left Distance", leftEncoder.getDistance() );
        SmartDashboard.putNumber("Right Distance", rightEncoder.getDistance() );


    }

    public double [] getYPR() {
        return ypr;
    }

    public void resetYaw (double value) {
        imu.setYaw(value);
    }

    public int getIMUHealth() {
        return imuErrorCode;
    } 

}
