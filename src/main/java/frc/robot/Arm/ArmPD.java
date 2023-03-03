package frc.robot.Arm;

import frc.robot.Util.PDController;
import frc.robot.Util.Tickable;
import frc.robot.Arm.Components.ArmAngler;
import frc.robot.Arm.Components.ArmExtender;

// joystick controls
// keep robot legal while in match

public class ArmPD implements Tickable {

    private ArmAngler armAngler;
    private ArmExtender extender;
    private PDController angleController;
    private PDController extensionController;
    private double angleTarget = 0;
    
    public void setAngleTarget(double angleTarget) {
        this.angleTarget = angleTarget;
    }

    private double extensionTarget = 0;

    public void setExtensionTarget(double extensionTarget) {
        this.extensionTarget = extensionTarget;
    }

    // arm limits (changing)
    // will be updated based on changes in arm length / pos
    public double currentLengthMax; // based on rotation
    public double currentRotateMax; // based on length
    // current length / pos
    public double currentLength = 35; // starts out at min length
    public double currentAngle = 0; // starts facing ups

    public ArmPD(ArmAngler armAngler, ArmExtender armExtender, PDController angleController, PDController extensionController) {
        this.armAngler = armAngler;
        this.extender = armExtender;
        this.angleController = angleController;
        this.extensionController = extensionController;
    }
   
    public void tick(double dTime) {
        double angleCorrect = angleController.tick(angleTarget - armAngler.getPosition());
        
        armAngler.setVoltage(angleController.tick(angleTarget - armAngler.getPosition()));
        extender.setPower(extensionController.tick(extensionTarget - extender.getLength()));
    }

    //! idk where to put this
    public void moveExtender(int pov){
        int isRev = pov == 0 ? 1 : pov == 180 ? -1: 0;
        extender.setPower(extensionController.tick(isRev * (extensionTarget - extender.getLength())));
    }



    public void resetController() {
        angleController.reset();
        extensionController.reset();
    }
}