package org.swj.leet_code.binary_tree.bst;

import java.util.Arrays;
import java.util.Random;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/30 15:27
 *        快速排序和 BST 的关系
 */
public class QuickSortAndBST {

    static class Quick {
        public static void sort(int[] nums) {
            Random random = new Random();
            shuffle(nums, random);
            sort(nums, 0, nums.length - 1);
        }

        static void shuffle(int[] nums, Random random) {
            for (int i = 0; i < nums.length; i++) {
                int j = i + random.nextInt(nums.length - i);
                swap(nums, i, j);
            }
        }

        static void swap(int[] nums, int i, int j) {
            if (i == j) {
                return;
            }
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }

        static void sort(int[] nums, int start, int end) {
            if (start >= end) {
                return;
            }
            int p = partition(nums, start, end);
            sort(nums, start, p - 1);
            sort(nums, p + 1, end);
        }

        private static int partition(int[] nums, int start, int end) {
            int pivort = nums[start];
            int i = start; // 或者 i = start+1
            int j = end;
            while (i <= j) { // 当 i>j 时循环结束，以保证区间 [start, end] 都被覆盖。
                while (i <= j && nums[i] <= pivort) {
                    // 找到 左边大于 pivort 的 i。 while 退出时 nums[i] > pivort
                    i++;
                }
                while (i <= j && nums[j] > pivort) {
                    // 找到右边 小于 pivort 的元素。while 退出时 nums[j] <= pivort
                    j--;
                }

                // 交换 i 和 j 的元素值
                if (i <= j) {
                    swap(nums, i, j);
                }

            }
            // 将 j 的位置和 pivort 交换
            swap(nums, j, start);

            return j;
        }

        /**
         * 使用快速选择排序，来返回第 k 大的元素。leetcode 第 215 题。
         * 
         * @param nums
         * @param k
         * @return
         */
        public static int findKthLargest(int[] nums, int k) {
            if (k < 1 || k > nums.length) {
                return -1;
            }
            int kPlus = nums.length - k;
            // shuffle the array
            Random random = new Random();
            shuffle(nums, random);
            return quickSelect(nums, kPlus);
        }

        private static int quickSelect(int[] nums, int k) {
            int start = 0, end = nums.length - 1;
            while (start <= end) {
                int p = partition(nums, start, end);
                if (p == k) {
                    return nums[p];
                } else if (k < p) { // 当前 p 值大，k在p 的左边
                    end = p - 1;
                } else if (k > p) { // 当前 p 值小，k 在 p 的右边
                    start = p + 1;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        // Quick quick = new Quick();
        int[] arr = new int[] { 7, 6, 5, 4, 3, 2, 1 };
        Quick.sort(arr);
        System.out.println(Arrays.toString(arr));

        int k = 3;
        System.out.println(Quick.findKthLargest(arr, k));

        arr = new int[] { 3, 2, 1, 5, 6, 4 };
        k = 2;
        System.out.println(Quick.findKthLargest(arr, k));
        arr = new int[] {3,2,3,1,2,4,5,5,6};
        k = 4;
        System.out.println(Quick.findKthLargest(arr, k));
    }
}
