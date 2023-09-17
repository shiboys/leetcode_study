package org.swj.leet_code.stackqueue;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/10 10:19
 *        单调栈
 */
public class MonotonyStackQueue {

    /**
     * 下一个更大的元素
     * nums:[2,1,2,4,3]
     * res:[4,2,4,-1,-1]
     * 
     * @param nums
     * @return
     */
    int[] nextGreaterElements(int[] nums) {
        // 存放答案的数组
        int[] res = new int[nums.length];
        Stack<Integer> stack = new Stack<>();
        // 从后往前遍历，将小于等于于当前 nums[i] 的栈中的元素全部弹出
        for (int i = nums.length - 1; i >= 0; i--) {
            // 判定个子高矮
            while (!stack.isEmpty() && nums[i] >= stack.peek()) {
                // 矮个起开，反正也被挡着了。。。
                stack.pop();
            }
            // nums[i] 身后的更大元素
            res[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(nums[i]);
        }
        return res;
    }

    /**
     * leetcode 496 题，下一个更大的元素
     * 求 nums2 的 子数组 nums1 中的元素的下一个更大的元素
     * 
     * @param nums1
     * @param nums2
     * @return
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int[] res = nextGreaterElements(nums2);
        Map<Integer, Integer> resMap = new HashMap<>();
        for (int i = 0; i < nums2.length; i++) {
            resMap.put(nums2[i], res[i]);
        }
        int[] subRes = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            subRes[i] = resMap.get(nums1[i]);
        }
        return subRes;
    }

    /**
     * leetcode 739 题，下一个更高温度需要等几天
     * Input: temperatures = [73,74,75,71,69,72,76,73]
     * Output: [1,1,4,2,1,1,0,0]
     * 
     * @param temperatures
     * @return
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] diff = new int[n];
        Stack<Integer> stack = new Stack<>();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && temperatures[i] >= temperatures[stack.peek()]) {
                stack.pop();
            }
            diff[i] = stack.isEmpty() ? 0 : stack.peek() - i;
            stack.push(i);
        }
        return diff;
    }

    /*
     * 处理环形数组，leetcode 503 题，「下一个更大的元素」，输入一个「环形数组」，请你计算其中每个元素的下一个更大的元素。
     * 比如输入[2,1,2,4,3],你应该返回[4,2,4,-1,4], 因为有了环形属性，左后一个元素 3 绕了一圈后才找到比自己大的元素 4.
     * 我们一般都是通过 % 运算符取模，来模拟环形特效。
     */

    public int[] nextGreaterElements2(int[] nums) {
        int n = nums.length;
        int[] nums2 = new int[n * 2];
        int[] res = new int[n];
        System.arraycopy(nums, 0, nums2, 0, n);
        System.arraycopy(nums, 0, nums2, n, n);
        Stack<Integer> stack = new Stack<>();
        for (int i = nums2.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums2[i] >= stack.peek()) {
                stack.pop();
            }
            res[i % n] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(nums2[i % n]);
        }
        return res;
    }

    public static void main(String[] args) {
        MonotonyStackQueue instance = new MonotonyStackQueue();
        // testMonotonicStack(instance);
        int[] items = new int[] { -7,-8,7,5,7,1,6,0};
        System.out.println(Arrays.toString(instance.maxSlidingWindow(items, 4)));
    }

    private static void testMonotonicStack(MonotonyStackQueue instance) {
        int[] nums = new int[] { 2, 1, 2, 4, 3 };
        System.out.println(Arrays.toString(instance.nextGreaterElements(nums)));

        nums = new int[] { 1, 3, 4, 2 };
        int[] nums2 = new int[] { 4, 1, 2 };
        System.out.println(Arrays.toString(instance.nextGreaterElement(nums2, nums)));

        int[] temperatures = new int[] { 73, 74, 75, 71, 69, 72, 76, 73 };
        System.out.println(Arrays.toString(instance.dailyTemperatures(temperatures)));

        nums = new int[] { 2, 1, 2, 4, 3 };
        System.out.println(Arrays.toString(instance.nextGreaterElements2(nums)));
    }

    interface MonotonicQueue {
        void push(int item);

        // 如果队头元素是 n，则删除它。
        void pop(int n);

        // 返回队列中的最大值
        int max();
    }

    static class MonotonicQueueImpl implements MonotonicQueue {
        Deque<Integer> queue = new LinkedList<>();

        @Override
        public void push(int item) {
            // 当前 入队的元素比队尾元素大，则弹出队尾的元素
            // 这里的判断条件是 item > queue.peekLast() 而不能是 item >= queue.peekLast()
            // 观察是 对于 case -7,-8,7,5,7,1,6,0 测试通不过，最后一个 7 会被移除，然后调用 pop() 的时候，再次移除一个 7，
            // 导致后面的窗口最大元素计算错误
            while (!queue.isEmpty() && item > queue.peekLast()) {
                queue.removeLast();
            }
            queue.addLast(item);
        }

        @Override
        public void pop(int n) {
            if (n == queue.peek()) {
                queue.removeFirst();
            }
            // 其他情况下 push 的时候，元素已经被压缩了，(这里的压缩，就是指弹出队列了)
        }

        @Override
        public int max() {
            return queue.peek();
        }
    }

    /**
     * leetcode 239 题，单调队列 求滑动窗口的最值
     * 
     * @param nums
     * @param k
     * @return
     */
    int[] maxSlidingWindow(int[] nums, int k) {
        int[] res = new int[nums.length - k + 1];
        MonotonicQueue window = new MonotonicQueueImpl();
        // 先填充 window，形成 k 大小的滑动窗口
        for (int i = 0; i < nums.length; i++) {
            if (i < k - 1) {
                // 先填满窗口 k -1 个元素
                window.push(nums[i]);
            } else {
                // 窗口向前滑动，加入新数字
                window.push(nums[i]);
                // 记录窗口的最大值
                res[i - k + 1] = window.max();
                // 移出窗口首位元素
                window.pop(nums[i - k + 1]);
            }
        }
        return res;
    }
}
