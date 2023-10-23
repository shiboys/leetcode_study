package org.swj.leet_code.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/19 11:27
 *        二维数组的花式遍历
 */
public class MultiWayTraverseMatrix {

    /**
     * leetcode 48 题，将二维矩阵顺时候旋转 90 度。
     * 
     * @param matrix
     */
    void rotate(int[][] matrix) {
        // 先按照 [0[]0], [n-1][n-1] 的对角线旋转
        int n = matrix[0].length;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i; j < n; j++) {
                swap(matrix, i, j);
            }
        }
        // 对角线旋转完毕，再每一行反转
        for (int[] arr : matrix) {
            reverse(arr);
        }
    }

    void swap(int[][] matrix, int i, int j) {
        if (i == j) {
            return;
        }
        int tmp = matrix[i][j];
        matrix[i][j] = matrix[j][i];
        matrix[j][i] = tmp;
    }

    void reverse(int[] arr) {
        int l = 0, r = arr.length - 1;
        while (l < r) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
            l++;
            r--;
        }
    }

    List<Integer> spiralOrder(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        List<Integer> res = new ArrayList<>(m * n);
        // 4 个边界
        int topIndex = 0, bottomIndex = m - 1;
        int leftIndex = 0, rightIndex = n - 1;
        // 右，下，左，上 方向遍历。
        while (topIndex <= bottomIndex && leftIndex <= rightIndex) {
            // 右
            for (int j = leftIndex; j <= rightIndex; j++) {
                res.add(matrix[topIndex][j]);
            }
            topIndex++;
            // 下
            for (int i = topIndex; i <= bottomIndex; i++) {
                res.add(matrix[i][rightIndex]);
            }
            rightIndex--;
            // 增加判断你的原因是右方向的遍历可能会跟左方向的是同一行，右方向已经遍历了，就不需要遍历左方向
            // 左
            if (topIndex <= bottomIndex) {
                for (int j = rightIndex; j >= leftIndex; j--) {
                    res.add(matrix[bottomIndex][j]);
                }
                bottomIndex--;
            }

            if (leftIndex <= rightIndex) {
                // 上
                for (int i = bottomIndex; i >= topIndex; i--) {
                    res.add(matrix[i][leftIndex]);
                }
                leftIndex++;
            }
        }
        return res;
    }

    /**
     * leetcode 59 题，螺旋矩阵II，生成矩阵
     * @param n
     * @return
     */
    int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        int topIndex = 0, bottomIndex = n - 1;
        int leftIndex = 0, rightIndex = n - 1;
        int val = 1;
        int limit = n * n;
        while (topIndex <= bottomIndex && leftIndex <= rightIndex) {
            // 右、下、左、上
            for (int j = leftIndex; j <= rightIndex; j++) {
                matrix[topIndex][j] = val++;
            }
            topIndex++;
            for (int i = topIndex; i <= bottomIndex; i++) {
                matrix[i][rightIndex] = val++;
            }
            rightIndex--;
            if (val > limit) {
                break;
            }
            // 左
            for (int j = rightIndex; j >= 0; j--) {
                matrix[bottomIndex][j] = val++;
            }
            bottomIndex--;
            if (val > limit) {
                break;
            }
            // 上
            for (int i = bottomIndex; i >= topIndex; i--) {
                matrix[i][leftIndex] = val++;
            }
            leftIndex++;
        }
        return matrix;
    }

    String reverseWords(String s) {
        s = s.trim();
        char[] charArr = s.toCharArray();
        // 先将所有字符都反转
        swapChars(charArr, 0, charArr.length - 1);
        // 再将相关单词反转
        int slow = 0, fast = 1;
        int n = charArr.length;
        StringBuilder sb = new StringBuilder(n);
        // 原始的方案，直接使用字符数组，发现无法移除中间多余的空格
        // 后面改为 stringbuilder 方式
        // while(fast <= n) {
        // if(fast == n || chs[fast] == ' ') {
        // swapAll(chs,slow,fast-1);
        // slow = fast + 1; // 空格不反转
        // }
        // fast++;
        // }
        while (fast <= n) {
            if (fast == n || charArr[fast] == ' ') {
                // 单词重新反转过来
                swapChars(charArr, slow, fast - 1);
                // 将单词和其后的空格加入 StringBuilder 中
                // 默认长度是不包含空格的
                int len = fast - slow;
                if (fast < n) {
                    len++; // 空格包含进来。
                }
                sb.append(charArr, slow, len);
                // 将所有 空格排除
                while (fast < n && charArr[fast] == ' ') {
                    fast++;
                }
                slow = fast;
            }
            fast++;
        }

        return sb.toString();
    }

    void swapChars(char[] arr, int start, int end) {
        if (start == end) {
            return;
        }
        char tmp;
        if (start >= arr.length || end >= arr.length || start > end) {
            System.out.println(
                    String.format("invalid parameter:start:%d,end:%d,s.length()=%d", start, end, arr.length));
            return;
        }
        while (start < end) {
            tmp = arr[start];
            arr[start] = arr[end];
            arr[end] = tmp;
            start++;
            end--;
        }

    }

    public static void main(String[] args) {
        int[][] matrix = new int[][] {
                new int[] { 5, 1, 9, 11 },
                new int[] { 2, 4, 8, 10 },
                new int[] { 13, 3, 6, 7 },
                new int[] { 15, 14, 12, 16 }
        };
        MultiWayTraverseMatrix instance = new MultiWayTraverseMatrix();
        instance.rotate(matrix);
        for (int[] arr : matrix) {
            System.out.println(Arrays.toString(arr));
        }

        matrix = new int[][] {
                new int[] { 1, 2, 3, 4 },
                new int[] { 5, 6, 7, 8 },
                new int[] { 9, 10, 11, 12 }
        };
        List<Integer> spiralOrderList = instance.spiralOrder(matrix);
        System.out.println(String.join(" ",
                spiralOrderList.stream().map(x -> String.valueOf(x)).collect(Collectors.toList())));

        matrix = instance.generateMatrix(3);
        for (int[] arr : matrix) {
            System.out.println(Arrays.toString(arr));
        }

        String s = "hello   world java ";
        System.out.println(instance.reverseWords(s));
    }

}