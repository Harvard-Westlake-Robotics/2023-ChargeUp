package frc.robot.Drive;

import com.revrobotics.*;

public class DriveSide {
    private CANSparkMax one;
    private CANSparkMax two;
    private CANSparkMax three;
    private RelativeEncoder encoder_one;
    private RelativeEncoder encoder_two;
    private RelativeEncoder encoder_three;

    public DriveSide(CANSparkMax one, CANSparkMax two, CANSparkMax three) {
        this.one = one;
        this.one.restoreFactoryDefaults();
        this.encoder_one = this.one.getEncoder();

        this.two = two;
        this.two.restoreFactoryDefaults();
        this.encoder_two = this.two.getEncoder();

        this.three = three;
        this.three.restoreFactoryDefaults();
        this.encoder_three = this.three.getEncoder();
    }

    public double get_position() {
        double pos_one = encoder_one.getPosition();
        double pos_two = encoder_two.getPosition();
        double pos_three = -encoder_three.getPosition();

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

    public void set_power(double power) {

        power = power * (12.0/100.0);
        one.setVoltage(power);
        two.setVoltage(power);
        three.setVoltage(-power);
    }
}
