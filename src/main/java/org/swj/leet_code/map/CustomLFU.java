package org.swj.leet_code.map;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 不借助于 LinkedHashMap 手动实现 LRU。
 * leetcode 460 题
 */
public class CustomLFU {
    int cap;
    Map<Integer, Integer> valMap;
    Map<Integer, Integer> keyToFreq;
    Map<Integer, Set<Integer>> freqToKeys;
    int minFreq;

    public CustomLFU(int capacity) {
        this.cap = capacity;
        valMap = new HashMap<>();
        keyToFreq = new HashMap<>();
        freqToKeys = new HashMap<>();
    }

    public int get(int key) {
        if (!valMap.containsKey(key)) {
            return -1;
        }
        int val = valMap.get(key);
        promoteKeyFreq(key);

        return val;
    }

    void promoteKeyFreq(int key) {
        // 维护 key:freq
        int freq = keyToFreq.get(key);
        int newFreq = freq + 1;
        keyToFreq.put(key, newFreq);

        // 维护 freq:key
        Set<Integer> keys = freqToKeys.get(freq);
        if (keys != null && !keys.isEmpty()) {
            keys.remove(key);
            if (keys.isEmpty()) { // 将该 freq 的 entry 从 map 中删除
                freqToKeys.remove(freq);
                if (freq == minFreq) { // 维护最小访问频率
                    minFreq = newFreq;
                }
            }
        }

        freqToKeys.putIfAbsent(newFreq, new LinkedHashSet<>());
        freqToKeys.get(newFreq).add(key);
    }

    public void put(int key, int value) {
        if (valMap.containsKey(key)) {// 存在 key
            valMap.put(key, value);
            promoteKeyFreq(key);
        } else {// 不存在 key
            // 首先判断是否超容
            while (valMap.size() >= cap) {
                // 删除使用频率最小的 key
                deleteMinFreq();
            }

            // 然后再将新 key-value 加入三个容器中。
            valMap.put(key, value);
            minFreq = 1;
            keyToFreq.put(key, 1);

            freqToKeys.putIfAbsent(1, new LinkedHashSet<>());
            freqToKeys.get(1).add(key);
        }
    }

    void deleteMinFreq() {
        Set<Integer> keys = freqToKeys.get(minFreq);
        if(keys == null && !freqToKeys.isEmpty()) {
            // todo, 去寻找 freq 最小的，后来想了一下，这个场景基本不会出现
            // 因为不会出现连续被删，删除不是一个外部 API，是内部的一个主动措施，被删除一次
            // map 就会有空间，可以执行正常的插入，这时候 minFreq 会被重置
            minFreq = freqToKeys.keySet().stream().min(Integer::compareTo).orElse(1);
            keys = freqToKeys.get(minFreq);
        }
        if (keys != null && !keys.isEmpty()) {
            // 最小频率且最早使用的元素。
            int key = keys.iterator().next();
            valMap.remove(key);
            int freq = keyToFreq.remove(key);
            // 维护 freq:key
            keys.remove(key);
            if (keys.isEmpty()) { // 将该 freq 的 entry 从 map 中删除
                freqToKeys.remove(freq);
            }
        }
    }

    public static void main(String[] args) {
        CustomLFU cache = new CustomLFU(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));

        cache.put(3, 3);
        System.out.println(cache.get(2));
        System.out.println(cache.get(3));
        cache.put(4, 4);

        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
    }
}
