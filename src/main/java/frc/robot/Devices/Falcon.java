package frc.robot.Devices;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class Falcon {
    private WPI_TalonFX maxspark;
    private boolean isReversed;
    boolean isStallable;

    public Falcon(int deviceNumber, boolean isReversed, boolean isStallable) {
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

        maxspark.setSelectedSensorPosition(0);
    }

    public Falcon(int deviceNumber, boolean isReversed) {
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
            if (Math.abs(volts) < 1.5) {
                maxspark.setVoltage(0);
                return;
            } else if (Math.abs(volts) < 3) {
                maxspark.setVoltage(3 * fac);
                return;
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
        return ((isReversed) ? -maxspark.getSelectedSensorPosition(0) : maxspark.getSelectedSensorPosition(0)) / 2048.0;
    }

    public void stop() {
        maxspark.stopMotor();
    }

    public void resetEncoder() {
        maxspark.setSelectedSensorPosition(0);
    }
}
