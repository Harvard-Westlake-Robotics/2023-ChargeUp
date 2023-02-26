package frc.robot.Drive.Auto.Movements;

import frc.robot.Util.Pair;

public interface Movement {
    /**
     * Gets the duration of the `Movement` in seconds
     */
    public double getDuration();
    /**
     * Gets whether to debt at a given time into a movement
     * @param seconds seconds into the movement
     * @return whether the robot can debt
     */
    public boolean canDebt(double seconds);
    /**
     * Gets the speed to apply at a given time into a movement
     * @param seconds seconds into the movement
     * @return the speed (in/sec)
     */
    public Pair<Double> sample(double seconds);
    /**
     * Gets the total distance each side of the drive should have gone during the Movement
     * @return (inches, inches)
     */
    public Pair<Double> getTotalDistance();
}
