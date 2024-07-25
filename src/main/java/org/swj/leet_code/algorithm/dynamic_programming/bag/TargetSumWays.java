package org.swj.leet_code.algorithm.dynamic_programming.bag;

import java.util.LinkedList;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/11 17:58
 *        leetcode 494 目标和
 *        给你输入一个非负整数数组 nums 和一个目标值 target，
 *        现在你可以给每一个元素 nums[i] 添加正号 + 或负号 -，请你计算有几种符号的组合能够使得 nums 中元素的和为 target。
 *        比如说输入 nums = [1,3,1,4,2], target = 5，算法返回 3，因为有如下 3 种组合能够使得 target 等于
 *        5
 *        +1+3-1+4-2=5
 *        -1+3+1+4-2=5
 *        +1-3+1+4+2=5
 */
public class TargetSumWays {
    public int findTargetSumWays(int[] nums, int target) {
        backtrack(nums, 0, target);
        return matchedCount;
    }

    int matchedCount = 0;
    List<Integer> trackList = new LinkedList<>();

    /* 回溯算法模板 */
    void backtrack(int[] nums, int i, int remain) {
        // base case
        if (i == nums.length) {
            if (remain == 0) {
                // 说明恰好凑出 target
                matchedCount++;
            }
            return;
        }
        // 给 nums[i] 选择 - 号
        remain += nums[i];
        // 穷举 nums[i + 1]
        backtrack(nums, i + 1, remain);
        // 撤销选择
        remain -= nums[i];

        // 给 nums[i] 选择 + 号
        remain -= nums[i];
        // 穷举 nums[i + 1]
        backtrack(nums, i + 1, remain);
        // 撤销选择
        remain += nums[i];
    }

    public static void main(String[] args) {
        int[] nums = new int[] { 1, 3, 1, 4, 2 };
        int target = 5;
        TargetSumWays instance = new TargetSumWays();
        System.out.println(instance.findTargetSumWays(nums, target));
    }

}
