package org.swj.leet_code.algorithm.dynamic_programming.basic_skill;

/**
 * 爬楼梯
 * leetcode 70. Climbing Stairs
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/29 11:20
 */
public class ClimbingStairs {
    public int climbStairs(int n) {

        // 先动动态规划求解，把 1 和 2 看成是零钱 1 和 2 ，然后 n 看成是 amount
        // 经过实践发现，凑零钱的 dp 无法解决跳台阶的 dp
        // return dp(new int[]{1,2}, n);
        return dp(n);
    }

    // 凑零钱的动态规划解法无法用在跳台阶上，因为凑零钱 1+2=2+1=3 表示一种凑法， 但是跳台阶 则是两种跳法
    int dp(int[] coins, int amount) {
        int m = coins.length;
        // dp 的定义为 凑够 金额为 i 的最大
        int[] dp = new int[amount + 1];
        // base case
        dp[0] = 1;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= amount; j++) {
                if (j >= coins[i - 1]) {
                    dp[j] = dp[j] + dp[j - coins[i - 1]];
                }
            }
        }
        return dp[amount];
    }

    int dp(int n) {
        // 台阶为 n 时的跳法种数
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            // 我可以跳一步，也可以跳两步 到 i。f(x) = f(x-1) + f(x-2);
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}
