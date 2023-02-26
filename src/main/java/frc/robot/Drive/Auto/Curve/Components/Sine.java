package frc.robot.Drive.Auto.Curve.Components;
/**
 * Width of the sin curver = ((height of the sine curve / 2) / the max slope of
 * the sine curve) * pi
 */
public class Sine {
    double max_derivative;
    double height;

    public Sine(double max_derivative, double height) {
        this.max_derivative = max_derivative;
        this.height = height;
    }

    public double sample(double sample) {
        if (sample < 0 || sample > get_duration())
            throw new Error("You have sampled the unsamplable");

        return (height / 2) * (Math.sin(sample - (Math.PI / 2)) + 1);
    }

    public double get_duration() {
        return Math.abs(height / max_derivative * Math.PI); // the duration of the default curve is PI, so if you
                                                              // increase

        // the derivative you decrease the duration proportionally

        // if you scale the height, the width scales linearly

        // the default ratio is pi/2
    }

    public double get_area() {
        return get_duration() * height / 2; // basically a triangle
    }
}