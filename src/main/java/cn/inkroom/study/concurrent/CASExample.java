package cn.inkroom.study.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS样例
 *
 * @author inkbox
 * @date 2021/6/13
 */
public class CASExample {

    private volatile int x = 0;


    public static void main(String[] args) {
        try {
            VarHandle xVarhandle = MethodHandles.lookup().findVarHandle(CASExample.class, "x", int.class);

            int count = 2;
            CyclicBarrier barrier = new CyclicBarrier(count);

            CASExample example = new CASExample();

            for (int i = 0; i < count; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            barrier.await();
                            while (true) {
                                if (xVarhandle.compareAndSet(example, 0, 1)) {
                                    break;
                                }
                            }
                            System.out.println(Thread.currentThread().getName() + "设置完成" + example.x);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
