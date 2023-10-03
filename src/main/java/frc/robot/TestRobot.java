package frc.robot;

import frc.robot.Core.BetterRobot.BetterRobot;
import frc.robot.Core.BetterRobot.RobotPolicy;
import frc.robot.Devices.Encoder;

public class TestRobot extends BetterRobot {

  public RobotPolicy init() {
    
    var encoder = new Encoder(4, 5, false);

    return new RobotPolicy((scheduler) -> {
      System.out.println(encoder.getRevs() + "revs");
    });
  }
}
