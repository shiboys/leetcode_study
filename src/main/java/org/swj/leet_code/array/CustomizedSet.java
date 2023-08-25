package org.swj.leet_code.array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/25 10:27
 *        O(1) 的时间内实现 insert，delete，getRandom 等方法，
 *        leetcode 第 380 题
 */
public class CustomizedSet {
    List<Integer> valList;
    Map<Integer, Integer> valToIndex;
    int valIndex;
    Random  random ;

    public CustomizedSet() {
        valList = new ArrayList<>();
        valToIndex = new HashMap<>();
        random = new Random();
    }

    public boolean insert(int val) {
        if (valToIndex.containsKey(val)) {
            return false;
        }
        valList.add(val);
        valToIndex.put(val, valList.size() - 1);
        return true;
    }

    public boolean remove(int val) {
        Integer idx = valToIndex.get(val);
        if (idx == null) {
            return false;
        }
        // 把 val 置换到数组尾部，然后再删除
        int lastIndex = valList.size() - 1;
        if (lastIndex != idx) { // 说明被删除的不是最后一个元素，则进行置换删除
            int temp = valList.get(lastIndex);
            valList.set(idx, temp);
            // 索引维护
            valToIndex.put(temp, idx);
        }
        // 删除操作
        valList.remove(lastIndex);
        // 维护索引
        valToIndex.remove(val);
        return true;
    }

    public int getRandom() {
        int idx = random.nextInt(valList.size());
        return valList.get(idx);
    }

    public static void main(String[] args) {
        CustomizedSet instance = new CustomizedSet();
        System.out.println(instance.insert(1));
        System.out.println(instance.remove(2));
        System.out.println(instance.insert(2));
        System.out.println(instance.getRandom());

        System.out.println(instance.remove(1));

        System.out.println(instance.insert(2));
        System.out.println(instance.getRandom());
    }
}
