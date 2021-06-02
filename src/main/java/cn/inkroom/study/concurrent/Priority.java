package cn.inkroom.study.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author inkbox
 * @date 2021/5/29
 */
public class Priority {

    private static volatile boolean notStart = true;
    private static volatile boolean notEnd = true;

    public static void main(String[] args) {
        List<Job> jobs = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            int priority = i < 5 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
            Job j = new Job();
            j.setPriority(priority);
            j.start();
            jobs.add(j);
        }

        notStart = false;
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notEnd = false;
        for (Job j : jobs) {
            System.out.println("priority: " + j.getPriority() + " count=" + j.count);
        }
    }

    static class Job extends Thread {
        private long count;

        @Override
        public void run() {
            while (notStart) {
                Thread.yield();

            }
            while (notEnd) {
                Thread.yield();
                count++;
            }
        }
    }
}

