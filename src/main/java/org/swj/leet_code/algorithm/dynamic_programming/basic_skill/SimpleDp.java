package org.swj.leet_code.algorithm.dynamic_programming.basic_skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 简单的动态规划
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/31 21:07
 */
public class SimpleDp {
    /**
     * 118. 杨辉三角
     * 
     * @param numRows
     * @return
     */
    public List<List<Integer>> generate(int numRows) {
        /**
         * 解法思路，这个就是普通的集合遍历，没有用到动态规划
         */
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 1; i <= numRows; i++) {
            List<Integer> subList = new ArrayList<>();
            subList.add(1);
            if (res.isEmpty()) { // 空集合
                res.add(subList);
                continue;
            }

            List<Integer> prevList = res.get(res.size() - 1);
            for (int j = 0; j + 1 < prevList.size(); j++) {
                subList.add(prevList.get(j) + prevList.get(j + 1));
            }
            subList.add(1);
            res.add(subList);
        }

        return res;
    }

    /**
     * 1277. 统计全为 1 的正方形子矩阵
     * 示例 1：
     * 
     * 输入：matrix =
     * [
     * [0,1,1,1],
     * [1,1,1,1],
     * [0,1,1,1]
     * ]
     * 输出：15
     * 解释：
     * 边长为 1 的正方形有 10 个。
     * 边长为 2 的正方形有 4 个。
     * 边长为 3 的正方形有 1 个。
     * 正方形的总数 = 10 + 4 + 1 = 15.
     * 
     * @param matrix
     * @return
     */
    public int countSquares(int[][] matrix) {
        /**
         * 解题思路：
         * 根据边长求矩阵中 1 的正方形，
         * base case
         * 当 边长为 1 时，所有 值为1 的都可以算作正方形
         * 
         * 但是，提交的时候，提示超时了，只能试试动态规划了
         */
        // return countSquaresOld(matrix);
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] dp = new int[m][n];
        int res = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    dp[i][j] = 0;
                } else {
                    if (i > 0 && j > 0) {
                        dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                    } else {
                        dp[i][j] = 1;
                    }
                    res += dp[i][j];
                }
            }
        }
        return res;
    }

    int countSquaresOld(int[][] matrix) {
        int squareWidth = Math.min(matrix.length, matrix[0].length);
        int res = 0;
        visited = new boolean[matrix.length][matrix[0].length];
        for (int i = 1; i <= squareWidth; i++) {
            for (boolean[] arr : visited) {
                Arrays.fill(arr, false);
            }
            res += countSquares(matrix, 0, 0, i);
        }
        return res;
    }

    boolean[][] visited;

    /**
     * 以 (i,j) 为起点的 width 长度的正方向长度判断
     * 
     * @param matrix
     * @param i
     * @param j
     * @param width
     * @return
     */
    int countSquares(int[][] matrix, int i, int j, int width) {
        if (width < 1) {
            return 0;
        }
        int res = 0;
        int m = matrix.length;
        int n = matrix[0].length;
        if (i + width > m || j + width > n) {
            return 0;
        }
        if (visited[i][j]) {
            return 0;
        }
        visited[i][j] = true;
        int rows = i + width;
        int cols = j + width;
        boolean allOne = true;
        for (int x = i; x < rows; x++) {
            for (int y = j; y < cols; y++) {
                if (matrix[x][y] != 1) {
                    allOne = false;
                    break;
                }
            }
        }
        if (allOne) {// 当前 width 宽度的 可以组成一个正方形
            res += 1;
        }

        // 递归
        res += countSquares(matrix, i + 1, j, width);
        res += countSquares(matrix, i, j + 1, width);
        return res;
    }

    public static void main(String[] args) {
        SimpleDp simpleDp = new SimpleDp();
        int[][] matrix = new int[][] {
                { 0, 1, 1, 1 },
                { 1, 1, 1, 1 },
                { 0, 1, 1, 1 }
        };

        System.out.println(simpleDp.countSquares(matrix));

        matrix = new int[][] {
                { 1, 0, 1 },
                { 1, 1, 0 },
                { 1, 1, 0 }
        };
        System.out.println(simpleDp.countSquares(matrix));

        matrix = new int[][] {
                { 0, 1 },
                { 1, 1 }
        };
        System.out.println(simpleDp.countSquares(matrix));
    }

}
