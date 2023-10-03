package frc.robot.Drive.Auto;

import frc.robot.Drive.Components.DriveSide;
import frc.robot.Util.PDController;
import frc.robot.Util.Tickable;

public class DumbyDrive implements Tickable {
    DriveSide left;
    DriveSide right;
    PDController cont;

    double target = 0;

    public DumbyDrive(DriveSide left, DriveSide right, PDController cont) {
        this.left = left;
        this.right = right;
        this.cont = cont;
    }

    public void incrementTarget(double dTarget) {
        target += dTarget;
    }

    public void tick(double dTime) {
        double error = target - left.getPositionInches();
        double correct = cont.solve(error);
        left.setPower(correct);
        right.setPower(correct);
    }
}
