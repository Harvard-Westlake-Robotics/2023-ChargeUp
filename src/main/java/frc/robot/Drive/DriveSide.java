package frc.robot.Drive;

// import frc.robot.Motor.WPI_TalonSRX;

import frc.robot.Motor.TalonSRX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


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

    public boolean isLowGear() {
        return isLowGear;
    }

    public void setLowGear(boolean isLowGear) {
        this.isLowGear = isLowGear;
    }

    public DriveSide(TalonSRX one, TalonSRX two, TalonSRX three, GearShifter shifter) {
        this.one = one;
        this.two = two;
        this.three = three;

        this.shifter = shifter;

        resetEncoders();
    }

    public DriveSide(TalonSRX one, TalonSRX two, TalonSRX three) {
        this(one, two, three, null);
    }

    double ticksAtLastShift = 0;
    double inchesAtLastShift = 0;

    private double getInchesSinceLastShift() {
        final double deltaTicks = getTicks() - ticksAtLastShift;
        /**
         * 14/60 output rot/input rot high gear
         * 30/42 output rot/input rot low gear
         */
        // TODO: account for wheel diameter and other gears in the gearbox
        // ! These are not real inches, they are wigglios, imaginary inches from an
        // ! alternate universe
        if (isLowGear) {
            // move the diff of ticks into inches
            return (30.0 / 42.0) * deltaTicks;
        } else {
            return (14.0 / 60.0) * deltaTicks;
        }
    }


    public double getTotalDistanceInches() {
        return inchesAtLastShift + getInchesSinceLastShift();
    }

    public double getTicks() {
        double pos_one = one.getPosition();
        double pos_two = two.getPosition();
        double pos_three = three.getPosition();

        double avg = (pos_one + pos_two + pos_three) / 3;

        double diff_one = Math.abs(pos_one - avg);
        double diff_two = Math.abs(pos_two - avg);
        double diff_three = Math.abs(pos_three - avg);

        if (diff_one < diff_two && diff_one < diff_three) {
            return pos_one;
        } else {
            if (diff_two < diff_three)
                return diff_two;
            else
                return diff_three;
        }
    }

    public void shift(boolean setHighGear) {
        if (!(shifter == null)) {
            if (setHighGear)
                shifter.setHighGear();
            else
                shifter.setLowGear();
        } else {
            System.out.println("YOU DONT HAVE A SHIFTER NO SHIFTING IS HAPPENING");
        }
        inchesAtLastShift += getInchesSinceLastShift();
        ticksAtLastShift = getTicks();
        isLowGear = !setHighGear;
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
        ticksAtLastShift = getTicks();
        inchesAtLastShift = 0;
    }

    public void stop() {
        for (TalonSRX motor : new TalonSRX[] { one, two, three }) {
            motor.stop();
        }
    }

    public void setPower(double power) {
        if (Math.abs(power) > 100.0)
            throw new Error("power too high: " + power);
        power = power * (12.0 / 100.0);
        for (TalonSRX motor : new TalonSRX[] { one, two, three }) {
            motor.setVoltage(power);
        }
    }
}
