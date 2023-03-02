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
    public double angleTarget = 0;
    
    public void setAngleTarget(double angleTarget) {
        this.angleTarget = angleTarget;
    }
    public void incrementAngleTarget(double dAngle) {
        this.angleTarget += dAngle;
    }

    private double extensionTarget = 0;

    public void setExtensionTarget(double extensionTarget) {
        this.extensionTarget = extensionTarget;
    }
    public void incrementExtensionTarget(double dTar) {
        this.extensionTarget += dTar;
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

    public double volt = 0;
   
    public void tick(double dTime) {
        double angleCorrect = angleController.tick(angleTarget - armAngler.getPosition());
        this.volt = angleCorrect;
        if (Math.abs(angleCorrect) > 10)
            angleCorrect = (angleCorrect > 0) ? 10 : -10;
        armAngler.setVoltage(angleCorrect);
        // extender.setPower(extensionController.tick(extensionTarget - extender.getLength()));
    }

    public void resetController() {
        angleController.reset();
        extensionController.reset();

        extensionTarget = extender.getLength();
        angleTarget = armAngler.getPosition();
    }
}