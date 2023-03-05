package frc.robot.Drive.Components;

import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class GearShifter extends SubsystemBase {
    private DoubleSolenoid pneumatic;
    private boolean state = false;
    
    private final PneumaticHub hub = new PneumaticHub(19);;
    private final double minPressure = 100;
    private final double maxPressure = 125;

    public GearShifter(int falseChannel, int trueChannel, int module) {
        pneumatic = new DoubleSolenoid(module, PneumaticsModuleType.REVPH, falseChannel, trueChannel);
    }

    public double getPressure() {
        return hub.getPressure(0);
    }

    public void autoRunCompressor() {
        hub.enableCompressorAnalog(minPressure, maxPressure);
    }

    public boolean isCompressorEnabled() {
        return hub.getCompressor();
    }

    @Override
    public void periodic()
    {
        // autoRunCompressor();
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

    public boolean isLow()
    {
        return (state) ;
    }

    public void toggle() {
        state = !state ;
        pneumatic.toggle();
    }

    public void setLowGear() {
        // actuate
        state = true;
        pneumatic.set(kForward);
    }

    public void setHighGear() {
        // un-actuate
        state = false;
        pneumatic.set(kReverse);
    }

    public Command toggleShifterCommand (boolean setLow)
    {
        if (setLow)
            return this.run(() -> pneumatic.set(kForward));
        else
            return this.run(() -> pneumatic.set(kReverse));
    }

}