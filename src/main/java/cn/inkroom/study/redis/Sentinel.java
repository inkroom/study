package cn.inkroom.study.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.client.WriteRedisConnectionException;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author inkbox
 */
public class Sentinel {

    public static void main(String[] args) {


        //主从切换的时候会有一段时间出现异常，切换完成后就没问题了

        JedisSentinelPool pool = new JedisSentinelPool("m", Set.of("192.168.3.72:26379"));

        while (true){
            try( Jedis resource = pool.getResource()) {
                TimeUnit.SECONDS.sleep(1);
                String key = resource.set("key", "key");
            }catch (Exception e){
                e.printStackTrace();
            }
        }


//
//        Config config = new Config();
//        config.useSentinelServers()
//                .setCheckSentinelsList(false)//如果只有一个哨兵，就设置为false
//                .setMasterName("m").setSentinelAddresses(Collections.singletonList("redis://192.168.3.72:26379"));
//
//        RedissonClient client = Redisson.create(config);
//
//        while (true){
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            try {
//                client.getList("key2").add(Math.random());
//            }catch (WriteRedisConnectionException e){
//                client.getList("key3").add(Math.random());
//                break;
//            }
//
//        }


        //测试master领先slave，然后master宕机，slave升级为master，随后master恢复，领先的命令是否能够恢复


//        Jedis jedis = new Jedis("192.168.3.72",26379);
//        jedis.set("1",2);
    }
}
