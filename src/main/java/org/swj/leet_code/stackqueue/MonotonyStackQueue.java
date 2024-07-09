package org.swj.leet_code.stackqueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import org.swj.leet_code.linked_list.ListNode;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/10 10:19
 *        单调栈和单调队列
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
        // 为什么要从后向前遍历，是因为我们要找的是右侧比 nums[i] 大的元素，因此先把
        // 右侧的元素入栈
        // 从后往前遍历，将小于等于于当前 nums[i] 的栈中的元素全部弹出
        for (int i = nums.length - 1; i >= 0; i--) {
            // 判定个子高矮。比我小的弹出，或者叫压扁也行
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
            // 当前 入队的元素比队尾元素大，则弹出队尾的元素。效果等同于压扁元素
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
            if (n == queue.peek() && !queue.isEmpty()) {
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

    /**
     * 1019. 链表中的下一个更大节点
     * 给定一个长度为 n 的链表 head
     * 
     * 对于列表中的每个节点，查找下一个 更大节点 的值。也就是说，对于每个节点，
     * 找到它旁边的第一个节点的值，这个节点的值 严格大于 它的值。
     * 
     * @param head
     * @return
     */
    public int[] nextLargerNodes(ListNode head) {
        /**
         * 因为是查找后面节点的大值，这个跟上面的数组的下一个更大的元素异曲同工，单调栈是跑不了了。
         * 至于单调栈怎么将数组元素加入栈中，我的初始想法是，用递归的方式逆序链表
         * 后来看了阿东的解法，发现丫是通过遍历链表，将链表的数值放入到一个 ArrayList，然后再倒序遍历 ArrayList
         * 不得不说，他这种解法更优。但是经过实践我发现，递归的代码也很优雅
         */
        // reverseTraverseNode 递归解法是我写的，
        // return reverseTraverseNode(head, new Stack<Integer>(), 0);

        // 下面的使用 ArrayList 的是阿东写的
        ArrayList<Integer> list = new ArrayList<>();
        for (ListNode p = head; p != null; p = p.next) {
            list.add(p.val);
        }
        Stack<Integer> stack = new Stack<>();
        int[] res = new int[list.size()];
        int num;
        for (int i = list.size() - 1; i >= 0; i--) {
            // 因为计算 res[i] 后面更大值的节点，因此res[i] 的计算需要在 nodei 压入栈之前
            num = list.get(i);
            // 将栈中小于当前元素的元素全部弹出，得到下一个就是当前元素之后的更大元素
            while (!stack.isEmpty() && stack.peek() < num) {
                stack.pop();
            }
            res[i] = stack.isEmpty() ? 0 : stack.peek();
            stack.push(num);
        }
        return res;
    }

    int[] reverseTraverseNode(ListNode node, Stack<Integer> stack, int counter) {
        if (node == null) { // 遍历到尾结点
            return new int[counter];
        }
        int[] res = reverseTraverseNode(node.next, stack, counter + 1);
        while (!stack.isEmpty() && node.val > stack.peek()) {
            stack.pop();
        }
        res[counter] = stack.isEmpty() ? 0 : stack.peek();
        stack.push(node.val);
        return res;
    }

    /**
     * 1944. 队列中可以看到的人数。难度等级为困难。解法参考 stackqueue.md 中的有关 章节
     * 
     * @param heights
     * @return
     */
    public int[] canSeePersonsCount(int[] heights) {
        /**
         * 解题思路，我刚开始看到这道题，也想到了用单点栈，但是我一看到用例1 的解释 第 0 个人能看到编号为 1，2 和 4 的人
         * 第 2 个人能看到编号为 3 和 4 的人。我就犯迷糊了，为啥第 0 个人看不到编号为 3 的人，看了题目要求，
         * 一个人能 看到 他右边的另一个人的条件是这两个人之间的所有人都比他们俩 矮，所以 0号人看不到 3 号人
         * 但是要使用最大栈来「压缩」并统计中间的人数，0号人是能计算到 3 号人的，所以我有点迷糊了
         * 直到后来，发现从后往前进行最大栈的压缩计算，发现在便利到 2 号人的时候，3号人就会被弹出，那遍历到 0 号人的时候，
         * 3号人自然也就不存在栈中了。从这里看出，我的思维还是比较容易被图中中的静态事物所影响，而没有考虑到计算机动态计算的过程。
         * 
         * 这同时也引申出另外一个问题，被弹出的比我矮的人算不是比我小的元素，如果算的话，那这个解释就自相矛盾
         * 这也是后面一道题，进栈的时候需要计算权重，出栈的是，权重需要参与计算的原因
         */

        Stack<Integer> stack = new Stack<>();
        int[] res = new int[heights.length];
        for (int i = heights.length - 1; i >= 0; i--) {
            int counter = 0;
            while (!stack.isEmpty() && heights[i] > stack.peek()) {
                // 将比我矮且我能看到的人弹出，计数+1。
                stack.pop();
                counter++;
            }
            // 如果当前队列为空，那么 heights[i] 只能看到 counter 个，
            // 否则 heights[i] 除了能看到 counter 个，还能看到 栈中比 heights[i] 高的那个元素 stack.peek(), 因此要 +1
            res[i] = stack.isEmpty() ? counter : counter + 1;
            stack.push(heights[i]);
        }
        return res;
    }

    /**
     * 1475. 商品折扣后的最终价格。
     * 给你一个数组 prices ，其中 prices[i] 是商店里第 i 件商品的价格。
     * 
     * 商店里正在进行促销活动，如果你要买第 i 件商品，那么你可以得到与 prices[j] 相等的折扣，
     * 其中 j 是满足 j > i 且 prices[j] <= prices[i] 的 最小下标 ，如果没有满足条件的 j ，你将没有任何折扣。
     * 
     * 请你返回一个数组，数组中第 i 个元素是折扣后你购买商品 i 最终需要支付的价格。
     * 
     * 示例1：
     * 输入：prices = [8,4,6,2,3]
     * 输出：[4,2,4,2,3]
     * 解释：
     * 商品 0 的价格为 price[0]=8，你将得到 prices[1]=4 的折扣，所以最终价格为 8 - 4 = 4。
     * 商品 1 的价格为 price[1]=4，你将得到 prices[3]=2 的折扣，所以最终价格为 4 - 2 = 2。
     * 商品 2 的价格为 price[2]=6，你将得到 prices[3]=2 的折扣，所以最终价格为 6 - 2 = 4。
     * 商品 3 和 4 都没有折扣。
     * 
     * @param prices
     * @return
     */
    public int[] finalPrices(int[] prices) {
        /**
         * 解题思路：
         * 这道题用到了 单调栈模板：计算下一个更小或者相等的元素。
         * 解法如下：
         */
        int[] res = new int[prices.length];
        Stack<Integer> stack = new Stack<>();
        for (int i = prices.length - 1; i >= 0; i--) {
            // 当前价格比栈顶的小，弹出
            while (!stack.isEmpty() && prices[i] < stack.peek()) {
                stack.pop();
            }
            res[i] = stack.isEmpty() ? prices[i] : prices[i] - stack.peek();
            stack.push(prices[i]);
        }
        return res;
    }

    /**
     * 901. 股票价格跨度
     * 设计一个算法收集某些股票的每日报价，并返回该股票当日价格的 跨度 。
     * 
     * 当日股票价格的 跨度 被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
     * 例如，假设未来 7 天股票的价格是 [100,80,60,70,60,75,85]，那么股票跨度将是 [1,1,1,2,1,4,6]。
     * 实现 SotckSpanner 类
     */
    static class StockSpanner {
        Stack<int[]> stack;

        /**
         * 这道题显然要用到单调栈技巧，当加入 price 时，把所有小于等于 price 的价格都「挤掉」，相当于计算前一个更大元素
         * 比如已经入栈的价格序列是 [40,30,20,10], 那么如果执行 next(25)，价格序列就变成了 [40,30,25], 20 和 10
         * 都会被「挤掉」
         * 算上 25 本身，函数返回 2+1=3。
         * 但还有个问题，这个 3 应该作为「权重」和 25 一起存储在栈中。因为之后 25 还可能被挤掉，比如说执行 next(26), 价格序列就变成了
         * [40,30,26], 但这种情况之下之前的 20 和 10 显然也应该被挤掉，函数应该返回 3+1 = 4。
         * 具体解法看代码
         */
        public StockSpanner() {
            stack = new Stack<>();
        }

        /**
         * 给出今天的报价 price, 返回该股票当日价格的 跨度。
         * 
         * @param price
         * @return
         */
        public int next(int price) {
            int weight = 1;// 默认权重为1
            while (!stack.isEmpty() && price >= stack.peek()[0]) {
                int[] item = stack.pop();
                if (item != null && item.length > 1) {
                    weight += item[1];
                }
            }
            stack.push(new int[] { price, weight });
            return weight;
        }
    }

    /**
     * 402. 移掉 K 位数字
     * 
     * 给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，使得剩下的数字最小。请你以字符串形式返回这个最小的数字
     * 示例1：
     * 输入：num = "1432219", k = 3
     * 输出："1219"
     * 解释：移除掉三个数字 4, 3, 和 2 形成一个新的最小的数字 1219
     * 
     * 示例 2 ：
     * 输入：num = "10200", k = 1
     * 输出："200"
     * 解释：移掉首位的 1 剩下的数字为 200. 注意输出不能有任何前导零
     * 
     * @param num
     * @param k
     * @return
     */
    public String removeKdigits(String num, int k) {
        /**
         * 解题思路：
         * 如果想让结果尽可能小，那么清除数字分两步：
         * 1、先删除 num 中的若干数字，使得 num 从左到右每一位都递增。比如 14329 转化成 129，这需要使用到单调栈技巧
         * 2、num 中的每一位变成单调递增之后，如果 k 还大于0(还可以继续删除)的话，则删除尾部的数字，比如 129 删除成为 12.
         */
        if (num == null || num.isEmpty()) {
            return num;
        }
        Stack<Character> stack = new Stack<>();
        char[] chs = num.toCharArray();
        char ch;
        for (int i = 0; i < chs.length; i++) {
            ch = chs[i];
            // 当前数字比栈顶元素小，弹出栈顶元素，且把当前数字压入栈中。
            while (k > 0 && !stack.isEmpty() && ch < stack.peek()) {
                stack.pop();
                k--;
            }
            // 这里有个细节，如果当前栈为空，且 ch =='0', 则说明接下来的数字是 0 开头的，为了防止出现前导 0，
            // 因此需要将 '0' 排除
            if (stack.isEmpty() && ch == '0') {
                continue;
            }
            stack.push(ch);
        }
        StringBuilder sb = new StringBuilder();
        // 如果 k 仍然大于0 ，则继续删除
        while (!stack.isEmpty()) {
            ch = stack.pop();
            if (k > 0) {
                k--;
            } else {// k==0 删除完了
                sb.append(ch);
            }
        }
        // 栈是反着存储元素，再反过来就行
        if (sb.length() == 0) {
            return "0";
        }
        return sb.reverse().toString();
    }

    /**
     * 1438. 绝对差不超过限制的最长连续子数组
     * 给你一个整数数组 nums ，和一个表示限制的整数 limit，请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于
     * limit 。
     * 如果不存在满足条件的子数组，则返回 0
     * 
     * 示例1：
     * 输入：nums = [8,2,4,7], limit = 4
     * 输出：2
     * [2,4] 最大绝对差 |2-4| = 2 <= 4.
     * [4,7] 最大绝对差 |4-7| = 3 <= 4.
     * [2,4,7] 最大绝对差 |2-7| = 5 > 4.
     * 因此，满足题意的最长子数组的长度为 2, 其他组合都不满足 。
     * 
     * @param nums
     * @param limit
     * @return
     */
    public int longestSubarray(int[] nums, int limit) {
        /**
         * 基本思路：
         * 很明显这道题要用到 最值的滑动窗口技巧
         * 当窗口内绝对值之差不超过 Limit 时扩大窗口，当新加入窗口的元素使得绝对值之差超过 Limit 时开始收缩窗口，窗口的最大宽度即最长子数组的长度。
         * 
         * 但有个问题，单窗口新进元素时，我们可以更新窗口中的最大值和最小值，当窗口收缩时，如果更新窗口的最大最小值那？遍历时肯定不能遍历的，
         * 这就用到单调队列结构了，我们的通用 MonotonicQueue 类就派上用场了，用来高效判断窗口中的最大值和最小值。
         * 
         * 总结一句话，就是保证窗口内的元素 绝对值 < limit，统计窗口大小，就欧了
         */
        if (nums == null || nums.length < 1) {
            return 0;
        }
        int right = 0;
        int span = -1;
        int windowSize = 0;
        org.swj.leet_code.stackqueue.MonotonicQueue<Integer> window = new org.swj.leet_code.stackqueue.MonotonicQueue<>();
        while (right < nums.length) {
            // 增加窗口元素，扩大窗口
            window.push(nums[right]);
            // 经过测试，发现 windowSize 可以被 window.size() 取代
            windowSize++;
            right++;
            // 当窗口的最大值-窗口的最小值 > limit ，则需要收缩窗口，需要保证收缩以后的窗口的 极值在 limit 之内
            // System.out.println("window.max=" + window.max() + ",window.min=" +
            // window.min());
            while (!window.isEmpty() && window.max() - window.min() > limit) {
                window.pop();
                windowSize--;
            }
            span = Math.max(span, windowSize);
        }
        return span;
    }

    /**
     * 862. 和至少为 K 的最短子数组
     * 给你一个整数数组 nums 和一个整数 k ，找出 nums 中和至少为 k 的 最短非空子数组 ，并返回该子数组的长度。如果不存在这样的 子数组 ，返回
     * -1 。
     * 子数组 是数组中 连续 的一部分。
     * 
     * @param nums
     * @param k
     * @return
     */
    public int shortestSubarray(int[] nums, int k) {
        /**
         * 这道题被标记了 hard，也确实难度比较大，同时结合了 滑动窗口算法，前缀和技巧 和 单调队列几个知识点。
         * 
         * 首先、想要快速记录子数组的和，需要前缀和 与计算一个 preSumArr 数组，然后在这个 preSumArr 数组上施展滑动窗口算法 寻找寻找一个差值
         * 大于 k 且宽度最小的「窗口」，这个窗口的大小就是题目想要的结果。
         * 
         * 这里面还有一个问题，当滑动窗口扩大时，新进入窗口的元素 preSumArr[right] 需要知道窗口中最小的那个元素是多少，
         * 和最下的那个元素相减才能得到尽可能大的子数组和。
         * 
         * 如何判断你窗口的最值？这就需要单点队列结构出马了，下面看解法吧：
         */
        if (nums == null || nums.length < 1) {
            return 0;
        }
        int left = 0, right = 0;
        org.swj.leet_code.stackqueue.MonotonicQueue<Integer> window = new org.swj.leet_code.stackqueue.MonotonicQueue<>();
        int[] preSumArr = new int[nums.length + 1];
        int res = Integer.MAX_VALUE;
        for (int i = 1; i <= nums.length; i++) {
            preSumArr[i] = preSumArr[i - 1] + nums[i - 1];
        }
        while (right < preSumArr.length) {
            // 加入窗口
            window.push(preSumArr[right]);
            right++;
            // 判断窗口内元素元素跟当前元素的子数组之和(就是 preSumArr[right] - window.min()) 是否满足条件
            while (right < preSumArr.length && !window.isEmpty()
                    && preSumArr[right] - window.min() >= k) { // 找到了满足条件元素
                // 至于这里为啥要用一个 preSumArr[right] 这个还没进入窗口的元素，这是因为当前的 right 经过 ++ 之后，表示的就是
                // nums 数组的 [right-1] 索引的元素，left 到 right 之间的窗口大小满足条件，只需要计算最小距离即可
                res = Math.min(res, right - left);
                // 缩小窗口。窗口被移出的元素，也一定是 window.pop() 出的元素
                left++;
                window.pop();
            }
        }
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    /**
     * 918. 环形子数组的最大和
     * 给定一个长度为 n 的环形整数数组 nums ，返回 nums 的非空 子数组 的最大可能和 。
     * 
     * 示例1：
     * 输入：nums = [1,-2,3,-2]
     * 输出：3
     * 解释：从子数组 [3] 得到最大和 3
     * 示例2：
     * 输入：nums = [5,-3,5]
     * 输出：10
     * 解释：从子数组 [5,5] 得到最大和 5 + 5 = 10
     * 
     * @param nums
     * @return
     */
    public int maxSubarraySumCircular(int[] nums) {
        /**
         * 解题思路，子数组的和，基本上都要用到前缀和。
         * 但是我的思路是累计窗口之和，如果窗口和变小，则缩小窗口, 后来发现此路行不通
         * 还的是乖乖的用前缀和
         */
        // int[] circularNums = new int[nums.length * 2];
        // System.arraycopy(nums, 0, circularNums, 0, nums.length);
        // System.arraycopy(nums, 0, circularNums, nums.length, nums.length);

        long[] preSumArr = new long[nums.length * 2 + 1];
        for (int i = 1; i < preSumArr.length; i++) {
            preSumArr[i] = preSumArr[i - 1] + nums[(i - 1) % nums.length];
        }
        int right = 0;
        long maxSum = 0;
        // while (right < preSumArr.length) {
        // right++;
        // for (left = 0; left < right && right < preSumArr.length; left++) {
        // maxSum = Math.max(maxSum, preSumArr[right] - preSumArr[left]);
        // }
        // }
        // 1,-2,3,-2 最大和为 3
        // 5,-3,5 最大和为 10. 5,-3,5,5,-3,5
        org.swj.leet_code.stackqueue.MonotonicQueue<Long> window = new org.swj.leet_code.stackqueue.MonotonicQueue<>();
        while (right < preSumArr.length) {
            window.push(preSumArr[right]);
            right++;
            // 保证窗口内的元素个数 <= nums.length
            while (window.size() > nums.length) {
                window.pop();
            }
            // 计算 最大子数组和
            if (right < preSumArr.length) {
                maxSum = Math.max(maxSum, preSumArr[right] - window.min());
            }
        }
        return (int) maxSum;
    }

    /**
     * 581. 最短无序连续子数组
     * 给你一个整数数组 nums ，你需要找出一个 连续子数组 ，如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。
     * 
     * 请你找出符合题意的 最短 子数组，并输出它的长度。
     * 示例 1：
     * 
     * 输入：nums = [2,6,4,8,10,9,15]
     * 输出：5
     * 解释：你只需要对 [6, 4, 8, 10, 9] 进行升序排序，那么整个表都会变为升序排序。
     * 
     * @param nums
     * @return
     */
    public int findUnsortedSubarray(int[] nums) {
        /**
         * 这道题的解法，当时已经比较晚了，我看了一天的算题，脑袋不好使了，
         * 后来就参考了阿东的思路，看完后就拍下脑袋，哦，原来是这样呀
         * 解法一：使用排序数组的方式，将原数组跟排序数组比对，找到不同的值位置的索引 left 和 right
         * 解法二：使用单调栈，之前用的单调栈基本都是从后往前，所以思维又被限制住了
         * 这道题就是单调栈的经典用法，单调递增栈，一个升序数组从前往后遍历的话，就是一个单调递增栈
         * 单调递减栈，一个升序数组如果从后往前遍历的话，就是一个单调递减栈
         * 利用这两种单调栈的特性，遍历两遍数组，就得到最左侧的 left 和左右侧的 right
         * 不过后来我想了下，既然是最左侧和最右侧，那左右两侧只取第一个元素就行了，栈都不需要用。
         * 但是后来又看了看用例，发现这个解法是错误的。比如说用例
         * [2,6,1,8,10,9,15], 你不能说我找到 左边 1 所在的索引是第一个 left，10 所在索引为 right
         * 返回 right-left+1，这肯定不对。
         * 最后还是发现 使用单调栈 yyds
         */
        int left = Integer.MAX_VALUE, right = -1;
        Stack<Integer> incrStack = new Stack<>();
        for (int i = 0; i < nums.length; i++) {
            while (!incrStack.isEmpty() && nums[i] < nums[incrStack.peek()]) {
                // 取最小的需要排序左侧索引位
                left = Math.min(left, incrStack.pop());
            }
            incrStack.push(i);
        }
        // 从后往前遍历，需要递减栈
        Stack<Integer> decrStack = new Stack<>();
        for (int j = nums.length - 1; j >= 0; j--) {
            while (!decrStack.isEmpty() && nums[j] > nums[decrStack.peek()]) {
                right = Math.max(right, decrStack.pop());
            }
            decrStack.push(j);
        }
        if (left == Integer.MAX_VALUE && right == -1) {
            return 0;
        }
        return right - left + 1;
    }

    /**
     * 最短无序连续子数组 的排序解法
     * 
     * @param nums
     * @return
     */
    public int findUnsortedSubarray2(int[] nums) {
        int[] copy = Arrays.copyOf(nums, nums.length);
        Arrays.sort(copy);
        int n = nums.length;
        int left = -1, right = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] != copy[i]) {
                left = i;
                break;
            }
        }
        for (int j = n - 1; j >= 0; j--) {
            if (nums[j] != copy[j]) {
                right = j;
                break;
            }
        }
        if (left == -1 && right == -1) {
            return 0;
        }
        return right - left + 1;
    }

    /**
     * 945. 使数组唯一的最小增量
     * 给你一个整数数组 nums 。每次 move 操作将会选择任意一个满足 0 <= i < nums.length 的下标 i，并将 nums[i] 递增
     * 1。
     * 
     * 返回使 nums 中的每个值都变成唯一的所需要的最少操作次数。
     * 示例 2：
     * 
     * 输入：nums = [3,2,1,2,1,7]
     * 输出：6
     * 解释：经过 6 次 move 操作，数组将变为 [3, 4, 1, 2, 5, 7]。
     * 可以看出 5 次或 5 次以下的 move 操作是不能让数组的每个值唯一的。
     * 
     * @param nums
     * @return
     */
    public int minIncrementForUnique(int[] nums) {
        /**
         * 这道题我想到的就是用 map 存储元素的出现次数，然后遍历 map
         * 将出现次数大于1 的元素给他找一个位置，然后 -1，并统计移动次数
         * 感觉没啥算法含量。
         * 但是提交之后，发现用 map 会导致超时
         */
        if (nums == null || nums.length < 1) {
            return 0;
        }
        Map<Integer, Integer> mapCounter = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            mapCounter.put(nums[i], mapCounter.getOrDefault(nums[i], 0) + 1);
        }
        int minMoveCounter = 0;
        int val, oldKey;
        for (int key : nums) {
            oldKey = key;
            val = mapCounter.get(oldKey);
            if (val == 1) {
                continue;
            }
            while (val > 1) {
                // 每次重新判断数量之后，重置 key 为原来的元素
                key = oldKey;
                while (mapCounter.containsKey(key)) {
                    key++;
                    minMoveCounter++;
                }
                // 当前的 key 在 map 中不存在，数量置为 1。
                mapCounter.put(key, 1);
                // 重复的 key 的数量减 1。
                val--;
            }
            // 所有重复的 key 都 move 后，重新设置 原始 key 的数量
            mapCounter.put(oldKey, val);

        }
        return minMoveCounter;
    }

    public int minIncrementForUnique2(int[] nums) {
        // 使用 HashMap 的 minIncrementForUnique 方法提交 leetcode 之后发现会超时
        // 也想到用排序和遍历的方式，但是实在没想出来排序后怎么才能将重复的数字消除掉
        // 最后参考了别人的解法，
        // 大致思路是：排序后，将每个元素都跟 它 前一个元素对比，如果 <= 则将当前元素 +1 然后遍历下一个元素
        // 比如用例 3,2,1,2,1,7 。排序后的结果是 1,1,2,2,3,7
        // 但是经过该方法的遍历和更改后，就变成 1,2,3,4,5,7。而不是用例说明中的 数组将变为 [3, 4, 1, 2, 5, 7]
        // 确实是被这个用例给误导了，想不出来.
        // 排序的时间复杂度是 O(nlogn), 遍历的时间复杂度是 O(N)，所以总的时间复杂度是 O(NlogN)
        if (nums == null || nums.length < 1) {
            return 0;
        }
        Arrays.sort(nums);
        int res = 0;
        int prev = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] <= prev) {
                // 累计移动次数
                res += prev - nums[i] + 1;
                nums[i] = prev + 1;
            }
            prev = nums[i];
        }
        return res;
    }

    public static void main(String[] args) {
        MonotonyStackQueue instance = new MonotonyStackQueue();
        // testMonotonicStack(instance);
        // int[] items = new int[] { -7, -8, 7, 5, 7, 1, 6, 0 };
        // System.out.println(Arrays.toString(instance.maxSlidingWindow(items, 4)));

        // ListNode head = ListNodeUtil.convertToNodeListFromArray(new int[] { 2, 1, 5
        // });
        // int[] arr = instance.nextLargerNodes(head);
        // System.out.println(Arrays.toString(arr));

        // head = ListNodeUtil.convertToNodeListFromArray(new int[] { 2, 7, 4, 3, 5 });
        // arr = instance.nextLargerNodes(head);
        // System.out.println(Arrays.toString(arr));

        // arr = new int[] { 10, 6, 8, 5, 11, 9 };
        // System.out.println(Arrays.toString(instance.canSeePersonsCount(arr)));

        int[] arr = new int[] { 8, 4, 6, 2, 3 };
        System.out.println(Arrays.toString(instance.finalPrices(arr)));

        // arr = new int[] { 28, 14, 28, 35, 46, 53, 66, 80, 87, 88 };
        // StockSpanner stockSpanner = new StockSpanner();
        // for (int price : arr) {
        // System.out.print(stockSpanner.next(price) + " ");
        // }
        // System.out.println();
        // String input = "14329";
        // System.out.println(instance.removeKdigits(input, 2));
        // System.out.println(instance.removeKdigits(input, 3));

        // input = "10";
        // System.out.println(instance.removeKdigits(input, 3));

        // arr = new int[] { 10, 1, 2, 4, 7, 2 };
        // System.out.println(instance.longestSubarray(arr, 5));

        // arr = new int[] { 4, 2, 2, 2, 4, 4, 2, 2 };
        // System.out.println(instance.longestSubarray(arr, 0));

        // arr = new int[] { 2, -1, 2 };
        // System.out.println(instance.shortestSubarray(arr, 3));
        // arr = new int[] { 2, 1 };
        // System.out.println(instance.shortestSubarray(arr, 4));
        // arr = new int[] { 1 };
        // System.out.println(instance.shortestSubarray(arr, 1));

        // arr = new int[] { 1, -2, 3, -2 };
        // System.out.println(instance.maxSubarraySumCircular(arr));

        // arr = new int[] { 5, -3, 5 };
        // System.out.println(instance.maxSubarraySumCircular(arr));

        // arr = new int[] { 3, -2, 2, -3 };
        // System.out.println(instance.maxSubarraySumCircular(arr));

        // arr = new int[] { 2, 6, 4, 8, 10, 9, 15 };
        // System.out.println(instance.findUnsortedSubarray(arr));

        arr = new int[] { 3, 2, 1, 2, 1, 7 };
        System.out.println("min move count:");
        System.out.println(instance.minIncrementForUnique2(arr));

        arr = new int[] { 2, 2, 2, 1 };
        System.out.println(instance.minIncrementForUnique2(arr));
    }
}
