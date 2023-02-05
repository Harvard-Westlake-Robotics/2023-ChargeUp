package frc.robot.Drive;

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

    public void setPower(double leftPercent, double rightPercent) {
        left.setPower(leftPercent);
        right.setPower(rightPercent);
    }

    public void stop() {
        left.stop();
        right.stop();
    }
}