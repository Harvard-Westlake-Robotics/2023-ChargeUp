// package frc.robot.Arm.Components;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Encoder;

<<<<<<< Updated upstream
// // motor group
// public class ArmAngler {
//     SparkMax arm1;
//     SparkMax arm2;
//     Encoder encoder;

//     public ArmAngler(SparkMax arm1, SparkMax arm2, Encoder armEncoder) {
//         this.arm1 = arm1;
//         this.arm2 = arm2;
//         this.encoder = armEncoder;
=======
// motor group
public class ArmAngler {
    CANSparkMax arm1;
    SparkMax arm2;
    Encoder encoder;

    public ArmAngler(int arm1, int arm2, Encoder armEncoder) {
        this.arm1 = new SparkMax(8, false, true);
        this.arm2 = new SparkMax(9, false, true);
        this.encoder = new Encoder(4, 5, true);
>>>>>>> Stashed changes

//         arm1.setBrake(true);
//         arm2.setBrake(true);
//     }

//     public void setBrake(boolean brake) {
//         arm1.setBrake(brake);
//         arm2.setBrake(brake);
//     }

//     public void setVoltage(double voltage) {
//         arm1.setVoltage(voltage);
//         arm2.setVoltage(voltage);
//     }

//     public double getPosition() {
//         return encoder.getRevs();
//     }

//     public void zero() {
//         encoder.reset();
//     }
// }