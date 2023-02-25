package frc.robot.Drive;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class GearShifter {
    private DoubleSolenoid pneumatic;
    boolean state = false ;

    public GearShifter(int falseChannel, int trueChannel) {
        pneumatic = new DoubleSolenoid(PneumaticsModuleType.REVPH, falseChannel, trueChannel);
    }

    public void setState(boolean state)
    {
        this.state = state ;

    }

    public boolean getState ()
    {
        return state ;
    }

    public void setLowGear() {
        // actuate
        state = true ;
        pneumatic.set(Value.kForward);
    }

    public void setHighGear() {
        // un-actuate
        state = false ;
        pneumatic.set(Value.kReverse);
    }

    

}