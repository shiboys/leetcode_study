package org.swj.leet_code.advanced_data_structure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 379. 电话目录管理系统
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/11 19:29
 */
public class PhoneDirectory {
    /**
     * leetcode 379,plus 会员题目
     * 设计一个电话目录管理系统，让它支持以下功能：
     * 
     * get: 分配给用户一个未被使用的电话号码，获取失败请返回 -1
     * check: 检查指定的电话号码是否被使用
     * release: 释放掉一个电话号码，使其能够重新被分配
     * 
     * // 初始化电话目录，包括 3 个电话号码：0，1 和 2。
     * PhoneDirectory directory = new PhoneDirectory(3);
     * 
     * // 可以返回任意未分配的号码，这里我们假设它返回 0。
     * directory.get();
     * 
     * // 假设，函数返回 1。
     * directory.get();
     * 
     * // 号码 2 未分配，所以返回为 true。
     * directory.check(2);
     * 
     * // 返回 2，分配后，只剩一个号码未被分配。
     * directory.get();
     * 
     * // 此时，号码 2 已经被分配，所以返回 false。
     * directory.check(2);
     * 
     * // 释放号码 2，将该号码变回未分配状态。
     * directory.release(2);
     * 
     * // 号码 2 现在是未分配状态，所以返回 true。
     * directory.check(2);
     */

    /**
     * 思路分析
     * 我当初看到这个序号和用例的描述时，发现用 Set 就可以了，HashSet 没问题，
     * 后来看了 阿东的思路，发现他除了用 set ，还用了 List 来记录元素。
     * 但是我认为没有必要，我就写两种方法把，看看提交 leetcode 是否通过
     */
    private Set<Integer> set;

    public PhoneDirectory(int maxNumbers) {
        set = new HashSet<>();
        for (int i = maxNumbers - 1; i >= 0; i--) {
            set.add(i);
        }
    }

    public int get() {
        int val = set.iterator().next();
        set.remove(val);
        return val;
    }

    public boolean check(int number) {
        return set.contains(number);
    }

    public void release(int number) {
        if (!set.contains(number)) {
            set.add(number);
        }
    }

    public static void main(String[] args) {
        int maxValue = 10;
        PhoneDirectory pd = new PhoneDirectory(maxValue);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < maxValue; i++) {
            list.add(i);
        }
        int val = -1;
        for (int i = 0; i < 3; i++) {
            val = pd.get();
            System.out.println("val is " + val);
            list.remove(val);
        }

        System.out.println("val = " + val + ", check val = " + pd.check(val));
        pd.release(val);
        System.out.println(pd.check(val));

    }
}
