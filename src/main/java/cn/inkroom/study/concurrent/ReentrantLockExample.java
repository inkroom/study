package cn.inkroom.study.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author inkbox
 * @date 2021/5/29
 */
public class ReentrantLockExample {


    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    try {
                        Thread.sleep(30000);
                        System.out.println(Thread.currentThread().getName()+" 执行结束");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }).start();
        }

    }
}
