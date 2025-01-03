package org.swj.leet_code.algorithm.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/14 12:43
 *        主要重写 插入排序，冒泡排序 和 计数排序
 */
public class TraditionalSort {

    /**
     * 插入排序
     * 插入排序，其实就像我们打牌的时候，从后面的待排序的扑克里面选择一个扑克插入之前已排序的扑克中
     * 初始化的时候，就是 i 从 1 到 n-1 遍历将将元素插入到 i-1 到 0 的位置
     * 怎么插入之前已排序，就是将比当前待插入元素大的元素向后移动一步，在待插入元素的数组中找到正确的位置 j
     * 将当前待插入的元素插入当前位置
     * 
     * @param arr
     */
    public void insertSort(int[] arr) {
        if (arr == null || arr.length < 1) {
            return;
        }
        int target;
        for (int i = 1; i < arr.length; i++) {
            target = arr[i];
            int j = i - 1;
            // 从后往前变查找边移动待插入元素前面已经排好序的元素
            for (; j >= 0 && arr[j] > target; j--) { // 从后往前遍历，查找插入的位置
                // 向后移动元素
                arr[j + 1] = arr[j];
            }
             //则j + 1 为insert需要插入的元素
            arr[j + 1] = target;
        }
    }

    /**
     * leetcode 996 烧饼排序法
     * 给你一个整数数组 arr ，请使用 煎饼翻转 完成对数组的排序。
     * 
     * 一次煎饼翻转的执行过程如下：
     * 
     * 选择一个整数 k ，1 <= k <= arr.length
     * 反转子数组 arr[0...k-1]（下标从 0 开始）
     * 例如，arr = [3,2,1,4] ，选择 k = 3 进行一次煎饼翻转，反转子数组 [3,2,1] ，得到 arr = [1,2,3,4] 。
     * 
     * 以数组形式返回能使 arr 有序的煎饼翻转操作所对应的 k 值序列。任何将数组排序且翻转次数在 10 * arr.length
     * 范围内的有效答案都将被判断为正确。
     * 示例 1：
     * 
     * 输入：[3,2,4,1]
     * 输出：[4,2,4,3]
     * 解释：
     * 我们执行 4 次煎饼翻转，k 值分别为 4，2，4，和 3。
     * 初始状态 arr = [3, 2, 4, 1]
     * 第一次翻转后（k = 4）：arr = [1, 4, 2, 3]
     * 第二次翻转后（k = 2）：arr = [4, 1, 2, 3]
     * 第三次翻转后（k = 4）：arr = [3, 2, 1, 4]
     * 第四次翻转后（k = 3）：arr = [1, 2, 3, 4]，此时已完成排序。
     * 
     * 烧饼排序算法，这个我刚开始读的时候，确实没理解，再看了阿东的解法后，秒懂了，非常精妙，也是分治的一种
     * 比较好理解和掌握
     * 方法2 是借鉴插入排序的原理实现的，每次插入排序插入一个数字，其实就是一次交换过程
     * 我们把这一次交换过程转换为 4 次烧饼的反转，就能够不使用递归的方式来接烧饼排序的这个问题，
     * 这个思路我是没有想出来，看别人的想法，不过确实比较精妙
     * 
     * @param arr
     * @return
     */
    public int[] pancakeSort(int[] arr) {
        if (arr == null) {
            return null;
        } else if (arr.length == 1) {
            return arr;
        }
        List<Integer> list = new ArrayList<>();
        pancakeSortRecur(arr, arr.length, list);
        return convertToArray(list);
    }

    int[] convertToArray(List<Integer> list) {
        int[] res = new int[list.size()];
        int i = 0;
        for (int val : list) {
            res[i++] = val;
        }
        return res;
    }

    /**
     * 递归反转烧饼的最大坏处是没有考虑局部的有序性
     * before sort:[3, 2, 4, 1]
     * after sort:[1, 2, 3, 4]
     * res is [3, 4, 2, 3, 1, 2]
     * 这个达不到题目的要求，所以只有方法2可以解题
     * 
     * @param arr
     * @param n
     * @param res
     */
    void pancakeSortRecur(int[] arr, int n, List<Integer> res) {
        if (n <= 1) {
            return;
        }
        int maxVal = arr[0], maxIdx = 0;
        // 先找到当前最大的元素，及其索引
        for (int i = 0; i < n; i++) {
            if (arr[i] > maxVal) {
                maxVal = arr[i];
                maxIdx = i;
            }
        }

        reverse(arr, 0, maxIdx);
        // 找到最大的元素，将该元素翻转到顶部
        res.add(maxIdx + 1);

        // 然后再把最大的元素从顶部翻转到底部

        reverse(arr, 0, n - 1);
        res.add(n);

        // 最大的元素完成了反转，则递归次大的元素
        pancakeSortRecur(arr, n - 1, res);
    }

    void reverse(int[] arr, int i, int j) {
        int tmp;
        while (i < j) {
            tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
            i++;
            j--;
        }
    }

    /**
     * 使用插入排序原理实现的烧饼排序
     * 
     * @param arr
     * @return
     */
    public int[] pancakeSortInsertion(int[] arr) {
        if (arr == null) {
            return null;
        } else if (arr.length == 1) {
            return arr;
        }
        int target, j;
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < arr.length; i++) {
            target = arr[i];
            j = i - 1;
            for (; j >= 0; j--) {
                if (arr[j] > target) {
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
            }
            // 需要交换的两个元素是 arr[i] 和 arr[j+1];

            if (i == j + 1) { // 元素是有序的，没有必须要反转
                continue;
            }
            // 实现插入排序
            arr[j + 1] = target;
            // 记录烧饼模拟反转的记录
            // 发生无序，进行翻转（一切符合条件，需翻转4次）
            // 1 2 3 5 4
            // ****j***i
            // 发生无序的两个元素 arr[j + 1] 和arr[i]
            /*
             * 1 2 3 5 4
             * 1st: [5 3 2 1] 4
             * 2nd: [4 1 2 3 5]
             * 3rd: [3 2 1 4] 5
             * 4th: [1 2 3] 4 5
             * 
             * 
             * 输入：[3,2,4,1]
             * 输出：[4,2,4,3]
             * 但是我这个输出是 2,3,4 我觉得是对着那
             * 提交 leetcode 之后，是正确的
             */
            // 由这两个步骤来反射出 4 次烧饼反转
            // 前两步将 arr[i] 反转出来，先把 arr[i-1] 翻转到最上面，
            if (i - 1 > 0) {// 顶部第一个元素没必要反转
                list.add(i);// 反转 arr[0...i-1]
            }
            list.add(i + 1); // 反转 arr[0...i] 将 i 元素翻转到顶部
            // 再反转 j+1 ,将 i 翻转到 j+1 这个正确的位置
            if (j + 1 > 0) {// 顶部元素不反转
                list.add(j + 2);
            }
            // 再反转前 j 个元素
            if (j > 0) {
                list.add(j + 1);
            }
        }

        return convertToArray(list);
    }

    /**
     * 
     * 冒泡排序，又叫交换排序，那因此肯定有交换了，而且是不停地交换
     * 每趟冒泡，需要将第 i 大的元素冒泡到最后面
     * 
     * @param arr
     */
    public void bubbleSort(int[] arr) {
        for (int i = 0, len = arr.length; i < len; i++) {
            // 冒泡到 arr[len-1] 的位置
            for (int j = 0; j < len - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
