package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import frc.robot.Drive.GearShifter;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and t0rigger mappings) should be declared here.
 */
public class RobotContainer {
    PS4Controller con = new PS4Controller(0);
  
    public RobotContainer ()
    {
        configureBindings();
    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
     * predicate, or via the named factories in {@link
     * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
     * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
     * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
     * joysticks}.
     */


    boolean isHighGear = false;

    private void configureBindings() {
      // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
      // if (!isHighGear && con.getR2ButtonPressed()) {
      //   isHighGear = true;
      //   shifter.setHighGear();
      // } else if (isHighGear && con.getR2ButtonPressed()) {
      //   isHighGear = false;
      //   shifter.setLowGear();
      // }
  
      // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
      // cancelling on release.
      //m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
    }
  
    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
      // An example command will be run in autonomous
      //return Autos.exampleAuto(m_exampleSubsystem);
      return null;
    }
  }
  