package frc.robot.Drive.Components;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class GearShifter extends SubsystemBase {
    private DoubleSolenoid pneumatic;
    private boolean state = false;

    public GearShifter(int falseChannel, int trueChannel, int module) {
        pneumatic = new DoubleSolenoid(module, PneumaticsModuleType.REVPH, falseChannel, trueChannel);
    }

    @Override
    public void periodic() {
      // This method will be called once per scheduler run
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

}