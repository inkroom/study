package cn.inkroom.study.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * @author inkbox
 */
public class RDB {
    public static void main(String[] args) {
// 最好使用 debug populate 10000000  来生成大量数据
        //构建大量的key
        Jedis jedis = new Jedis("192.168.3.72", 6379);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            Pipeline pipelined = jedis.pipelined();
            for (int j = 0; j < 100; j++) {
                pipelined.set("RDB+33" + (i * 100 + j), "rdbsdfjkllllllllllllll sduffweu pidsfsdj athwey 89 3492 ur8349 ihfas9d8yq40nv8w9eh7rq230n48v25siuyv n038289r7v323n2");
            }

            pipelined.close();
        }
        System.out.println((System.currentTimeMillis() - start));

//
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            jedis.set("RDB!"+i,"RDB"+i);
//        }
//        System.out.println((System.currentTimeMillis() - start));
//        start = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            Pipeline pipelined = jedis.pipelined();
//            pipelined.set("RDB!"+i,"RDB"+i);
//            pipelined.close();
//        }
//        System.out.println((System.currentTimeMillis() - start));
    }
}
