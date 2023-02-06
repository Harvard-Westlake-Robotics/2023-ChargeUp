// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Core.Scheduler;
import frc.robot.Drive.*;
import frc.robot.Util.*;
import frc.robot.Motor.SparkMax;

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

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    { // Drive initalization
      var leftFront = new SparkMax(3, true);
      var leftBack = new SparkMax(1, true);
      var leftTop = new SparkMax(2, false);
      var rightFront = new SparkMax(7, false);
      var rightBack = new SparkMax(6, false);
      var rightTop = new SparkMax(4, true);

      this.left = new DriveSide(leftFront, leftBack, leftTop);
      this.right = new DriveSide(rightFront, rightBack, rightTop);
    }
  }

  @Override
  public void autonomousInit() {
    Scheduler.getInstance().clear();

    final var HIGHGEARCONTROLLER = new PDController(300, 0);
    final var LOWGEARCONTROLLER = new PDController(300, 0);

    DriveSidePD leftPD = new DriveSidePD(left, LOWGEARCONTROLLER, HIGHGEARCONTROLLER);
    DriveSidePD rightPD = new DriveSidePD(left, LOWGEARCONTROLLER, HIGHGEARCONTROLLER);

    leftPD.reset();
    rightPD.reset();

    left.shiftLow();
    right.shiftLow();

    Scheduler.getInstance().setInterval(() -> {
      leftPD.incrementTarget(0.003);
      leftPD.tick(null);
    }, 0);
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
    Drive drive = new Drive(left, right);
    
    Scheduler.getInstance().setInterval(() -> {
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

    }, 0);
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
