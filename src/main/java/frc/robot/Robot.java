// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Drive.Components.GearShifter;


import edu.wpi.first.wpilibj.PS4Controller;
// import edu.wpi.first.wpilibj.Joystick;
// import frc.robot.DriverStation.Interface;
// import frc.robot.DriverStation.LimeLight;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; 

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
    // import frc.robot.Core.Scheduler;



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

  private CommandScheduler m_scheduler = CommandScheduler.getInstance();

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;


  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    {
      m_robotContainer = new RobotContainer();
    }
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    m_scheduler.run();
  }

  @Override
  public void autonomousInit() {

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
   
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.

    SmartDashboard.putNumber("E", 3432432) ;

    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    drive.resetEncoders();

    angler.setBrake(true);
    angler.zero();
    extender.reset();

    arm.resetController();

    // Scheduler.getInstance().registerTick(arm);

    Interface.updateDashboard(drive, gearShifter, angler, extender, intake, pneumatics, con, joystick);

    Scheduler.getInstance().setInterval(() -> {
      System.out.println("extender pos: " + extender.getExtension());

      System.out.println("overextending: " + extender.overExtending.get());
      System.out.println("overretracting: " + extender.overRetracting.get());

      // System.out.println("position: " + Round.rd(extender.getLength()));
      // System.out.println("target: " + arm.extensionTarget);
      // System.out.println("correction: " + arm.extenderCorrect);

      // System.out.println("position: " + Round.rd(angler.getPosition()));
      // System.out.println("target: " + arm.angleTarget);
      // System.out.println("correction: " + arm.angleCorrect);
    }, 0.5);

    drive.resetEncoders();

    limeLight.setDriverMode();

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

      // THIS CONTROLS THE ARM EXTENSION
      // extender.setPower(100*joystick.getY());
      switch (joystick.getPOV()) {
        case 0:
          System.out.println("extending");
          extender.setPower(40);
          break;
        case 180:
          System.out.println("retracting");
          extender.setPower(-30);
          break;
        default:
          extender.setPower(0);
          break;
      }

      // arm.incrementAngleTarget(dTime * joystick.getY() / 10);
      angler.setVoltage(joystick.getY() * 5
          + ArmCalculator.getAntiGravTorque(angler.getPosition(), extender.getExtension()));

      // intake
      if (joystick.getTrigger())
        intake.setVoltage(10);
      else if (joystick.getRawButton(2))
        intake.setVoltage(-5); // outtake
      else
        intake.setVoltage(0.1);

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
    
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    
    Scheduler.getInstance().clear();

    // angler.setBrake(false);

    left.stop();
    right.stop();
  }

  @Override
  public void disabledPeriodic() {
    
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
