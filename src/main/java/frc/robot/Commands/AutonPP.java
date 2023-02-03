
package frc.robot.Commands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Drive.DriveSide;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command;

public final class AutonPP{
  /** Example static factory for an autonomous command. */
  
  public static CommandBase exampleAuto(DriveTrain subsystem) {
    

    return Commands.sequence(new DriveForwardCommand(subsystem));
  }

  private AutonPP(DriveTrain dt) {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}


