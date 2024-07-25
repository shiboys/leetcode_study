package org.swj.leet_code.algorithm.dynamic_programming.bag;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/11 21:02
 *        leetcode 第 416 题，分割等和子集。
 *        输入一个只包含正整数的非空数组 nums, 请你写一个算法，判断这个数组是否可以被分割成两个子集，使得连个子集的元素和相等。
 *        比如说 nums=[1,5,11,5],算法返回 true，因为 nums 可以分割成 [1,5,5] 和 [11] 这两个子集。
 *        再比如说 nums=[1,3,2,5],算法返回 false，因为 nums 无论如何都不能分割成两个相等的子集。
 */
public class PartitionEqualSubsetSum {
  boolean canPartition(int[] nums) {
    int sum = Arrays.stream(nums).sum();
    if (sum % 2 != 0) { // 奇数时不能平均分割的
      return false;
    }
    sum = sum / 2;
    int m = nums.length;
    int n = sum;
    boolean[][] dp = new boolean[m + 1][n + 1];
    // base case
    // dp[0][...] 如果数组无任何数字，那么能凑出 sum 肯定为 false
    // 如果 sum = 0 ，任何数字都可以凑出 0。也可以理解为 sum 可以经过背包装入物品，容量为 0，被恰好装满
    for (int i = 0; i < m; i++) {
      dp[i][0] = true;
    }
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        // dp[i][j] 的定义为是否可以满足 前 i 个数可以凑出 j
        // 如果 第 i-1 个数能凑出 j - nums[i-1] , 则 第 i 个数就能凑出 j。
        // 或者说 如果前 i-1 个数能凑出 j ，那前 i 个数更是能凑出 j 了。
        // 最后返回 dp[m][n] 就表示是否能凑出 sum 了，能凑出 sum 则原始的 sum*2 就肯定也能凑出
        //
        if (j < nums[i - 1]) {
          dp[i][j] = dp[i - 1][j];
        } else {
          dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]];
        }
      }
    }
    return dp[m][n];
  }

  boolean canPartition2(int[] nums) {
    int sum = Arrays.stream(nums).sum();
    sum = sum / 2;
    int m = nums.length;
    boolean[] dp = new boolean[sum + 1];
    // 空背包或者背包被填满则为 true
    dp[0] = true;
    for (int i = 1; i <= m; i++) {
      // j 是倒着遍历的，卧槽，鸡贼呀，想不起来
      // 倒着遍历的目的是，每个物品或者说数字，只能用一次，以免以前的结果影响其他结果
      for (int j = sum; j >= 0; j--) {
        if (j - nums[i - 1] >= 0) {
          // dp[j] 上一次装入本次不装入的结果；本次装入num[i-1]物品的结果dp[j - nums[i - 1]]
          // 等于二维数组的 dp[i-1][j..] 的 i-1 没有了
          dp[j] = dp[j] || dp[j - nums[i - 1]];
        }
      }
    }
    return dp[sum];
  }

  public static void main(String[] args) {
    int[] nums = new int[] { 1, 5, 11, 5 };
    PartitionEqualSubsetSum instance = new PartitionEqualSubsetSum();
    System.out.println(instance.canPartition(nums));
    nums = new int[] { 1, 3, 2, 5 };
    System.out.println(instance.canPartition(nums));

    nums = new int[] { 19, 9, 7, 5, 3, 1 };

    System.out.println(instance.canPartition(nums));
  }
}
