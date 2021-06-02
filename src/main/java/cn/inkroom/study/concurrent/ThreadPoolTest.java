package cn.inkroom.study.concurrent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试使用线程池和不使用线程池的性能差距
 *
 * @author inkbox
 * @date 2021/5/31
 */
public class ThreadPoolTest {

    public static final int count = 50000;
    public static final int poolCount = 100;

    /**
     * 统计结果：
     * 耗时 30秒左右，CPU 6%-10%,内存能达到一百m以上
     * @param latch
     */
    public static void thread(CountDownLatch latch) {
        for (int i = 0; i < count; i++) {
            new Thread(new Run(latch)).start();
        }
    }

    /**
     * 耗时更短，一半多一些，但是其他资源没有少
     * @param latch
     */
    public static void pool(CountDownLatch latch){
        ExecutorService executorService = Executors.newFixedThreadPool(poolCount);

        for (int i = 0; i < count; i++) {
            executorService.submit(new Run(latch));
        }
    }

    public static void main(String[] args) {

        // 要测试性能，首先需要保证完成相同的工作量。
        // 假设写入100个文件
        // 直接创建100线程测试非线程池，观察cpu资源占用


        try {
            Thread.sleep(12000);//预留时间打开jconsole
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始");
        CountDownLatch latch = new CountDownLatch(count);
        long start = System.currentTimeMillis();

        pool(latch);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start) / 1000);


        // 新建 400个线程，执行400个任务，线程池 20,执行400 任务

        // 同时任务体非空

        // 线程池的情况下，内存最高120m左右，cpu略高
        // 单个线程耗时 3600 毫秒左右，总体耗时72秒; 可见耗时在于等待前面的任务完成

        // 新建线程情况下，内存最高可达150m，cpu最高12%
        // 单个线程任务耗时平均  125462 毫秒 ,总体耗时125 秒，这里耗时应该是过于频繁的线程切换导致单个线程执行效率低下，vmstat 显示上下文切换会达到100
    }

    //首先写一个任务
    static class Run implements Runnable {
        private CountDownLatch latch;

        public Run(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                long now = System.currentTimeMillis();
//                for (int i = 0; i < 5000; i++) {
//                    FileWriter writer = new FileWriter("/home/ink/"+Thread.currentThread().getName());
//                    writer.append("中文");
//                    writer.close();
//                }
//                new File(Thread.currentThread().getName()).delete();
                System.out.println(System.currentTimeMillis() - now);
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    ;
}
