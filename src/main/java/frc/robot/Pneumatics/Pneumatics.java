package frc.robot.Pneumatics;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.PneumaticHub;

public class Pneumatics {
    
    private final PneumaticHub hub = new PneumaticHub(1);
    private final double minPressure = 80;
    private final double maxPressure = 120;

    public double getPressure()
    {
        return hub.getPressure(0) ;
    }

    public void autoRunCompressor ()
    {
        hub.enableCompressorAnalog(minPressure, maxPressure);
    }

}
