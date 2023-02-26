package frc.robot.Drive.Components;

import frc.robot.Motor.TalonSRX;

/**
 * Holds the three motors and shifter on either side of the drive. Shifter can
 * be null
 */
public class DriveSide {
    private GearShifter shifter;
    private TalonSRX one;
    private TalonSRX two;
    private TalonSRX three;
    private boolean isLowGear = false;

    double revsAtLastShift = 0;
    double inchesAtLastShift = 0;

    public DriveSide(TalonSRX one, TalonSRX two, TalonSRX three, GearShifter shifter) {
        this.one = one;
        this.two = two;
        this.three = three;

        this.shifter = shifter;

        resetEncoders();
    }

    public boolean getIsLowGear() {
        return isLowGear;
    }

    public double getEncoderRevsSinceLastShift() {
        return getEncoderPositionRevs() - revsAtLastShift;
    }

    public double getInchesSinceLastShift() {
        return encoderRevsToInches(getEncoderRevsSinceLastShift());
    }

    public double getPositionInInches() {
        return inchesAtLastShift + getInchesSinceLastShift();
    }

    public double getEncoderPositionRevs() {
        double pos_one = one.getPosition();
        // double pos_two = two.getPosition();
        // double pos_three = three.getPosition();

        // double avg = (pos_one + pos_two + pos_three) / 3;

        // double diff_one = Math.abs(pos_one - avg);
        // double diff_two = Math.abs(pos_two - avg);
        // double diff_three = Math.abs(pos_three - avg);

        // if (diff_one < diff_two && diff_one < diff_three) {
        //     return pos_one;
        // } else {
        //     if (diff_two < diff_three)
        //         return pos_two;
        //     else
        //         return pos_three;
        // }
        return pos_one;
    }

    public double encoderRevsToInches(double encoderRevs) {
        return (encoderRevs * // revolutions of the encoder
                (isLowGear ? DriveConstants.LOW_GEAR_RATIO : DriveConstants.HIGH_GEAR_RATIO) *
                DriveConstants.GEARBOX_RATIO * // revolutions of the wheel
                DriveConstants.WHEEL_CIRCUMFERENCE // distance the wheel has travelled
        );
    }

    public void shift(boolean setHighGear) {
        isLowGear = !setHighGear;

        if (shifter == null) {
            System.out.println("YOU DONT HAVE A SHIFTER NO SHIFTING IS HAPPENING");
            return;
        } else {
            if (setHighGear)
                shifter.setHighGear();
            else
                shifter.setLowGear();
        }

        revsAtLastShift = getEncoderPositionRevs();
        inchesAtLastShift += getInchesSinceLastShift();
    }

    public void shiftLow() {
        shift(false);
    }

    public void shiftHigh() {
        shift(true);
    }

    public void resetEncoders() {
        for (TalonSRX motor : new TalonSRX[] { one, two, three }) {
            motor.resetEncoder();
        }
        revsAtLastShift = getEncoderPositionRevs();
        inchesAtLastShift = 0;
    }

    public void stop() {
        for (TalonSRX motor : new TalonSRX[] { one, two, three }) {
            motor.stop();
        }
    }

    public void setPower(double percentage) {
        if (Math.abs(percentage) > 100.0)
            throw new Error("power too high: " + percentage);
        double voltage = percentage * (12.0 / 100.0);
        for (TalonSRX motor : new TalonSRX[] { one, two, three }) {
            motor.setVoltage(voltage);
        }
    }
}
