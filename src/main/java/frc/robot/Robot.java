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
import frc.robot.Devices.Imu;
import frc.robot.Devices.Motor.Falcon;
import frc.robot.Devices.Motor.SparkMax;
import frc.robot.Drive.*;
import frc.robot.Drive.Auto.AutonomousDrive;
import frc.robot.Drive.Auto.DriveSidePD;
import frc.robot.Drive.Auto.DumbyDrive;
import frc.robot.Drive.Auto.Shauton;
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

  Scheduler scheduler = new Scheduler();

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
      var leftFront = new SparkMax(5, true, false);
      var leftBack = new SparkMax(3, true, false);
      var leftTop = new SparkMax(4, false, false);
      var encoderLeft = new Encoder(0, 1, false);

      var rightFront = new SparkMax(2, false, false);
      var rightBack = new SparkMax(12, false, false);
      var rightTop = new SparkMax(1, true, false);
      var encoderRight = new Encoder(2, 3, true);

      this.left = new DriveSide(leftFront, leftBack, leftTop, encoderLeft);
      this.right = new DriveSide(rightFront, rightBack, rightTop, encoderRight);

      this.pneumatics = new PneumaticsSystem(110, 120, 19);
      this.gearShifter = new GearShifter(2, 0, 19);

      var arm1 = new SparkMax(8, false, true);
      var arm2 = new SparkMax(9, false, true);
      var encoder = new Encoder(4, 5, false);
      this.angler = new ArmAngler(arm1, arm2, encoder);

      var extender1 = new SparkMax(6, true, true);
      var extender2 = new SparkMax(11, true, true);
      this.extender = new ArmExtender(extender1, extender2);

      var intakeLeft = new SparkMax(10, false);
      var intakeRight = new SparkMax(7, true);
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

    limeLight.setDriverMode();

    scheduler.registerTick((double dTime) -> {
      angler.setVoltage(ArmCalculator.getAntiGravTorque(angler.getRevs(), extender.getExtension()) - 0.3);
    });

    int autoNum = 2;

    switch (autoNum) {
      case 0:
        left.setPower(10);
        right.setPower(10);

        scheduler.setTimeout(() -> {
          left.setPower(0);
          right.setPower(0);
        }, 0.7);
        break;
      case 1:
        intake.setVoltage(-10);

        scheduler.setTimeout(() -> {
          intake.setVoltage(0);
        }, 3);

        break;
      case 2:
        left.resetEncoder();
        right.resetEncoder();

        var drive = new DumbyDrive(left, right, new PDController(20, 20));

        scheduler.setInterval(() -> {
          System.out.println("left: " + left.getPositionInches());
          System.out.println("right: " + right.getPositionInches());
          System.out.println("imu: " + imu.getPitch());
        }, 0.3);

        // lets the drive pdcontroller run every tick
        scheduler.registerTick(drive);

        // outtakes cubes
        intake.setVoltage(-10);

        scheduler.setTimeout(() -> {
          intake.setVoltage(0);
        }, 2).then(() -> {

          double speed = 20; // in/sec
          double dist = 30 + 36 + 10; // in

          var stopDriving = scheduler.registerTick((double dTime) -> {
            drive.incrementTarget(dTime * speed);
          });

          scheduler.setTimeout(() -> {
            stopDriving.run();

            var atCenter = new Container<Boolean>(false);

            var balTime = new Container<Double>(0.0);

            var levelingPD = new DSAController(0.6, 7, 20).withMagnitude(0.7);

            scheduler.registerTick((double dTime) -> {
              if (Math.abs(imu.getPitch()) < 2.5) {
                atCenter.val = true;
                balTime.val += dTime;
              }
              if (balTime.val < 3 || Math.abs(imu.getPitch()) > 2)
                drive.incrementTarget(Autolevel.autolevel(levelingPD, imu, atCenter.val ? 5 : 15) * dTime);
            });
          }, dist / speed);
        });
        break;
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    scheduler.tick();
  }

  @Override
  public void teleopInit() {
    scheduler.clear();

    pneumatics.autoRunCompressor();

    imu.resetYaw();

    drive.resetEncoders();

    angler.setBrake(true);

    if (Settings.testing) {
      angler.zero();
      extender.reset();
    }

    // registerTick calls the tick function on an object every tick to run until the
    // robot is disabled
    // The Scheduler should be called only in `init()` functions
    // scheduler.registerTick(arm);

    Interface.updateDashboard(drive, gearShifter, angler, extender, intake, pneumatics, con, joystick);

    Container<Boolean> angling = new Container<Boolean>(false);

    scheduler.setInterval(() -> {

      // System.out.println("extender pos: " + extender.getExtension());

      // System.out.println("position: " + Round.rd(extender.getExtension()));
      // System.out.println("target: " + arm.extensionTarget);
      // System.out.println("correction: " + arm.extenderCorrect);

      // System.out.println("angler position: " + Round.rd(angler.getRevs()));
      // System.out.println("target: " + arm.angleTarget);
      // System.out.println("correction: " + arm.angleCorrect);
    }, 0.2);

    scheduler.setInterval(() -> {

      var ltemps = left.getTemps();
      var rtemps = right.getTemps();

      System.out.println("left temps - front: " + ltemps[0] + " back - " + ltemps[1] + " top: " + ltemps[2]);
      System.out.println("right temps - front: " + rtemps[0] + " back - " + rtemps[1] + " top: " + rtemps[2]);
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
      if (ArmCalculator.maxLength(angler.getDegrees()) - 4 > extender.getExtension() + 33) {
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
      } else {
        extender.setPower(-30);
      }

      // turns off any sensors that influence driving
      if (joystick.getRawButtonPressed(8)) {
        Settings.armBeingBadMode = !Settings.armBeingBadMode;
      }
      if (!angling.val) { // angler if within bounds
        angler.setVoltage(joystick.getY() * 5 +
            ArmCalculator.getAntiGravTorque(angler.getRevs(), extender.getExtension()));
      }
      // intake
      if (joystick.getTrigger() || con.getR1Button())
        intake.setVoltage(5);
      else if (joystick.getRawButton(2))
        intake.setVoltage(-5); // outtake
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

        Container<Lambda> stopContainer = new Container<Lambda>(null);
        stopContainer.val = scheduler.registerTick((double i_dTime) -> {
          double error = targetAngle - angler.getDegrees();
          double fac = ((error > 0) ? 1 : -1) * ArmCalculator.getVoltFac(extender.getExtension());
          double absError = Math.abs(error);

          double antiGravTorque = ArmCalculator.getAntiGravTorque(angler.getRevs(), extender.getExtension());

          if (absError > 3) {
            angler.setVoltage((fac * 1.5) + antiGravTorque);
          } else if (absError > 45) {
            angler.setVoltage((fac * 8.0) + antiGravTorque);
          } else {
            angling.val = false;
            stopContainer.val.run();
          }
        });

        scheduler.setTimeout(() -> {
          angling.val = false;
          stopContainer.val.run();
        }, 2);

        return null;
      };

      // arm pos presets if not already angling
      if (!angling.val) {
        if (con.getL1ButtonPressed()) {
          moveToAngle.map(0.0);
        }
        if (joystick.getRawButtonPressed(3)) {
          moveToAngle.map(0.0);
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