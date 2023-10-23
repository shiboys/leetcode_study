package org.swj.leet_code.algorithm.dynamic_programming.subsequence;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/27 20:34
 *        最大子数组
 *        leecode 第 53 题
 *        比如说输入 nums=[-3,1,3,-1,2,-4,2], 算法返回 5，因为最大子数组 [1,3,-1,2]的和为 5.
 */
public class MaxSubArray {

    /**
     * 其实这道题也适合用滑动窗口，滑动窗口是专门用来处理子串/子数组的问题的
     * 这里不就是子数组的问题吗？
     * 想用滑动窗口，想问自己几个问题：
     * 1、什么时候应该扩大窗口
     * 2、什么时候应该缩小窗口
     * 3、什么时候更新答案
     * 滑动窗口对 nums 包含负数也是可以用滑动窗口表示的
     * 
     * 我们可以在窗口内元素之和大于 0 的时候扩大窗口，在窗口内元素之和小于 0 的时候缩小窗口，在每次移动窗口的时候更新答案。
     */

    int maxSumOfSubArray(int[] array) {
        int window_left = 0;
        int window_right = 0;
        int curSum = 0;
        // maxSum 不能初始化为 0 ，否则如果 array=[-1], 则结果就是错误的，必须初始化为数组的第一个元素
        int maxSum = array[0];
        for (int i = 0; i < array.length; i++) {
            // 计算窗口内元素的sum
            curSum += array[window_right];
            // 每次循环都扩大窗口
            window_right++;
            // 根据情况更新元素
            if (curSum > maxSum) {
                maxSum = curSum;
            }
            // curSum 小于0 时，缩小窗口
            while (curSum < 0) {
                curSum -= array[window_left];
                window_left++;
            }
        }
        return maxSum;
    }

    /**
     * 动态规划解决办法，
     * nums[0..i] 中的「最大子数组和」为 dp[i]。
     * 如果按照这样定义的话，整个 nums[i] 数组的最大子数组和 就是 dp[n-1]。如果找状态转移方程那？
     * 按照数学归纳法，假设我们知道了 dp[i-1], 如何推导出 dp[i] 那？
     * 如下所示
     * i i+1
     * -3, 4, -1, 2, -6, 1, 4
     * dp[i]=5,也就是等于 nums[0...i] 中的最大子数组和，
     * 那么在上面的表示中，利用数学归纳法，我们能利用 dp[i] 到出 dp[i+1] 吗？
     * 实际上是不行的，因为子数组一定是连续的，按照我们当前 dp 数组的定义，并不能保证 nums[0..i] 中的最大子数组与 nums[i+1] 是相临的，
     * 也就没有办法从 dp[i] 推导出 dp[i+1]。
     * 所以说我们这样定义 dp 数组是不正确的，无法得到合适的状态转移方程。对于这类子数组的问题，我们就重新定义 dp 数组的含义：
     * 
     * 以 nums[i] 为结尾的「最大子数组和」为 dp[i]，而不是 nums[0..i] 中的最大子数组和（ps：我感觉是一个东西呀？不一样，以
     * nums[i] 结尾的不一定是从 0 开头）
     * 当然了，最大子数组也可能不包含结尾的nums[i]。
     * 这种定义之下，想得到整个 nums 数组的「最大子数组和」，不能直接返回 dp[n-1]。而是需要遍历整个 dp 数组
     * 我们依然使用数学归纳法来寻找状态转移关系：假设我们已经算出了 dp[i-1]，如果推到出 dp[i] 那？
     * 可以做到，dp[i] 有两种选择：要么与前面的相邻子数组连接，形成一个和更大的子数组；要么不与前面的子数组连接，自成一派，自己作为一个子数组。
     * 如果选择？既然要求「最大子数组和」，当然选择结果更大的那个了。
     * dp[i]=Math.max(dp[i],dp[i]+dp[i-1]).
     * 综上分析，我们可以写出状态转移方程
     * 
     * @param arr
     * @return
     */
    int maxSumWithDp(int[] arr) {
        int[] dp = new int[arr.length];
        // base case 第一个元素没有子数组
        dp[0] = arr[0];
        // 状态转移方程
        for (int i = 1; i < arr.length; i++) {
            dp[i] = Math.max(arr[i], arr[i] + dp[i - 1]);
        }
        // 获取 dp 数组中的最大子数组之和
        // return Arrays.stream(dp).max().getAsInt();
        int res = Integer.MIN_VALUE;
        for (int dpVal : dp) {
            res = Math.max(res, dpVal);
        }
        return res;
    }

    public static void main(String[] args) {
        MaxSubArray instance = new MaxSubArray();
        int[] arr = new int[] { -3, 1, 3, -1, 2, -4, 2 };
        // System.out.println(instance.maxSumOfSubArray(arr));
        System.out.println(instance.maxSumWithDp(arr));
        System.out.println(instance.maxSubArraySumNoDp(arr));
    }

    /**
     * 上面的解法时间复杂度是 O(n),空间复杂度也是O(n)，较暴力解法已经很优秀了，不过注意到 dp[i] 仅仅和 dp[i-1] 的状态有关
     * 那么我们可以将 dp 压缩甚至替换掉，进一步优化空间复杂度。
     * 下面是 剑指 offer 中同类方法的解法
     * com.swj.ics.dataStructure.array.MaxSumOfSeqSubArray 这个就是完全相同的解法了
     * 子数组之和大于 0 就加上 nums[i], 否则就等于 nums[i]
     * 也就是供公司合并，A 公司扣除债务后有盈利则合并 B 公司，否则 A 公司破产清零，然后加入 B 公司成为 B 公司一部分
     * 
     * @param nums
     * @return
     */
    int maxSubArraySumNoDp(int[] nums) {
        int curSum = nums[0];
        // 这里是个 bug，在数组只有一个元素的时候，没能正确初始化 maxSum，尴尬了
        // int maxSum = 0;
        int maxSum = curSum;
        for (int i = 1, len = nums.length; i < len; i++) {
            if (curSum > 0) {
                curSum += nums[i];
            } else {
                curSum = nums[i];
            }
            maxSum = Math.max(maxSum, curSum);
        }
        return maxSum;
    }
}
