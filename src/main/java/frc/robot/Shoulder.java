// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class Shoulder extends PIDSubsystem {
  /** Creates a new Shoulder. */

  private ArmFeedforward shoFF;  // keeps arm from falling 

  private CANSparkMax shoulder;
  private DutyCycleEncoder shoEnc; // shoulder encoder


  public Shoulder() {
    super(
        // The PIDController used by the subsystem
        new PIDController(12.0 / 90, 0, 0));

        shoFF = new ArmFeedforward(0, 1.6, 0);  // TODO find constants 

        shoulder = new CANSparkMax(7, MotorType.kBrushless);
        shoEnc = new DutyCycleEncoder(0);
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here

    shoulder.setVoltage(output + shoFF.calculate(setpoint, 15));  // TODO figure out how fast we want it to move

  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    double encVal = shoEnc.getAbsolutePosition() * 360;

    SmartDashboard.putNumber("shoPos", encVal);  // see what all positions are :D

    return encVal;
  }

  public void kindaManual(double move) {
    if(Math.abs(move) > 0.2) {
      setSetpoint(move + getMeasurement());
    }
  }
}