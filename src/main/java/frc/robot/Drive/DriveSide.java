package frc.robot.Drive;

import com.revrobotics.*;

/**
 * Holds the three motors and shifter on either side of the drive. Shifter can be null
 */
public class DriveSide {
    private GearShifter shifter;
    private CANSparkMax one;
    private CANSparkMax two;
    private CANSparkMax three;
    private RelativeEncoder encoder_one;
    private RelativeEncoder encoder_two;
    private RelativeEncoder encoder_three;

    public DriveSide(CANSparkMax one, CANSparkMax two, CANSparkMax three, GearShifter shifter) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.one.restoreFactoryDefaults();
        this.two.restoreFactoryDefaults();
        this.three.restoreFactoryDefaults();

        this.encoder_one = this.one.getEncoder();
        this.encoder_two = this.two.getEncoder();
        this.encoder_three = this.three.getEncoder();

        this.shifter = shifter;
    }

    public DriveSide(CANSparkMax one, CANSparkMax two, CANSparkMax three) {
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
        double pos_one = encoder_one.getPosition();
        double pos_two = encoder_two.getPosition();
        double pos_three = encoder_three.getPosition();

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
        for (CANSparkMax motor : new CANSparkMax[] { one, two, three }) {
            motor.stopMotor();
        }
    }

    public void setPower(double power) {
        power = power * (12.0 / 100.0);
        for (CANSparkMax motor : new CANSparkMax[] { one, two, three }) {
            motor.setVoltage(power);
        }
    }
}
