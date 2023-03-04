package frc.robot.Arm ;

import frc.robot.Arm.Components.ArmExtender;
import frc.robot.Arm.Components.ArmAngler;

// arm control for drivers
public class ArmP
{
    ArmAngler angler;
    ArmExtender extender;

    double[] scoreAnglePresets =  {0,         30,     90,     120};
                                //{default,   high,   mid,    low}
    double[] scoreLengthPresets = {35,                62.5,   35};
                                //{default,   high,   mid,    low}

    public ArmP (ArmAngler angler, ArmExtender extender)
    {
        this.angler = angler ;
        this.extender = extender ;
    }
}