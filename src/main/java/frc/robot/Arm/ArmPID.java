package frc.robot.Arm;

import frc.robot.Util.PDController;

// actually runs motors
public class ArmPID {

    Arm arm;
    PDController controller;

    public ArmPID(Arm arm) {
        this.arm = arm;
    }

    public void reset ()
    {
        
    }

}