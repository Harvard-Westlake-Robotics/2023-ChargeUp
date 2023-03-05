package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// import subsystems
import frc.robot.Drive.Components.DriveBase;
import frc.robot.Drive.Components.GearShifter;
// import frc.robot.Intake.Intake;
// import frc.robot.Arm.Components.ArmAngler;
// import frc.robot.Arm.Components.ArmExtender;
// import frc.robot.Pneumatics.PneumaticsSystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// import controllers
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  /**
   * SUBSTYSTEMS
   */
  // Driveable
  // private final ArmAngler armAngler = new ArmAngler();
  // private final ArmExtender armExtender = new ArmExtender();
  private final DriveBase drivebase = new DriveBase();
  private final GearShifter gearShifter = new GearShifter(2,0,19);
  // private final Intake intake = new Intake();
  // private final PneumaticsSystem pneumatics = new PneumaticsSystem();
  // Support
  // SmartDashboard
  // LimeLight
  // Controllers
  private final CommandPS4Controller con = new CommandPS4Controller(0);
  // private final CommandJoystick joy = new CommandJoystick(1);

  // Commands
  private final SequentialCommandGroup testStuffXD = new SequentialCommandGroup(

  );

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {




    // define drivetrain
    drivebase.setDefaultCommand(Commands.run(() -> drivebase.driveRobot(con.getLeftY(), con.getRightY()), drivebase)) ;

    // define gearshifter
    gearShifter.setDefaultCommand(Commands.run(() -> gearShifter.setLowGear(), gearShifter));


    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    con.R1().onTrue(gearShifter.toggleShifterCommand());
    // con.R2().onTrue(gearShifter.toggleShifterCommand(false));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is
    // pressed,
    // cancelling on release.
    // m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    // return Autos.exampleAuto(m_exampleSubsystem);
    return null;
  }
}