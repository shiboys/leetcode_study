package org.swj.leet_code.data_structure_rewrite;

import java.util.NoSuchElementException;

/**
 * 使用数组实现 Deque 的功能，且不再进行元素搬运
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/22 10:29
 */
public class MyArrayDeque<E> {
    private static final int DEFAULT_CAPACITY = 2;
    private int size;
    private E[] elements;
    // 队列的大小范围是 [head,tail), 左闭右开的区间
    private int head, tail;

    public MyArrayDeque(int cap) {
        if (cap < DEFAULT_CAPACITY) {
            cap = DEFAULT_CAPACITY;
        }
        elements = (E[]) new Object[cap];
        head = tail = 0;
    }

    public MyArrayDeque() {
        this(DEFAULT_CAPACITY);
    }

    public void addFirst(E e) {
        if (size == elements.length) {
            resize(size * 2);
        }
        if (head == 0) {// head指针循环使用
            head = elements.length - 1;
        } else {
            head--;
        }
        elements[head] = e;
        size++;
    }

    public void addLast(E e) {
        if (size == elements.length) {
            resize(size * 2);
        }

        elements[tail] = e;
        // 闭区间
        tail++;
        if (tail >= elements.length) {
            tail = 0;
        }

        size++;
    }

    public E removeFirst() {
        checkSize();
        E first = elements[head];
        // 先 remove 再操作指针
        elements[head] = null;
        if (head == elements.length - 1) {
            head = 0;
        } else {
            head++;
        }
        size--;
        return first;
    }

    public E removeLast() {
        checkSize();
        // 先操作指针，然后再remove
        if (tail == 0) {
            tail = elements.length - 1;
        } else {
            tail--;
        }
        E oldItem = elements[tail];
        elements[tail] = null;
        size--;
        return oldItem;
    }

    public E getFirst() {
        checkSize();
        return (E) elements[head];
    }

    public E getLast() {
        checkSize();
        return (E) elements[tail];
    }

    void checkSize() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty!");
        }
    }

    boolean isEmpty() {
        return size == 0;
    }

    /**
     * 扩容或者缩容
     * 
     * @param newSize
     */
    private void resize(int newSize) {
        if (newSize < size) {
            throw new IllegalArgumentException("newSize is smaller than size. size=" + size + ", newSize=" + newSize);
        }
        E[] newArray = (E[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            newArray[i] = elements[(head + i) % elements.length];
        }
        elements = newArray;
        head = 0;
        tail = size;
    }

    public static void main(String[] args) {
        // 用 MyDeque 实现栈和队列的需求
        MyArrayDeque<Integer> stack = new MyArrayDeque<>();
        MyArrayDeque<Integer> queue = new MyArrayDeque<>();

        for (int i = 0; i < 10; i++) {
            stack.addLast(i);
            queue.addLast(i);
        }
        while (!stack.isEmpty()) {
            System.out.print(stack.removeLast() + " ");
        }
        System.out.println();

        while (!queue.isEmpty()) {
            System.out.print(queue.removeFirst() + " ");
        }
        System.out.println();

    }

}
