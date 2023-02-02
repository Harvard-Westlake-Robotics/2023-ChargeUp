
package frc.robot.Commands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Drive.DriveSide;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command;

public class AutonPP{
  /** Example static factory for an autonomous command. */
  //private DriveTrain dt;
  
  public static CommandBase exampleAuto(DriveTrain subsystem) {
    
    return Commands.sequence();
  }

  private AutonPP(DriveTrain dt) {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}


