package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType ;
import edu.wpi.first.wpilibj.Solenoid ;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class GearShifter extends SubsystemBase
{
    private Solenoid shift ;

    public GearShifter ()
    {
        shift = new Solenoid(PneumaticsModuleType.REVPH, 0) ;
    }


    public void setLowGear ()
    {
        // actuate
        shift.set(true);
    }

    public void setHighGear ()
    {
        // un-actuate
        shift.set(false);
    }

    // method is called once every 20 ms
    @Override
    public void periodic ()
    {

    }
}