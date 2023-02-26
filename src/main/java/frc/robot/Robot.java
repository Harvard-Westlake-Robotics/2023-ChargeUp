// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Core.Scheduler;
import frc.robot.Drive.*;
import frc.robot.Drive.Auto.AutonomousDrive;
import frc.robot.Drive.Auto.DriveSidePD;
import frc.robot.Drive.Auto.Movements.DriveForwardMovement;
import frc.robot.Drive.Components.DriveSide;
import frc.robot.Drive.Components.GearShifter;
import frc.robot.Util.*;
import frc.robot.Arm.*;
import frc.robot.Intake.*;
import frc.robot.Arm.Components.ArmAngler;
import frc.robot.Arm.Components.ArmExtender;
import frc.robot.Motor.LimitSwitch;
import frc.robot.Motor.SparkMax;
import frc.robot.Motor.TalonSRX;
import frc.robot.Pneumatics.PneumaticsSystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  DriveSide left;
  DriveSide right;

  GearShifter gearShifter;

  ArmAngler angler;
  ArmExtender extender;
  Intake intake;
  PneumaticsSystem pneumatics;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    { // Drive initalization
      var leftFront = new TalonSRX(5, true);
      var leftBack = new TalonSRX(3, true);
      var leftTop = new TalonSRX(4, false);
      var rightFront = new TalonSRX(2, false);
      var rightBack = new TalonSRX(0, false);
      var rightTop = new TalonSRX(1, true);

      this.left = new DriveSide(leftFront, leftBack, leftTop, null);
      this.right = new DriveSide(rightFront, rightBack, rightTop, null);

      this.pneumatics = new PneumaticsSystem(110, 120, 19);
      this.gearShifter = new GearShifter(2, 0, 19);

      var arm1 = new SparkMax(8, false, true);
      var arm2 = new SparkMax(9, false, true);
      this.angler = new ArmAngler(arm1, arm2);

      var armExtender = new TalonSRX(6, true);
      // var armOverExtendingSwitch = new LimitSwitch(8);
      // var armOverRetractingSwitch = new LimitSwitch(9);
      this.extender = new ArmExtender(armExtender);
      // new ArmExtender(armExtender, armOverExtendingSwitch,
      // armOverRetractingSwitch);

      var intakeLeft = new SparkMax(7, false);
      var intakeRight = new SparkMax(10, true);
      intake = new Intake(intakeLeft, intakeRight);

    }
  }

  @Override
  public void autonomousInit() {
    Scheduler.getInstance().clear();
    AutonomousDrive drive;
    { // Initializes `drive`
      final var HIGHGEARCONTROLLER = new PDController(2, 0);
      final var LOWGEARCONTROLLER = new PDController(0.1, 0);

      DriveSidePD leftPD = new DriveSidePD(left, LOWGEARCONTROLLER, HIGHGEARCONTROLLER);
      DriveSidePD rightPD = new DriveSidePD(right, LOWGEARCONTROLLER, HIGHGEARCONTROLLER);

      left.shiftLow();
      right.shiftLow();

      leftPD.reset();
      rightPD.reset();

      drive = new AutonomousDrive(leftPD, rightPD);
    }

    Scheduler.getInstance().registerTick(drive);

    drive.setMovement(new DriveForwardMovement(10, 1, 1, 5));
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().tick();
  }

  @Override
  public void teleopInit() {
    Scheduler.getInstance().clear();

    PS4Controller con = new PS4Controller(0);
    Joystick joystick = new Joystick(1);

    Drive drive = new Drive(left, right);

    drive.resetEncoders();

    Scheduler.getInstance().registerTick((double dTime) -> {
      final double deadzone = 0.05;
      final double turnCurveIntensity = 7;
      final double pwrCurveIntensity = 5;
      final Pair<Double> powers = ScaleInput.scale(
          con.getLeftY(),
          con.getRightY(),
          deadzone,
          turnCurveIntensity,
          pwrCurveIntensity);
      drive.setPower(powers.left, powers.right);

      // arm
      angler.setVoltage(joystick.getY() * 5);
      switch (joystick.getPOV()) {
        case 0:
          extender.setPower(50);
          break;
        case 180:
          extender.setPower(-50);
          break;
        default:
          extender.setPower(0);
          break;
      }

      // intake
      if (joystick.getTrigger())
        intake.setVoltage(10);
      else if (joystick.getRawButtonPressed(2))
        intake.setVoltage(-5); // outtake
      else
        intake.setVoltage(0);

      // pneumatics
      pneumatics.autoRunCompressor();

      if (con.getR2ButtonPressed()) {
        gearShifter.toggle();
      }
    });
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().tick();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    Scheduler.getInstance().clear();
    left.stop();
    right.stop();
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  // ! we won't run any code beyond this point

  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
  }
}
