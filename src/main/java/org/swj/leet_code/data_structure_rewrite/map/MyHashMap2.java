package org.swj.leet_code.data_structure_rewrite.map;

import java.util.Random;

import com.google.common.base.Objects;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/28 19:02
 *        线性探查法实现 hashMap。线性探查是开放地址法解决 hash 冲突的常用方法之一。
 */
public class MyHashMap2<K, V> {
    private static final int DEFAULT_CAP = 4;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final int MASK = 0x7fffffff;
    private static final double LOAD_FACTOR = 0.8;

    private int size;

    private Node<K, V>[] elements;

    public MyHashMap2() {
        this(DEFAULT_CAP);
    }

    public MyHashMap2(int capacity) {
        capacity = ceilingToPowerOf2(capacity);
        elements = new Node[capacity];
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (size >= elements.length * LOAD_FACTOR) {
            resize(elements.length * 2);
        }
        int mask = elements.length - 1;
        int idx = hash(key) & mask;
        Node<K, V> node;
        while ((node = elements[idx]) != null) {
            if (Objects.equal(key, node.key)) {
                V oldVal = node.value;
                node.value = value;
                return oldVal;
            }
            idx = (idx + 1) & mask;
        }
        elements[idx] = new Node<>(key, value);
        size++;
        return null;
    }

    public V get(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    public V remove(K key) {
        int mask = elements.length - 1;
        int idx = hash(key) & mask;
        Node<K, V> node;
        while ((node = elements[idx]) != null) {
            if (Objects.equal(key, node.key)) {
                break;
            }
            idx = (idx + 1) & mask;
        }
        if (node == null) { // 当前节点不存在
            return null;
        }
        V oldVal = node.value;
        node = elements[idx];
        elements[idx] = null;
        size--;
        // 将 index 以及之后所有的不为 null 的元素需要进行重新 rehash ，重新线性探查
        idx = (idx + 1) & mask;
        while ((node = elements[idx]) != null) {
            // 把 idx 位置腾出来，可以被重新 rehash 的元素能落座
            elements[idx] = null;
            put(node.key, node.value);
            idx = (idx + 1) & mask;
        }
        // 缩容判断
        if (size <= elements.length / 4) {
            resize(elements.length / 2);
        }

        return oldVal;
    }

    Node<K, V> getNode(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        int mask = elements.length - 1;
        int idx = hash(key) & mask;
        Node<K, V> node;
        while ((node = elements[idx]) != null) {
            if (Objects.equal(key, node.key)) {
                return node;
            }
            idx = (idx + 1) & mask;
        }
        return null;
    }

    int hash(K key) {
        int h = 0;
        h = (h = key.hashCode()) ^ (h >>> 16);
        return h & MASK;
    }

    private void resize(int newCapacity) {
        Node<K, V>[] newContainer = new Node[newCapacity];
        for (Node<K, V> node : elements) {
            if (node == null) {
                continue;
            }
            int mask = newCapacity - 1;
            int idx = hash(node.key) & mask;
            while (newContainer[idx] != null) {
                idx = (idx + 1) & mask;
            }
            newContainer[idx] = node;
        }
        elements = newContainer;
    }

    private int ceilingToPowerOf2(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n <= 0 ? 1 : n > MAX_CAPACITY ? MAX_CAPACITY : n + 1;
    }

    static class Node<K, V> {
        public K key;
        public V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node() {

        }
    }

    public static void main(String[] args) {
        final int max = 24;
        final int multi = 10;
        MyHashMap2<Integer, Integer> map = new MyHashMap2<>();
        for (int i = 1; i <= max; i++) {
            map.put(i, i * multi);
        }
        System.out.println("capacity is " + map.elements.length);
        for (int i = 1; i <= max; i++) {
            System.out.println("key = " + i + ", and get(key)=" + map.get(i));
        }

        int delCount = max >> 1;
        Random random = new Random();
        for (int i = 0; i < delCount; i++) {
            int key = random.nextInt(max) + 1;

            System.out.println("remove key = " + key + ", and value =" + map.remove(key) + ", get(key) = " + map.get(key));

        }
    }
}
