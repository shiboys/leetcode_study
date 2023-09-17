package org.swj.leet_code.stackqueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 
 * 用栈实现队列，和用队列实现栈
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
            return queue.poll();
        }

        public int top() {
            return topEle;
        }

        public boolean empty() {
            return queue.isEmpty();
        }
    }
}
