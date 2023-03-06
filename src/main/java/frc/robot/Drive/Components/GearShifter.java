package frc.robot.Drive.Components;

import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
        SmartDashboard.putBoolean("shifterState", this.state) ;
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
        return this.state;
    }

    public boolean isLow()
    {
        return (this.state) ;
    }

    public void toggle() {
        this.state = !this.state ;
        pneumatic.toggle();
    }

    public void setLowGear() {
        // actuate
        this.state = true;
        pneumatic.set(kForward);
    }

    public void setHighGear() {
        // un-actuate
        this.state = false;
        pneumatic.set(kReverse);
    }

    public Command toggleShifterCommand ()
    {
        return this.run (() -> {
            toggle();
        });
        // System.out.println (this.state) ;
        // if (this.state == false)
        // {
        //     this.state = true;
        //     System.out.println ("true now") ;
        //     return this.run(() -> pneumatic.set(kForward));
        // }
        // else
        // {
        //     this.state = false;
        //     System.out.println ("false now") ;
        //     return this.run(() -> pneumatic.set(kReverse));
        // }

    }

}