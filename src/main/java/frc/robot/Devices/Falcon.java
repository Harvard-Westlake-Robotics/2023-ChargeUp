package frc.robot.Devices;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class Falcon {
    private WPI_TalonFX falcon;
    private boolean isReversed;
    double stallVolt;

    public Falcon(int deviceNumber, boolean isReversed, boolean isStallable) {
        this.falcon = new WPI_TalonFX(deviceNumber);
        falcon.getSensorCollection();
        this.isReversed = isReversed;
        this.stallVolt = isStallable ? 3 : 1;

        /* newer config API */
        TalonFXConfiguration configs = new TalonFXConfiguration();
        /* select integ-sensor for PID0 (it doesn't matter if PID is actually used) */
        configs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        /* config all the settings */
        falcon.configAllSettings(configs);

        falcon.setSelectedSensorPosition(0);
    }

    public Falcon(int deviceNumber, boolean isReversed) {
        this(deviceNumber, isReversed, false);
    }

    public void setPercentVoltage(double percent) {
        setVoltage(percent * (12.0 / 100.0));
    }

    public void setVoltage(double volts) {
        volts = (isReversed) ? -volts : volts;
        if (Math.abs(volts) > 12.0)
            throw new Error("Illegal voltage");

        double fac = (volts > 0) ? 1 : -1;
        if (Math.abs(volts) < stallVolt / 2) {
            falcon.setVoltage(0);
            return;
        } else if (Math.abs(volts) < stallVolt) {
            falcon.setVoltage(stallVolt * fac);
            return;
        }

        falcon.setVoltage(volts);
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

    public double getRevs() {
        return ((isReversed) ? -falcon.getSelectedSensorPosition(0) : falcon.getSelectedSensorPosition(0)) / 2048.0;
    }

    public void stop() {
        falcon.stopMotor();
    }

    public void resetEncoder() {
        falcon.setSelectedSensorPosition(0);
    }
}
