package frc.robot.Util;

public class ScaleInput {
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
    public static Pair<Double> scale(double leftpwr, double rightpwr, double deadzone,
            double turnCurveIntensity, double pwrCurveIntensity) {
        if (Math.abs(leftpwr) < deadzone && Math.abs(rightpwr) < deadzone) {
            return new Pair<Double>(0.0);
        }
        double pwr = curve(((leftpwr + rightpwr) / 2) * 100, pwrCurveIntensity); // range -100 to 100
        double turn = curve((leftpwr - rightpwr) / 2 * 100, turnCurveIntensity); // range -100 to 100

        return new Pair<Double>(
                (pwr + turn), // left
                (pwr - turn) // right
        );
    }

    /**
     * Takes a pair of two doubles and scales them down so they are both below 100
     * e.g.
     * (150.0, 100.0) -> (100.0, 66.6)
     * 
     * @param inp pair to be scaled
     * @return
     */
    public static Pair<Double> normalize(Pair<Double> inp) {
        if (Math.abs(inp.left) < 100.0 && Math.abs(inp.right) < 100.0) {
            return new Pair<Double>(inp.left, inp.right);
        }
        double scale_one = 100.0 / Math.abs(inp.left);
        double scale_two = 100.0 / Math.abs(inp.right);
        double scale_value = (scale_one < scale_two) ? scale_one : scale_two;
        double left_pwr = inp.left * scale_value;
        double right_pwr = inp.right * scale_value;
        // account for loss of precision
        if (Math.abs(right_pwr) > 100) {
            right_pwr = (right_pwr > 0) ? 100 : -100;
        }
        if (Math.abs(left_pwr) > 100) {
            left_pwr = (left_pwr > 0) ? 100 : -100;
        }
        return new Pair<Double>(left_pwr, right_pwr);
    }
}
