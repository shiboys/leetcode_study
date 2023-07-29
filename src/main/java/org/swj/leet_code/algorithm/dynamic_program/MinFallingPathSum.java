package org.swj.leet_code.algorithm.dynamic_program;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/28 18:21
 *  最小下沉路径，
 *  leetcode 第 931. Minimum Falling Path Sum
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
         dpM(matrix,i - 1,j - 1),
         dpM(matrix,i - 1,j), 
         dpM(matrix,i - 1,j + 1)
        );
        return memo[i][j];
    }

    int minFallingPathSum(int[][] matrix) {
        memo = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < memo.length; i++) {
            // 备忘录被初始化为 66666
            Arrays.fill(memo[i], MEMO_DEFULT_VAL);
        }
        int res = Integer.MAX_VALUE;
        int n = matrix.length;
        // 因为最短落地距离可能在最后一行的任何一个位置，这里采用遍历方式获取最小
        for (int j = 0; j < matrix[0].length; j++) {
            res = Math.min(res, dpM(matrix, n - 1, j));
        }
        return res;
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
    /**
     * 这里对于这个 dp 函数仔细探讨 3 个问题：
     * 1、对于索引的合法性，返回值为什么是 99999？其他值行不行？
     * 2、base case 为什么是 i==0 ？
     * 3、备忘录 memo 的初始值为什么是 66666 ？ 其他值行不行？
     * 
     * 首先说下 base case 为什么是 i == 0，返回值为什么是 matrix[0][j]，这是根据 dp 函数的定义决定的
     * dp[i] 的定义为 第一行 matrix[0][..] 向下落，落到位置 matrix[i][..] 的最小路径和为 dp(matrix,i,j)
     * 根据这个定义，以及我们实现的是自顶向下，从图上看是从下到上下落，最后我们落地的目的地就是 i==0，所以所需的路径和就是
     * dp[0][j]
     * 再说备忘录 memo 的初始值为啥是 66666，这是由题目给出的数据范围决定的。
     * matrix 是 n x n 的二维数组，其中 1 <= n <= 100；对于二维数组中的元素，有 -100 <= matrix[i][j] <= 100。
     * 假设 matrix 的大小是 100*100，所有元素都是 100，从第一行往下落，得到的路径和就是 100*100 = 10000，也就是最大合法答案
     * 同理最小合法答案是 -10000，
     * 也就是，这个问题的合法区间会落在[-10000,10000] 之间
     * 所以，我们 memo 的初始值就要避开区间 [-10000,10000], 换句话说我们的初始化值只要在(-inf,-10001] U [10001,+inf) 之间就可以
     * 最后，对于不合法索引，返回值应该如何确定，这要根据我们的状态转移方程来确定。我们的状态转移逻辑在：
     *  return matrix[i][j] + min(
                dp(matrix, i - 1, j),
                dp(matrix, i - 1, j - 1),
                dp(matrix, i - 1, j + 1));
     显然，i-1,j-1,j+1 这几个运算可能造成数组索引越界，对于索引越界的 dp 函数，应该返回一个不可能被调用到的值。
     刚才我们多了，合法区间是 [-10000,10000] 之间，所以只要我们的返回值大于10000 就相当于一个永不会取到的最大值。
     */
}
