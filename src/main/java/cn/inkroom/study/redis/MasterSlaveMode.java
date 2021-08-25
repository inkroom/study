package cn.inkroom.study.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author inkbox
 * @date 2021/6/3
 */
public class MasterSlaveMode {

    public static void main(String[] args) {
        // 测试主从延迟

//        Config config = new Config();
//        config.useMasterSlaveServers()
//                .setMasterAddress("redis://192.168.3.72:6380").setSlaveAddresses(Set.of("redis://192.168.3.72:6379"));
//
//        RedissonClient client = Redisson.create(config);


//        Codec codec = new StringCodec();
        int i = 0;
//        while (true) {
//            i++;
//
//            client.getMap("keyMap").put("key" + i, i + "");
//            Object keyMap = client.getMap("keyMap")
//                    .get("key" + i);
//            System.out.printf("设置%d,获取%s %n", i, keyMap);
//        }

//        Jedis master = new Jedis("192.168.3.72",6380);
//        Jedis slave = new Jedis("192.168.3.72",6379);
//
//        while (true){
//            i++;
//            master.set("key"+i,i+"");
//            String s = slave.get("key" + i);
//            System.out.printf("设置%d,获取%s %n", i, s);
//        }




    }
}
