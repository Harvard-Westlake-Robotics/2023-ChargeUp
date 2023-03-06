package frc.robot.DriverStation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PS4Controller;
// import edu.wpi.first.wpilibj.Joystick;

// import frc.robot.Arm.*;
// import frc.robot.Arm.Components.*;
import frc.robot.Drive.Components.DriveBase;
// import frc.robot.Drive.Components.GearShifter;
// import frc.robot.Intake.*;
// import frc.robot.Pneumatics.*;

public class Interface {
    public static void updateDashboard(DriveBase drive, PS4Controller con/**, GearShifter shifter, ArmAngler angler, ArmExtender extender, Intake intake, PneumaticsSystem pneumatics, PS4Controller con, Joystick joy*/) {
        // Drive
        // SmartDashboard.putNumber("Left Pos", drive.getLeft());
        // SmartDashboard.putNumber("Right Pos", drive.getRight());
        // SmartDashboard.putNumber("Left Dist", drive.getLeftDist());
        // SmartDashboard.putNumber("Right Dist", drive.getRightDist());

        // GearShifter
        // SmartDashboard.putBoolean("Shifter|State", shifter.getState());

        // Arm
        SmartDashboard.putNumber ("Angle", angler.getPosition());
        SmartDashboard.putNumber ("Extender:", extender.getExtension());

        // // SmartDashboard.putString ("Extender|Status", (extender.isOverExtended()) ? "OVER Extend" : (extender.isReverseExtended()) ? "UNDER Extend": "Normal");

        SmartDashboard.putNumber ("Max Height:", ArmConstants.HEIGHT_LIMIT);
        SmartDashboard.putNumber ("Max Length:", ArmConstants.LENGTH_LIMIT);
        SmartDashboard.putNumber ("Length:", ArmCalculator.xDistance(angler.getPosition(), extender.getExtension()));
        SmartDashboard.putNumber ("Height:", ArmCalculator.yDistance(angler.getPosition(), extender.getExtension()));

        // // Intake
        // SmartDashboard.putNumber ("Intake Volt", intake.getVoltage());

        // // Pneumatics
        // SmartDashboard.putNumber ("PSI:", pneumatics.getPressure());
        // SmartDashboard.putBoolean("Compressor:", pneumatics.isCompressorEnabled());

        // Controller
        SmartDashboard.putNumber ("Con|Left Y-Axis", con.getLeftY ());
        SmartDashboard.putNumber ("Con|Right Y-Axis", con.getRightY ());
        // SmartDashboard.putBoolean("Con|R2", con.getR2Button());

        // SmartDashboard.putBoolean("Joy|Trigger:", joy.getTrigger());
        // SmartDashboard.putNumber ("Joy|POV:", joy.getPOV());
        // SmartDashboard.putNumber ("Joy|Y-Axis:", joy.getY());
        // SmartDashboard.putNumber ("Joy|Z-Rotate:", joy.getZ());
    }
}
