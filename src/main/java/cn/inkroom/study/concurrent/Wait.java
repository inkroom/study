package cn.inkroom.study.concurrent;

/**
 * @author inkbox
 * @date 2021/5/29
 */
public class Wait {
    public static void main(String[] args) {
        Object wait = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (wait) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 拿到锁，开始wait");
                        wait.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 开始执行");
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (wait) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 拿到锁，开始wait");

                        wait.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 开始执行");
                }
            }
        }).start();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (wait) {
                    System.out.println(Thread.currentThread().getName() + " notify");
                    wait.notify();
                }
            }
        });
        thread.setName("notify");
        thread.start();
    }
}
