package frc.robot.Devices;

import com.ctre.phoenix.sensors.Pigeon2;

public class Imu {
    private Pigeon2 imu;

    public Imu(int port) {
        this.imu = new Pigeon2(port);
    }

    public double getRotation() {
        return imu.getYaw();
    }

    public void reset() {
        imu.setYaw(0);
    }
}
