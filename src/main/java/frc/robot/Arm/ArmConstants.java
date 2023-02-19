package frc.robot.Arm ;

// once complete, combine with Constants.java


// all lengths are from Center of Rotation
public class ArmConstants
{
    // MUST REMEASURE VIA CAD OR ROBOT

    // robot size limits
    public static final double HEIGHT_LIMIT = 58 ; // in
    public static final double LENGTH_LIMIT = 62.5 ; // in

    // arm limits
    public static final double LENGTH_ABS_MAX = 85 ; // in
        // absolutely max length for arm in any state (hypotenuse of LENGTH_LIMIT x HEIGHT_LIMIT triangle)
    public static final double LENGTH_ABS_MIN = 35 ; // in
        // absolutely min length in any state (fully retracted)

    public static final double ROTATE_ABS_MAX = 120 ; // degrees
        // absolutely max angle for arm (fully retracted and towards ground)
}
