package frc.robot.Devices.Motor;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.Devices.MotorController;

public class Falcon extends MotorController {
    private WPI_TalonFX falcon;
    double stallVolt;

    public Falcon(int deviceNumber, boolean isReversed, boolean isStallable) {
        super(isReversed);

        this.falcon = new WPI_TalonFX(deviceNumber);
        falcon.getSensorCollection();
        this.stallVolt = isStallable ? 3
                : 1;

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

    protected void uSetVoltage(double volts) {
        
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

    protected double uGetRevs() {
        return falcon.getSelectedSensorPosition(0) / 2048.0;
    }

    public void stop() {
        falcon.stopMotor();
    }

    public void resetEncoder() {
        falcon.setSelectedSensorPosition(0);
    }
}
