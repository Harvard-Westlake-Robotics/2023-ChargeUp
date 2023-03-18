package frc.robot.Util;

import java.util.ArrayList;

public class Promise {
    boolean resolved = false;
    ArrayList<Lambda> thens = new ArrayList<Lambda>();

    public Promise then(Lambda then) {
        Promise prom = new Promise();
        if (resolved) {
            then.run();
            prom.resolve();
        } else {
            thens.add(() -> {
                then.run();
                prom.resolve();
            });
        }
        return prom;
    }

    public Promise then(Getter<Promise> then) {
        if (resolved) {
            return then.get();
        } else {
            Container<Promise> nextProm = new Container<Promise>(null);
            Promise retProm = new Promise();

            thens.add(() -> {
                nextProm.val = then.get();
            });

            this.then(() -> {
                nextProm.val.then(() -> {
                    retProm.resolve();
                });
            });

            return retProm;
        }
    }

    public Promise then(Promise prom) {
        return then(() -> {
            return prom;
        });
    }

    public static Promise all(Promise ...proms) {
        var retProm = new Promise();

        final int total = proms.length;
        Container<Integer> resolved = new Container<Integer>(0);

        for (var promise : proms) {
            promise.then(() -> {
                resolved.val++;
                if (resolved.val >= total)
                    retProm.resolve();
            });
        }
        return retProm;
    }

    public void resolve() {
        if (!resolved) {
            resolved = true;
            for (var exec : thens) {
                exec.run();
            }
        }
    }
}
