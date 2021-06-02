package cn.inkroom.study.redis;

/**
 * 漏水算法
 *
 * @author inkbox
 */
public class FunnelRateLimiter {

    static class Funnel {
        /**
         * 容器的容量
         */
        int capacity;
        /**
         * 流水速率，单位是 个/秒
         * 以毫秒为单位，流水速率会比较小
         */
        float leakingRate;
        /**
         * 容器内的剩余容量
         */
        int leftQuota;

        /**
         * 上次漏水时间，实际代表的是上次请求时间
         */
        long lastLeakTime;

        /**
         * 初始化限流器
         * <p>
         * 假设某个行为需要100秒内最多50次(2秒一次)，最多连续操作15次
         * 那么容器应该是15，流水速率为 50 / 100 = 0.5
         *
         * 容器会在第30秒装满，然后维持两秒一次请求通过
         *
         *
         * @param capacity    容器数量
         * @param leakingRate 流水速率
         */
        public Funnel(int capacity, float leakingRate) {
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.lastLeakTime = System.currentTimeMillis();
        }


        void makeSpace() {
            long nowTs = System.currentTimeMillis();
            // 距离上次漏水差了多少时间
            long deltaTs = nowTs - lastLeakTime;
            // 计算应该流走多少数据
            int deltaQuota = (int) (deltaTs / 1000 * leakingRate);


            if (deltaQuota < 0) {// 书中注释，间隔时间太长，整数数字过大溢出
                this.leftQuota = capacity;
                this.lastLeakTime = nowTs;
                return;
            }
            // 腾出空间太小，最小单位为1
            if (deltaQuota < 1) {
                return;
            }
            // 修改剩余容量
            this.leftQuota += deltaQuota;
            this.lastLeakTime = nowTs;
            if (this.leftQuota > this.capacity) {
                this.leftQuota = this.capacity;
            }

        }

        boolean watering(int quota) {
            makeSpace();
            if (this.leftQuota >= quota) {
                // 剩余容量减小
                this.leftQuota -= quota;
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        Funnel funnel = new Funnel(15, 0.5f);
        long now = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("当前：" + ((System.currentTimeMillis() - now) / 1000) + " " + funnel.watering(1));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();
    }

}
