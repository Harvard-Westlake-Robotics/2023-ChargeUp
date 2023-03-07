package frc.robot.Devices.Motor;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Devices.MotorController;

public class SparkMax extends MotorController {
    private CANSparkMax maxspark;
    private RelativeEncoder encoder;

    public SparkMax(int canID, boolean isReversed) {
        super(isReversed);

        this.maxspark = new CANSparkMax(canID, MotorType.kBrushless);
        this.encoder = maxspark.getEncoder();
        maxspark.setIdleMode(IdleMode.kCoast);
    }

    public void setBrake(boolean brake) {
        maxspark.setIdleMode((brake) ? IdleMode.kBrake : IdleMode.kCoast);
    }

    public SparkMax(int canID, boolean isReversed, boolean brakeMode) {
        super(isReversed);
        this.maxspark = new CANSparkMax(canID, MotorType.kBrushless);
        this.encoder = maxspark.getEncoder();
        if (brakeMode)
            maxspark.setIdleMode(IdleMode.kBrake);
        else
            maxspark.setIdleMode(IdleMode.kCoast);
    }

    public void uSetVoltage(double volts) {
        maxspark.setVoltage(volts);
    }

    public double uGetRevs() {
        return encoder.getPosition();
    }

    public void stop() {
        maxspark.stopMotor();
    }

    public void resetEncoder() {
        encoder.setPosition(0);
    }
}
