package frc.robot.Arm;

import org.opencv.features2d.AgastFeatureDetector;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Arm.Components.*;
import frc.robot.Arm.ArmConstants;

// store all calculations here

public class ArmCalculator {

    private static double LENGTH_LIMIT = ArmConstants.LENGTH_LIMIT;

    private final double HOLD_90DEGREE_FULLYEXTEND_VOLTAGE = 1 ;


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

    public static double holdPosVoltage (double angle, double extenderLength)
    {
        // 1.6 is appox constant volt needed to hold arm at horizontal & max extend
        if (Math.abs(angle) >= 45)
        {
            double val = (1.6 * Math.cos(angle) * (extenderLength / ArmConstants.LENGTH_LIMIT)) ;
            System.out.println (val);
            return val ;
        }
        return 0 ; // motor brake can prob hold it
    }
}
