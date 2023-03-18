package frc.robot.Arm.Auto;

import frc.robot.Arm.ArmCalculator;
import frc.robot.Arm.Components.ArmAngler;
import frc.robot.Arm.Components.ArmExtender;
import frc.robot.Core.Scheduler;
import frc.robot.Util.Container;
import frc.robot.Util.Lambda;
import frc.robot.Util.Promise;

public class MoveArm {

    public static Promise moveToAngle(double targetAngle, ArmAngler angler, ArmExtender extender, Scheduler scheduler) {
        Promise prom = new Promise();

        Container<Lambda> stopContainer = new Container<Lambda>(null);
        stopContainer.val = scheduler.registerTick((double i_dTime) -> {
            double error = targetAngle - angler.getDegrees();
            double fac = ((error > 0) ? 1 : -1) * ArmCalculator.getVoltFac(extender.getExtension());
            double absError = Math.abs(error);

            System.out.println(error);

            double antiGravTorque = ArmCalculator.getAntiGravTorque(angler.getRevs(), extender.getExtension());
            if (absError > 45) {
                angler.setVoltage((fac * 5.0) + antiGravTorque);
            } else if (absError > 5) {
                angler.setVoltage((fac * 1.5) + antiGravTorque);
            } else {
                scheduler.setTimeout(() -> {
                    stopContainer.val.run();
                    angler.setVoltage(0);
                    prom.resolve();
                }, i_dTime);
            }
        });

        scheduler.setTimeout(() -> {
            stopContainer.val.run();
            angler.setVoltage(0);
            prom.resolve();
        }, 2);

        return prom;
    };

    public static Promise extendToPos(double targetExtension, ArmExtender extender, Scheduler scheduler) {
        Promise prom = new Promise();

        Container<Lambda> stopContainer = new Container<Lambda>(null);
        stopContainer.val = scheduler.registerTick((double i_dTime) -> {
            double error = targetExtension - extender.getExtension();
            double fac = (error > 0) ? 1 : -1;
            double absError = Math.abs(error);

            if (absError > 2) {
                extender.setPower(fac * 30.0);
            } else {
                scheduler.setTimeout(() -> {
                    stopContainer.val.run();
                    extender.setPower(0);
                    prom.resolve();
                }, i_dTime);
            }
        });

        scheduler.setTimeout(() -> {
            stopContainer.val.run();
            extender.setPower(0);
            prom.resolve();
        }, 2);

        return prom;
    };
}
