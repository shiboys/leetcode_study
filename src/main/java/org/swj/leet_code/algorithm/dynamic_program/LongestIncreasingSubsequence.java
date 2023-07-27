package org.swj.leet_code.algorithm.dynamic_program;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/27 15:16
 * Leecode 第 300 题，
 * 最长增长子序列 LongestIncreasingSubsequence，简称 LIS ，是非常经典的一个算法问题，比较容易想到的是动态规划解法，时间
 * 复杂度是O(N^2), 我们借这个问题由浅入深讲解下状态转移方程，如果写出动态规划解法。
 * 另外一个解法是，利用二分查找，该想法是比较难想到的，此时的时间复杂度是O(NlogN)，我们通过一种简单的纸牌游戏来辅助理解这种巧妙的解法
 * 纸牌游戏的过程参考 note.md
 * 比如说输入 nums=[10,9,2,5,3,7,101,18]，其中最长的递增子序列是 [2,3,7,101], 所以算法的输出应该是 4
 * 注意「子序列」和「子串」这两个名词的区别，子串一定是连续的，而子序列不一定是连续的。
 * 下面我们来设计动态规划算法来解决这个问题
 */
public class LongestIncreasingSubsequence {
    /**
     * dp[i] 定义为 表示以 nums[i] 这个数结尾的最长递增子序列的长度
     *      0   1   2   3   4   5
     * nums 1   4   3   4   2   3  
     *   dp 1   2   2   3   2   3
     * 观察上面这个数组，发现 nums[5]=3,要怎么算 dp[5] 那？
     * 既然是递增子序列，我们只要找到前面哪些结尾比 3 小的子序列，然后把 3 接到这些子序列的末尾，就可以形成一个新的
     * 递增自序列，而这个新的子序列的长度加 1
     * 以我们举的例子来看，nums[0] 和 nums[4] 都是小于 nums[5] 的，然后 可以的得出
     * dp[5] = max(dp[1]+1,dp[4]+1) = max(1+1,2+1)
     * nums[5] 前面有哪些元素小于 nums[5]? 这个好算，用 for 循环就能比较一波把这些元素找出来
     * for (int j=0;j<i; j++) {
     *  if(nums[i] > nums[j]) {
     *      dp[i] = Math.max(dp[i],dp[j]+1)
     *  }
     * }
     * 当 i=5 时，这段代码的逻辑就可以算出 dp[5]。其实到这里，这道算法题我们基本已经实现了
     * dp[4], dp[3] 怎么算 ？ 类似数学归纳法，我们已经可以算出 dp[5] 了，其他的就可以算出来。
     */

    int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        // 每个元素的最长自序列长度至少是 1（元素本身的长度）
        Arrays.fill(dp,1);
        for(int i=0;i<nums.length;i++) {
            for(int j =0;j<i;j++) {
                if(nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j]+1);
                }
            }
        }
        return Arrays.stream(dp).max().getAsInt();
    }

    /**
     * 上述解法是标准的动态规划，但是对于最长递增子序列来说，这个解法不是最优的，可能无法通过所有测试用例
     * 下面我们使用 二分法来查找最长子序列。二分法查找的详细表述在 note.md 中最长子序列查找——二分法章节
     * @param nums
     * @return
     */
    int lengthOfLisBs(int[] nums) {
        return 0;
    }


    public static void main(String[] args) {
        int[] arr = new int[] {10,9,2,5,3,7,101,18};
        LongestIncreasingSubsequence instance = new LongestIncreasingSubsequence();
        System.out.println(instance.lengthOfLIS(arr));
    }
}