package frc.robot.Devices.Motor;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Devices.MotorController;

public class SparkMax extends MotorController {
    private CANSparkMax maxspark;
    private RelativeEncoder encoder;

    boolean braking;

    public boolean isBraking() {
        return braking;
    }

    public SparkMax(int canID, boolean isReversed) {
        super(isReversed);

        this.maxspark = new CANSparkMax(canID, MotorType.kBrushless);
        this.maxspark.restoreFactoryDefaults();
        this.encoder = maxspark.getEncoder();
        maxspark.setIdleMode(IdleMode.kCoast);

        braking = false;

        // sends a max of ten amps when stalling, 100 amps when not
        maxspark.setSmartCurrentLimit(40, 100);
    }

    public void setBrake(boolean brake) {
        maxspark.setIdleMode((brake) ? IdleMode.kBrake : IdleMode.kCoast);
        braking = brake;
    }

    public SparkMax(int canID, boolean isReversed, boolean brakeMode) {
        this(canID, isReversed);
        if (brakeMode)
            maxspark.setIdleMode(IdleMode.kBrake);
        else
            maxspark.setIdleMode(IdleMode.kCoast);
        braking = brakeMode;
    }

    protected void uSetVoltage(double volts) {
        maxspark.setVoltage(volts);
    }

    protected double uGetRevs() {
        return encoder.getPosition();
    }

    public void setCurrentLimit (int stall, int free)
    {
        maxspark.setSmartCurrentLimit(stall, free);
    }

    public void stop() {
        maxspark.stopMotor();
    }

    public void resetEncoder() {
        encoder.setPosition(0);
    }

    public double getTemp() {
        return maxspark.getMotorTemperature();
    }
}
