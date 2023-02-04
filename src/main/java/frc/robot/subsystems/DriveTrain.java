// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.javatuples.Pair;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveTrain extends SubsystemBase {
    private CANSparkMax leftFront;
    private CANSparkMax leftBack;
    private CANSparkMax leftTop;
    private CANSparkMax rightFront;
    private CANSparkMax rightBack;
    private CANSparkMax rightTop;
    private MotorControllerGroup leftDrive;
    private MotorControllerGroup rightDrive;
    private DifferentialDrive robotDrive;

    private RelativeEncoder encoderLeftFront;
    private RelativeEncoder encoderLeftBack;
    private RelativeEncoder encoderLeftTop;
    private RelativeEncoder encoderRightFront;
    private RelativeEncoder encoderRightBack;
    private RelativeEncoder encoderRightTop;

    public DriveTrain(int lf, int lb, int lt, int rf, int rb, int rt) {
        // create motors
        // Note: do not type "deviceId: " ; just type int
        leftFront = new CANSparkMax(lf, MotorType.kBrushless);
        leftBack = new CANSparkMax(lb, MotorType.kBrushless);
        leftTop = new CANSparkMax(lt, MotorType.kBrushless);
        
        encoderLeftFront = leftFront.getEncoder();
        encoderLeftBack = leftBack.getEncoder();
        encoderLeftTop = leftTop.getEncoder();
        encoderRightFront = rightFront.getEncoder();
        encoderRightBack = rightBack.getEncoder();
        encoderRightTop = rightTop.getEncoder();


        // invert motor - must be done before creating motorgroup
        leftFront.setInverted(true);
        leftBack.setInverted(false);

        // create motor group
        leftDrive = new MotorControllerGroup(leftFront, leftBack, leftTop);

        // create motors
        // Note: do not type "deviceId: " ; just type int
        rightFront = new CANSparkMax(rf, MotorType.kBrushless);
        rightBack = new CANSparkMax(rb, MotorType.kBrushless);
        rightTop = new CANSparkMax(rt, MotorType.kBrushless);

        // invert motor - must be done before creating motorgroup
        rightFront.setInverted(true);
        rightBack.setInverted(true);

        // create motor group
        rightDrive = new MotorControllerGroup(rightFront, rightBack, rightTop);

        // create new differential drive
        robotDrive = new DifferentialDrive(leftDrive, rightDrive);
    }

    public Pair<Double, Double> getPosition() {
        double posOneLeft = encoderLeftFront.getPosition();
        double posTwoLeft = encoderLeftBack.getPosition();
        double posThreeLeft = encoderLeftTop.getPosition();

        double avgLeft = (posOneLeft + posTwoLeft + posThreeLeft) / 3;

        double posOneRight = encoderRightFront.getPosition();
        double posTwoRight = encoderRightBack.getPosition();
        double posThreeRight = encoderRightTop.getPosition();

        double avgRight = (posOneRight + posTwoRight + posThreeRight) / 3;

        return new Pair<Double, Double>(avgLeft, avgRight);
    }

    public void driveRobot(double leftSpeed, double rightSpeed) {
        robotDrive.tankDrive(leftSpeed, rightSpeed);
    }

    // cuts power ; not a brake function
    public void stop() {
        leftDrive.stopMotor();
        rightDrive.stopMotor();
    }

    public Pair<Double, Double> getPositions() {
        return new Pair<Double>(leftDrive.)
    }

    // method is called once every 20 ms
    @Override
    public void periodic() {

    }

}