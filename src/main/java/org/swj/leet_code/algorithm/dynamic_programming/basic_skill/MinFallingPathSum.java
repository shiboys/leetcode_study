package org.swj.leet_code.algorithm.dynamic_programming.basic_skill;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/28 18:21
 *        最小下沉路径，这个和最小路径和（MinPathSum，leetcode 第 64 题）的区别是，路径和是从 (0,0) 到 (m,n)
 *        下沉路径是，只需要从第一行下沉到最后一行就行，求最小的一个路径和。
 *        leetcode 第 931. Minimum Falling Path Sum
 *        输入一个 n*n 的二维数组 matrix , 请计算从第一行落到左后一行，经过的路径和最小为多少
 *        也就是说你可以站在 matrix 的第一行的任意一个元素，需要下降到最后一行。
 *        每次下降，可以向下，向左下，向右下三个方向移动一格。也就是说，可以从 matrix[i][j-1] 降到 matrix[i+1][j] 或者
 *        matrix[i+1][j]
 *        或者 matrix[i+1][j+1] 这 3 个位置。比如说如下例子：
 *        输入：matrix=[[2,1,3],[6,5,4],[7,8,9]]
 *        输出：13
 *        解释：下面是两条和最小的下降路径:
 *        1->5->7 和 1->4->8
 *        这道题和最小路径和非常相似，所以借助这道题我们可以来好好讲述下 base case 的返回值，备忘录的初始值、索引越界情况的返回值该如何确定
 *        dp 函数的签名为：
 *        int dp(int[][] matrix,int i,int j) 表示从第一行 matrix[0][j] 向下落，落到位置
 *        matrix[x][j] 的最小路径和为 dp(matrix,i,j)
 *        dp 函数的实现思路，对于 matrix[i][j],只能从
 *        matrix[i-1][j],matrix[i-1][j-1],matrix[i-1][j+1] 转移而来
 *        那么，只要知道到达(i-1,j),(i-1,j-1),(i-1,j+1) 这 3 个位置的最小路径和，加上 matrix[i][j]
 *        的值，就能算出来到达位置 (i,j) 的
 *        最小路径和
 */
public class MinFallingPathSum {

    private static final int TOP_MAX = 99999;
    private static final int MEMO_DEFULT_VAL = 66666;

    int dp(int[][] matrix, int i, int j) {
        // 索引越界检查
        if (i < 0 || j < 0 || i >= matrix.length || j >= matrix[0].length) {
            // 返回一个特殊的值，
            /**
             * 该特殊值为啥取 99999 那？
             */
            return TOP_MAX;
        }
        // base case
        if (i == 0) {
            return matrix[0][j];
        }
        return matrix[i][j] + min(
                dp(matrix, i - 1, j),
                dp(matrix, i - 1, j - 1),
                dp(matrix, i - 1, j + 1));
    }

    int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    int[][] memo;

    /**
     * 上述方法是暴力穷举法，我们可以使用备忘录来消除重叠子问题
     * 
     * @param matrix
     * @param i
     * @param j
     * @return
     */
    int dpM(int[][] matrix, int i, int j) {
        // base case
        if (i < 0 || j < 0 || i >= matrix.length || j >= matrix[0].length) {
            return TOP_MAX;
        }
        if (i == 0) {
            return matrix[0][j];
        }
        if (memo[i][j] != MEMO_DEFULT_VAL) {
            return memo[i][j];
        }
        // 图上是向上查找
        // 算法上是自顶向下
        memo[i][j] = matrix[i][j] + min(
                dpM(matrix, i - 1, j - 1),
                dpM(matrix, i - 1, j),
                dpM(matrix, i - 1, j + 1));
        return memo[i][j];
    }

    int minFallingPathSum(int[][] matrix) {
        memo = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < memo.length; i++) {
            // 备忘录被初始化为 66666
            Arrays.fill(memo[i], MEMO_DEFULT_VAL);
        }
        int n = matrix.length;
        // 因为最短落地距离可能在最后一行的任何一个位置，这里采用遍历方式获取最小
        int[] dp = new int[matrix[0].length];
        for (int j = 0; j < dp.length; j++) {
            dp[j] = dpM(matrix, n - 1, j);
        }
        return Arrays.stream(dp).min().getAsInt();
    }

    public static void main(String[] args) {
        int[][] arr = new int[][] {
                new int[] { 2, 1, 3 },
                new int[] { 6, 5, 4 },
                new int[] { 7, 8, 9 }
        };
        MinFallingPathSum instance = new MinFallingPathSum();
        System.out.println(instance.minFallingPathSum(arr));
    }
}
