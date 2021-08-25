package cn.inkroom.study.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.CountDownLatch;

/**
 * @author inkbox
 * @date 2021/6/13
 */
public class AtomicInt {

    private volatile int value;
    private VarHandle handle;

    public AtomicInt() {
        try {
            value = 0;
            handle = MethodHandles.lookup().findVarHandle(AtomicInt.class, "value", int.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public boolean add() {
        while (true) {
            if (handle.compareAndSet(this, value, value + 1)) {
                System.out.println(value);
                return true;
            }
        }
    }

    public static void main(String[] args) {
        int threadCount = 100;
        int count = 1000;
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInt atomicInt = new AtomicInt();

        for (int i = 0; i < threadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < count; j++) {
                        atomicInt.add();
                    }
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(atomicInt.value);
    }

}
