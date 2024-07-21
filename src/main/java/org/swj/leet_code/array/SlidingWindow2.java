package org.swj.leet_code.array;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/07 21:27
 *        滑动窗口2
 */
public class SlidingWindow2 {

    /**
     * 1658. 将 x 减到 0 的最小操作数
     * 给你一个整数数组 nums 和一个整数 x 。每一次操作时，你应当移除数组 nums 最左边或最右边的元素，
     * 然后从 x 中减去该元素的值。请注意，需要 修改 数组以供接下来的操作使用。
     * 如果可以将 x 恰好 减到 0 ，返回 最小操作数 ；否则，返回 -1 。
     * 
     * 输入：nums = [1,1,4,2,3], x = 5
     * 输出：2
     * 解释：最佳解决方案是移除后两个元素，将 x 减到 0 。
     */
    public int minOperations(int[] nums, int x) {
        /**
         * 解法思路：
         * 这道题需要转换下思路，题目让我们从边缘删除掉和为 x 的元素，那剩下的是什么？剩下来的不就是 nums 中的一个子数组吗？
         * 让你极可能地从边缘删除元素说明啥？是不是就是说剩下这个子数组大小尽可能大？
         * 
         * 所以这道题等价于让你寻找 nums 中和为 sum(num)-x 的最长子数组(这个思路转化确实比较牛逼)
         * 
         * 寻找子数组就是考察滑动窗口。前面滑动窗口的灵魂 3 拷问：
         * 1、什么时候应该扩大窗口，
         * 2、什么时候应该缩小窗口，
         * 3、什么时候得到一个合法的答案
         * 
         * 针对本题，以上三个问题的答案是：
         * 1、当窗口内元素之和小于目标和 target 时，扩大窗口
         * 2、当元素内窗口元素之和大于目标之和 target 时，缩小窗口，空余出更多可替换次数。
         * 3、当窗口内元素之和等于目标 target 是，找到一个符合条件的子数组，我们想找的是最长的子数组长度。
         */
        if (nums == null || nums.length < 1) {
            return -1;
        }
        // int sum = Arrays.stream(nums).sum();
        int sum = 0;// Arrays.stream(nums).sum(); 这个效率太低，坑爹
        for (int n : nums) {
            sum += n;
        }
        int target = sum - x;
        if (target < 0) {
            return -1;
        }
        int left = 0, right = 0;
        int windowSum = 0;
        int res = -1;
        while (right < nums.length) {
            // 扩大窗口
            windowSum += nums[right];
            right++;
            // 缩小窗口的实际
            while (left < right && windowSum > target) {
                windowSum -= nums[left];
                left++;
            }
            // 得到合法答案的时机
            if (windowSum == target) {
                res = Math.max(res, right - left);
            }
        }
        // 数组的长度减去最长子串的长度，得到最短的操作次数
        return res != -1 ? nums.length - res : res;
    }

    /*
     * 713. 乘积小于 K 的子数组
     * 给你一个整数数组 nums 和一个整数 k ，请你返回子数组内所有元素的乘积严格小于 k 的连续子数组的数目。
     * 示例1：
     * 输入：nums = [10,5,2,6], k = 100
     * 输出：8
     * 解释：8 个乘积小于 100 的子数组分别为：[10]、[5]、[2], [6]、[10,5]、[5,2]、[2,6]、[5,2,6]。
     * 需要注意的是 [10,5,2] 并不是乘积小于 100 的子数组。
     * 
     * 这道题考察滑动窗口技巧，维护一个窗口在 nums 上滑动， 然后计算那些元素之积小于 k 的窗口个数即可
     * 灵魂 3 问：
     * 1、什么时候应该扩大窗口
     * 2、什么时候缩小窗口
     * 3、什么时候得到一个合法的答案
     * 
     * 针对本题，以上三个问题的答案如下：
     * 1、当窗口元素之积小于 k 时，扩大窗口，让积更大
     * 2、当窗口之积大于 k 时，缩小窗口，让积更小
     * 3、当窗口元素之积小于 k 时，窗口内的所有子数组都是合法子数组
     */
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        int validNum = 0;
        if (nums == null || nums.length < 1) {
            return 0;
        }
        int left = 0, right = 0;
        // 窗口之积
        int windowMulti = 1;
        while (right < nums.length) {
            windowMulti *= nums[right];
            right++;
            while (left < right && windowMulti >= k) {
                windowMulti /= nums[left];
                left++;
            }
            // 当前的 [left..right]就是一个合法的窗口
            /*
             * 但是窗口如何计算合法的子数组个数？
             * 比方说 left=1,righ=4 划定了 [1,2,3] 这个窗口(right 是开区间)
             * 但不止[left..right] 是合法子数组，[left+1..right]，[left+2..right] 等都是合法子数组
             * 所以我们需要把 [3],[2,3],[1,2,3] 这 right- left 个子数组都算上。
             */
            validNum += right - left;
        }
        return validNum;
    }

    /**
     * 209. 长度最小的子数组
     * 
     * @param target
     * @param nums
     * @return
     */
    public int minSubArrayLen(int target, int[] nums) {
        // 子数组和，离不开前缀和，然后加上滑动窗口
        int[] preSumArr = new int[nums.length + 1];
        for (int i = 1; i <= nums.length; i++) {
            preSumArr[i] = preSumArr[i - 1] + nums[i - 1];
        }
        int left = 0, right = 0;
        int minLen = preSumArr.length;
        while (right < preSumArr.length) {
            while (left < right && preSumArr[right] - preSumArr[left] >= target) {
                minLen = Math.min(minLen, right - left);
                left++;
            }
            right++;
        }
        return minLen == preSumArr.length ? 0 : minLen;
    }

    public static void main(String[] args) {
        SlidingWindow2 instance = new SlidingWindow2();
        int[] nums = new int[] { 3, 2, 20, 1, 1, 3 };
        // System.out.println(instance.minOperations(nums, 10));

        // nums = new int[] { 5, 6, 7, 8, 9 };
        // System.out.println(instance.minOperations(nums, 4));

        // nums = new int[] { 1, 1, 4, 2, 3 };
        // System.out.println(instance.minOperations(nums, 5));

        nums = new int[] { 10, 5, 2, 6 };
        System.out.println(instance.numSubarrayProductLessThanK(nums, 100));

        nums = new int[] { 2, 3, 1, 2, 4, 3 };
        System.out.println(instance.minSubArrayLen(7, nums));

        nums = new int[] { 1, 4, 4 };
        System.out.println(instance.minSubArrayLen(4, nums));

        nums = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
        System.out.println(instance.minSubArrayLen(11, nums));
    }
}
