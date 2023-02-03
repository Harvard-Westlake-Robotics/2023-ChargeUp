package frc.robot.Drive;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

public class GearShifter {
    private Solenoid shift;

    public GearShifter(int channel) {
        shift = new Solenoid(PneumaticsModuleType.REVPH, channel);
    }

    public void setLowGear() {
        // actuate
        shift.set(true);
    }

    public void setHighGear() {
        // un-actuate
        shift.set(false);
    }
}