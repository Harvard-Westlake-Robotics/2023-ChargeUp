package frc.robot.Drive.Auto.Movements;

import frc.robot.Drive.Auto.Curve.Curve;
import frc.robot.Util.Pair;

public class DriveForwardMovement implements Movement {
    double distance;
    double acceleration;
    double deceleration;
    double speed;
    Curve curve;

    public DriveForwardMovement(double distance, double acceleration, double deceleration, double speed) {
        this.distance = distance;
        this.acceleration = acceleration;
        this.deceleration = deceleration;
        this.speed = speed;
        this.curve = new Curve(distance, speed, 0, 0, acceleration, deceleration);
    }

    public Pair<Double> sample(double sample) {
        return new Pair<Double>(curve.sample(sample));
    }

    public boolean canDebt(double sample) {
        // it can put off incomplete parts of the movement for later if not decelerating
        return !curve.getPhase(sample).equals("dec");
    }

    public Pair<Double> getTotalDistance() {
        return new Pair<Double>(distance);
    }

    public double getDuration() {
        return curve.getDuration();
    }
}
