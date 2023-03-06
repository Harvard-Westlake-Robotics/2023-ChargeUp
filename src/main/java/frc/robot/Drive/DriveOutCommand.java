// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Drive.Components.DriveBase;

public class DriveOutCommand extends CommandBase {
  private DriveBase drivebase;
  private final double DISTANCE_TO_DRIVE = 3; // TODO in feet, find exact distance
  private int cyclesToStop; // TODO find meaning of this variable

  /** Creates a new DriveOutCommand. */
  public DriveOutCommand(DriveBase drivebase) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drivebase = drivebase;

    addRequirements(drivebase);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivebase.resetYaw(0);
    drivebase.resetEncoder();
    cyclesToStop = 200;

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivebase.driveRobot(1, 1);
    cyclesToStop--;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (drivebase.getLeftDist() <= DISTANCE_TO_DRIVE || drivebase.getRightDist() <= DISTANCE_TO_DRIVE || cyclesToStop <= 0) {
      return true;
    }
    return false;
  }
}
