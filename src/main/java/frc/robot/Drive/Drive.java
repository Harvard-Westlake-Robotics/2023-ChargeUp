package frc.robot.Drive;

import frc.robot.Util.Pair;

/**
 * Holds two `DriveSides` and allows you to call their methods together
 */
public class Drive {
    DriveSide left;
    DriveSide right;

    public Drive(DriveSide left, DriveSide right) {
        this.left = left;
        this.right = right;
    }

    public void shiftHighGear() {
        left.shiftHigh();
        right.shiftHigh();
    }

    public void shiftLowGear() {
        left.shiftLow();
        right.shiftLow();
    }

    public Pair<Double> getPositions() {
        return new Pair<Double>(left.getPosition(), right.getPosition());
    }

    public void setPower(double leftPercent, double rightPercent) {
        left.setPower(leftPercent);
        right.setPower(rightPercent);
    }

    public void stop() {
        left.stop();
        right.stop();
    }
}