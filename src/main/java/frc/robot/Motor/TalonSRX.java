package frc.robot.Motor;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class TalonSRX {
    private WPI_TalonFX maxspark;
    private boolean isReversed;
    boolean isStallable;

    public TalonSRX(int deviceNumber, boolean isReversed, boolean isStallable) {
        this.maxspark = new WPI_TalonFX(deviceNumber);
        maxspark.getSensorCollection();
        this.isReversed = isReversed;
        this.isStallable = isStallable;

        /* newer config API */
        TalonFXConfiguration configs = new TalonFXConfiguration();
        /* select integ-sensor for PID0 (it doesn't matter if PID is actually used) */
        configs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        /* config all the settings */
        maxspark.configAllSettings(configs);
    }

    public TalonSRX(int deviceNumber, boolean isReversed) {
        this(deviceNumber, isReversed, false);
    }

    public void setPercentVoltage(double percent) {
        setVoltage(percent * (12.0 / 100.0));
    }

    public void setVoltage(double volts) {
        if (Math.abs(volts) > 12.0)
            throw new Error("Illegal voltage");
        if (isStallable) {
            double fac = (volts > 0) ? 1 : -1;
            if (Math.abs(volts) < 10.0 * (12.0 / 100.0)) {
                maxspark.setVoltage(0);
            } else if (Math.abs(volts) < 20 * (12.0 / 100.0)) {
                maxspark.setVoltage(20 * (12.0 / 100.0) * fac);
            }
        }
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
         * If you don't understand and need to make a change, you can
         * uncomment this
         * code
         */
    }

    public double getPosition() {
        return (isReversed) ? -maxspark.getSelectedSensorPosition(0) : maxspark.getSelectedSensorPosition(0);
    }

    public void stop() {
        maxspark.stopMotor();
    }

    public void resetEncoder() {
        maxspark.setSelectedSensorPosition(0);
    }
}
