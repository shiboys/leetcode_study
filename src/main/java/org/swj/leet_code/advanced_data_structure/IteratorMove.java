package org.swj.leet_code.advanced_data_structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.val;

/**
 * 自定义的迭代器
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/11 22:00
 */
public class IteratorMove {

    /**
     * leetcode 251 题
     * 设计并实现一个能战展开二维向量的迭代器。该迭代器需要支持 next 和 hasNext 两种操作
     * Vector2D iterator = new Vector2D([[1,2],[3],[4]]);
     * 
     * iterator.next(); // 返回 1
     * iterator.next(); // 返回 2
     * iterator.next(); // 返回 3
     * iterator.hasNext(); // 返回 true
     * iterator.hasNext(); // 返回 true
     * iterator.next(); // 返回 4
     * iterator.hasNext(); // 返回 false
     */
    static class Vector2D implements Iterator<Integer> {
        public int[][] vector;
        private int i, j;
        private int m, n;

        public Vector2D(int[][] vector) {
            this.vector = vector;
            m = vector.length;
        }

        @Override
        public boolean hasNext() {
            if (i >= m || i < 0) {
                return false;
            }
            return true;
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                return null;
            }
            int val = vector[i][j];
            j++;
            if (j >= vector[i].length) {
                i++;
                j = 0;
            }
            return val;
        }

    }

    static class MyVector2DIterator implements Iterable<Integer> {
        private int[][] verctor;

        public MyVector2DIterator(int[][] verctor) {
            this.verctor = verctor;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new Vector2D(verctor);
        }

    }

    /**
     * 顶端迭代器 leetcode 284
     * PeekingIterator peekingIterator = new PeekingIterator([1, 2, 3]); // [1,2,3]
     * peekingIterator.next(); // 返回 1 ，指针移动到下一个元素 [1,2,3]
     * peekingIterator.peek(); // 返回 2 ，指针未发生移动 [1,2,3]
     * peekingIterator.next(); // 返回 2 ，指针移动到下一个元素 [1,2,3]
     * peekingIterator.next(); // 返回 3 ，指针移动到下一个元素 [1,2,3]
     * peekingIterator.hasNext(); // 返回 False
     * 
     */
    static class PeekingIterator implements Iterator<Integer> {
        private Iterator<Integer> iterator;
        private Integer next;

        public PeekingIterator(Iterator<Integer> iterator) {
            // initialize any member here.
            if (iterator == null || !iterator.hasNext()) {
                throw new IllegalArgumentException("iterator is empty");
            }
            this.iterator = iterator;
            next = iterator.next();
        }

        // Returns the next element in the iteration without advancing the iterator.
        public Integer peek() {
            return next;
        }

        // hasNext() and next() should behave the same as in the Iterator interface.
        // Override them if needed.
        @Override
        public Integer next() {
            int val = next;
            if (iterator.hasNext()) {
                next = iterator.next();
            } else {
                next = null;
            }
            return val;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }
    }

    public static void main(String[] args) {
        int[][] vector = new int[][] { { 1, 2 }, { 3 }, { 4 } };
        // MyVector2DIterator instance = new MyVector2DIterator(vector);
        // for(int val : instance) {
        // System.out.print(val+" ");
        // }
        // System.out.println();
        // Vector2D iterator = new Vector2D(vector);
        // System.out.println(iterator.next()); // 返回 1
        // System.out.println(iterator.next()); // 返回 2
        // System.out.println(iterator.next()); // 返回 3
        // System.out.println(iterator.hasNext()); // 返回 true
        // System.out.println(iterator.hasNext()); // 返回 true
        // System.out.println(iterator.next()); // 返回 4
        // System.out.println(iterator.hasNext()); // 返回 false

        int[] arr = new int[] { 1, 2, 3 };
        List<Integer> list = new ArrayList<>();
        for(int val : arr) {
            list.add(val);
        }

        PeekingIterator peekingIterator = new PeekingIterator(list.iterator()); // [1,2,3]
        System.out.println(peekingIterator.next()); // 返回 1 ，指针移动到下一个元素 [1,2,3]
        System.out.println(peekingIterator.peek()); // 返回 2 ，指针未发生移动 [1,2,3]
        System.out.println(peekingIterator.next()); // 返回 2 ，指针移动到下一个元素 [1,2,3]
        System.out.println(peekingIterator.next()); // 返回 3 ，指针移动到下一个元素 [1,2,3]
        System.out.println(peekingIterator.hasNext()); // 返回 False
    }
}
