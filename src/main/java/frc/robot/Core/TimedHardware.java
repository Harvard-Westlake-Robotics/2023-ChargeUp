package frc.robot.Core;

import frc.robot.Util.Tickable;

public abstract class TimedHardware implements Tickable {
    public TimedHardware() {
        Scheduler.getInstance().registerGlobalTickUnclearableAlways(this);
    }

    public abstract void tick(double dTime);
}
