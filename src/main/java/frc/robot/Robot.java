// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
// import frc.robot.Core.Scheduler;
import frc.robot.Drive.*;
import frc.robot.Util.*;
import frc.robot.Motor.TalonSRX;

import frc.robot.Pneumatics.Pneumatics ;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.javatuples.Pair;

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
  Drive drive;
  PS4Controller con;
  DriveSidePD left;
  DriveSidePD right;

  Pneumatics pneu;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    { // Drive initalization
      var leftFront = new TalonSRX(true);
      var leftBack = new TalonSRX(true);
      var leftTop = new TalonSRX(false);
      var rightFront = new TalonSRX(false);
      var rightBack = new TalonSRX(false);
      var rightTop = new TalonSRX(true);

      DriveSide left = new DriveSide(leftFront, leftBack, leftTop);
      DriveSide right = new DriveSide(rightFront, rightBack, rightTop);

      final var HIGHGEARCONTROLLER = new PDController(300, 0);
      final var LOWGEARCONTROLLER = new PDController(300, 0);

      this.left = new DriveSidePD(left, LOWGEARCONTROLLER, HIGHGEARCONTROLLER);
      this.right = new DriveSidePD(left, LOWGEARCONTROLLER, HIGHGEARCONTROLLER);

      this.drive = new Drive(left, right);
    }
    this.con = new PS4Controller(0);
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    // drive.shiftLowGear();
    // left.reset();
    // right.reset();
    
    CommandScheduler.getInstance().schedule();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    // Scheduler.getInstance().tick();
    // left.incrementTarget(0.003);
    // left.tick(null);
    CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // Scheduler.getInstance().clear();
    // drive.shiftLowGear();

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Scheduler.getInstance().tick();

    final double deadzone = 0.03;
    final double turnCurveIntensity = 7;
    final double pwrCurveIntensity = 5;
    final Pair<Double, Double> powers = ScaleInput.normalize(ScaleInput.scale(
        con.getLeftY(),
        con.getRightY(),
        deadzone,
        turnCurveIntensity,
        pwrCurveIntensity));
    drive.setPower(powers.getValue0(), powers.getValue1());



    // pneumatics fun
    SmartDashboard.putNumber("Pressure: ", pneu.getPressure());
    pneu.autoRunCompressor();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    // Scheduler.getInstance().clear();
    drive.stop();
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
