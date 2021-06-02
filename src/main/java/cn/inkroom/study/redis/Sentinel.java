package cn.inkroom.study.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author inkbox
 */
public class Sentinel {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.3.72",6380);
        jedis.set("222",""+Math.random());
        System.out.println(jedis.get("222"));
    }
}
