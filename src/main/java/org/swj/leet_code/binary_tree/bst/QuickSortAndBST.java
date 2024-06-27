package org.swj.leet_code.binary_tree.bst;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/30 15:27
 *        快速排序和 BST 的关系
 */
@Slf4j
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
            int pivot = nums[start];
            //int i = start; // 或者 i = start+1
            int i = start+1;
            int j = end;
            while (i <= j) { // 当 i>j 时循环结束，以保证区间 [start, end] 都被覆盖。
                while (i <= j && nums[i] <= pivot) {// == 的时候，不交换
                    // 找到 左边大于 pivot 的 i。 while 退出时 nums[i] > pivot
                    i++;
                }
                while (i <= j && nums[j] > pivot) {
                    // 找到右边 小于 pivot 的元素。while 退出时 nums[j] <= pivot
                    j--;
                }

                // 交换 i 和 j 的元素值
                if (i <= j) {
                    swap(nums, i, j);
                }

            }
            // 将 j 的位置和 pivot 交换，因为此时 j 位置的元素比 start 小，符合 j 位置左侧的都比 arr[j] 大
            swap(nums, j, start);

            return j;
        }

        /**
         * 使用快速选择排序，来返回第 k 大的元素。leetcode 第 215 题。
         * 用小顶堆的算法的话，逻辑是插入 k 个元素，当元素个数大于 k 时，弹出 top 元素，
         * 最终堆里面的元素为最大的 k 个数，其中堆顶的元素就是第 k 大的元素。时间复杂度是 O(nLogn)
         * 快速选择算法的话，是 O(N)
         * @param nums
         * @param k
         * @return
         */
        public static int findKthLargest(int[] nums, int k) {
            if (k < 1 || k > nums.length) {
                return -1;
            }
            // 「第 `k` 个最大元素」，相当于数组升序排序后「排名第 `n-k` 的元素」
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
        int[] arr = new int[] { 7, 6, 5, 4, 3, 2, 1,8,9,10,20,14,11,18 };
        Quick.sort(arr);
        System.out.println(Arrays.toString(arr));
//
//        int k = 3;
//        System.out.println(Quick.findKthLargest(arr, k));
//
//        arr = new int[] { 3, 2, 1, 5, 6, 4 };
//        k = 2;
//        System.out.println(Quick.findKthLargest(arr, k));
//        arr = new int[] {3,2,3,1,2,4,5,5,6};
//        k = 4;
//        System.out.println(Quick.findKthLargest(arr, k));
    }
}
