package frc.robot.Core;

import java.util.Arrays;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Util.*;

/**
 * IF YOU DON'T KNOW WHAT THIS CLASS DOES, PLEASE DON'T DELETE IT
 * it allows us to schedule functions for execution at various times during the
 * auton
 */

class ScheduleItem {
    public Lambda executable;
    public double executeTime;

    public ScheduleItem(Lambda executable, double executeTime) {
        this.executable = executable;
        this.executeTime = executeTime;
    }
}

public class Scheduler {
    private ScheduleItem[] items = new ScheduleItem[] {};

    /**
     * Runs a `Tickable` every tick
     * 
     * @param tickable the `Tickable` instance to run the `tick()` method on every
     *                 tick in the event loop
     * @return a function to remove the tickable from the event loop
     */
    public Lambda registerTick(Tickable tickable) {
        double[] lastTime = new double[] { Timer.getFPGATimestamp() };
        return setInterval(() -> {
            tickable.tick(Timer.getFPGATimestamp() - lastTime[0]);
            lastTime[0] = Timer.getFPGATimestamp();
        }, 0);
    }

    /**
     * calls a callback at a given interval
     * 
     * @param callBack the function to call periodically
     * @param delay    how often to call the function
     * @return a function to cancel the interval
     */
    public Lambda setInterval(Lambda callBack, double delay) {
        var item = new ScheduleItem(null, delay + Timer.getFPGATimestamp());
        Container<Lambda> interval = new Container<Lambda>(null);
        interval.val = () -> {
            callBack.run();
            item.executeTime = Timer.getFPGATimestamp() + delay;
        };
        interval.val.run();

        item.executable = interval.val;

        // appends the new item to the schedule
        items = Arrays.copyOf(items, items.length + 1);
        items[items.length - 1] = item;

        return () -> {
            item.executable = () -> {
            };
        };
    }

    /**
     * calls a callback after a given period
     * 
     * @param callBack the function to call after the timeout
     * @param delay    how long the timeout will be before the function is called
     * @return a function to cancel the calling of the function
     */
    public CancelablePromise setTimeout(Lambda callBack, double delay) {

        Container<CancelablePromise> prom = new Container<CancelablePromise>(null);

        items = Arrays.copyOf(items, items.length + 1);
        var item = new ScheduleItem(() -> {
            callBack.run();
            prom.val.resolve();
        }, delay + Timer.getFPGATimestamp());

        prom.val = new CancelablePromise(() -> {
            item.executable = () -> {
            };
        });

        items[items.length - 1] = item;
        return prom.val;
    }

    public CancelablePromise setTimeout(double delay) {
        return setTimeout(() -> {}, delay);
    }

    public Getter<Promise> timeout(double delay) {
        return () -> {
            Promise prom = new Promise();
            setTimeout(() -> {
                prom.resolve();
            }, delay);
            return prom;
        };
    }

    public void tick() {
        boolean runCleanUp = false;
        double currentTime = Timer.getFPGATimestamp();
        for (var e : items.clone()) {
            if (currentTime >= e.executeTime) {
                e.executable.run();
                if (currentTime >= e.executeTime)
                    runCleanUp = true;
            }
        }
        if (runCleanUp) {
            var newItems = Arrays.stream(items).filter((e) -> {
                return currentTime < e.executeTime;
            }).toList();
            items = new ScheduleItem[newItems.size()];
            int index = 0;
            for (var item : newItems) {
                items[index] = item;
                index++;
            }
        }
    }

    public void clear() {
        items = new ScheduleItem[0];
    }
}
