package org.swj.leet_code.array.binarysearch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/05 20:27
 *        二分法其他实践
 */
public class BinarySearchOther {
    /**
     * leetcode 74 题，查找二维数组
     * 给你一个满足下述两条属性的 m x n 整数矩阵：
     * 
     * 每行中的整数从左到右按非递减顺序排列。
     * 每行的第一个整数大于前一行的最后一个整数。
     * 
     * 给你一个整数 target ，如果 target 在矩阵中，返回 true ；否则，返回 false 。
     * 
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        /**
         * 思路，将二维矩阵映射为 i*n+j（i,j) 为元素在矩阵中的索引, 其中 n 为列数，然后使用二分法来查找元素，因为矩阵刚好满足二分查找的单调性
         * 同时也可以将 k 转换为二维矩阵的索引
         * i=k/n,j=k%n
         */
        if (matrix == null || matrix.length < 1) {
            return false;
        }
        int m = matrix.length, n = matrix[0].length;
        int left = 0, right = m * n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int val = get(matrix, mid);
            if (val == target) {
                return true;
            } else if (val > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        System.out.println("left is " + left);
        return false;
    }

    int get(int[][] matrix, int k) {
        if (k == 0) {
            return matrix[0][0];
        }
        int n = matrix[0].length;
        int i = k / n, j = k % n;

        return matrix[i][j];
    }

    /**
     * leetcode 240 题搜索二维矩阵2
     * 编写一个高效的算法来搜索 m x n 矩阵 matrix 中的一个目标值 target 。该矩阵具有以下特性：
     * 
     * 每行的元素从左到右升序排列。
     * 每列的元素从上到下升序排列。
     * 
     * @param matrix
     * @param target
     * @return
     */
    public boolean searchMatrix2(int[][] matrix, int target) {
        /**
         * 本题的解法，其实有点遍历的思想，没怎么用到二分法，不过遍历的角度是从右上角开始，而不是左上角，
         * 然后跟 target 对比，小的话，下沉 i++, 大的话，左移 j--
         */
        if (matrix == null || matrix.length < 1) {
            return false;
        }
        int m = matrix.length, n = matrix[0].length;
        int i = 0, j = n - 1;
        while (i < m && j >= 0) {
            if (matrix[i][j] == target) {
                return true;
            } else if (matrix[i][j] > target) {
                j--;
            } else {
                i++;
            }
        }

        return false;
    }

    /**
     * leetcode 658 找到 k 个最接近的元素。
     * 给定一个 排序好 的数组 arr ，两个整数 k 和 x ，从数组中找到最靠近 x（两数之差最小）的 k 个数。返回的结果必须要是按升序排好的。
     * 整数 a 比整数 b 更接近 x 需要满足：
     * |a - x| < |b - x| 或者
     * |a - x| == |b - x| 且 a < b
     * 
     * 示例1：
     * 输入：arr = [1,2,3,4,5], k = 4, x = 3
     * 输出：[1,2,3,4]
     * 
     * @param arr
     * @param k
     * @param x
     * @return
     */
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        /**
         * 解题思路：
         * 简单直接的方法时：用 二分查找算法 来查找左侧边界来找到 x 的位置，然后用 leetcode5 最长回文子串中 中介绍的
         * 从中间向两端的双指针算法找到 这 k 个元素。
         * 
         * 为什么是二分法是搜索左侧边界，因为题目给出了需求 |a - x| == |b - x| 且 a < b 表示整数 a 比整数 b 更接近 x
         * 
         * 另外，因为题目要求返回的结果必须是按升序排序，所以我们必须用 linkedList 来从两端添加结果，使得结果有序
         */
        if (k > arr.length || k < 1) {
            return null;
        }
        LinkedList<Integer> res = new LinkedList<>();
        // 查找到 x 的位置或者 > x 的第一个元素位置
        int pos = leftBound(arr, x);
        System.out.println("pos is " + pos);
        int left = pos - 1, right = pos;
        // 凑够 k 个数。从中间向两边扩散
        while (res.size() < k) {
            if (left == -1) {
                res.addLast(arr[right++]);
            } else if (right == arr.length) {
                res.addFirst(arr[left--]);
            } else if (x - arr[left] > arr[right] - x) {
                res.addLast(arr[right++]);
            } else {
                res.addFirst(arr[left--]);
            }
        }
        return res;
    }

    // 二分法查找最值为 target 的左边界
    int leftBound(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                right = mid - 1;
            } else if (arr[mid] > target) {
                right = mid - 1;
            } else if (arr[mid] < target) {
                left = mid + 1;
            }
        }
        return left;
    }

    /**
     * leetcode 162 寻找峰值
     * 峰值元素是指其值严格大于左右相邻值的元素。
     * 给你一个整数数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。
     * 你必须实现时间复杂度为 O(log n) 的算法来解决此问题。
     * 示例1：
     * 输入：nums = [1,2,1,3,5,6,4]
     * 输出：1 或 5
     * 解释：你的函数可以返回索引 1，其峰值元素为 2；
     * 或者返回索引 5， 其峰值元素为 6。
     * 
     * @param nums
     * @return
     */
    public int findPeakElement(int[] nums) {
        /**
         * 本题是让我们求峰值索引元素，要是线性遍历的话没什么难度，所以题目要求你必须使用二分法来解决。
         * 
         * 一般的二分法要根据 left，right 和 mid 的大小关系来判断倒带是应该搜索左边还是右边，
         * 而这道题考察 left,right 和 mid 之间的相对大喜爱会比较麻烦，可能需要分很多中情况讨论，比如 nums[mid] < nums[left]
         * < nums[right]、nums[mid] > nums[left] > nums[right] 等等
         * 写起来比较繁琐
         * 
         * 这道题更好的思路是不要考虑 left 和 right，而是单纯考虑 mid 周边的情况。具体来说，我们计算 nums[mid] 和 nums[mid+1]
         * 这两个元素的相对大小，即可得到 mid 附近的元素走势：
         * 如果走势下行(nums[mid] > nums[mid+1])，说明 Mid 本身就是峰值或者其左侧有一个峰值。所以需要收缩右边界
         * (right=mid);
         * 
         * 如果走势上行(nums[mid] < nums[mid+1]), 则说明该 mid 右侧有一个峰值，需要收缩左边界(left =mid+1).
         * 同时题目说明 nums 中不存在相等的相邻元素，因此不用考虑 nums[mid] = nums[mid+1] 的情况。
         * 
         * 依据以上情况，可以写出代码逻辑
         */
        if (nums == null || nums.length < 1) {
            return -1;
        }
        int n = nums.length;
        // 这里 right 需要为 n-1 ,比较判断为 闭区间，
        int left = 0, right = n - 1;

        while (left < right) {// 由于不存在重复元素，因此 left == right 的时候就是答案。
            int mid = left + (right - left) / 2;
            // 下行趋势，mid 本身就是峰值或者其左侧有一个峰值。收缩右侧
            if (mid < n - 1 && nums[mid] > nums[mid + 1]) {
                right = mid;
            } else { // 上升趋势，mid 右侧有峰值，收缩左侧
                left = mid + 1;
            }
        }
        return left;
    }

    /**
     * leetcode 852. 山脉数组的峰顶索引
     * 这道题和 上面 寻找峰值的解法差不多，直接把寻找峰值的解法拿过来，即可通过
     * 
     * @param arr
     * @return
     */
    public int peakIndexInMountainArray(int[] arr) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        int n = arr.length;
        int left = 0, right = n;
        while (left < right) {
            int mid = left + (right - left) / 2;
            // 下降趋势，峰值在 mid 或者 mid 左侧，收缩右边界
            if (arr[mid] > arr[mid + 1]) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /**
     * LCR 172. 统计目标成绩的出现次数。也就是统计一个数字在排序数组中出现的次数。
     * 这道题考察二分搜索查找 target 的左右边界，和 leetcode 34 题查找元素的第一个和最后一个位置有些类似
     * 用二分搜索左右边界的索引，就可以判断重复出现的次数了。
     * 
     * @param nums
     * @param target
     * @return
     */
    public int search(int[] nums, int target) {
        int left = leftBound(nums, target);
        if (left == -1) {
            return 0;
        }
        int right = rightBound(nums, target);
        return right - left + 1;
    }

    int rightBound(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) { // 压缩左边界
                left = mid + 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            }
        }
        return right;
    }

    /**
     * 剑指 offer 53, 寻找缺失的数字。
     * 一个长度为 n-1 的底层增排序数组中的所有数字都是唯一，并且每个数字的方位都在 o~n-1 之内。
     * 在范围 0~n-1内的 n 个数字中有且只有一个数字不再该数组中，请找出这个数字。
     * 
     * 示例1——输入：[0,1,3]，输出：2
     * 示例2——输入：[0,1,2,3,4,5,6,7,9]；输出：8
     * 
     * @param records
     * @return
     */
    public int missingNumber(int[] records) {
        /**
         * 分析：这道题考察 二分查找算法。常规的二分搜索让你在 nums 中搜索目标值 target, 但是这道题没有给你一个显式的 target，怎么办？
         * 其实，二分搜索的关键在于，你能否找到一个规律，能够在搜索区间中一次排除掉一半。
         * 比如让你在 nums 中搜索 target，你可以通过判断 nums[mid] 和 target 的大小关系判断 target
         * 在左边还是在右边，一次排除半个数组。
         * 所以这道题的关键是，你能否找到一些规律，能够判断缺失的元素在哪一边？
         * 
         * 其实是有规律的，你可以观察 nums[mid] 和 mid 的关系，如果 nums[mid] == mid, 则缺失的元素再右半边，否则在左半边
         * 我们就用左侧边界的二分法来搜索缺失的元素。（这里元素就是位置）
         */
        if (records == null || records.length < 1) {
            return -1;
        }
        int n = records.length;
        int left = 0, right = n - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            // records[mid] 只能是 >= mid
            if (records[mid] > mid) { // 说明缺失的元素在 [left...mid) 之间
                right = mid - 1;
            } else {// mid == records[mid], 缺失的数据在 mid..right 之间
                left = mid + 1;
            }
        }
        return left;
    }

    /*
     * 33. 搜索旋转排序数组 II
     * 这个题目的详解过程请参考 binarysearch.md 中有关 搜索旋转排序数组 章节的介绍
     */
    public int search2(int[] nums, int target) {
        if (nums == null || nums.length < 1) {
            return -1;
        }
        int n = nums.length;
        int left = 0, right = n - 1;

        // nums[left..mid] 都是有序的
        while (left <= right) {
            int mid = left + (right - left) / 2;
            // 先判断 target 是否跟 nums[mid] 相等
            if (target == nums[mid]) {
                return mid;
            }
            if (nums[left] < nums[mid]) { // mid 落在了断崖前
                if (nums[left] <= target && target < nums[mid]) { // target 落在 [left..mid-1] 之间，收缩右边界
                    right = mid - 1;
                } else {
                    left = mid + 1; // target 落在了 [mid+1...right] 中，收缩左边界
                }
            } else { // 前面大于后面，mid 落在了断崖以后。nums[mid..right] 是有序的
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }

        return -1;
    }

    /**
     * 81. 搜索旋转排序数组 II
     * 
     * @param nums
     * @param target
     * @return
     */
    public boolean search3(int[] nums, int target) {
        if (nums == null || nums.length < 1) {
            return false;
        }
        int n = nums.length;
        int left = 0, right = n - 1;
        while (left <= right) {
            while (left < n - 1 && nums[left] == nums[left + 1]) {
                left++;
            }
            while (right > 0 && nums[right] == nums[right - 1]) {
                right--;
            }
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return true;
            }
            if (nums[left] <= nums[mid]) {// mid 落在断崖之前
                if (nums[left] <= target && target < nums[mid]) {// target 在 left 和 mid-1 之间，收缩右区间
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (nums[right] >= target && target > nums[mid]) {// target 落在 mid+1..right 之间
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return false;
    }

    /**
     * leetcode 392. 判断子序列
     * 
     * @param s
     * @param t
     * @return
     *         简单的解法就是直接遍历两个数组，两个指针对比
     */
    public boolean isSubsequence(String s, String t) {
        int i = 0, j = 0;
        while (i < s.length() && j < t.length()) {
            if (s.charAt(i) == t.charAt(j)) {
                j++;
            }
            i++;
        }
        return j == t.length();
    }

    public boolean isSubsequence2(String s, String t) {

        List<Integer>[] indexList = new ArrayList[256];
        for (int i = 0, len = t.length(); i < len; i++) {
            List<Integer> list = indexList[t.charAt(i)];
            if (list == null) {
                indexList[t.charAt(i)] = list = new ArrayList<>();
            }
            list.add(i);
        }
        // j 是 t 上的指针
        int j = 0;
        char ch;
        // 判断 s 是否为 t 的子序列
        for (int i = 0, len = s.length(); i < len; i++) {
            ch = s.charAt(i);
            // t 中没有 s 的字符 s[i]
            if (indexList[ch] == null) {
                return false;
            }
            int pos = left_bound(indexList[ch], j);
            if (pos < 0) { // 二分搜索没有找到 字符 ch
                return false;
            }
            // t 字符串上的指针 j 向前一步
            j = indexList[ch].get(pos) + 1;
        }
        return true;
    }

    int left_bound(List<Integer> list, int target) {
        int left = 0, right = list.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            int midVal = list.get(mid);
            if (midVal < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        if (left == list.size()) {
            return -1;
        }
        return left;
    }

    public int numMatchingSubseq(String s, String[] words) {
        /**
         * 
         * 解题思路，根据上一题的二分法来辨别子序列的方式，我们可以沿用这个思想
         * 对每个 word 进行判断，对，就是这么简单
         *
         * 总结，这道题缺失非常难以想到使用二分法，主要 target 有点难以寻找
         */
        List<Integer>[] index = new List[256];
        char ch;
        for (int i = 0; i < s.length(); i++) {
            ch = s.charAt(i);
            List<Integer> list = index[ch];
            if (list == null) {
                index[ch] = list = new ArrayList<>();
            }
            list.add(i);
        }
        int i = 0, j = 0;
        int res = 0;
        // 这里其实可以将上一题的二分查找给重构出来以便重复使用
        for (String w : words) {
            // w 字符的指针
            i = 0;
            // s 字符的指针
            j = 0;
            for (; i < w.length(); i++) {
                ch = w.charAt(i);
                // 不包含 ch 字符，当期的 w 就不是 s 的子序列
                if (index[ch] == null) {
                    break;
                }
                int pos = left_bound(index[ch], j);
                if (pos < 0) { // 没有找到当前合适位置的 ch 字符
                    break;
                }
                // 将 j 的位置推进一下
                j = index[ch].get(pos) + 1;
            }
            if (i == w.length()) {
                res++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] matrix = new int[][] { { 1, 3, 5, 7 }, { 10, 11, 16, 20 }, { 23, 30, 34, 60 } };
        BinarySearchOther instance = new BinarySearchOther();
        // System.out.println(instance.searchMatrix(matrix, 3));

        // matrix = new int[][] { { 1, 3, 5, 7 }, { 10, 11, 16, 20 }, { 23, 30, 34, 60 }
        // };
        // System.out.println(instance.searchMatrix(matrix, 13));

        // matrix = new int[][] { { 1 } };
        // System.out.println(instance.searchMatrix(matrix, 2));

        // matrix = new int[][] { { 1, 4, 7, 11, 15 }, { 2, 5, 8, 12, 19 }, { 3, 6, 9,
        // 16, 22 }, { 10, 13, 14, 17, 24 },
        // { 18, 21, 23, 26, 30 } };
        // System.out.println(instance.searchMatrix2(matrix, 5));
        // System.out.println(instance.searchMatrix2(matrix, 20));

        int[] arr = new int[] { 3, 5, 8, 10 };
        System.out.println(instance.findClosestElements(arr, 2, 15));

        // arr = new int[] { 1, 2, 1, 3, 5, 6, 4 };
        // System.out.println(instance.findPeakElement(arr));

        // arr = new int[] { 1, 2, 3, 1 };
        // System.out.println(instance.findPeakElement(arr));

        // arr = new int[] { 5, 7, 7, 8, 8, 10 };
        // System.out.println(instance.search(arr, 8));

        // arr = new int[] { 5, 7, 7, 8, 8, 10 };
        // System.out.println(instance.search(arr, 6));

        // arr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 9 };
        // System.out.println(instance.missingNumber(arr));

        // arr = new int[] { 4, 5, 6, 7, 0, 1, 2 };
        // System.out.println(instance.search2(arr, 0));
        // System.out.println(instance.search2(arr, 3));

        // arr = new int[] { 3, 1 };
        // System.out.println(instance.search3(arr, 0));
        // System.out.println(instance.search3(arr, 1));

        String[] words = new String[] { "a", "bb", "acd", "ace" };
        System.out.println(instance.numMatchingSubseq("abcde", words));
    }

}
