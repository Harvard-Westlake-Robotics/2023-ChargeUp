package frc.robot.Arm;

import frc.robot.Util.PDController;

// actually runs motors
public class ArmPID {

    Arm arm;
    PDController controller;

    public ArmPID(Arm arm, PDController controller) {
        this.arm = arm;
        this.controller = controller;
    }

}