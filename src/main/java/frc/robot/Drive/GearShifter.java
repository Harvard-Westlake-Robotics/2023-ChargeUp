package frc.robot.Drive;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

public class GearShifter {
    private Solenoid seanIsHot;

    public GearShifter(int channel) {
        seanIsHot = new Solenoid(PneumaticsModuleType.REVPH, channel);
    }

    public void setLowGear() {
        // actuate
        seanIsHot.set(true);
    }

    public void setHighGear() {
        // un-actuate
        seanIsHot.set(false);
    }

    

}