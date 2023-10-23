package org.swj.leet_code.stackqueue;

import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import org.swj.leet_code.linked_list.ListNode;

import javafx.scene.layout.Priority;
import lombok.val;

/**
 * 
 * 用栈实现队列，和用队列实现栈
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/10 10:19
 */
public class StackQueue {

    /**
     * 用栈实现队列，核心原理是使用两个栈 s1,s2 其中 s1 用来装入元素，
     * pop 的时候，将 s1 的所有元素 pop 后，push 入 s2 ，这样 s2 的栈顶元素就是 s1 要 pop 的元素
     * leetcode 232
     */
    static class MyQueue {

        private Stack<Integer> s1;
        private Stack<Integer> s2;

        public MyQueue() {
            s1 = new Stack<>();
            s2 = new Stack<>();
        }

        public void push(int x) {
            s1.push(x);
        }

        /**
         * pop 调用了 peek() 放，因此 pop 的时间复杂度跟 peek 相同
         * 最坏时间为 O(N),最好时间是 s2 非空的时候的 O(1)
         * 
         * @return
         */
        public int pop() {
            peek();
            return s2.pop();
        }

        public int peek() {
            // 如果 s2 为空，则将 s1 的所有元素 pop 然后在 push 进入 s2
            // 如果 s2 非空，则直接 peek s2 的栈顶元素就符合要求。
            if (s2.isEmpty()) {
                while (!s1.isEmpty()) {
                    s2.push(s1.pop());
                }
            }
            return s2.peek();
        }

        public boolean empty() {
            return s1.isEmpty() && s2.isEmpty();
        }
    }

    /**
     * 使用队列来实现栈
     * leetcode 225
     */
    static class MyStack {

        private Queue<Integer> queue;
        private int topEle = 0;

        public MyStack() {
            queue = new LinkedList<>();
        }

        public void push(int x) {
            queue.offer(x);
            topEle = x;
        }

        /**
         * pop 这里有些技巧，需要把队列原元素的前 n-2 元素重新加入队尾，这样 topEle 指向第 n-1 元素，
         * 然后在 pop 第 n 个元素。
         * pop 的时间复杂度为 O(n)
         * 
         * @return
         */
        public int pop() {
            int size = queue.size();
            while (size > 2) {
                queue.offer(queue.poll());
                size--;
            }
            topEle = queue.poll();
            queue.offer(topEle);
            return queue.poll();
        }

        public int top() {
            return topEle;
        }

        public boolean empty() {
            return queue.isEmpty();
        }
    }

    /**
     * 数据流的中位数，leetcode 346 题
     * 给定一个数据流和一个窗口大小，根据该滑动窗口的大小，计算其所有整数的移动平均值。
     * 
     * 输入：
     * ["MovingAverage", "next", "next", "next", "next"]
     * [[3], [1], [10], [3], [5]]
     * 输出：
     * [null, 1.0, 5.5, 4.66667, 6.0]
     * 
     * 解释：
     * MovingAverage movingAverage = new MovingAverage(3);
     * movingAverage.next(1); // 返回 1.0 = 1 / 1
     * movingAverage.next(10); // 返回 5.5 = (1 + 10) / 2
     * movingAverage.next(3); // 返回 4.66667 = (1 + 10 + 3) / 3
     * movingAverage.next(5); // 返回 6.0 = (10 + 3 + 5) / 3
     */
    static class MovingAverage {
        int windowSum;
        // windowsize
        int ws;
        Queue<Integer> queue;

        public MovingAverage(int size) {
            if (size < 1) {
                throw new IllegalArgumentException("size must be greater than 0");
            }
            this.ws = size;
            queue = new LinkedList<>();
        }

        public double next(int val) {
            windowSum += val;
            queue.offer(val);
            while (queue.size() > ws) {
                windowSum -= queue.poll();
            }
            return windowSum / (queue.size() * 1.0);
        }
    }

    /**
     * 最近请求次数
     * 写一个 RecentCounter 类来计算特定时间范围内最近的请求。
     * 
     * 请你实现 RecentCounter 类：
     * 
     * RecentCounter() 初始化计数器，请求数为 0 。
     * int ping(int t) 在时间 t 添加一个新请求，其中 t 表示以毫秒为单位的某个时间，并返回过去 3000
     * 毫秒内发生的所有请求数（包括新请求）。确切地说，返回在 [t-3000, t] 内发生的请求数。
     * 保证 每次对 ping 的调用都使用比之前更大的 t 值。
     * 用例：
     * RecentCounter recentCounter = new RecentCounter();
     * recentCounter.ping(1); // requests = [1]，范围是 [-2999,1]，返回 1
     * recentCounter.ping(100); // requests = [1, 100]，范围是 [-2900,100]，返回 2
     * recentCounter.ping(3001); // requests = [1, 100, 3001]，范围是 [1,3001]，返回 3
     * recentCounter.ping(3002); // requests = [1, 100, 3001, 3002]，范围是 [2,3002]，返回
     * 3
     * 
     */
    static class RecentCounter {
        /**
         * 实现思路用队列保存 ping 的元素，窗口长度为 3000，在下次ping 的时候，需要将窗口外的元素删除
         * 
         */
        private static final int WINDOW_SPAN = 3000;
        Queue<Integer> queue;

        public RecentCounter() {
            queue = new LinkedList<>();
        }

        public int ping(int t) {
            queue.offer(t);
            // 题目说了会保证进入队列的元素是从大到小
            while (t - queue.peek() > WINDOW_SPAN) {
                queue.poll();
            }
            return queue.size();
        }
    }

    /**
     * 敲击的次数
     * 设计一个敲击计数器，使它可以统计在过去 5 分钟内被敲击次数。（即过去 300 秒）
     * 
     * 您的系统应该接受一个时间戳参数 timestamp (单位为秒)，并且您可以假定对系统的调用是按时间顺序进行的 (即 timestamp 是单调递增的)。
     * 几次撞击可能同时发生。
     * HitCounter counter = new HitCounter();
     * counter.hit(1);// 在时刻 1 敲击一次。
     * counter.hit(2);// 在时刻 2 敲击一次。
     * counter.hit(3);// 在时刻 3 敲击一次。
     * counter.getHits(4);// 在时刻 4 统计过去 5 分钟内的敲击次数，函数返回 3。
     * counter.hit(300);// 在时刻 300 敲击一次。
     * counter.getHits(300); // 在时刻 300 统计过去 5 分钟内的敲击次数，函数返回 4。
     * counter.getHits(301); // 在时刻 301 统计过去 5 分钟内的敲击次数，函数返回 3。
     * 
     */
    static class HitCounter {
        /**
         * 解题思路：仍然是跟上一题相似，用一个队列装入敲击的时间戳，
         * 然后在 getHits 的时候，判断是否需要缩小窗口进而删除元素
         */
        private static final int TIME_SPAN = 300;
        private Queue<Integer> queue;

        public HitCounter() {
            queue = new LinkedList<>();
        }

        public void hit(int timestamp) {
            queue.offer(timestamp);
        }

        public int getHits(int timestamp) {
            if (queue.isEmpty()) {
                return 0;
            }
            while (!queue.isEmpty() && timestamp - queue.peek() >= TIME_SPAN) {
                queue.poll();
            }
            return queue.size();
        }
    }

    /**
     * leetcod 143 题，重排链表。
     * 示例1：
     * 输入：head = [1,2,3,4]
     * 输出：[1,4,2,3]
     * 
     * 示例2：
     * 输入：head = [1,2,3,4,5]
     * 输出：[1,5,2,4,3]
     * 
     * @param head
     */
    public void reorderList(ListNode head) {
        /**
         * 解题思路：
         * 看到这道题，我的想法是从中间将链表切断，后半部的链表装入栈中，然后跟前半步的链表节点重排。中点怎么查找？快慢指针呀，不是之前学过吗？
         * 在看了阿东的解法思路后，发现他是将全部链表装入栈中，然后边进行重排，边通过相交点判断中点，然后将中点之后的链表切断。这种想法不太容易想起来，还是我的想法比较容易理解
         * 所以我这里会写两个方法，首先写 阿东的方法，在写我的方法
         */
        ListNode p = head;
        Stack<ListNode> stack = new Stack<>();
        while (p != null) {
            stack.push(p);
            p = p.next;
        }
        p = head;
        ListNode next;
        while (p != null && !stack.isEmpty()) {
            ListNode lastNode = stack.pop();
            next = p.next;
            // 下面这两种请覆盖了链表的节点个个数为奇数或者偶数的请
            if (lastNode == next /* 节点个数为偶数 */ || lastNode.next == next /* 节点个数为奇数 */) {
                lastNode.next = null;
                break;
            }
            p.next = lastNode;
            lastNode.next = next;
            p = next;
        }
    }

    public void reorderList2(ListNode head) {
        ListNode p = head;
        if (p == null || p.next == null) {
            return;
        }
        ListNode p1 = head, p2 = head;
        while (p2.next != null && p2.next.next != null) {
            p1 = p1.next;
            p2 = p2.next.next;
        }
        // 经过上面的循环，此时 p1 为中间节点位置，断开p1后面的链表节点
        Stack<ListNode> stack = new Stack<>();
        p = p1.next;
        p1.next = null;
        while (p != null) {
            stack.push(p);
            p = p.next;
        }
        p = head;
        ListNode next, lastNode;
        while (p != null && !stack.isEmpty()) {
            next = p.next;
            lastNode = stack.pop();
            p.next = lastNode;
            lastNode.next = next;
            p = next;
        }
    }

    /**
     * 有效的括号，leetcode 20 题
     * 
     * @param s
     * @return
     */
    public boolean isValidParentheses(String s) {
        /**
         * 解题思路：栈是一种后进先出的数据结构，用来处理括号问题尤其有用
         */
        Stack<Character> stack = new Stack<>();
        for (char ch : s.toCharArray()) {
            if (ch == '{' || ch == '[' || ch == '(') {
                stack.push(ch);
            } else {
                if (!stack.isEmpty() && rightOf(stack.peek()) == ch) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    char rightOf(char parenthese) {
        switch (parenthese) {
            case '(':
                return ')';
            case '{':
                return '}';
            case '[':
                return ']';
            default:
                return '\0';
        }
    }

    /**
     * 150. 逆波兰表达式求值
     * 
     * 有效的运算符包括 +-、*、/。每个运算对象都可以是整数，也可以是另一个逆波兰表达式，两个整数之间的除法保留整数部分。
     * 可以保证给定的逆波兰表达式总是有效的。换句话说，表达式得出的有效数值不存在征世为 0 的情况，当然也能够被正确解析。
     * 
     * 示例1：
     * 输入：tokens = ["2","1","+","3","*"]
     * 输出：9
     * 解释：该算式转化为常见的中缀算术表达式为：((2 + 1) * 3) = 9
     * 
     * @param tokens
     * @return
     */
    public int evalRPN(String[] tokens) {
        /**
         * 基本思路：
         * 逆波兰表达式发明出来就是为了方面计算机运用「栈」进行表达式运算的，其预算规则如下：
         * 按顺序遍历逆波兰表达式中的字符，如果是数字，则入栈；如果是字符，则将栈顶的两个元素拿出来进行运算，再将结果放入栈。
         * 对于减法和除法，运算顺序别搞反了，栈顶是第二个数是被除(减)数
         */
        if (tokens == null || tokens.length < 1) {
            return -1;
        }
        int b, a;
        Stack<Integer> stack = new Stack<>();
        for (String ch : tokens) {
            if (ch.equals("+") || ch.equals("-") || ch.equals("*") || ch.equals("/")) {
                a = stack.pop();
                b = stack.pop();
                stack.push(getOperationResult(a, b, ch));
            } else {
                stack.push(Integer.parseInt(ch));
            }
        }
        return stack.pop();
    }

    int getOperationResult(int a, int b, String op) {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                // 因为 b 晚于 a 弹出，根据计算规则，b 数字在 a 数字之前
                return b - a;
            case "*":
                return a * b;
            case "/":
                return b / a;
        }
        return 0;
    }

    /**
     * leetcode 388 题，文件的最长绝对路径。
     * 这道题目的描述和解法请参考 stackqueue.md 中有关文件最长绝对路径的描述
     * 
     * @param input
     * @return
     */
    public int lengthLongestPath(String input) {
        String[] pathArr = input.split("\n");
        Deque<String> stack = new LinkedList<>();
        int maxLength = 0;
        for (String path : pathArr) {
            // 这里，我对 jdk 的 string.lastIndexOf 有了新的认识
            // "\txxx".lastIndexOf("\t") ==0,"\t\txxx".lastIndexOf("\t")==1,
            // "\t\t\txxx".lastIndexOf("\t")==2
            // "xxx".lastIndexOf("\t") == -1
            int level = path.lastIndexOf("\t") + 1;
            // 这个逻辑需要细细品味才能知晓其中的逻辑
            // 就是把叔辈路径给 弹出，栈中只留父路径
            // 比如说现在 level ==2 ，那就是 "\t\txxx", 当前路径要是入栈，就必须将 "\t\t\txxx" 和 其他 "\t\txxx" 弹出去
            // 只留 "\txxx"。level ==1 时，stack 弹出到只剩一个，就是 dir 根目录，此时正好是 path 的父目录
            while (level < stack.size()) {
                stack.removeLast();
            }
            stack.addLast(path.substring(level));
            // 当前路径是一个文件路径，计算其绝对路径长度
            if (path.contains(".")) {
                int pathLength = stack.stream().mapToInt(String::length).sum();
                // 加上路径分隔符 / 的长度
                pathLength += stack.size() - 1;
                maxLength = Math.max(pathLength, maxLength);
            }
        }
        return maxLength;
    }

    /**
     * 最小栈 leetcode 155 题
     * 
     * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
     * 
     * 实现 MinStack 类:
     * 
     * MinStack() 初始化堆栈对象。
     * void push(int val) 将元素val推入堆栈。
     * void pop() 删除堆栈顶部的元素。
     * int top() 获取堆栈顶部的元素。
     * int getMin() 获取堆栈中的最小元素。
     * 
     * 示例1：
     * MinStack minStack = new MinStack();
     * minStack.push(-2);
     * minStack.push(0);
     * minStack.push(-3);
     * minStack.getMin(); --> 返回 -3.
     * minStack.pop();
     * minStack.top(); --> 返回 0.
     * minStack.getMin(); --> 返回 -2.
     * 
     * 
     */
    class MinStack {

        /**
         * 解题思路，我当时看到这道题的时候，想到的是用栈压入正常的数据，最小的数据用指针执行，后来一想，最小的弹出后，第二小的找不到
         * 就想到用双链表将元素从小到大链起来，可是转头发现增加数据的时候，无法维持排序的双链表，即使带 hash 功能的双链表也不行，
         * 然后又想到了优先级队列，可是优先级队列在 栈弹出一个元素的时候，如果这个元素不是最小的，优先级队列不太好做到直接删除该元素
         * 最后慢慢的认知枯竭了。。。就想不出来了，拿出阿东的思路，发现用两个栈：一个存储整数的栈数据，一个用来存储当前入栈元素范围内的最小值
         * 有点 Mvcc 感觉，只能说还是自己的思维太狭隘了。
         * 具体思考过程请参考 markdown 文档 stackqueue.md 中的有关 最小栈 描述
         */
        Stack<Integer> stack;
        Stack<Integer> minStk;

        public MinStack() {
            stack = new Stack<>();
            minStk = new Stack<>();
        }

        public void push(int val) {
            stack.push(val);
            if (minStk.isEmpty() || val < minStk.peek()) {
                minStk.push(val);
            } else {
                minStk.push(minStk.peek());
            }
        }

        public void pop() {
            stack.pop();
            minStk.pop();
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStk.peek();
        }
    }

    class MinStack2 {
        Stack<Integer> stack;
        Stack<Integer> minStk;

        public MinStack2() {
            stack = new Stack<>();
            minStk = new Stack<>();
        }

        public void push(int val) {
            stack.push(val);
            // 这里注意，是小于等于的情况都要 push 进去
            if (minStk.isEmpty() || val <= minStk.peek()) {
                minStk.push(val);
            }
        }

        public void pop() {
            if (stack.isEmpty()) {
                return;
            }
            int val = stack.pop();
            if (val == minStk.peek() && !minStk.isEmpty()) {
                minStk.pop();
            }
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStk.peek();
        }
    }

    /**
     * 895. 最大频率栈
     * 解释：
     * FreqStack = new FreqStack();
     * freqStack.push (5);//堆栈为 [5]
     * freqStack.push (7);//堆栈是 [5,7]
     * freqStack.push (5);//堆栈是 [5,7,5]
     * freqStack.push (7);//堆栈是 [5,7,5,7]
     * freqStack.push (4);//堆栈是 [5,7,5,7,4]
     * freqStack.push (5);//堆栈是 [5,7,5,7,4,5]
     * freqStack.pop ();//返回 5 ，因为 5 出现频率最高。堆栈变成 [5,7,5,7,4]。
     * freqStack.pop ();//返回 7 ，因为 5 和 7 出现频率最高，但7最接近顶部。堆栈变成 [5,7,5,4]。
     * freqStack.pop ();//返回 5 ，因为 5 出现频率最高。堆栈变成 [5,7,4]。
     * freqStack.pop ();//返回 4 ，因为 4, 5 和 7 出现频率最高，但 4 是最接近顶部的。堆栈变成 [5,7]。
     * 
     */
    static class FreqStack {
        private final AtomicInteger counter = new AtomicInteger();
        private PriorityQueue<int[]> queue;
        private java.util.Map<Integer, Integer> valFreqMap;

        /**
         * 解题思路，肯定有一个 map 记录着数字的频率
         * 至于 pop 的实现方式，我想到的是用优先级队的大顶堆的方式，堆的元素为数组，数组的格式为 [val,freq,ts]
         * 而阿东的解法是用另外一个 map freqToMap<Integer,LinkedList<Integer>> 记录频率和 val 列表的 的映射
         * 两种方式我都会写下，提交到 leetcode 做下性能分析
         */

        public FreqStack() {
            // 大顶堆，freq 大的排在堆顶，freq 相等的，时间晚的优先级高
            queue = new PriorityQueue<>((a, b) -> {
                return a[1] == b[1] ? b[2] - a[2] : b[1] - a[1];
            });
            valFreqMap = new HashMap<>();
        }

        public void push(int val) {
            int freq = valFreqMap.getOrDefault(val, 0);
            freq++;
            valFreqMap.put(val, freq);
            queue.offer(new int[] { val, freq, counter.incrementAndGet() });
        }

        public int pop() {
            if (queue.isEmpty()) {
                return -1;
            }
            int[] res = queue.poll();
            int val = res[0];
            int freq = res[1];
            freq--;
            if (freq == 0) {
                valFreqMap.remove(val);
            } else {
                valFreqMap.put(val, freq);
            }
            return val;
        }
    }

    static class FreqStack2 {

        private java.util.Map<Integer, Integer> valFreqMap;
        private Map<Integer, LinkedList<Integer>> freqToVal;
        int maxFreq;

        /**
         * 解题思路，肯定有一个 map 记录着数字的频率
         * 至于 pop 的实现方式，我想到的是用优先级队的大顶堆的方式，堆的元素为数组，数组的格式为 [val,freq,ts]
         * 而阿东的解法是用另外一个 map freqToMap<Integer,LinkedList<Integer>> 记录频率和 val 列表的 的映射
         * 两种方式我都会写下，提交到 leetcode 做下性能分析
         */

        public FreqStack2() {
            valFreqMap = new HashMap<>();
            freqToVal = new HashMap<>();
        }

        public void push(int val) {
            int freq = valFreqMap.getOrDefault(val, 0);
            freq++;
            valFreqMap.put(val, freq);
            // 该元素之前的 freq 也会留在 freqToVal map 中
            freqToVal.putIfAbsent(freq, new LinkedList<>());
            freqToVal.get(freq).addLast(val);
            maxFreq = Math.max(freq, maxFreq);
        }

        public int pop() {
            if (valFreqMap.isEmpty()) {
                return -1;
            }
            LinkedList<Integer> list = freqToVal.get(maxFreq);
            int val = list.removeLast();
            valFreqMap.put(val, maxFreq - 1);
            if (list.isEmpty()) {
                freqToVal.remove(maxFreq);
                maxFreq--;
            }
            return val;
        }
    }

    /**
     * 32. 最长有效括号
     * 给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。
     * 
     * 示例 1：
     * 
     * 输入：s = "(()"
     * 输出：2
     * 解释：最长有效括号子串是 "()"
     * 
     * 示例 2：
     * 
     * 输入：s = ")()())"
     * 输出：4
     * 解释：最长有效括号子串是 "()()"
     * 
     * @param s
     * @return
     */
    public int longestValidParentheses(String s) {
        /**
         * 思路，必须用栈呀
         * 
         */

        // 想了好久，试了好几种办法，都没搞定所有用例。
        // 没办法了，先用笨办法搞一波
        int maxLen = 0;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + 1; j < s.length(); j++) {
                if (isValidParentheses(s.substring(i, j + 1), stack)) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }
        return maxLen;
        // 测试用例发现，居然超时，说明这个方法的时间复杂度还是太高了
    }

    /**
     * 参考 阿东的解法，使用栈来处理一般情况，包括括号嵌套的，
     * 然后使用动态规划数组 dp 来记录前一个字符串为结尾的合法括号字符串长度
     * 
     * @param s
     * @return
     */
    public int longestValidParentheses2(String s) {
        char[] chs = s.toCharArray();
        Stack<Integer> stack = new Stack<>();
        // dp 定义：以 i 结尾的字符串是合法的括号结尾的最长的长度
        int[] dp = new int[s.length() + 1];
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] == '(') {
                stack.push(i);
                // 左括号结尾，肯定不是一个合法的结尾，长度为 0。
                dp[i + 1] = 0;
            } else { // 当前字符是 右括号
                if (!stack.isEmpty()) {
                    int left = stack.pop();
                    // i - left + 1 是 left...i 这段子串的长度，dp[left] 是 0...left-1 这段字符串之间的合法最长长度
                    int len = i - left + 1 + dp[left];
                    dp[i + 1] = len;
                } else { // 没有配对的左括号
                    dp[i + 1] = 0;
                }
            }
        }
        int maxLength = 0;
        for (int i = 0; i < dp.length; i++) {
            maxLength = Math.max(maxLength, dp[i]);
        }
        return maxLength;
    }

    boolean isValidParentheses(String s, Stack<Character> stack) {
        /**
         * 解题思路：栈是一种后进先出的数据结构，用来处理括号问题尤其有用
         */
        while (!stack.isEmpty()) {
            stack.pop();
        }
        for (char ch : s.toCharArray()) {
            if (ch == '{' || ch == '[' || ch == '(') {
                stack.push(ch);
            } else {
                if (!stack.isEmpty() && rightOf(stack.peek()) == ch) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * 224. 基本计算器
     * 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
     * 
     * 注意:不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。
     * s 由数字、'+'、'-'、'('、')'、和 ' ' 组成
     * '-' 可以用作一元运算(即 "-1" 和 "-(2 + 3)" 是有效的)
     * 示例 2：
     * 
     * 输入：s = " 2-1 + 2 "
     * 输出：3
     * 示例 3：
     * 
     * 输入：s = "(1+(4+5+2)-3)+(6+8)"
     * 输出：23
     * 
     * @param s
     * @return
     */
    Stack<Character> opStack;
    Stack<Integer> numStack;

    public int calculate(String s) {
        /**
         * 解题思路：这道题被标记为 hard
         * 使用两个栈，一个符号栈，一个数字栈进行计算，然后倒着遍历字符串
         */
        if (s == null || s.length() < 1) {
            return 0;
        }
        opStack = new Stack<>();
        numStack = new Stack<>();

        int firstOpIdx = firstOpIdx(s);
        char firstOperator = s.charAt(firstOpIdx);
        if (firstOperator == '-') {
            s = fixMinusWithZeros(s);
        }
        char[] chs = s.toCharArray();
        char ch;
        StringBuilder numSb = new StringBuilder();
        int j;
        for (int i = chs.length - 1; i >= 0; i--) {
            ch = chs[i];
            if (ch == ' ') {
                continue;
            } else if (ch == '+' || ch == '-' || ch == '(' || ch == ')') {
                if (ch != '(') {
                    opStack.push(ch);
                } else { // 倒着遍历,遇到了一个'(', 那么表示遇到一个可计算的括号表达式，计算这个表达式，然后重新压入栈中
                    numStack.push(calculateSimple());
                }
            } else {
                numSb.delete(0, numSb.length());
                // 有可能是多个数字字符,比如，50,500
                j = i;
                while (j >= 0 && chs[j] >= '0' && chs[j] <= '9') {
                    numSb.append(chs[j]);
                    j--;
                }
                if (numSb.length() > 1) {
                    numStack.push(Integer.parseInt(numSb.reverse().toString()));
                } else {
                    numStack.push(ch - '0');
                }
                if (j < i) {
                    i = j + 1;
                }
            }
        }
        // 此处已经把括号都消除完毕，可以跟无括号一样进行计算。
        return calculateSimple();
    }

    /**
     * 计算不包含括号的普通表达式
     * 
     * @param s
     * @return
     */
    int calculateSimple() {
        if (opStack.isEmpty() && numStack.isEmpty()) {
            return 0;
        }
        while (!opStack.isEmpty() && !numStack.isEmpty()) {
            char op = opStack.pop();
            if (op == ')') {
                // 简单表达式遇到 '('，无法进行计算,返回栈顶处理结果
                break;
            }
            int n1 = numStack.pop();
            int n2 = 0;
            if (!numStack.isEmpty()) {
                n2 = numStack.pop();
            } else {
                if (op == '-') {
                    // 压入计算结果
                    numStack.push(-n1);
                    continue;
                }
            }
            if (op == '+') {
                // 压入计算结果
                numStack.push(n1 + n2);
            } else if (op == '-') {
                // 压入计算结果
                numStack.push(n1 - n2);
            }
        }
        return numStack.pop();
    }

    int firstOpIdx(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ' && s.charAt(i) != '(') {
                return i;
            }
        }
        return 0;
    }

    String fixMinusWithZeros(String s) {
        char[] chs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < chs.length; i++) {
            if (chs[i] >= '0' && chs[i] <= '9') {
                break;
            }
            if (chs[i] == '-') {
                sb.append(0);
            }
            sb.append(chs[i]);
        }
        sb.append(s.substring(i));
        return sb.toString();
    }

    /**
     * 计算包含括号，加减乘除和空格的基本计算
     * 227. Basic Calculator II
     * 
     * @param s
     * @return
     */
    public int calculate2(String s) {
        /**
         * 上面的计算方式，是我自己捣鼓出来的，确实有点复杂了，不过能解决问题，提交也能通过所有用例
         * 下面的解决方法是参考 阿东的，他的思路确实独辟蹊径，令人耳目一新
         * 表达式 1-2+3 ，首先可以转化成 +1,-2,+3 压入栈中，不再像我那样使用操作数栈，使用一个栈来完成大一统。最后弹出所有元素进行累加
         * 同理，乘除也是变成 *n, /m 这样的字符串 由于有优先级，结果的计算就是 stack.peek() * n 或者 stack.peek() /m
         * 最后处理括号的问题
         * 括号的处理具有递归性，之前真没看出来，比如说下面的计算方式：
         * calculate(3 * (4 - 5/2) - 6)
         * = 3 * calculate(4 - 5/2) - 6
         * = 3 * 2 - 6
         * = 0
         * 而递归开始的条件是 '(', 结束的条件是 ')'
         * 之前我觉得在字符串身上实现递归复杂，不好控制递归开始和结束。后来看阿东的解法，将字符串中的字符放到队列中
         * 队列，这么牛逼的数据结构，我都没想起来，队里不就刚好处理一个少一个吗？递归处理和函数处理，完全无影响呀
         */
        if (s == null || s.isEmpty()) {
            return 0;
        }
        Queue<Character> queue = new LinkedList<>();
        for (char ch : s.toCharArray()) {
            queue.add(ch);
        }
        return helper(queue);
    }

    // 将字符串转换成字符队列，方便进行递归处理
    private int helper(Queue<Character> queue) {
        Stack<Integer> stack = new Stack<>();
        // 初始的符号，如果遇到 1+2+3 这样的，+ 可以编程 +1,+2,+3, 如果遇到 -(-2)+1 这样的， + 入栈的是 +0,-2,+1
        // 所以下面的 num 也很重要
        char sign = '+';
        int num = 0;
        char ch;
        while (!queue.isEmpty()) {
            ch = queue.poll();
            if (ch == '(') { // 递归开始
                num = helper(queue);
            }
            if (Character.isDigit(ch)) {
                // 下面这个表达式是将字符串转换成表达式的最简洁的一种方式。
                // 比我上面的使用 StringBuilder 收集数字字符串，然后用 Integer.parseInt() 强了不是一星半点
                num = 10 * num + (ch - '0');
            }
            if ((!Character.isDigit(ch) && ch != ' ') || queue.isEmpty()) { // 排除空格，处理操作符，如果是字符末尾，也需要进行处理
                if (sign == '+') {
                    stack.push(num);
                } else if (sign == '-') {
                    stack.push(-num);
                } else if (sign == '*') {
                    // 1-3*4+5=>+1,-3*4, 符号始终在数字前面
                    stack.push(stack.pop() * num);
                } else if (sign == '/') {
                    stack.push(stack.pop() / num);
                }
                // 重置 sign 和 num
                sign = ch;
                num = 0;
            }
            if (ch == ')') {// 递归结束, 跳出循环
                break;
            }
        }
        int res = 0;
        for (int val : stack) {
            res += val;
        }
        return res;
    }

    public static void main(String[] args) {
        // testHitCounter();
        // String input = "\t\tfile2.ext";
        // System.out.println(input.lastIndexOf("\t"));
        // System.out.println(input.indexOf("\t"));
        // input = "file2.txt";
        // System.out.println(input.indexOf("\t"));

        StackQueue instance = new StackQueue();

        // ListNode head = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 3,
        // 4, 5 });
        // instance.reorderList(head);
        // ListNodeUtil.printLinkedNode(head);

        // head = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 3, 4 });
        // instance.reorderList(head);
        // ListNodeUtil.printLinkedNode(head);

        // System.out.println(instance.isValidParentheses("()[]{}"));
        // System.out.println(instance.isValidParentheses("([)]"));

        // String[] expr = new String[] { "2", "1", "+", "3", "*" };
        // System.out.println(instance.evalRPN(expr));

        // expr = new String[] { "10", "6", "9", "3", "+", "-11", "*", "/", "*", "17",
        // "+", "5", "+" };
        // System.out.println(instance.evalRPN(expr));

        // System.out.println(instance.lengthLongestPath("dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext"));
        // testMaxFreqStack();
        // System.out.println(instance.longestValidParentheses2("()(()"));
        // System.out.println(instance.longestValidParentheses2("(()(()"));
        // System.out.println(instance.longestValidParentheses2(")()())"));
        // System.out.println(instance.longestValidParentheses2("()(())"));
        // System.out.println(instance.calculate(" 2-10 + 2 "));
        // System.out.println(instance.calculate("(1+(4+5+12)-3)+(6+8)"));
        // System.out.println(instance.calculate("1-( -2)"));
        // System.out.println(instance.calculate2("(-2)+ 1"));
        // System.out.println(instance.calculate2("-(-2)+4"));
        System.out.println(instance.calculate2("3+2*2"));
    }

    static void testMaxFreqStack() {
        FreqStack2 freqStack = new FreqStack2();
        // FreqStack freqStack = new FreqStack();
        freqStack.push(5);// 堆栈为 [5]
        freqStack.push(7);// 堆栈是 [5,7]
        freqStack.push(5);// 堆栈是 [5,7,5]
        freqStack.push(7);// 堆栈是 [5,7,5,7]
        freqStack.push(4);// 堆栈是 [5,7,5,7,4]
        freqStack.push(5);// 堆栈是 [5,7,5,7,4,5]
        System.out.println(freqStack.pop());// 返回 5 ，因为 5 出现频率最高。堆栈变成 [5,7,5,7,4]。
        System.out.println(freqStack.pop());// 返回 7 ，因为 5 和 7 出现频率最高，但7最接近顶部。堆栈变成 [5,7,5,4]。
        System.out.println(freqStack.pop());// 返回 5 ，因为 5 出现频率最高。堆栈变成 [5,7,4]。
        System.out.println(freqStack.pop());// 返回 4 ，因为 4, 5 和 7 出现频率最高，但 4 是最接近顶部的。堆栈变成 [5,7]。
    }

    private static void testHitCounter() {
        HitCounter counter = new HitCounter();
        counter.hit(1);// 在时刻 1 敲击一次。
        counter.hit(2);// 在时刻 2 敲击一次。
        counter.hit(3);// 在时刻 3 敲击一次。
        System.out.println(counter.getHits(4));// 在时刻 4 统计过去 5 分钟内的敲击次数，函数返回 3。
        counter.hit(300);// 在时刻 300 敲击一次。
        System.out.println(counter.getHits(300)); // 在时刻 300 统计过去 5 分钟内的敲击次数，函数返回 4。
        System.out.println(counter.getHits(301)); // 在时刻 301 统计过去 5 分钟内的敲击次数，函数返回 3。
    }
}
