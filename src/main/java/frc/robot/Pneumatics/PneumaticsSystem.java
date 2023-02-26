package frc.robot.Pneumatics;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.PneumaticHub;

public class PneumaticsSystem {
    
    private final PneumaticHub hub = new PneumaticHub(19);
    private final double minPressure;
    private final double maxPressure;

    public PneumaticsSystem (int minPressure, int maxPressure)
    {
        this.minPressure = minPressure;
        this.maxPressure = maxPressure;
    }

    public double getPressure()
    {
        return hub.getPressure(0) ;
    }

    public void autoRunCompressor ()
    {
        hub.enableCompressorAnalog(minPressure, maxPressure);
    }

    public boolean isCompressorEnabled()
    {
        return hub.getCompressor();
    }
}
