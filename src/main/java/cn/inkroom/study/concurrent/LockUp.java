package cn.inkroom.study.concurrent;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;
import org.openjdk.jol.vm.VirtualMachine;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 锁升级
 * <p>
 * java中锁有四种级别，分别是
 * <ul>
 *     <li>无锁</li>
 *     <li>偏向锁</li>
 *     <li>轻量级锁</li>
 *     <li>重量级锁</li>
 * </ul>
 * <p>
 * 以下列出一次测试数据，此处是偏向锁升级轻量级锁
 * <pre>
 *     获取了锁 30190
 *     获取了锁 7165
 *     获取了锁 9322
 *     获取了锁 8237
 *     获取了锁 8061
 *     获取了锁 7585
 *     获取了锁 9970
 *     获取了锁 25178
 *     获取了锁 6516
 *     竞争锁
 *     获取了锁 269768
 *     获取了锁 10914
 *     获取了锁 8881
 *     获取了锁 9363
 *     获取了锁 10229
 *     获取了锁 12085
 *     获取了锁 16577
 * </pre>
 * 可以看出，在竞争之后，获取时间由四五位数变成了六位数；多次测试都能看出数量级的变化
 * <p>
 * 再次模拟两个线程循环获取锁的过程，结果如下
 * <pre>
 * Thread-0获取了锁 42069
 * Thread-0获取了锁 11818
 * Thread-0获取了锁 11702
 * Thread-0获取了锁 11687
 * Thread-0获取了锁 13672
 * Thread-0获取了锁 13185
 * Thread-0获取了锁 13595
 * Thread-0获取了锁 17157
 * Thread-0获取了锁 13052
 * Thread-0获取了锁 21557
 * Thread-0获取了锁 10561
 * Thread-0获取了锁 12408
 * Thread-0获取了锁 16721
 * Thread-0获取了锁 11558
 * Thread-0获取了锁 11267
 * Thread-0获取了锁 15075
 * Thread-0获取了锁 14944
 * 后的的Thread-1获取了锁 12085707971
 * 后的的Thread-1获取了锁 12417
 * 后的的Thread-1获取了锁 14735
 * 后的的Thread-1获取了锁 11964
 * 后的的Thread-1获取了锁 15172
 * Thread-0获取了锁 5010773312
 * Thread-0获取了锁 13285
 * Thread-0获取了锁 13546
 * Thread-0获取了锁 41837
 * Thread-0获取了锁 15667
 * Thread-0获取了锁 17152
 * Thread-0获取了锁 11363
 * Thread-0获取了锁 13901
 * Thread-0获取了锁 13389
 * Thread-0获取了锁 11040
 * Thread-0获取了锁 13411
 * Thread-0获取了锁 75532
 * Thread-0获取了锁 14245
 * Thread-0获取了锁 12482
 * Thread-0获取了锁 16455
 * Thread-0获取了锁 15818
 * Thread-0获取了锁 12914
 * Thread-0获取了锁 10374
 * 后的的Thread-1获取了锁 18052560886
 * 后的的Thread-1获取了锁 14563
 * 后的的Thread-1获取了锁 14105
 * 后的的Thread-1获取了锁 12807
 * Thread-0获取了锁 4007292232
 * Thread-0获取了锁 15244
 * </pre>
 *
 * @author inkbox
 * @date 2021/6/3
 */
public class LockUp {


    private static Object sync = new Object();

    public static void main(String[] args) {

        VirtualMachine vm = VM.current();
        long mark = vm.getLong(sync, 0);
        System.out.println(lockDes(mark));

        // 测试偏向锁升级成轻量级锁
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {//怀疑是这里使用了
                    long l = System.nanoTime();
                    long start = System.currentTimeMillis();
                    //如果没有线程竞争锁，这里就是偏向锁，有竞争升级成轻量级锁，自旋以获取锁，理论上获取锁的时间会更长
                    synchronized (sync) {
                        long mark = vm.getLong(sync, 0);
                        System.out.printf("%s获取了锁 %d %d %s %n", Thread.currentThread().getName(), System.nanoTime() - l, (System.currentTimeMillis() - start) / 1000, lockDes(mark));
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


        try {
            TimeUnit.SECONDS.sleep(5);
            TimeUnit.MILLISECONDS.sleep(500);//把获取锁间隔开
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long l = System.nanoTime();
        long start = System.currentTimeMillis();
        System.out.println("开始竞争");
        synchronized (sync) {
            mark = vm.getLong(sync, 0);
            System.out.printf("只竞争一次的%s获取了锁 %d %d %s %n", Thread.currentThread().getName(), System.nanoTime() - l, (System.currentTimeMillis() - start) / 1000, lockDes(mark));
        }
        try {
            TimeUnit.SECONDS.sleep(5);
            TimeUnit.MILLISECONDS.sleep(500);//把获取锁间隔开
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    long l = System.nanoTime();
                    long start = System.currentTimeMillis();
                    //如果没有线程竞争锁，这里就是偏向锁，有竞争升级成轻量级锁，自旋以获取锁，理论上获取锁的时间会更长
                    synchronized (sync) {
                        long m = vm.getLong(sync, 0);
                        System.out.printf("第三个线程%s获取了锁 %d %d %s  %n", Thread.currentThread().getName(), System.nanoTime() - l, (System.currentTimeMillis() - start) / 1000, lockDes(m));

                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).start();

    }


    private static String lockDes(long mark) {

        long bits = mark & 0b11;
        switch ((int) bits) {
            case 0b11:
                return "(marked: GC)";
            case 0b00:
                return "(thin lock: 轻量级锁)";
            case 0b10:
                return "(fat lock: 重量级锁)";
            case 0b01:
                int tribits = (int) (mark & 0b111);
                switch (tribits) {
                    case 0b001:
                        return "(non-biasable)";
                    case 0b101:
                        return "(biased: 偏向锁)";
                }
        }

        return "错误数据";
    }

}
