package cn.inkroom.study.algorithm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现一个简单的LRU算法。尽管Redis中用的并不是标准LRU
 *
 * @author inkbox
 * @date 2021/6/7
 */
public class LRU<K, V> {


    private int size = 0;
    /**
     * 头尾节点会存储数据
     */
    private Node<K, V> head;
    private Node<K, V> tail;
    private Map<K, Node<K, V>> map;

    public LRU(int size) {
        this.size = size;
        map = new ConcurrentHashMap<>(size);
    }

    public void put(K key, V value) {
        Node<K, V> v = map.get(key);
        if (v != null) {
            // 移动到头部
            v.value = value;
            moveToHead(v);
            return;
        }
// 缓存中没有数据
        //添加到头部
        v = new Node<>();
        v.key = key;
        v.value = value;

        if (map.size() >= size) {
            //淘汰最后一个值
            map.remove(tail.key);
            head.pre = tail.pre;
            tail.pre.next = head;

            tail = tail.pre;
        }

        if (this.head == null) {
            this.head = v;
            this.tail = v;
            head.pre = tail;
            tail.next = head;
        }

        map.put(key, v);
        moveToHead(v);
    }

    public V get(K key) {
        Node<K, V> v = map.get(key);
        if (v != null) {
            // 移动到头部
            moveToHead(v);
        }
        return v.value;
    }



    private void moveToHead(Node<K, V> v) {

        if (head == v) {
            return;
        }

        // 先把当前节点从链表中抽离出来
        if (v.pre != null && v.next != null) {//只要是在链表中，这两个属性就一定不为null
            v.pre.next = v.next;
            v.next.pre = v.pre;
        }
        if (tail == v){
            tail = tail.pre;
        }
        //然后放在头节点前面
        v.pre = tail;
        v.next = head;
        head.pre = v;
        tail.next = v;
        this.head = v;
    }

    class Node<T, V> {
        Node<T, V> pre;
        Node<T, V> next;
        T key;
        V value;

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node<K, V> node = this.head;
        while (true) {
            builder.append("{key:" + node.key + ",value:" + node.value + "}").append("-");
            node = node.next;
            if (node == this.head) {
                break;
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        LRU<String, Integer> lru = new LRU<>(4);

        //添加4个值
        lru.put("1", 1);
        lru.put("2", 2);
        lru.put("3", 3);
        lru.put("4", 4);

        System.out.println(lru);

        lru.put("5", 5);
        System.out.println(lru);

        lru.get("2");
        System.out.println(lru);

    }
}
