package frc.robot.Util;

public class AngleMath {
    public static double minMagnitude(double... nums) {
        double min = nums[0];
        for (double num : nums) {
            if (Math.abs(num) < Math.abs(min))
                min = num;
        }
        return min;
    }

    public static double getDelta(double current, double target) {
        double diff = target - current;
        return conformAngle(diff);
    }

    public static boolean shouldReverseCorrect(double current, double target) {

        var frontFaceError = AngleMath.getDelta(current, target);
        var backFaceError = AngleMath.getDelta(current, target - 180);
        var error = AngleMath.minMagnitude(frontFaceError, backFaceError);

        return error == frontFaceError;
    }

    public static double getDeltaReversable(double current, double target) {
        var frontFaceError = AngleMath.getDelta(current, target);
        var backFaceError = AngleMath.getDelta(current, target - 180);
        var error = AngleMath.minMagnitude(frontFaceError, backFaceError);

        return error;
    }

    /**
     * Makes the angle between (-180, 180]
     */
    public static double conformAngle(double angle) {
        angle %= 360;
        if (angle > 180) {
            angle -= 360;
        } else if (angle <= -180) {
            angle += 360;
        }
        return angle;
    }

    /**
     * converts an angle in standard position to an angle from the front to the
     * right
     */
    public static double toTurnAngle(double standardPositionAngle) {
        return AngleMath.conformAngle(90 - standardPositionAngle);
    }

    public static double toStandardPosAngle(double turnAngle) {
        return toTurnAngle(turnAngle);
    }
}
