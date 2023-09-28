package frc.robot.Util;

import edu.wpi.first.wpilibj.Timer;

public class DeSpam {
    private double lastTime = 0;
    private double minTimeDiff;

    public DeSpam(double minTimeDiffSec) {
        this.minTimeDiff = minTimeDiffSec;
    }

    public boolean exec(Lambda func) {
        if (Timer.getFPGATimestamp() - lastTime > minTimeDiff) {
            func.run();
            lastTime = Timer.getFPGATimestamp();
            return true;
        }
        return false;
    }
}
