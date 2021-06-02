package cn.inkroom.study.redis;

import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author inkbox
 */
public class RedLock {


    public static void main(String[] args) {


        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.3.72:30000");
        RedissonClient client1 = Redisson.create(config);
        config = new Config();
        config.useSingleServer().setAddress("redis://192.168.3.72:30001");
        RedissonClient client2 = Redisson.create(config);
        config = new Config();
        config.useSingleServer().setAddress("redis://192.168.3.72:30002");
        RedissonClient client3 = Redisson.create(config);


        String key = "red";
        RLock lock1 = client1.getLock(key);
        RLock lock2 = client2.getLock(key);
        RLock lock3 = client3.getLock(key);

        RedissonRedLock redLock = new RedissonRedLock(lock1,lock2,lock3);
        boolean b = false;
        try {
            b = redLock.tryLock(1000, TimeUnit.SECONDS);
            if (b){
                System.out.println("加锁成功");
                Thread.sleep(500000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("开始释放锁");
            redLock.unlock();
        }


//        Config config= new Config();
////        config.useSingleServer().setAddress("redis://192.168.3.72:6379");
//        config.useClusterServers().addNodeAddress(
//                "redis://192.168.3.72:30000"
//                ,"redis://192.168.3.72:30001"
//                ,"redis://192.168.3.72:30002"
//                ,"redis://192.168.3.72:30003"
//                ,"redis://192.168.3.72:30004"
//                ,"redis://192.168.3.72:30005"
//                ,"redis://192.168.3.72:30006"
//                ,"redis://192.168.3.72:30007"
//                ,"redis://192.168.3.72:30008"
//                ,"redis://192.168.3.72:30009"
//        );
//        RedissonClient redissonClient = Redisson.create(config);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                RLock red3 = redissonClient.getLock("red9");
//                System.out.println(Thread.currentThread().getName()+" 开始上锁");
//                red3.lock();
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                red3.unlock();
//                System.out.println(Thread.currentThread().getName()+" 开始释放了锁");
//
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                RLock red3 = redissonClient.getLock("red9");
//                System.out.println(Thread.currentThread().getName()+" 开始尝试上锁");
//                red3.lock();
//                System.out.println(Thread.currentThread().getName()+" 获取了锁");
//
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                red3.unlock();
//                System.out.println(Thread.currentThread().getName()+" 释放了锁");
//
//            }
//        }).start();
//
//        RLock red = redissonClient.getLock("red2");
//         red = redissonClient.getMultiLock(red);
//         red.lock();
//        System.out.println(red.getClass());
//        RSet<Object> set = redissonClient.getSet("set");
//        set.add("1111");
//        try {
//            Thread.sleep(50000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        red.unlock();



    }
}
