package org.swj.leet_code.array;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/20 23:27
 *        前缀和数组
 */
public class PreSumArray {

    /**
     * 前缀和存储数组，前缀和，指的是当前元素之前的所有元素的和，不包含当前元素
     */
    int[] preSumArr;

    /**
     * preSum[i][j] 定义为从顶点(0,0) 到节点 (i-1,j-1) 的前缀和
     */
    int[][] preSumMatrix;

    public PreSumArray(int[] nums) {
        preSumArr = new int[nums.length + 1];
        for (int i = 1; i <= nums.length; i++) {
            preSumArr[i] = preSumArr[i - 1] + nums[i - 1];
        }
    }

    public void preSumArray2(int[] nums) {
        preSumArr = new int[nums.length];
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            // 下面的代码交换位置，就是前缀和
            sum += nums[i];
            preSumArr[i] = sum;
        }
    }

    public PreSumArray(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        if (m == 0 || n == 0) {
            return;
        }
        preSumMatrix = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // 根据 array.md 中的有关二维矩阵的前缀和的图片描述可知如下公式
                preSumMatrix[i][j] = preSumMatrix[i - 1][j] + preSumMatrix[i][j - 1] - preSumMatrix[i - 1][j - 1]
                        + matrix[i - 1][j - 1];
            }
        }
    }

    /**
     * leetcode 303 区域检索和
     * 使用前缀和的方式实现 O(1) 的时间解决
     * 
     * @param left
     * @param right
     * @return
     */
    public int sumRange2(int left, int right) {
        if (left < 0 || right < 0 || right < left || right > preSumArr.length - 1) {
            return -1;
        }
        // 在 方法2 中，preSum 是当前和，而不是前缀和
        // left ==0 ,则表示求当前和
        if (left == 0) {
            return preSumArr[right];
        }
        // 因为 函数的 left,right 是 闭区间 [left,right] 表示求 left 到right 之间的元素和，
        // 那么如果 preSum 是当前和，区间和必须是 sum[right] - sum[left-1] 才是 [left,right] 的区间和
        int leftSum = preSumArr[left - 1];
        int rightSum = preSumArr[right];
        return rightSum - leftSum;
    }

    public int sumRange(int left, int right) {
        if (left < 0 || right < 0 || right < left || right >= preSumArr.length - 1) {
            return -1;
        }
        int leftSum = preSumArr[left];
        int rightSum = preSumArr[right + 1];
        return rightSum - leftSum;
    }

    /*
     * 二维矩阵的前缀和
     * leetcode 304 二维矩阵区域和检索，矩阵不可变
     * 给定一个二维矩阵 matrix，一下类型的多个请求：
     * 计算其子矩阵范围内元素的总和，该子矩阵的左上角为 (row1,col1), 右下角为 (row2,col2)
     * numMatrix(int[][] matrix) 矩阵初始化，
     * int sumRegion(int row1,int col1,int row2,int col2) 返回左上角和右下角所描述的子矩阵元素总和。
     */

    public int sumRegion(int row1, int col1, int row2, int col2) {
        int preSum1 = preSumMatrix[row1][col1];
        int preSum2 = preSumMatrix[row2 + 1][col2 + 1];
        // 目标矩阵之和由四个相邻矩阵运算获得
        return preSum2 - preSumMatrix[row1][col2 + 1] - preSumMatrix[row2 + 1][col1] + preSum1;
    }

    public static void main(String[] args) {
        int arr[] = new int[] { -2, 0, 3, -5, 2, -1 };
        PreSumArray instance = new PreSumArray(arr);
        // System.out.println(instance.sumRange(0, 2));
        // System.out.println(instance.sumRange(2, 5));
        // System.out.println(instance.sumRange(0, 5));

        instance.preSumArray2(arr);
        System.out.println(instance.sumRange2(0, 2));
        System.out.println(instance.sumRange2(2, 5));
        System.out.println(instance.sumRange2(0, 5));

        int[][] matrixArr = new int[][] {
                new int[] { 3, 0, 1, 4, 2 },
                new int[] { 5, 6, 3, 2, 1 },
                new int[] { 1, 2, 0, 1, 5 },
                new int[] { 4, 1, 0, 1, 7 },
                new int[] { 1, 0, 3, 0, 5 }
        };
        instance = new PreSumArray(matrixArr);
        System.out.println(instance.sumRegion(2, 1, 4, 3));
        System.out.println(instance.sumRegion(1, 1, 2, 2));
        System.out.println(instance.sumRegion(1, 2, 2, 4));
    }
}
