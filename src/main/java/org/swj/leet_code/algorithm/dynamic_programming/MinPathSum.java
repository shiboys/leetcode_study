package org.swj.leet_code.algorithm.dynamic_programming;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/28 21:19
 *        最小路径和，这是一道经典的动态规划题目，leetcode 第 64 题
 *        现在给你一个二维数组 grid, 其中的元素都是非负数，，现在你站在左上角，只能向右或者向下移动，需要到达右下角。现在请计算
 *        经过的路径最小是多少。比如如下二维数组：
 *        1 3 1
 *        1 5 1
 *        4 2 1
 *        算法应该返回 7，最小路径和为 7 ，就是 1->3->1->1->1
 *        一般来说，让你在二维矩阵中秋最优化的问题(最大值或者最小值)，肯定需要递归+备忘录，也就是需要动态规划技巧
 *        依然是跟 最小下落路径一样，求最小的右下角的路径 dp[m-1][n-1](m n 分别表示二维数组和行和列数)
 *        通过 grid[m-1][n-1] + min (dp(grid,m-2,n-1), dp(grid,m-1,n-2))
 *        的类似数学归纳法的方式求 min
 *        (f(n-1),f(m-1))
 *        然后加上 memo
 */
public class MinPathSum {
    int[][] memo;

    int dp(int[][] grid, int i, int j) {
        // base case
        if (i == 0 && j == 0) {
            return grid[0][0];
        }
        // 数组越界
        if (i < 0 || j < 0) {
            // 返回一个比价大的数
            return Integer.MAX_VALUE;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        memo[i][j] = grid[i][j] + Math.min(
                dp(grid, i - 1, j),
                dp(grid, i, j - 1));
        return memo[i][j];
    }

    /**
     * 阿里算法面试手册是
     * dp[i,j]=min(dp[i+1,j],dp[i,j+1])+m[i,j] 从小到大推导
     * 从 0,0 到 i,j
     * 
     * @param grid
     * @param i
     * @param j
     * @return
     */
    int dp2(int[][] grid, int i, int j) {
        // base case
        // 走到 target node
        if (i == grid.length - 1 && j == grid[0].length - 1) {
            return grid[i][j];
        }
        if (i >= grid.length || j >= grid[0].length) {
            return Integer.MAX_VALUE;
        }

        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        memo[i][j] = grid[i][j] +
                Math.min(dp2(grid, i + 1, j), dp2(grid, i, j + 1));
        return memo[i][j];
    }

    /**
     * 使用 dp 数组+循环的方式实现，而不是使用 dp 函数递归调用
     * 
     * @param grid
     * @return
     */
    int minPathSumByDp(int[][] grid) {
        int[][] dp = new int[grid.length][grid[0].length];
        // base case
        dp[0][0] = grid[0][0];
        // 二维数组的第一行，可以进行初始化,因为只能同一行移动
        for (int j = 1; j < grid[0].length; j++) {
            dp[0][j] = grid[0][j] + dp[0][j - 1];
        }
        // 同理，二维数组的第一列也是如此
        for (int i = 1; i < grid.length; i++) {
            dp[i][0] = grid[i][0] + dp[i - 1][0];
        }
        // 推导公式
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = grid[i][j] + Math.min(dp[i - 1][j], dp[i][j - 1]); // 两种走路方式，取最小的
            }
        }
        return dp[m - 1][n - 1];
    }

    public int minPathSum(int[][] grid) {
        memo = new int[grid.length][grid[0].length];
        Arrays.stream(memo).forEach(subArray -> {
            Arrays.fill(subArray, -1);
        });
        int m = grid.length;
        int n = grid[0].length;
        return dp(grid, m - 1, n - 1);
    }

    public int minPathSum2(int[][] grid) {
        memo = new int[grid.length][grid[0].length];
        Arrays.stream(memo).forEach(subArray -> {
            Arrays.fill(subArray, -1);
        });
        return dp2(grid, 0, 0);
    }

    public static void main(String[] args) {
        int[][] arr = new int[][] {
                new int[] { 1, 3, 1 },
                new int[] { 1, 5, 1 },
                new int[] { 4, 2, 1 }
        };
        int[][] arr2 = new int[][] {
                new int[] { 4, 1, 5, 3 },
                new int[] { 3, 2, 7, 7 },
                new int[] { 6, 5, 2, 8 },
                new int[] { 8, 9, 4, 5 }
        };

        MinPathSum instance = new MinPathSum();
        // System.out.println(instance.minPathSum(arr));
        // System.out.println(instance.minPathSumByDp(arr));
        System.out.println(instance.minPathSum2(arr2));
    }
}
