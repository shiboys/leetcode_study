package org.swj.leet_code.algorithm.dynamic_programming.bag;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/11 22:39
 * 找零钱 II
 *题目描述：

给定不同面额的硬币 coins 和一个总金额 amount，写一个函数来计算可以凑成总金额的硬币组合数。假设每一种面额的硬币有无限个。

比如说输入 amount = 5, coins = [1,2,5]，算法应该返回 4，因为有如下 4 种方式可以凑出目标金额：

5=5

5=2+2+1

5=2+1+1+1

5=1+1+1+1+1

如果输入的 amount = 5, coins = [3]，算法应该返回 0，因为用面额为 3 的硬币无法凑出总金额 5。
 */
public class CoinChange2 {
    int change(int amount, int[] coins) {

        int[][] dp = new int[coins.length+1][amount+1];
        for(int i=0;i<coins.length;i++) {
            dp[i][0] = 1;// 0 容量表示刚好可以凑出来或者
        }
        for(int i=1;i<=coins.length;i++) {
            for(int j=1;j<=amount;j++) {
                if(j>=coins[i-1]) {
                    dp[i][j] = dp[i-1][j] + dp[i-1][j-coins[i-1]];
                }
            }
        }
        return dp[coins.length][amount];
    }
    public static void main(String[] args) {
        
    }
}
