package org.swj.leet_code.data_structure_rewrite.map;

import java.util.Map;
import java.util.Random;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/28 17:02
 *        拉链法实现 Map 功能
 */
public class MyHashMap1<K, V> {
    private static final int DEFAULT_CAP = 4;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final int MASK = 0x7fffffff;
    private static final double LOAD_FACTOR = 0.75;

    private int size;
    private Slot<K, V>[] elements;

    public MyHashMap1() {
        this(DEFAULT_CAP);
    }

    public MyHashMap1(int capacity) {
        capacity = ceilingToPowerOf2(capacity);
        elements = new Slot[capacity];
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        int cap = elements.length;
        if (size > LOAD_FACTOR * cap) {
            resize(elements.length * 2);
            cap = elements.length;
        }
        int idx = hash(key) & (cap - 1);
        Slot<K, V> slot = elements[idx];
        V oldVal = null;
        if (slot == null) {
            slot = elements[idx] = new Slot<>();
        }
        oldVal = slot.put(key, value);
        size++;
        return oldVal;
    }

    public boolean containsKey(K key) {
        Slot<K, V> slot = getSlot(key);
        return slot != null ? slot.containsKey(key) : false;
    }

    public V get(K key) {
        Slot<K, V> slot = getSlot(key);
        return slot != null ? slot.get(key) : null;
    }

    public V remove(K key) {
        Slot<K, V> slot = getSlot(key);
        if (slot == null) {
            return null;
        }
        V val = null;
        if (slot.containsKey(key)) {
            val = slot.remove(key);
            size--;
            if (size <= elements.length / 4) {
                // 缩容
                resize(elements.length / 2);
            }
        }
        return val;
    }

    private Slot<K, V> getSlot(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        return elements[hash(key) & (elements.length - 1)];
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

    int hash(K key) {
        int h = 0;
        // 让 高低位参与运算，这样 hash 函数会更合理，元素分布更均匀
        h = key == null ? h : (h = key.hashCode()) ^ (h >>> 16);
        return h < 0 ? h & MASK : h;
    }

    private void resize(int newCapacity) {
        Slot<K, V>[] newContainer = new Slot[newCapacity];
        for (Slot<K, V> slot : elements) {
            if (slot == null) {
                continue;
            }
            for (Map.Entry<K, V> entry : slot.entries()) {
                // 这里 有个优化，参考 hashMap ，todo：暂时不做
                int index = hash(entry.getKey()) & (newCapacity - 1);
                Slot<K, V> newSlot = newContainer[index];
                if (newSlot == null) {
                    newContainer[index] = newSlot = new Slot<>();
                }
                newSlot.put(entry.getKey(), entry.getValue());
            }
        }
        elements = newContainer;
    }

    public static void main(String[] args) {
        int ten = 10;
        MyHashMap1<Integer, Integer> map = new MyHashMap1<>();
        for (int i = 1; i <= 10; i++) {
            map.put(i, i * ten);
        }

        for (int i = 1; i <= 10; i++) {
            System.out.println("key = " + i + ",map.get(key) = " + map.get(i));
        }

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int key = random.nextInt(ten) + 1;
            System.out.println("remove key = " + key + ",value = " + map.remove(key));
            System.out.println("key = " + key + ",map.get(key) = " + map.get(key));
        }
    }
}
