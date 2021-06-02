package cn.inkroom.study.virtual;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试堆内存oom
 * @author inkbox
 * @date 2021/5/31
 */
public class HeapOOM {

    public static void main(String[] args) {
//        -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
        List<Object> list = new ArrayList<>();
        while (true){
            list.add(new Object());
        }
    }
}
