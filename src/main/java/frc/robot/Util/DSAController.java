package frc.robot.Util;

public class DSAController {
    double P_CONSTANT;
    double D_CONSTANT;
    double DD_CONSTANT;
    double deadzone;

    double lastError = 0;
    double lastLastError = 0;

    public DSAController(double p_CONSTANT, double d_CONSTANT, double dd_CONSTANT, double deadzone) {
        P_CONSTANT = p_CONSTANT;
        D_CONSTANT = d_CONSTANT;
        DD_CONSTANT = dd_CONSTANT;
        this.deadzone = deadzone;
    }

    public DSAController(double p_CONSTANT, double d_CONSTANT, double dd_CONSTANT) {
        this(p_CONSTANT, d_CONSTANT, dd_CONSTANT, 0);
    }

    /**
     * @param currentError the distance to the target from the current value (target
     *                     value - current value)
     * @return A correctional value
     */
    public double solve(double currentError) {
        double p_correct = P_CONSTANT * currentError;
        if (Math.abs(p_correct) < deadzone) {
            p_correct = 0;
        } else {
            p_correct = (p_correct > 0) ? p_correct - deadzone : p_correct + deadzone;
        }

        double d_correct = D_CONSTANT * (currentError - lastError);
        double dd_correct = DD_CONSTANT * ((lastError - lastLastError) - (currentError - lastError));

        lastLastError = lastError;
        lastError = currentError;

        return p_correct + d_correct + dd_correct;
    }

    /**
     * returns a new PD controller with a scaled magnitude
     * 
     * @param fac
     * @return
     */
    public DSAController withMagnitude(double fac) {
        return new DSAController(P_CONSTANT * fac, D_CONSTANT * fac, DD_CONSTANT * fac, deadzone);
    }

    public void reset() {
        lastError = 0;
    }

    public DSAController clone() {
        return new DSAController(P_CONSTANT, D_CONSTANT, DD_CONSTANT, deadzone);
    }
}
