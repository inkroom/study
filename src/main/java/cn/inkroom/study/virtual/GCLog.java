package cn.inkroom.study.virtual;

/**
 * @author inkbox
 * @date 2021/6/1
 */
public class GCLog {
    public static void main(String[] args) {
        // -Xlog:gc* -Xlog:safepoint
        for (int i = 0; i < 10000; i++) {
            Object o =new Object();
            System.gc();
        }
    }
}
