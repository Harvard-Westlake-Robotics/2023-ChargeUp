package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import frc.robot.Arm.ArmCalculator;
import frc.robot.Arm.Components.ArmAngler;
import frc.robot.Arm.Components.ArmExtender;
import frc.robot.Core.Scheduler;
import frc.robot.Core.BetterRobot.BetterRobot;
import frc.robot.Core.BetterRobot.RobotPolicy;
import frc.robot.Devices.Imu;
import frc.robot.Devices.Motor.SparkMax;
import frc.robot.Drive.Drive;
import frc.robot.Drive.Auto.AutonomousDrive;
import frc.robot.Drive.Auto.DriveSidePD;
import frc.robot.Drive.Components.DriveSide;
import frc.robot.Drive.Components.GearShifter;
import frc.robot.DriverStation.LimeLight;
import frc.robot.Intake.Intake;
import frc.robot.Pneumatics.PneumaticsSystem;
import frc.robot.Util.PDController;
import frc.robot.Util.Pair;
import frc.robot.Util.Promise;
import frc.robot.Util.ScaleInput;
import frc.robot.Devices.Encoder;

public class Robot extends BetterRobot {
    public RobotPolicy init() {
        // Drive
        DriveSide left;
        DriveSide right;

        // Subsystems
        ArmExtender extender;
        ArmAngler angler;
        Intake intake;

        // Pneumatics
        GearShifter shifter;
        PneumaticsSystem pneumatics;

        // Sensors
        LimeLight limeLight = new LimeLight();
        var imu = new Imu(18);

        // Controllers
        var con = new PS4Controller(0);
        var joystick = new Joystick(1);

        { // Component initalization
          // Drive
            var leftFront = new SparkMax(11, true, false);
            var leftBack = new SparkMax(2, true, false);
            var leftTop = new SparkMax(1, false, false);
            var encoderLeft = new Encoder(6, 7, false);

            var rightFront = new SparkMax(15, false, false);
            var rightBack = new SparkMax(5, false, false);
            var rightTop = new SparkMax(4, true, false);
            var encoderRight = new Encoder(8, 9, true);

            left = new DriveSide(leftFront, leftBack, leftTop, encoderLeft);
            right = new DriveSide(rightFront, rightBack, rightTop, encoderRight);

            // Arm
            var arm1 = new SparkMax(25, false, true);
            var arm2 = new SparkMax(12, false, true);
            var encoder = new Encoder(0, 1, true);
            angler = new ArmAngler(arm1, arm2, encoder);
            angler.setBrake(true);

            // Extender
            var extender1 = new SparkMax(10, true, true);
            var extender2 = new SparkMax(16, true, true);
            extender = new ArmExtender(extender1, extender2);

            // Intake
            var intakeRight = new SparkMax(7, true);
            var intakeLeft = new SparkMax(6, false);
            intake = new Intake(intakeLeft, intakeRight);

            // Pneumatics
            pneumatics = new PneumaticsSystem(110, 120, 19);
            pneumatics.autoRunCompressor();

            // Shifter
            shifter = new GearShifter(2, 0, 19);

        }

        return new RobotPolicy(
                // ! TELEOP
                (Scheduler scheduler) -> {
                    // Sensors
                    imu.resetYaw();
                    limeLight.setDriverMode();

                    // Drive
                    Drive drive = new Drive(left, right);
                    drive.resetEncoders();

                    left.setCurrentLimit(70, 100);
                    right.setCurrentLimit(70, 100);

                    logTemps(left, right, scheduler); // logs drive motor temps every few seconds

                    // Logs
                    scheduler.setInterval(() -> {
                        System.out.println(right.getPositionInches());
                    }, 2);

                    // registerTick calls the tick function on an object (or a lambda function
                    // passed into it) every tick to run until the robot is disabled

                    scheduler.registerTick((double dTime) -> {
                        // Drive Control
                        final double deadzone = 0.05;
                        final double turnCurveIntensity = 4.5;
                        final double pwrCurveIntensity = 5;
                        final Pair<Double> powers = ScaleInput.scale(
                                con.getLeftY(),
                                con.getRightX(),
                                deadzone,
                                turnCurveIntensity,
                                pwrCurveIntensity);
                        if (Math.abs(powers.left) >= Math.abs(powers.right))
                            drive.setPower(powers.left, powers.left);
                        else {
                            drive.setPower(-powers.right + powers.left, powers.right + powers.left);
                        }

                        // Extender Control
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

                        // Intake Control
                        if (joystick.getTrigger() && joystick.getRawButton(2))
                            intake.setVoltage(1.7);
                        else if (joystick.getTrigger() || con.getR1Button())
                            intake.setVoltage(7);
                        else if (joystick.getRawButton(2))
                            intake.setVoltage(-3); // outtake
                        else
                            intake.setVoltage(0);

                        // Gearshifting Control (brakes when shifted)
                        if (con.getR2ButtonPressed()) {
                            shifter.toggle();
                            left.setBrake(!left.isMotorBraking());
                            right.setBrake(!right.isMotorBraking());
                        }

                        // Angler Control (with a quirky special function that makes the arm not sag)
                        angler.setVoltage(
                                joystick.getY() * 5// the input from the joystick
                                        + ArmCalculator.getAntiGravTorque(angler.getRevs(), extender.getExtension())
                        // how
                        // much
                        // voltage
                        // it
                        // takes
                        // to
                        // hold
                        // the
                        // arm
                        // up
                        // --------------------------------------------------------------------- the
                        // formatter did that ^
                        );
                    });
                },
                // ! AUTON
                (Scheduler scheduler) -> {
                    shifter.setLowGear();

                    // positive is backward
                    // scheduler.setInterval(() -> {

                    var controller = new PDController(2, 10).withMagnitude(5);

                    var leftPD = new DriveSidePD(left, controller, controller);
                    var rightPD = new DriveSidePD(right, controller, controller);
                    var autodrive = new AutonomousDrive(scheduler, leftPD, rightPD, shifter, imu);

                    var cancel = scheduler.registerTick((dTime) -> {
                        leftPD.setPercentVoltage(leftPD.getCorrection(true));
                        rightPD.setPercentVoltage(rightPD.getCorrection(true));
                    });

                    Promise.instant().then(() -> {
                        return autodrive.goFor(30, 10, 20);
                    })
                            .then(() -> {
                                System.out.println("done");
                                // scheduler.setTimeout(() -> {
                                // cancel.run();

                                // }, 1);
                            });
                    // }, 20);

                });
    }

    private static void logTemps(DriveSide left, DriveSide right, Scheduler scheduler) {
        scheduler.setInterval(() -> {
            var ltemps = left.getTemps();
            var rtemps = right.getTemps();

            System.out.println(
                    "left temps - front: " + ltemps[0] + " back: " + ltemps[1] + " top: " + ltemps[2]);
            System.out.println(
                    "right temps - front: " + rtemps[0] + " back: " + rtemps[1] + " top: " + rtemps[2]);
        }, 5);
    }
}
