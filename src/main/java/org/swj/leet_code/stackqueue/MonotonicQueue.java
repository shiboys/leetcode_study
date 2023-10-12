package org.swj.leet_code.stackqueue;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 带泛型的单调队列，主要给滑动窗口使用，包含窗口/队列的最大值最小值
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/10 21:03
 */
public class MonotonicQueue<E extends Comparable<E>> {
    /**
     * 实现思路，主要是使用 3 个队列，q1 正常队列，保持队列的先进先出
     * q2 从后向前，单调递增队列 队首是当前队列的最大值
     * q3 抽后先前，单点递减队列，队首是当前队列的最小值
     */
    private Deque<E> deque;
    private Deque<E> smallQueue;
    private Deque<E> largeQueue;

    public MonotonicQueue() {
        deque = new LinkedList<>();
        smallQueue = new LinkedList<>();
        largeQueue = new LinkedList<>();
    }

    public void push(E e) {
        if (e == null) {
            return;
        }
        deque.offerLast(e);
        // 实现压扁效果,将小于 e 的元素全部删除
        // 从前往后依次递减
        while (!largeQueue.isEmpty() && e.compareTo(largeQueue.peekLast()) > 0) {
            largeQueue.pollLast();
        }
        largeQueue.offerLast(e);

        // 小队列，从前往后依次递增。从后先前查找，一直到比当前元素还小的元素为止。
        while (!smallQueue.isEmpty() && e.compareTo(smallQueue.peekLast()) < 0) {
            smallQueue.pollLast();
        }
        smallQueue.offerLast(e);
    }

    public E min() {
        if (smallQueue.isEmpty()) {
            return null;
        }
        return smallQueue.peekFirst();
    }

    public E max() {
        if (largeQueue.isEmpty()) {
            return null;
        }
        return largeQueue.peekFirst();
    }

    public E pop() {
        E e = deque.pollFirst();
        if (e != null && !smallQueue.isEmpty() && e.compareTo(smallQueue.peekFirst()) == 0) {
            smallQueue.pollFirst();
        }
        if (e != null && !largeQueue.isEmpty() && e.compareTo(largeQueue.peekFirst()) == 0) {
            largeQueue.pollFirst();
        }
        return e;
    }

    public int size() {
        return deque.size();
    }

    public boolean isEmpty() {
        return deque.isEmpty();
    }
}
