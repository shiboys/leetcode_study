package org.swj.leet_code.algorithm.dynamic_programming.bag;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/11 22:39
 *        找零钱 II
 *        题目描述：
 * 
 *        给定不同面额的硬币 coins 和一个总金额 amount，写一个函数来计算可以凑成总金额的硬币组合数。假设每一种面额的硬币有无限个。
 * 
 *        比如说输入 amount = 5, coins = [1,2,5]，算法应该返回 4，因为有如下 4 种方式可以凑出目标金额：
 * 
 *        5=5
 * 
 *        5=2+2+1
 * 
 *        5=2+1+1+1
 * 
 *        5=1+1+1+1+1
 * 
 *        如果输入的 amount = 5, coins = [3]，算法应该返回 0，因为用面额为 3 的硬币无法凑出总金额 5。
 */
public class CoinChange2 {
    int change(int amount, int[] coins) {

        int[][] dp = new int[coins.length + 1][amount + 1];
        
        for (int i = 0; i < dp.length; i++) {
            dp[i][0] = 1;// 0 金额表示刚好可以凑出来
        }
        dp[0][0] = 0;

        for (int i = 1; i <= coins.length; i++) {
            for (int j = 1; j <= amount; j++) {
                if (j >= coins[i - 1]) {
                    // dp[i][j] 的数量主要包括两种凑法
                    // 1、不把第 coins[i-1] 的金额放进书包/加入，而是继承之前的结果凑出 j 的种类数。
                    // 2、加入 coins[i-1] 的金额，但之前的金额的是 j-coins[i-1]
                    dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]];
                    if(i==coins.length && j == amount) {
                        System.out.println("dp[i - 1][j] is " + dp[i - 1][j] +", and dp[i][j - coins[i - 1]] is "  + dp[i][j - coins[i - 1]] );
                    }
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[coins.length][amount];
    }

    public static void main(String[] args) {
        CoinChange2 instance = new CoinChange2();

        System.out.println(instance.change(5, new int[] { 1, 2, 5 }));
    }
}
