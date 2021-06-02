package cn.inkroom.study.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @author ink
 * @date 2021/5/29
 */
public class VolatileExample {

    int a = 0;
    volatile boolean flag = false;

    public void write() {
        a = 1;
        flag = true;
    }

    public void render() {
        if (flag) {
            int i = a * a;
            System.out.println("i==" + i);
        }
    }

     static boolean s = true;//给这个变量加volatile是最直接的方法
    volatile static  int i = 3;
    public static void main(String[] args) {
        // 测试线程A write，线程B render

//        CountDownLatch latch = new CountDownLatch(1);
//
//        VolatileExample renderExample = new VolatileExample();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    latch.await();
//                    renderExample.write();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                latch.countDown();
//                renderExample.render();
//            }
//        }).start();

        // idea中多次执行后，输出和不输出无规律交替出现; 在terminal中执行为始终不出现


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (s){
//                    System.out.println(1);
                    int a= i;
                }
                System.out.println("结束循环");
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("修改值");
        s = false;
        System.out.println(i);

    }

}
