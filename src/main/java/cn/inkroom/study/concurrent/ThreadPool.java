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
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
                10,
                10000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private AtomicInteger integer = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(Thread.currentThread().getThreadGroup(), r, "ink-thread-" + integer.getAndIncrement(), 0);
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


        try {
            System.out.println(submit.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
