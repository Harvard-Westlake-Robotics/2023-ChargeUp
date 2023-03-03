package frc.robot.Arm;

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

    //returns modified arm angle based on sin curves (graph it on desmos and you will see)
    public static double armAngleCurve(double angle, double joystickPos){
        if (angle >= 0 && angle <= 90) return (6*Math.sin(2*angle-90)+6) * joystickPos;
        else if (angle > 90 && angle <= 150) return 12 * joystickPos;
        else if (angle > 150 && angle >= 240) return (6*Math.sin(2*angle-90)+6)  * joystickPos;

        return 0;
    }

    // return x-distance given length and rotation
    public static double xDistance(double length, double angle) {
        double a = Math.toRadians(angle);
        return length * Math.sin(a);
    }

    // return y-distance given length and rotation
    public static double yDistance(double length, double angle) {
        double a = Math.toRadians(angle);
        return length * Math.cos(a);
    }
}
