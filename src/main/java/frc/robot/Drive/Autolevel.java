package frc.robot.Drive;

import edu.wpi.first.math.MathUtil;
import frc.robot.Devices.Imu;
import frc.robot.Util.PDController;

public class Autolevel {
    public static double autolevel(PDController levelingController, Imu imu) {
        double max = 3;
        return MathUtil.clamp(levelingController.solve(0 - imu.getPitch()), -max, max);
    }
}
