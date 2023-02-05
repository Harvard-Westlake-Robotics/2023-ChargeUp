package frc.robot.Util;

import org.javatuples.Pair;

public class CurveInput {
    /**
     * curve.
     * 
     * @param input     [-100, 100]
     * @param intensity [0, 20]
     * @return [-100, 100]
     */
    public static double curve(double input, double intensity) {
        return Math.exp((Math.abs(input) - 100) * intensity / 1000) * input;
    }

    /**
     * Scale curves the avg distance of both sticks from zero and their difference
     * independently, so that we can turn at high velocities
     * 
     * @param leftpwr            [-1, 1]
     * @param rightpwr           [-1, 1]
     * @param deadzone           [-1, 1]
     * @param turnCurveIntensity 7 reccomended [0, 20]
     * @param pwrCurveIntensity  5 reccomended [0, 20]
     * @return Pair([-100, 100], [-100, 100])
     */
    public static Pair<Double, Double> scale(double leftpwr, double rightpwr, double deadzone,
            double turnCurveIntensity, double pwrCurveIntensity) {
        if (Math.abs(leftpwr) < deadzone && Math.abs(rightpwr) < deadzone) {
            return new Pair<Double, Double>(0.0, 0.0);
        }
        double pwr = curve(((leftpwr + rightpwr) / 2) * 100, pwrCurveIntensity); // range -100 to 100
        double turn = curve((leftpwr - rightpwr) * 100, turnCurveIntensity); // range -100 to 100

        return new Pair<Double, Double>(
                (pwr + turn), // left
                (pwr - turn) // right
        );
    }
}
