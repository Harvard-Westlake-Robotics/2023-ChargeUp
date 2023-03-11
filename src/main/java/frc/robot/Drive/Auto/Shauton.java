package frc.robot.Drive.Auto;

import javax.lang.model.util.ElementScanner14;

import frc.robot.Drive.Components.*;

/**
 * grace thy eyes upon the simplest and least accurate auton program known to man
 * Shit + auton = shauton
 */
public class Shauton {
    
    private double[] targetsList;
    int count = 0 ;

    DriveSide side;

    public Shauton (DriveSide side, int listSize)
    {
        this.side = side ;
        targetsList = new double [listSize] ;
    }

    // public double inchToEncoder (double inch)
    // {
    //     // 6 inch diam wheel ; 2048 tick encoder
    //     return inch / (Math.PI * 6) * 2048;
    // }

    public void addTarget (double inch)
    {
        if (count < targetsList.length)
        {
            // targetsList [count] = inchToEncoder(inch);
            targetsList [count] = inch ;
            count ++;
        }
    }


    boolean status = false ;
    int targetCounter = 0 ;
    int timeTicker = 0 ;
    public boolean getStatus ()
    {
        return status;
    }
    public void setStatus (boolean status)
    {
        this.status = status ;
    }
    public void driveArray ()
    {
        // if (timeTicker < targetsList[0])
        // {
        //     side.setPower(50);
        //     timeTicker ++;
        // }
        // side.setPower(0);
        if (targetCounter < targetsList.length && side.getPositionInches() >= targetsList[targetCounter])
        {
            side.setPower(0);
            targetCounter++;
        }
        else if (targetCounter < targetsList.length && side.getPositionInches() < targetsList[targetCounter])
        {
            side.setPower(50);
        }
    }

}
