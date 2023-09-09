package org.swj.leet_code.array;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/03/02 19:09 力扣第 1 题，在一个数组中 求 两个数字之和为定值 S 的两个数组的索引
 */
public class TwoNumberSum {

  /**
   * 解法 1，使用蛮力法求解，通过获取数组中的第 i 个元素，将 i 个元素和后面的所有元素求和，如果等于目标值，则返回
   *
   * @param nums
   * @param target 目标和
   * @return
   */
  public int[] twoSum(int[] nums, int target) {
    for (int i = 0; i < nums.length; i++) {
      for (int j = i + 1; j < nums.length; j++) {
        if (nums[i] + nums[j] == target) {
          return new int[] {i, j};
        }
      }
    }
    return null;
  }

  /**
   * 算法1 的时间复杂度很明显为 O(n^2), 如何进行优化那？ 如果给定一个数和目标和，需要寻找下一个元素是否在数组中，如果能在 O(1) 的时间里面找到另外一个数，是不是复杂度就讲到
   * O(n) 了？ O(1) 从集合中找到一个数，我们一般想到的数据结构就是 Map<K,V>，因此解法 2 采用 Map 来解
   */
  public int[] twoSum2(int[] nums, int target) {
    java.util.Map<Integer, Integer> map = new java.util.HashMap<>();
    for (int i = 0; i < nums.length; i++) {
      int curr = nums[i];
      int targetNum = target - curr;
      if (map.containsKey(targetNum)) {
        // map 里面的数据是数组的第一个元素
        return new int[] {map.get(targetNum), i};
      }
      map.put(curr, i);
    }
    return null;
  }

  public static void main(String[] args) {
    int[] arr = new int[] {2, 7, 11, 15};
    int target = 22;
    System.out.println(Arrays.toString(new TwoNumberSum().twoSum(arr, target)));
    System.out.println(Arrays.toString(new TwoNumberSum().twoSum2(arr, target)));
  }
}
