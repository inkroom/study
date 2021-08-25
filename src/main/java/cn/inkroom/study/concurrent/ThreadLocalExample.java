package cn.inkroom.study.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author inkbox
 * @date 2021/6/5
 */
public class ThreadLocalExample {

    public static ThreadLocal local=new ThreadLocal();

    public static void main(String[] args) {

//        ThreadLocal threadLocal = new ThreadLocal();
//        try {
//            TimeUnit.SECONDS.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        threadLocal.set(new byte[100 * 1024 * 1024]);
//
//        threadLocal = null;
//        try {
//            Thread.currentThread().join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadLocalExample.local.set(new byte[100 * 1024 * 1024]);//100MB空间

                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                local = null;
                System.out.println("local的引用");
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程死了");
            }
        });
        thread.start();



        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //释放对thread的引用
        thread = null;
        System.out.println("释放引用");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
