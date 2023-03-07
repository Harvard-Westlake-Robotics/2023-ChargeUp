package frc.robot.Devices;

public class Encoder {
    edu.wpi.first.wpilibj.Encoder encoder;
    boolean isReversed;
    public Encoder(int port1, int port2, boolean isReversed) {
        this.encoder = new edu.wpi.first.wpilibj.Encoder(port1, port2, isReversed, edu.wpi.first.wpilibj.Encoder.EncodingType.k2X);
        this.isReversed = isReversed;
    }
    public double getRevs() {
        return (isReversed ? -encoder.get() : encoder.get()) / 2048.0;
    }
    public void reset() {
        encoder.reset();
    }
}
