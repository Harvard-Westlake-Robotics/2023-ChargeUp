package frc.robot.DriverStation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import oi.limelightvision.limelight.frc.ControlMode.*;

public class LimeLight {
    NetworkTable masterTable = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = masterTable.getEntry("tx");
    NetworkTableEntry ty = masterTable.getEntry("ty");
    NetworkTableEntry ta = masterTable.getEntry("ta");

    NetworkTableEntry tv = masterTable.getEntry("tv");

    public void setCamMode (int camMode) // 0 - Vision Processing, 1-DriverCam
    {
        masterTable.getEntry("camMode").setValue(camMode);
    }


    //read values periodically
    public double getHorixDegreeToTarget () {
        double x = tx.getDouble(0.0);
        return x;
    }
    public double getVerticalD () {
        double y = ty.getDouble(0.0);
        return y;
    }
    public double getArea () {
        double area = ta.getDouble(0.0);
        return area;
    }
    public double foundTarget () {
        return tv.getDouble(0) ;
    } // 1 is true, 0 is false

    //post to smart dashboard periodically

    // SmartDashboard.putNumber("LimelightX", x);
    // SmartDashboard.putNumber("LimelightY", y);
    // SmartDashboard.putNumber("LimelightArea", area);
}
