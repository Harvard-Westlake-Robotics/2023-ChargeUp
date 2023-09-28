// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.DriverStation.Interface;
import frc.robot.DriverStation.LimeLight;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Core.Scheduler;
import frc.robot.Devices.Encoder;
import frc.robot.Devices.Imu;
import frc.robot.Devices.Motor.SparkMax;
import frc.robot.Drive.*;
import frc.robot.Drive.Components.DriveSide;
import frc.robot.Drive.Components.GearShifter;
import frc.robot.Util.*;
import frc.robot.Intake.*;
import frc.robot.Arm.Components.ArmAngler;
import frc.robot.Arm.Components.ArmExtender;
import frc.robot.Pneumatics.PneumaticsSystem;
import frc.robot.Arm.ArmCalculator;
import frc.robot.Arm.Auto.MoveArm;
import frc.robot.Drive.Auto.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class RobotOld extends TimedRobot {

  Scheduler scheduler = new Scheduler();
  GearShifter gearShifter;
  ArmAngler angler;
  ArmExtender extender;
  Intake intake;
  PneumaticsSystem pneumatics;

  PS4Controller con;
  Joystick joystick;
  Drive drive;

  LimeLight limeLight = new LimeLight();

  Imu imu;

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
      var leftFront = new SparkMax(11, true, false);
      var leftBack = new SparkMax(2, true, false);
      var leftTop = new SparkMax(1, false, false);
      var encoderLeft = new Encoder(6, 7, false);

      var rightFront = new SparkMax(15, false, false);
      var rightBack = new SparkMax(5, false, false);
      var rightTop = new SparkMax(4, true, false);
      var encoderRight = new Encoder(8, 9, true);

      this.left = new DriveSide(leftFront, leftBack, leftTop, encoderLeft);
      this.right = new DriveSide(rightFront, rightBack, rightTop, encoderRight);

      this.leftPD = new DriveSidePD(this.left, lowGearController, highGearController);
      this.rightPD = new DriveSidePD(this.right, lowGearController, highGearController);
      this.pneumatics = new PneumaticsSystem(110, 120, 19);
      this.gearShifter = new GearShifter(2, 0, 19);

      var arm1 = new SparkMax(25, false, true);
      var arm2 = new SparkMax(12, false, true);
      var encoder = new Encoder(1, 2, true);
      this.angler = new ArmAngler(arm1, arm2, encoder);

      var extender1 = new SparkMax(10, true, true);
      var extender2 = new SparkMax(16, true, true);
      this.extender = new ArmExtender(extender1, extender2);

      var intakeLeft = new SparkMax(7, false);
      var intakeRight = new SparkMax(6, true);
      intake = new Intake(intakeLeft, intakeRight);

      this.con = new PS4Controller(0);
      this.joystick = new Joystick(1);
      this.drive = new Drive(left, right);

      this.imu = new Imu(18);
      Interface.updateDashboard(drive, gearShifter, angler, extender, intake, pneumatics, con, joystick);
    }
  }

  @Override
  public void autonomousInit() {
    scheduler.clear();

    pneumatics.autoRunCompressor();

    angler.zero();
    extender.reset();

    gearShifter.setLowGear();
    left.resetEncoder();
    right.resetEncoder();

    limeLight.setDriverMode();

    left.setCurrentLimit(70, 200);
    right.setCurrentLimit(70, 200);
    var auto = new Autonomous(scheduler, left, right, gearShifter, angler, extender, intake, pneumatics, limeLight,
        imu, autodrive);
    // auto.scoreHighAndPlatform();
    auto.driveForwardDoNothing();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    scheduler.tick();
  }

  @Override
  public void teleopInit() {
    scheduler.clear();

    RobotController.getBrownoutVoltage();

    left.setCurrentLimit(70, 100);
    right.setCurrentLimit(70, 100);

    pneumatics.autoRunCompressor();

    imu.resetYaw();

    drive.resetEncoders();

    angler.setBrake(true);

    if (Settings.testing) {
      angler.zero();
      extender.reset();
    }

    scheduler.setInterval(() -> {
      System.out.println(right.getPositionInches());
    }, 2);

    // registerTick calls the tick function on an object every tick to run until the
    // robot is disabled
    // The Scheduler should be called only in `init()` functions
    // scheduler.registerTick(arm);

    Interface.updateDashboard(drive, gearShifter, angler, extender, intake, pneumatics, con, joystick);

    Container<Boolean> angling = new Container<Boolean>(false);

    scheduler.setInterval(() -> {
      // logs
    }, 0.2);

    scheduler.setInterval(() -> {

      var ltemps = left.getTemps();
      var rtemps = right.getTemps();

      System.out.println("left temps - front: " + ltemps[0] + " back: " + ltemps[1] + " top: " + ltemps[2]);
      System.out.println("right temps - front: " + rtemps[0] + " back: " + rtemps[1] + " top: " + rtemps[2]);
    }, 5);

    drive.resetEncoders();

    limeLight.setDriverMode();

    scheduler.registerTick((double dTime) -> {
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

      // extender if within bounds
      // if (ArmCalculator.maxLength(angler.getDegrees()) - 4 >
      // extender.getExtension() + 33) {
      switch (joystick.getPOV()) {
        case 0:
          extender.setPower(30);
          break;
        case 180:
          extender.setPower(-20);
          break;
        default:
          extender.setPower(0);
          break;
      }
      // } else {
      // extender.setPower(-30);
      // }

      // turns off any sensors that influence driving
      if (joystick.getRawButtonPressed(8)) {
        Settings.armBeingBadMode = !Settings.armBeingBadMode;
      }
      if (!angling.val) { // angler if within bounds
        angler.setVoltage(joystick.getY() * 5 +
            ArmCalculator.getAntiGravTorque(angler.getRevs(), extender.getExtension()));
      }
      // intake
      if (joystick.getTrigger() && joystick.getRawButton(2))
        intake.setVoltage(1.7);
      else if (joystick.getTrigger() || con.getR1Button())
        intake.setVoltage(7);
      else if (joystick.getRawButton(2))
        intake.setVoltage(-3); // outtake
      else
        intake.setVoltage(0);

      // gearshifting (brakes when shifted)
      if (con.getR2ButtonPressed()) {
        gearShifter.toggle();
        left.setBrake(!left.isMotorBraking());
        right.setBrake(!right.isMotorBraking());
      }

      // an inline function to move the angler to a certain angle
      Mapper<Double, Void> moveToAngle = (Double targetAngle) -> {
        angling.val = true;

        MoveArm.moveToAngle(targetAngle, angler, extender, scheduler).then(() -> {
          angling.val = false;
        });

        return null;
      };

      // arm pos presets if not already angling
      if (!angling.val) {

        if (Math.abs(joystick.getPOV()) == 90.0) {
          moveToAngle.map(0.0);
        }

        if (joystick.getRawButtonPressed(5)) {
          moveToAngle.map(45.0);
        }
        if (joystick.getRawButtonPressed(3) || con.getL1ButtonPressed()) {
          moveToAngle.map(-45.0);
        }

        if (extender.getExtension() < 10) {
          if (joystick.getRawButtonPressed(6)) {
            moveToAngle.map(120.0);
          }
          if (joystick.getRawButtonPressed(4) || con.getL2ButtonPressed()) {
            moveToAngle.map(-118.0);
          }
        }
      }
    });
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    scheduler.tick();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    scheduler.clear();

    angler.setBrake(true);
  }

  @Override
  public void disabledPeriodic() {
    // DO NOT RUN THE SCHEDULER WHILE DISABLED
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