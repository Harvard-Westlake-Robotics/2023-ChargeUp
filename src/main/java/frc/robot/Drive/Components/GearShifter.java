package frc.robot.Drive.Components;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class GearShifter {
    private DoubleSolenoid pneumatic;
    boolean state = false;

    public GearShifter(int falseChannel, int trueChannel, int module) {
        pneumatic = new DoubleSolenoid(module, PneumaticsModuleType.REVPH, falseChannel, trueChannel);
        state = false;
    }

    public void setState(boolean state) {
        this.state = state;
        if (!state)
            setHighGear();
        else
            setLowGear();
    }

    public boolean getState() {
        return state;
    }

    public void toggle() {
        setState(!getState());
    }

    public void setLowGear() {
        // actuate
        state = true;
        pneumatic.set(kReverse);
    }

    public void setHighGear() {
        // un-actuate
        state = false;
        pneumatic.set(kForward);
    }

}