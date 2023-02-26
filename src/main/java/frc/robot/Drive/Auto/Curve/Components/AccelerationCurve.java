package frc.robot.Drive.Auto.Curve.Components;
public class AccelerationCurve {
    private double start_speed;
    private double end_speed;
    private double max_acc;

    public AccelerationCurve(double start_speed, double end_speed, double max_acc) {
        this.start_speed = start_speed;
        this.end_speed = end_speed;
        this.max_acc = max_acc;
    }

    public double sample(double sample) {
        var curve = new Sine(max_acc, end_speed - start_speed);
        return start_speed + curve.sample(sample);
    }

    public double getDistance() {
        // the distance (in) required to get from 0 to 1 in/sec is multiplied by the
        // speed because when the curve scales vertically so does the integral
        var curve = new Sine(max_acc, end_speed - start_speed);
        return curve.get_area() + (start_speed * curve.get_duration());
    }

    public double getDuration() {
        // the duration (s) required to get from 0 to 1 in/sec is multiplied by the
        // speed because when the curve scales vertically so does the integral
        return (new Sine(max_acc, end_speed - start_speed)).get_duration();
    }
}
