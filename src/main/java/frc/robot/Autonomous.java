package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import frc.robot.Arm.ArmCalculator;
import frc.robot.Arm.Auto.MoveArm;
import frc.robot.Arm.Components.ArmAngler;
import frc.robot.Arm.Components.ArmExtender;
import frc.robot.Core.Scheduler;
import frc.robot.Devices.Imu;
import frc.robot.Drive.Autolevel;
import frc.robot.Drive.Auto.DumbyDrive;
import frc.robot.Drive.Components.DriveSide;
import frc.robot.Drive.Components.GearShifter;
import frc.robot.DriverStation.LimeLight;
import frc.robot.Intake.Intake;
import frc.robot.Pneumatics.PneumaticsSystem;
import frc.robot.Util.Container;
import frc.robot.Util.DSAController;
import frc.robot.Util.Lambda;
import frc.robot.Util.PDController;
import frc.robot.Util.Promise;

public class Autonomous {

    private Scheduler scheduler;

    private DriveSide left;
    private DriveSide right;

    private GearShifter gearShifter;

    private ArmAngler angler;
    private ArmExtender extender;
    private Intake intake;
    private PneumaticsSystem pneumatics;

    private LimeLight limeLight;

    private Imu imu;

    public Autonomous(Scheduler scheduler, DriveSide left, DriveSide right, GearShifter gearShifter, ArmAngler angler,
            ArmExtender extender, Intake intake, PneumaticsSystem pneumatics, LimeLight limeLight, Imu imu) {
        this.scheduler = scheduler;
        this.left = left;
        this.right = right;
        this.gearShifter = gearShifter;
        this.angler = angler;
        this.extender = extender;
        this.intake = intake;
        this.pneumatics = pneumatics;
        this.limeLight = limeLight;
        this.imu = imu;
    }

    Lambda stopAntiGrav = null;

    private void stopAntiGrav() {
        if (stopAntiGrav != null)
            stopAntiGrav.run();
    }

    private void runAntiGrav() {
        stopAntiGrav();
        stopAntiGrav = scheduler.registerTick((double dTime) -> {
            angler.setVoltage(ArmCalculator.getAntiGravTorque(angler.getRevs(), extender.getExtension()));
        });
    }

    public void driveForwardDoNothing() {
        runAntiGrav();

        left.setPower(10);
        right.setPower(10);

        scheduler.setTimeout(() ->

        {
            left.setPower(0);
            right.setPower(0);
        }, 0.7);
    }

    public void scoreAndDoNothing() {
        runAntiGrav();

        intake.setVoltage(-10);

        scheduler.setTimeout(() -> {
            intake.setVoltage(0);
        }, 3);
    }

    public void scoreLowAndPlatform() {
        scoreLowAndPlatform(true);
    }

    public void scoreLowAndPlatform(boolean shouldScore) {

        scheduler.registerTick((double dTime) -> {
            angler.setVoltage(ArmCalculator.getAntiGravTorque(angler.getRevs(), extender.getExtension()) - 0.3);
        });

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

        scheduler.timeout((shouldScore) ? 2 : 0).then(() -> {

            intake.setVoltage(0);

        }).then(() -> {

            double speed = 30; // in/sec
            double dist = 76 + 80; // in

            var stopDriving = scheduler.registerTick((double dTime) -> {
                drive.incrementTarget(dTime * speed);
            });

            return scheduler.setTimeout(() -> {
                stopDriving.run();
            }, dist / speed);

        }).then(() -> {

            double speed = 30; // in/sec
            double dist = 80; // in

            var stopDriving = scheduler.registerTick((double dTime) -> {
                drive.incrementTarget(-dTime * speed);
            });

            return scheduler.setTimeout(() -> {
                stopDriving.run();
            }, dist / speed);

        }).then(() -> {

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

        });
    }

    public void scoreHighAndPlatform() {
        Promise.all(
                MoveArm.moveToAngle(-45, angler, extender, scheduler),
                MoveArm.extendToPos(22, extender, scheduler))
                .then(() -> {
                    runAntiGrav();
                    intake.setVoltage(-4);
                })
                .then(scheduler.timeout(2))
                .then(() -> {
                    stopAntiGrav();
                    intake.setVoltage(0);
                })
                .then(() -> {
                    return Promise.all(
                            MoveArm.moveToAngle(0, angler, extender, scheduler),
                            MoveArm.extendToPos(2, extender, scheduler));
                }).then(() -> {
                    scoreLowAndPlatform(false);
                });
    }
}