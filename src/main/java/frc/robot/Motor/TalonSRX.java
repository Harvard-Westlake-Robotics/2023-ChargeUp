package frc.robot.Motor;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonSRX {
    private WPI_TalonSRX maxspark;
    private SensorCollection encoder;
    private boolean isReversed;

    public TalonSRX(boolean isReversed) {
        this.maxspark = new WPI_TalonSRX(0);
        this.encoder = maxspark.getSensorCollection();
        this.isReversed = isReversed;
    }

    public void setVoltage(double volts) {
        maxspark.setVoltage((isReversed) ? -volts : volts);
        /**
         * This is a ternerary
         * Equivalent to
         * 
         * if (isReversed)
         * maxspark.setVoltage(-volts);
         * else
         * maxspark.setVoltage(volts);
         * 
         * If you don't understand and need to make a change, you can uncomment this
         * code
         */
    }

    public double getPosition() {
        return (isReversed) ? -maxspark.getSelectedSensorPosition() : maxspark.getSelectedSensorPosition();
    }

    public void stop() {
        maxspark.stopMotor();
    }

    public void resetEncoder() {
        maxspark.setSelectedSensorPosition(0);
    }
}
