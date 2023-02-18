package frc.robot.Arm;

import frc.robot.Util.PDController;

// actually runs motors
public class ArmPID {

    ArmSwivel arm;

    PDController controller;

    public ArmPID(ArmSwivel arm, PDController controller) {
        this.arm = arm;
        this.controller = controller;
    }

}