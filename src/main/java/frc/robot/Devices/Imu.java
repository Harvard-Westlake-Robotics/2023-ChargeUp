package frc.robot.Devices;

import com.ctre.phoenix.sensors.WPI_Pigeon2;

public class Imu {
    private WPI_Pigeon2 imu;

    public Imu(int port) {
        this.imu = new WPI_Pigeon2(port);
        
        imu.configFactoryDefault();
        imu.configMountPoseYaw(90);
        imu.configMountPosePitch(0);
        imu.configMountPoseRoll(0);
        imu.setYaw(0);
        imu.configEnableCompass(false);
    }

    public double getRotation() {
        return imu.getYaw();
    }

    public double getPitch() {
        return imu.getPitch();
    }

    public void resetYaw() {
        imu.setYaw(0);
    }
}
