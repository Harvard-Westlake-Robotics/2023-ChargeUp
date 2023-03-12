package frc.robot.Util;

public class CancelablePromise extends Promise {
    Lambda cancel;

    public CancelablePromise(Lambda cancel) {
        this.cancel = cancel;
    }
    
    public void cancel() {
        cancel.run();
    }
}
