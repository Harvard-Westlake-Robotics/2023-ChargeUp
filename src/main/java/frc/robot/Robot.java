// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.DriverStation.Interface;
import frc.robot.DriverStation.LimeLight;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Core.Scheduler;
import frc.robot.Devices.Encoder;
import frc.robot.Devices.LimitSwitch;
import frc.robot.Devices.SparkMax;
import frc.robot.Devices.Falcon;
import frc.robot.Drive.*;
import frc.robot.Drive.Auto.AutonomousDrive;
import frc.robot.Drive.Auto.DriveSidePD;
import frc.robot.Drive.Auto.Movements.DriveForwardMovement;
import frc.robot.Drive.Components.DriveSide;
import frc.robot.Drive.Components.GearShifter;
import frc.robot.Util.*;
import frc.robot.Intake.*;
import frc.robot.Arm.Components.ArmAngler;
import frc.robot.Arm.Components.ArmExtender;
import frc.robot.Pneumatics.PneumaticsSystem;
import frc.robot.Arm.ArmCalculator;
import frc.robot.Arm.ArmPD;

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

  PS4Controller con;
  Joystick joystick;

  Drive drive;

  LimeLight limeLight = new LimeLight();

  // ! If you change the pd constant numbers (anywhere in this code) the related
  // ! subsystem might oscilate or harm somebody

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    { // Drive initalization
      var leftFront = new Falcon(5, true);
      var leftBack = new Falcon(3, true);
      var leftTop = new Falcon(4, false);
      var encoderLeft = new Encoder(0, 1, false);

      var rightFront = new Falcon(2, false);
      var rightBack = new Falcon(0, false);
      var rightTop = new Falcon(1, true);
      var encoderRight = new Encoder(2, 3, true);

      this.left = new DriveSide(leftFront, leftBack, leftTop, null, encoderLeft);
      this.right = new DriveSide(rightFront, rightBack, rightTop, null, encoderRight);

      this.pneumatics = new PneumaticsSystem(110, 120, 19);
      this.gearShifter = new GearShifter(2, 0, 19);

      var arm1 = new SparkMax(8, false, true);
      var arm2 = new SparkMax(9, false, true);
      var encoder = new Encoder(4, 5, true);
      this.angler = new ArmAngler(arm1, arm2, encoder);

      var armExtender = new Falcon(6, true, true);
      var armOverExtendingSwitch = new LimitSwitch(8);
      var armOverRetractingSwitch = new LimitSwitch(9);
      this.extender = new ArmExtender(armExtender, armOverExtendingSwitch,
          armOverRetractingSwitch);

      var intakeLeft = new SparkMax(10, false);
      var intakeRight = new SparkMax(7, true);
      intake = new Intake(intakeLeft, intakeRight);

      this.con = new PS4Controller(0);
      this.joystick = new Joystick(1);

      this.drive = new Drive(left, right);

      Interface.updateDashboard(drive, gearShifter, angler, extender, intake, pneumatics, con, joystick);

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

      leftPD.reset();
      rightPD.reset();

      drive = new AutonomousDrive(leftPD, rightPD, gearShifter);
    }

    limeLight.setDriverMode();

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

    ArmPD arm = new ArmPD(angler, extender,
        new PDController(50, 0),
        new PDController(5, 0, 20));

    drive.resetEncoders();

    angler.setBrake(true);
    angler.zero();

    arm.resetController();

    Scheduler.getInstance().registerTick(arm);

    Interface.updateDashboard(drive, gearShifter, angler, extender, intake, pneumatics, con, joystick);

    Scheduler.getInstance().setInterval(() -> {
      System.out.println(angler.getPosition());

      // System.out.println("overextending: " + extender.overExtending.get());
      // System.out.println("overretracting: " + extender.overRetracting.get());

      // System.out.println("position: " + Round.rd(extender.getLength()));
      // System.out.println("target: " + arm.extensionTarget);
      // System.out.println("correction: " + arm.extenderCorrect);

      System.out.println("position: " + Round.rd(angler.getPosition()));
      System.out.println("target: " + arm.angleTarget);
      System.out.println("correction: " + arm.angleCorrect);
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
          arm.incrementExtensionTarget(dTime * 20.0);
          break;
        case 180:
          System.out.println("retracting");
          arm.incrementExtensionTarget(dTime * -20.0);
          break;
      }

      // arm.incrementAngleTarget(dTime * joystick.getY() / 10);
      // angler.setVoltage(0);

      angler.setVoltage(joystick.getY() * 10);

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
    Scheduler.getInstance().tick();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    Scheduler.getInstance().clear();

    angler.setBrake(false);

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
