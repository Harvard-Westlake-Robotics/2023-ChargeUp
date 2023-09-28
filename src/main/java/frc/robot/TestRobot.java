package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Devices.Motor.SparkMax;
import frc.robot.Drive.Drive;
import frc.robot.Core.BetterRobot.BetterRobot;
import frc.robot.Core.BetterRobot.RobotPolicy;
import frc.robot.Devices.Encoder;

public class TestRobot extends BetterRobot {

  public RobotPolicy init() {
    
    var encoder = new Encoder(4, 5, false);

    return new RobotPolicy((scheduler) -> {
      System.out.println(encoder.getRevs());
    });
  }
}
