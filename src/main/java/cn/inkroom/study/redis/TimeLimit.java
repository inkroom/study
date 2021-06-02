package cn.inkroom.study.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 简单限流器
 *
 * @author inkbox
 */
public class TimeLimit {

    private Jedis jedis;

    public TimeLimit() {
        jedis = new Jedis("192.168.3.64", 6379);
    }

    /**
     * 操作是否允许被执行。用户userId的action操作在period时间段内，只允许最多执行maxCount次
     *
     * @param userId   用户
     * @param action   操作类型，比如 like:123 可以代表给id为123的文章点赞
     * @param period   从现在往前的时间段，单位秒。例如过去一分钟内，60
     * @param maxCount 指定时间段内的最大次数
     * @return
     */
    public boolean allowExecute(String userId, String action, int period, int maxCount) {
        String key = String.format("limit:%s:%s", userId, action);
        long now = System.currentTimeMillis();
        Pipeline pipelined = jedis.pipelined();
        pipelined.multi();
        // 这里有个问题，假设在边界时间里不断重试，将会导致永远无法执行
        // 假设 5秒内最多执行2次，但是每秒都在重试
        // 那么除了最开始的两次，之后不管过了多少时间都无法执行
        // 解决方法就是把 add 放到 zremrange 后面，不计数无效尝试

        pipelined.zadd(key, now, now + "");
        // 移除当前时间-period时间前的所有数据
        pipelined.zremrangeByScore(key, 0, now - period * 1000L);
        Response<Long> zcard = pipelined.zcard(key);
        //设置过期时间
        pipelined.expire(key, (period) + 1);
        pipelined.exec();
        pipelined.close();

        return zcard.get() <= maxCount;
    }

    /**
     * 操作是否允许被执行。用户userId的action操作在period时间段内，只允许最多执行maxCount次
     * <p>
     * 和 {@link TimeLimit#allowExecute(String, String, int, int)} 的区别在于，该方法不会统计不被允许的执行次数
     *
     * @param userId   用户
     * @param action   操作类型，比如 like:123 可以代表给id为123的文章点赞
     * @param period   从现在往前的时间段，单位秒。例如过去一分钟内，60
     * @param maxCount 指定时间段内的最大次数
     * @return
     */
    public boolean allowExecuteIgnoreFail(String userId, String action, int period, int maxCount) {
        String key = String.format("limit:%s:%s", userId, action);
        long now = System.currentTimeMillis();

        Long size = ((Long) jedis.eval(
                "redis.call('zremrangeByScore',KEYS[1],0,ARGV[1])\n"
                        + "local size=redis.call('zcard',KEYS[1])\n"
                        + "if (size < tonumber(ARGV[2])) then\n"
                        + "  redis.call('zadd',KEYS[1],tonumber(ARGV[3]),ARGV[3])\n"
                        + "end\n"
                        + "redis.call('expire',KEYS[1],tonumber(ARGV[4]))\n"
                        + "return size"
                ,
                1, key, (now - period * 1000L) + "", maxCount + "", now + "", (period + 1) + ""));
        return size < maxCount;
        // 等同以下代码，只是具有原子性
//        jedis.zremrangeByScore(key, 0, now - period * 1000L);
//        Long size = jedis.zcard(key);
//        if (size < maxCount) {
//            //合法操作
//            jedis.zadd(key, now, now + "");
//            return true;
//        }
//        jedis.expire(key, period + 1);
//        return false;

    }

    public static void main(String[] args) {
        TimeLimit timeLimit = new TimeLimit();

        int count = 2;
        for (int i = 0; i < 100; i++) {
            System.out.println(timeLimit.allowExecuteIgnoreFail("userId-", "action", 5, count));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 首先
    }

}

