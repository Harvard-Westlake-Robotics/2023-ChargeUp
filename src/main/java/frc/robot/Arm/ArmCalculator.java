package frc.robot.Arm;

import java.math.*;
import frc.robot.Arm.ArmConstants ;

// store all calculations here

public class ArmCalculator {
    
    private double LENGTH_LIMIT = ArmConstants.LENGTH_LIMIT ;

    // return max rotation given length
    public double maxRotation (double length)
    {
        double a = Math.cos(LENGTH_LIMIT / length) ; // rad
        return Math.toDegrees (a) ;
    }

    // return max length given rotation
    public double maxLength (double angle)
    {
        double a = 1 / Math.toRadians (angle) ;
        return (Math.acos(a) * LENGTH_LIMIT) ; // in
    }

}
