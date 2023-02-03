package frc.robot.Commands;




import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class DriveForwardCommand extends CommandBase {
  private DriveTrain drivetrain;
  
  private final double DISTANCE_TO_DRIVE = -3;//in ft
  private  int cyclesToStop;

  /** Creates a new DriveForwardCommand. */
  public DriveForwardCommand(DriveTrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drivetrain = drivetrain;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // drivetrain.resetYaw(0);
    // drivetrain.resetEncoders();
    
    cyclesToStop = 200;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.driveRobot(1, 1);
    cyclesToStop--;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // if(drivetrain.getLeftDistance() <= DISTANCE_TO_DRIVE || drivetrain.getRightDistance() <= DISTANCE_TO_DRIVE || cyclesToStop <= 0) {
    //   return true;
    // }
    // return false;
    if(cyclesToStop <= 0) return true;
    else return false;
  }
}