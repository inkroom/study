package cn.inkroom.study.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @author ink
 * @date 2021/5/29
 */
public class RenderExample {

    int a = 0;
    boolean flag = false;

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


    public static void main(String[] args) {
        // 测试线程A write，线程B render

        CountDownLatch latch = new CountDownLatch(1);

        RenderExample renderExample = new RenderExample();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    renderExample.write();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                latch.countDown();
                renderExample.render();
            }
        }).start();

        // idea中多次执行后，输出和不输出无规律交替出现; 在terminal中执行为始终不出现。但是这没有达到想要的效果
        // 即 Thread A 先执行write 然后 Thread B执行render。现在的其实是两个方法执行顺序随机，同时方法体过短，没法体现指令交叉执行

    }

}
