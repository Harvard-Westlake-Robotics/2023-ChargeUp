package frc.robot.Devices.Motor;

import frc.robot.Util.Tickable;

public class NoisyFalcon implements Tickable {
    Falcon falcon;
    double percentVoltage;

    public void setPercentVoltage(double voltage) {
        this.percentVoltage = voltage;
    }

    public NoisyFalcon(Falcon falcon) {
        this.falcon = falcon;
    }

    public void resetEncoder() {
        falcon.resetEncoder();
    }

    public double getRevs() {
        return falcon.getRevs();
    }

    public void tick(double tock) {
        double noisyVoltage = percentVoltage;
        if (Math.abs(percentVoltage) < 25) {
            if (Math.random() * 4 < 1)
                noisyVoltage = (percentVoltage * 4);
            else
                noisyVoltage = (0);
        }

        // falcon.setPercentVoltage(noisyVoltage);
        System.out.println(noisyVoltage);
    }
}
