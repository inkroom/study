package cn.inkroom.study.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 同时最多两个线程获取锁
 *
 * @author inkbox
 * @date 2021/5/30
 */
public class TwinsLock implements Lock {
    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    static class Sync extends AbstractQueuedSynchronizer {
        public Sync(int status) {
            setState(status);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            while (true) {
                int c = getState();
                int newC = c - arg;
                if (newC < 0 || compareAndSetState(c, newC)) {
                    return newC;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            while (true){
                int c = getState();
                int newC = c + arg;
                if (compareAndSetState(c, newC)) {
                    return true;
                }
            }
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    private Sync sync = new Sync(2);


    public static void main(String[] args) {
        TwinsLock lock = new TwinsLock();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    System.out.println(Thread.currentThread().getName());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        lock.unlock();
                        System.out.println(Thread.currentThread().getName()+"释放锁");

                    }
                }
            }).start();
        }
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName()+" 开始获取锁");
//                lock.lock();
//                System.out.println(Thread.currentThread().getName()+"获取了锁");
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    lock.unlock();
//                }
//            }
//        }).start();
    }

}
