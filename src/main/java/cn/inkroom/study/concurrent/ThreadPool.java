package cn.inkroom.study.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池相关
 *
 * @author inkbox
 * @date 2021/6/2
 */
public class ThreadPool {

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("正在尝试退出程序");
            }
        }));
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
                10,
                10000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private AtomicInteger integer = new AtomicInteger(1);

                    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            System.out.printf("%s 出错了 %s %n",t.getName(),e.getMessage());
                        }
                    };

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(Thread.currentThread().getThreadGroup(), r, "ink-thread-" + integer.getAndIncrement(), 0);
                        thread.setUncaughtExceptionHandler(handler);
                        return thread;
                    }
                },
                (r, executor) -> {
                    throw new RejectedExecutionException("Task " + r.toString() +
                            " rejected from " +
                            executor.toString());
                });


        Future<Integer> submit = threadPoolExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(5000);
                System.out.println(Thread.currentThread().getName());
                return 1;
            }
        });

        // 设置全局异常接口
//        Thread.setDefaultUncaughtExceptionHandler();

        try {
            System.out.println(submit.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("随便一个错误");
            }
        });

    }
}
