package frc.robot.Drive.Auto.Curve;

import frc.robot.Drive.Auto.Curve.Components.AccelerationCurve;

public class Curve {
    private double distance;
    private double coast_speed;
    private double acc_acc;
    private double dec_acc;
    private boolean isReversed;
    private AccelerationCurve acc_curve;
    private AccelerationCurve dec_curve;

    /**
     * 
     * @param distance inches
     * @param coast_speed inches/sec
     * @param start_speed inches/sec
     * @param end_speed inches/sec
     * @param acceleration // inches/sec^2
     */
    public Curve(double distance, double coast_speed, double start_speed, double end_speed, double acceleration, double deceleration) {
        isReversed = distance < 0;
        this.distance = Math.abs(distance);
        this.coast_speed = Math.abs(coast_speed);

        acc_acc = Math.abs(acceleration);
        dec_acc = Math.abs(deceleration);

        acc_curve = new AccelerationCurve(Math.abs(start_speed), this.coast_speed, acc_acc);
        dec_curve = new AccelerationCurve(Math.abs(end_speed), this.coast_speed, dec_acc);
    }

    public double getCoastDuration() {
        return (distance - (acc_curve.getDistance() + dec_curve.getDistance())) / coast_speed;
    }

    public double getDuration() {
        return acc_curve.getDuration() + getCoastDuration() + dec_curve.getDuration();
    }

    public String getPhase(double sample) {
        if (sample < acc_curve.getDuration()) {
            return "acc";
        } else if (sample < distance && sample > distance - dec_curve.getDuration()) {
            return "dec";
        } else {
            return "coast";
        }
    }

    public double sample(double sample) {
        double fac = (isReversed) ? -1 : 1;
        if (sample < acc_curve.getDuration()) {
            return acc_curve.sample(sample) * fac;
        } else if (sample < distance && sample > distance - dec_curve.getDuration()) {
            return dec_curve.sample(distance - sample) * fac;
        } else {
            return coast_speed * fac;
        }
    }
}
