package org.swj.leet_code.algorithm.dynamic_programming.playing_games;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/12 20:43
 *        leetcode 第 174 题，地下城游戏
 */
public class DungeonGame {

    int[][] memo = null;

    int calculateMinimumHP(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        memo = new int[m][n];
        for (int[] arr : memo) {
            Arrays.fill(arr, -1);
        }
        return dp(grid, 0, 0);
    }

    /**
     * dp 函数的定义 grid[i][j] 到右下角的最小生命值
     * 
     * @param grid
     * @param i
     * @param j
     * @return
     */
    private int dp(int[][] grid, int i, int j) {
        int m = grid.length;
        int n = grid[0].length;
        // base case
        if (i == m - 1 && j == n - 1) {
            // 到达了右下角, 如果右下角是血点则，则需最少的 1 个生命值就行，否则需要 -grid[i][j] + 1
            // 比如 -3 ,则事少需要 4 点生命值
            return grid[i][j] >= 0 ? 1 : -grid[i][j] + 1;
        }
        // 数组越界的情况
        if (i >= m || j >= n) {
            return Integer.MAX_VALUE;
        }
        // 命中备忘录情况
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        // 状态转移逻辑
        int res = Math.min(
                dp(grid, i, j + 1),
                dp(grid, i + 1, j)
        ) - grid[i][j];
        memo[i][j] = (res <= 0 ? 1 : res);
        return memo[i][j];
    }

    public static void main(String[] args) {
        int[][] grid = new int[][] {
                new int[] { -2, -3, 3 },
                new int[] { -5, -10, 1 },
                new int[] { 10, 30, -5 }
        };
        DungeonGame instance = new DungeonGame();

        System.out.println(instance.calculateMinimumHP(grid));
    }
}
