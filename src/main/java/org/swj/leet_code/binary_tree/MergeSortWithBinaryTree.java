package org.swj.leet_code.binary_tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/28 20:27
 *        归并排序使用二叉树版本
 */
public class MergeSortWithBinaryTree {

    /**
     * 归并排序比较像二叉树的后序遍历
     * 线分割，再排序。
     * 
     * @param arr
     */
    public void mergeSort(int[] arr) {
        temp = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1);
    }

    void mergeSort(int[] arr, int start, int end) {
        if (start >= end) {// 分割到只有一个元素的时候，需要返回了，以便进行归并。
            return;
        }
        int mid = start + (end - start) / 2;
        // 先分割
        mergeSort(arr, start, mid);
        mergeSort(arr, mid + 1, end);
        // 再归并
        merge(arr, start, mid, end);
    }

    // 阿东的解法是利用成员变零 tmp 数组，降低分配的频率，
    // tmp 数组主要用来存储归并之前的数组 [start..end] 之间的数据，辅助更新原数组之用
    int[] temp;

    void merge(int[] arr, int start, int mid, int end) {
        if (start > end) {
            return;
        }
        int i = start, j = mid + 1;
        for (int k = start; k <= end; k++) {
            temp[k] = arr[k];
        }
        for (int p = start; p <= end; p++) {
            if (i == mid + 1) { // i..mid 已经归并完毕
                arr[p] = temp[j++];
            } else if (j == end + 1) {
                arr[p] = temp[i++];
            } else if (temp[i] < temp[j]) {
                arr[p] = temp[i++];
            } else {
                arr[p] = temp[j++];
            }
        }
    }

    // 归并方法2 主要是把 排序后的元素都存入 temp 数组，然后在将 temp 数组的内容重新写会原数组

    /**
     * 找出数组中比当前元素 nums[i] 小的元素个数。 leetcode 315 题。
     * 这道题那，暴力解法就不说了，for 循环
     * 这里仍然借用 阿东的方法，利用归并排序的思想，思路见 complex.md 中有关归并排序在二叉树中的使用
     * 
     * @param nums
     * @return
     */
    public List<Integer> countSmaller(int[] nums) {
        countArr = new int[nums.length];
        Pair[] pairs = new Pair[nums.length];
        tempPairArr = new Pair[nums.length];

        for (int i = 0; i < nums.length; i++) {
            pairs[i] = new Pair(nums[i], i);
        }
        mergeSort(pairs, 0, nums.length - 1);
        List<Integer> res = new ArrayList<>(countArr.length);
        for (int i = 0; i < countArr.length; i++) {
            res.add(countArr[i]);
        }
        return res;
    }

    Pair[] tempPairArr;
    int[] countArr;

    void mergeSort(Pair[] paris, int start, int end) {
        if (start >= end) {// 一定是大于等于，让只有1 个原始的时候开始归并
            return;
        }

        int mid = start + (end - start) / 2;
        mergeSort(paris, start, mid);
        mergeSort(paris, mid + 1, end);
        mergeAndCount(paris, start, mid, end);
    }

    void mergeAndCount(Pair[] pairs, int start, int mid, int end) {
        int i = start, j = mid + 1;
        for (int k = start; k <= end; k++) {
            tempPairArr[k] = pairs[k];
        }
        // 归并过程
        for (int p = start; p <= end; p++) {
            if (i == mid + 1) {
                pairs[p] = tempPairArr[j++];
            } else if (j == end + 1) { // j 走完了，剩下 [i,mid] 都是比 end 大的元素，需要更新统计信息
                pairs[p] = tempPairArr[i++];
                countArr[pairs[p].idx] += j - mid - 1;
            } else if (tempPairArr[i].val > tempPairArr[j].val) {
                pairs[p] = tempPairArr[j++];
            } else { // i <= j ， 此时正好符合 md 文档中描述的要求，因为归并都是有序的
                pairs[p] = tempPairArr[i++];
                // 想不到系列
                countArr[pairs[p].idx] += j - mid - 1; // 这里是 +=
                // 就像田忌赛马一样，我 i 这边 比不过 你 j，你 [mid+1,j-1] 之间的所有数字都是 都是比我小的。
            }
        }
        // 总之，发现这个规律的人,对算法真是有研究，我只能说我的思维肤浅了，局限了，只想着眼前的排好序就行了。
    }

    public int reversePair(int[] nums) {
        sortAndReversePair(nums);
        return reversePairCount;
    }

    void sortAndReversePair(int[] nums) {
        temp = new int[nums.length];
        mergeSortAndReversePair(nums, 0, nums.length - 1);
    }

    void mergeSortAndReversePair(int[] nums, int start, int end) {
        if (start >= end) { // 拆分递归返回入口
            return;
        }
        int mid = start + (end - start) / 2;
        // 拆分
        mergeSortAndReversePair(nums, start, mid);
        mergeSortAndReversePair(nums, mid + 1, end);
        // 归并
        mergeAndReversePair(nums, start, mid, end);
    }

    /**
     * leetcode 493 反转对，利用归并排序的链表有序的规则，仍然是以夹带私货的方式求出反转对的数量
     * 
     * @param nums
     * @param start
     * @param mid
     * @param end
     */
    void mergeAndReversePair(int[] nums, int start, int mid, int end) {
        for (int i = start; i <= end; i++) {
            temp[i] = nums[i];
        }
        // 这里夹带私货
        // for(int i =start;i<=mid;i++) {
        // for(int j = mid+1;j<=end;j++) {
        // if(temp[i] > 2 * temp[j]) {
        // reversePairCount ++;
        // }
        // }
        // }
        // 上面这段代码O(N^2)效率不高，无法通过 leetcode 的测试，
        // 我们要想办法实现单个循环，根据归并排序的规律，我们知道如果 nums[i] >= nums[j] * 2, 那么我们就继续判断 nums[i] 和
        // nums[j+1]
        // 想办法维护一个 [mid+1,j) 的区间，在这个区间内， j-mid-1 的个数就是 nums[i] > 2*nums[j] 的个数
        int hi = mid + 1;// hi 这里在 while 内层 while 循环之后没有被重置，是因为 nums[i+1] > nums[i] where i between start
                         // and mid
        for (int i = start; i <= mid; i++) {
            // 这里使用 while 的 > 条件，是因为根据归并排序的特点，mid+1..end 之间是从小到大有序的
            // 另外这里为了防止 2*int 的整数溢出，转换成 long 型
            while (hi <= end && ((long) nums[i] > (long) 2 * nums[hi])) {
                hi++;
            }
            // 退出循环时，nums[i] <= 2*nums[hi] ,此时判断 nums[i] 位置满足条件的个数是多少
            reversePairCount += hi - (mid + 1);
        }

        int i = start, j = mid + 1;
        for (int p = start; p <= end; p++) {
            if (i == mid + 1) {
                nums[p] = temp[j++];
            } else if (j == end + 1) {
                nums[p] = temp[i++];
            } else if (temp[i] > temp[j]) {
                nums[p] = temp[j++];
            } else {
                nums[p] = temp[i++];
            }
        }
    }

    // 反转对数量
    int reversePairCount;

    /**
     * 区间和的个数。leetcode 327 题
     * 
     * @param nums
     * @param lower
     * @param upper
     * @return
     */
    public int countRangeSum(int[] nums, int lower, int upper) {
        long[] preSum = new long[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            preSum[i + 1] = preSum[i] + nums[i];
        }
        this.lower = lower;
        this.upper = upper;
        sort(preSum);
        return rangeCount;
    }

    private int lower;
    private int upper;
    private long[] tempPreSum;
    private int rangeCount;

    void sort(long[] nums) {
        tempPreSum = new long[nums.length];
        mergeSortAndCountRangeSum(nums, 0, nums.length - 1);
    }

    void mergeSortAndCountRangeSum(long[] nums, int start, int end) {
        if (start >= end) {
            return;
        }
        int mid = start + (end - start) / 2;
        mergeSortAndCountRangeSum(nums, start, mid);
        mergeSortAndCountRangeSum(nums, mid + 1, end);
        mergeAndCountRangeSum(nums, start, mid, end);
    }

    void mergeAndCountRangeSum(long[] nums, int start, int mid, int end) {
        for (int i = start; i <= end; i++) {
            tempPreSum[i] = nums[i];
        }
        // 进行 lower 和 upper 区间的判断
        // for(int i=start;i<=mid;i++) {
        // for(int j = mid+1;j<=end;j++) {
        // long delta = nums[j] - nums[i];
        // if(delta<=upper && delta >= lower) {
        // rangeCount++;
        // }
        // }
        // }
        // 上述方式是最容易懂的，如果在生产环境其实也是没问题的，如果是运行在非常严苛的环境，则不行，比如 leetcode 的提交，则会提示超时无法通过
        // 所以这里跟上面那道反转树相似 nums[i] >= nums[j]*2, 这里需要敲定两个边界 lower =<nums[j] <= upper
        // 这个区间的总数量
        for (int i = start; i <= mid; i++) {
            int lo = mid + 1, hi = mid + 1;
            while (lo <= end && nums[i] - nums[i] < lower) {// 找到 >= lower 的停止
                lo++;
            }
            while (hi <= end && nums[hi] - nums[i] <= upper) { // 找到 > upper 的则停止
                hi++;
            }
            rangeCount += hi - lo;
        }

        // 仍然是进行归并
        int i = start, j = mid + 1;
        for (int p = start; p <= end; p++) {
            if (i == mid + 1) {
                nums[p] = tempPreSum[j++];
            } else if (j == end + 1) {
                nums[p] = tempPreSum[i++];
            } else if (tempPreSum[i] > tempPreSum[j]) {
                nums[p] = tempPreSum[j++];
            } else {
                nums[p] = tempPreSum[i++];
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[] { 1, 2, 3, 4, 5, 6, 7 };
        MergeSortWithBinaryTree instance = new MergeSortWithBinaryTree();
        // instance.mergeSort(arr);
        // System.out.println(Arrays.toString(arr));

        arr = new int[] { 1,9,7,8,5 };
        System.out.println(instance.countSmaller(arr));
        // arr = new int[] { 2, 4, 3, 5, 1 };
        // System.out.println(instance.reversePair(arr));

        // arr = new int[] { -2, 5, -1 };
        // System.out.println(instance.countRangeSum(arr, -2, 2));
    }

    static class Pair {
        // 数组值
        int val;
        // 数组索引
        int idx;

        public Pair(int val, int idx) {
            this.val = val;
            this.idx = idx;
        }
    }

}