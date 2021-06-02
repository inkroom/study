package cn.inkroom.study.concurrent;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * 尝试使用一个阻塞的有界队列
 *
 * @author inkbox
 * @date 2021/5/30
 */
public class BoundedQueue<T> extends AbstractQueue<T> {

    int count ;
    private ReentrantLock lock = new ReentrantLock();
    private List<T> list;

    private Condition full;
    private Condition empty;

    public BoundedQueue(int count) {
        this.count = count;
        list = new LinkedList<T>();
        full = lock.newCondition();
        empty = lock.newCondition();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean offer(T t) {
        lock.lock();
        try {
            while (size()==count){
                //满了，开始阻塞
                full.await();
            }

            list.add(t);
            //有值插入，通知出队方
            empty.signal();

        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

        return false;
    }

    @Override
    public T poll() {

        lock.lock();
        try {
            while (size()==0){//没有值，等待
                empty.await();
            }

            T t = list.get(0);
            list.remove(0);

            //出队了，通知入队方
            full.signal();
            return t;
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        //除非被中断，否则不会执行
        return null;

    }

    @Override
    public T peek() {

        lock.lock();
        try {
            while (size()==0){//没有值，等待
                empty.await();
            }

            //没有出队，所以不通知入队方
            return list.get(0);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        //除非被中断，否则不会执行
        return null;
    }


    public static void main(String[] args) {
        BoundedQueue<Integer> queue = new BoundedQueue<>(3);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //入队线程
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("开始入队第一个值");
                queue.offer(1);
                queue.offer(1);
                queue.offer(1);
                queue.offer(1);
                System.out.println("开始入队第5个值");
                //此时满了，会开始阻塞
                queue.offer(5);
                System.out.println("第5个值入队成功");


            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始出队");
                queue.poll();
                System.out.println("第一个值出队成功");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("出队第四个值");
                queue.poll();
            }
        }).start();



    }
}
