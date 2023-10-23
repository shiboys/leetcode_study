package org.swj.leet_code.algorithm.dynamic_programming.basic_skill;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/26 20:20
 *        找零钱
 *        leecode 第 322 题。
 *        找零钱问题跟青蛙跳台阶问题基本是同一类问题，但是我们这次使用动态规划法来解这道题
 * 
 */
public class CoinChange {

    /**
     * 采用递归方法的动态规划法，由子问题得出当前问题的解法
     * dp 的定义为 coins 为不同种类/价值的硬币,amount 为需要凑出的目前钱数，dp 函数的返回值
     * 表示最少 需要 dp(...）个硬币才能凑出目标钱数
     * 
     * @param coins
     * @param amount
     * @return
     */
    int dpR(int[] coins, int amount) {
        // base case
        if (amount == 0) {
            return 0;
        } else if (amount < 0) {
            return -1;
        }
        int res = Integer.MAX_VALUE;
        for (int coin : coins) {
            // 求 n-1 的子问题的硬币数
            int subProblem = dpR(coins, amount - coin);
            // 表示要求的金额，当前硬币实现不了，比如说硬币面额为 1 2 5，现在要求的是 4 ，则无法实现
            if (subProblem < 0) {
                continue;
            }
            // 在子问题中寻找最优解, 然后加 1
            // 这是一种自顶向下的递归动态规划解决方案
            // res 在每次递归中都是 Integer.MAX_VALUE
            res = Math.min(res, subProblem + 1);
        }
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    /**
     * 最终我们得出其状态转移方程
     * |~0,n=0
     * dp(n)= |~-1,n<0
     * |~min{dp(n-coin)+1) | coin ∈ coins},n >0
     */

    /**
     * 递归算法的时间复杂度：子问题总数 * 解决每个子问题的所需时间
     * 子问题的总数为递归树的节点个数，但是算法会进行剪枝，剪枝的实际和题目给出的具体硬币面额有关，我们可以想象，这个树的生成并不
     * 规则，确切算出树上有多少节点比较困难。对于这种情况，我们一般的做法是按照最坏的情况估算一个时间复杂度上限
     * 假设目标金额为 n，给定的硬币个数为 k，那么递归树最坏的情况下高度为 n(全部使用面额为 1 的硬币)，，然后在假设这是一颗满 k 叉树，
     * 则节点的总数在 k^n 这个数量级
     * 接下来是每个自问的复杂度，由于每次递归包含一个 for 循环，复杂度为 O(k),相乘得出宗时间复杂度为 k*k^n= k^(n+1)，仍然是 k^n
     * 级别
     * 消除递归的重复问题，一般是用带备忘录的递归
     */

    /**
     * 带备忘录的递归
     * 
     * @param coins
     * @param amount
     * @return
     */
    int dpM(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        } else if (amount < 1) {
            return -1;
        }
        // 从备忘录中查询，如果存在则从备忘录中返回
        if (memo[amount] != -6666) {
            return memo[amount];
        }
        int res = Integer.MAX_VALUE;
        for (int coin : coins) {
            int subProblem = dpM(coins, amount - coin);
            if (subProblem < 0) {
                continue;
            }
            res = Math.min(res, subProblem + 1);
        }
        memo[amount] = (res != Integer.MAX_VALUE ? res : -1);
        return memo[amount];
    }

    int[] memo;

    int coinChange(int[] coins, int amount) {
        memo = new int[amount + 1];
        Arrays.fill(memo, -6666);
        return dpM(coins, amount);
    }

    // 使用循环的方式处理硬币分组，原理一样
    int coinChangeL(int[] coins, int amount) {
        // 使用 dp table （dynamic programing table) 数组来避免递归问题
        // 数组的每个元素都默认 amount+1，比amount 数量的硬币还要多
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        // base case
        dp[0] = 0;
        // 外层循环遍历遍历所有状态的所有取值
        for (int i = 1; i <= amount; i++) {
            // 内层循环则求所有硬币面额的最小值
            for (int coin : coins) {
                // 子问题无解，则跳过
                if (i < coin) {
                    continue;
                }
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }

        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        CoinChange coinChange = new CoinChange();
        System.out.println(coinChange.coinChange2(new int[] { 1, 2, 5 }, 24, true));
    }

    int coinChange2(int[] coins, int amount, boolean dpOrTable) {
        if (dpOrTable) {
            memo = new int[amount + 1];
            Arrays.fill(memo, 10001);
            return coinChangeWithDpMemo(coins, amount);
        } else {
            return coinChangeWithDpTable(coins, amount);
        }
    }

    /**
     * 在写一遍硬币最小分配个数，使用动态规划函数
     * 
     * @param coins
     * @param amount
     * @return
     */
    int coinChangeWithDpMemo(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }
        // 0 <= amount <= 10^4
        if (memo[amount] != 10001) {
            return memo[amount];
        }
        int res = Integer.MAX_VALUE;
        for (int coin : coins) {
            int subProblem = coinChangeWithDpMemo(coins, amount - coin);
            if (subProblem < 0) {
                continue;
            }
            res = Math.min(res, subProblem + 1);
        }
        memo[amount] = (res == Integer.MAX_VALUE ? -1 : res);
        return memo[amount];
    }

    /**
     * 采用 dp table 方式求解找零钱，自底向上方式
     * 
     * @param coins
     * @param amount
     * @return
     */
    int coinChangeWithDpTable(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        // dp 元素的最大数不可能超过 amount + 1，因为就算按照最低的1零钱，最多也就 amount 个。
        Arrays.fill(dp, amount + 1);
        // base case

        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i < coin) {
                    continue;
                }
                int subProblem = i - coin;
                dp[i] = Math.min(dp[i], 1 + subProblem);
            }
        }
        return dp[amount];
    }
}
