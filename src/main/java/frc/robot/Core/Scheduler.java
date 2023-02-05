package frc.robot.Core;

import java.util.Arrays;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Core.Util.Recursive;
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
    static Scheduler scheduler;

    public static Scheduler getInstance() {
        if (scheduler == null)
            scheduler = new Scheduler();
        return scheduler;
    }

    private ScheduleItem[] items = new ScheduleItem[] {};

    /**
     * calls a callback at a given interval
     * 
     * @param callBack the function to call periodically
     * @param delay    how often to call the function
     * @return a function to cancel the interval
     */
    public Lambda setInterval(Lambda callBack, double delay) {
        Recursive<Lambda> interval = new Recursive<>();
        interval.func = () -> {
            callBack.run();
            System.out.println("setting timeout");
            setTimeout(() -> {
                System.out.println("timeout executed");
                interval.func.run();
            }, delay);
        };
        interval.func.run();
        return () -> {
            interval.func = () -> {
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
    public Lambda setTimeout(Lambda callBack, double delay) {
        System.out.println("timeout added to queue");
        items = Arrays.copyOf(items, items.length + 1);
        var item = new ScheduleItem(callBack, delay + Timer.getFPGATimestamp());
        items[items.length - 1] = item;
        System.out.println(items);
        return () -> {
            item.executable = () -> {
            };
        };
    }

    public void tick() {
        double currentTime = Timer.getFPGATimestamp();
        for (var e : items.clone()) {
            if (currentTime >= e.executeTime) {
                e.executable.run();
            }
        }
        var newItems = Arrays.stream(items).filter((e) -> {
            System.out.println("tick");
            return currentTime < e.executeTime;
        }).toList();
        items = new ScheduleItem[newItems.size()];
        if (items.length != 0) {
            System.out.print(items);
            System.out.println(newItems);
        }
        int index = 0;
        for (var item : newItems) {
            items[index] = item;
            index++;
        }
    }

    public void clear() {
        items = new ScheduleItem[] {};
    }
}
