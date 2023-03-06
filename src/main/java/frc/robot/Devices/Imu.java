package frc.robot.Devices;

import com.ctre.phoenix.sensors.Pigeon2;

public class Imu {
    private Pigeon2 imu;

    public Imu(Pigeon2 imu) {
        this.imu = imu;
    }

    public double getRotation() {
        return imu.getYaw();
    }
}
