// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveTrain extends SubsystemBase {
    private MotorControllerGroup leftDrive;
    private MotorControllerGroup rightDrive;
    private DifferentialDrive robotDrive;

    public DriveTrain(int lf, int lb, int lt, int rf, int rb, int rt) {
        // create motors
        // Note: do not type "deviceId: " ; just type int
        var leftFront = new CANSparkMax(lf, MotorType.kBrushless);
        var leftBack = new CANSparkMax(lb, MotorType.kBrushless);
        var leftTop = new CANSparkMax(lt, MotorType.kBrushless);

        // invert motor - must be done before creating motorgroup
        leftFront.setInverted(true);
        leftBack.setInverted(false);

        // create motor group
        leftDrive = new MotorControllerGroup(leftFront, leftBack, leftTop);

        // create motors
        // Note: do not type "deviceId: " ; just type int
        var rightFront = new CANSparkMax(rf, MotorType.kBrushless);
        var rightBack = new CANSparkMax(rb, MotorType.kBrushless);
        var rightTop = new CANSparkMax(rt, MotorType.kBrushless);

        // invert motor - must be done before creating motorgroup
        rightFront.setInverted(true);
        rightBack.setInverted(true);

        // create motor group
        rightDrive = new MotorControllerGroup(rightFront, rightBack, rightTop);

        // create new differential drive
        robotDrive = new DifferentialDrive(leftDrive, rightDrive);

    }

    public void driveRobot(double leftSpeed, double rightSpeed) {
        robotDrive.tankDrive(leftSpeed, rightSpeed);
    }

    // cuts power ; not a brake function
    public void stop() {
        leftDrive.stopMotor();
        rightDrive.stopMotor();
    }

    // method is called once every 20 ms
    @Override
    public void periodic() {

    }

}