package frc.robot.Core.BetterRobot;

import frc.robot.RobotOld;
import frc.robot.Core.Scheduler;

public abstract class BetterRobot extends RobotOld {
    private final Scheduler scheduler = new Scheduler();
    RobotPolicy policy;

    public abstract RobotPolicy init();

    public void robotInit() {
        this.policy = init();
    }

    @Override
    public void teleopInit() {
        scheduler.clear();
        policy.teleop(scheduler);
    }

    @Override
    public void teleopPeriodic() {
        scheduler.tick();
    }
    
    @Override
    public void autonomousInit() {
        scheduler.clear();
        policy.auto(scheduler);
    }

    @Override
    public void autonomousPeriodic() {
        scheduler.tick();
    }

    @Override
    public void testInit() {
        scheduler.clear();
        policy.test(scheduler);
    }

    @Override
    public void testPeriodic() {
        scheduler.tick();
    }

    @Override
    public void disabledInit() {
        scheduler.clear();
        policy.disabled(scheduler);
    }

    @Override
    public void disabledPeriodic() {
        scheduler.tick();
    }
}
