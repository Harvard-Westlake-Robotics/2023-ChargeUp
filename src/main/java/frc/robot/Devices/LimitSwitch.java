package frc.robot.Devices;

import edu.wpi.first.wpilibj.DigitalInput;

public class LimitSwitch {
    private DigitalInput switchIn;

    public LimitSwitch(int gpioPort) {
        switchIn = new DigitalInput(gpioPort);
    }

    public boolean get() {
        return !switchIn.get();
    }
}
