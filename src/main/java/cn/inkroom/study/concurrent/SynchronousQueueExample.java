package cn.inkroom.study.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author inkbox
 * @date 2021/5/30
 */
public class SynchronousQueueExample {

    public static void main(String[] args) {
        // SynchronousQueue.offer不会阻塞线程，仅当有调用poll(time,timeUnit) 未超时才会发送成功；同样 poll()也不会阻塞队列

        SynchronousQueue<Integer> integers = new SynchronousQueue<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer poll = null;
                try {
                    poll = integers.poll(600, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(poll);
            }
        }).start();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(integers.offer(1));
    }
}
