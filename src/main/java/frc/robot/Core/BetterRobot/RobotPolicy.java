package frc.robot.Core.BetterRobot;

import frc.robot.Core.Scheduler;

public class RobotPolicy {
    Phase teleop;
    Phase auto;
    Phase test;
    Phase disabled;

    public RobotPolicy(Phase teleop) {
        this.teleop = teleop;
    }

    public RobotPolicy(Phase teleop, Phase auton) {
        this.teleop = teleop;
        this.auto = auton;
    }

    public void teleop(Scheduler scheduler) {
        if (teleop != null)
            teleop.exec(scheduler);
    }

    public void auto(Scheduler scheduler) {
        if (auto != null)
            auto.exec(scheduler);
    }

    public void test(Scheduler scheduler) {
        if (test != null)
            test.exec(scheduler);
    }

    public void disabled(Scheduler scheduler) {
        if (disabled != null)
            disabled.exec(scheduler);
    }
}
