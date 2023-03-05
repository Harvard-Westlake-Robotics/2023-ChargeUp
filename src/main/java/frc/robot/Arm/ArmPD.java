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
    public double extensionTarget = 0;

    public void incrementAngleTarget(double dAngle) {
        this.angleTarget += dAngle;
    }
    public void setAngleTarget(double angleTarget) {
        this.angleTarget = angleTarget;
    }

    public void incrementExtensionTarget(double dAngle) {
        this.extensionTarget += dAngle;
    }
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

    public double extenderCorrect = 0;
    public double angleCorrect = 0;
   
    public void tick(double dTime) {
        this.angleCorrect = angleController.solve(angleTarget - armAngler.getPosition());
        // this.angleCorrect += ArmCalculator.getAntiGravTorque(armAngler.getPositio n());
        armAngler.setVoltage(angleCorrect);
        this.extenderCorrect = extensionController.solve(extensionTarget - extender.getLength());
        extender.setPower(extenderCorrect);
    }

    public void resetController() {
        angleController.reset();
        extensionController.reset();

        angleTarget = armAngler.getPosition();
        extensionTarget = extender.getLength();
    }
}