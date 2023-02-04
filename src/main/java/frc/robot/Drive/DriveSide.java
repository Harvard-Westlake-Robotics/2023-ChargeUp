package frc.robot.Drive;

import com.revrobotics.*;

import frc.robot.Motor.SparkMax;

/**
 * Holds the three motors and shifter on either side of the drive. Shifter can be null
 */
public class DriveSide {
    private GearShifter shifter;
    private SparkMax one;
    private SparkMax two;
    private SparkMax three;

    public DriveSide(SparkMax one, SparkMax two, SparkMax three, GearShifter shifter) {
        this.one = one;
        this.two = two;
        this.three = three;

        this.shifter = shifter;
    }

    public DriveSide(SparkMax one, SparkMax two, SparkMax three) {
        this(one, two, three, null);
    }

    public void shiftLow() {
        if (shifter == null)
            throw new Error("YOU DIDNT DEFINE A SHIFTER HOW DO YOU EXPECT ME TO SHIFT THE GEARS");
        shifter.setLowGear();
    }

    public void shiftHigh() {
        if (shifter == null)
            throw new Error("YOU DIDNT DEFINE A SHIFTER HOW DO YOU EXPECT ME TO SHIFT THE GEARS");
        shifter.setHighGear();
    }

    public double getPosition() {
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

    public void stop() {
        for (SparkMax motor : new SparkMax[] { one, two, three }) {
            motor.stop();
        }
    }

    public void setPower(double power) {
        power = power * (12.0 / 100.0);
        for (SparkMax motor : new SparkMax[] { one, two, three }) {
            motor.setVoltage(power);
        }
    }
}
