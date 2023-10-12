package org.swj.leet_code.advanced_data_structure;

import java.util.LinkedList;

/**
 * leetcode 1670. 设计前中后队列
 * 请你设计一个队列，支持在前，中，后三个位置的 push 和 pop 操作
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/08 17:29
 */
public class FrontMiddleBackQueue {
    /**
     * 这题有点难度，主要是细节不好把控。常规的队列只能在首尾进行操作，想在中间操作队列，需要在底层把队列切成 left right 两个列表
     * 但是这里面的细节问题就是元素为奇数时两个链表元素的分配问题。
     * 总体上来说，这道题的解法思路是底层用两个链表来完成一个队列的前中后三个位置的 push 和 pop 操作。
     * 
     *
     * 如果是奇数个元素，维护左边少，右边多，所以：
     * 1、如果有偶数个元素时，pushMiddle 优先向右边添加
     * 2、如果有奇数个元素时，popMiddle 优先从右边删除
     * 3、如果只有 1 个元素，popFront 的时候，要去右边删除。
     */
    private LinkedList<Integer> left;
    private LinkedList<Integer> right;

    public FrontMiddleBackQueue() {
        left = new LinkedList<>();
        right = new LinkedList<>();
    }

    public void pushFront(int val) {
        // size 为奇数的时候，直接在 left 队列的队首插入元素
        left.addFirst(val);
        balance();
    }

    public void pushMiddle(int val) {
        if (size() % 2 == 0) {
            // 偶数的时候，维护右边的链表元素多
            right.addFirst(val);
        } else { // 奇数的时候，插入 left 的尾部
            left.addLast(val);
        }
        /**
         * 符合测试用例要求
         * q.init=[1,2]
         * q.pushMiddle(3); // [1, 3, 2]
         * q.pushMiddle(4); // [1, 4, 3, 2]
         */
        balance();
    }

    public void pushBack(int val) {
        right.addLast(val);
        balance();
    }

    public int popFront() {
        int sz = size();
        if (sz == 0) {
            return -1;
        }
        if (sz == 1) {
            return right.removeFirst();
        }
        int val = left.removeFirst();
        balance();
        return val;
    }

    public int popMiddle() {
        // popMiddle 是 pushMiddle 的逆操作
        // 偶数的话，先从 left popMiddle
        int sz = size();
        if (sz == 0) {
            return -1;
        }
        if (sz % 2 == 0) {
            return left.removeLast();
        } else {
            return right.removeFirst();
        }
    }

    public int popBack() {
        int sz = size();
        if (sz == 0) {
            return -1;
        }
        int val = right.removeLast();
        balance();
        return val;
    }

    public int size() {
        return left.size() + right.size();
    }

    /**
     * 维护左边少，右边多的状态，每次增删元素之后都要执行一次
     */
    void balance() {
        // 维护右边最多比左边多一个元素
        if (right.size() > left.size() + 1) {
            left.addFirst(right.removeLast());
        } else if (left.size() > right.size()) {
            right.addFirst(left.removeLast());
        }
    }
}
