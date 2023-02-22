package frc.robot.Arm;

import java.math.*;
import frc.robot.Arm.ArmConstants;

// store all calculations here

public class ArmCalculator {

    private static double LENGTH_LIMIT = ArmConstants.LENGTH_LIMIT;

    // return max rotation given length
    public static double maxRotation(double length) {
        double a = Math.cos(LENGTH_LIMIT / length); // rad
        return Math.toDegrees(a);
    }

    // return max length given rotation
    public static double maxLength(double angle) {
        double a = Math.toRadians(angle);
        double maxLengthAccordingToHeightLim = Math.abs((ArmConstants.HEIGHT_LIMIT / Math.cos(a))); // in
        double maxLengthAccordingToLengthLim = Math.abs((ArmConstants.LENGTH_LIMIT / Math.sin(a))); // in
        return Math.min(maxLengthAccordingToHeightLim, maxLengthAccordingToLengthLim);
    }
}
