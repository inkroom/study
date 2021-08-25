package cn.inkroom.study.concurrent;

import java.util.HashMap;

/**
 * @author inkbox
 * @date 2021/6/16
 */
public class HashMapExample {

    public static void main(String[] args) {
        int count = 256;
        HashMap<Key, Integer> map = new HashMap<>(128);
        for (int i = 0; i < count; i++) {
            map.put(new Key(i), i);

        }

        System.out.println(map.size());


    }

    static class Key {
        Integer value;

        public Key(Integer value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            if (value < 128)
                return super.hashCode();
            else return 23;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return value.equals(key.value);
        }
    }
}
