package org.swj.leet_code.data_structure_rewrite;

import java.util.NoSuchElementException;

/**
 * 设计循环双端队列
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/08 16:29
 */
public class MyArrayCycleDeque<E> {
    private static final int DEFAULT_CAPACITY = 2;
    int size;
    int first, last;
    E[] elements;

    public MyArrayCycleDeque(int cap) {
        if (cap < 1) {
            throw new IllegalArgumentException("capacity must be greater than 0");
        }
        elements = (E[]) new Object[cap];
    }

    public MyArrayCycleDeque() {
        this(DEFAULT_CAPACITY);
    }

    public void addFirst(E e) {
        if (size == elements.length) {
            resize(size * 2);
        }
        if (first == 0) {
            first = elements.length - 1;
        } else {
            first--;
        }
        elements[first] = e;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        // 缩容的判断要放在操作之前
        if (size == elements.length / 4) {
            resize(elements.length / 2);
        }
        E e = elements[first];
        elements[first] = null;
        first++;
        if (first == elements.length) {
            first = 0;
        }
        size--;

        return e;
    }

    public void addLast(E e) {
        if (size == elements.length) {
            resize(size * 2);
        }
        elements[last] = e;
        last++;
        if (last == elements.length) {
            last = 0;
        }
        size++;
    }

    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (size == elements.length / 4) {
            resize(elements.length / 2);
        }
        if (last == 0) {
            last = elements.length - 1;
        } else {
            last--;
        }
        E e = elements[last];
        elements[last] = null;
        size--;
        return e;
    }

    public E peekFirst() {
        if (isEmpty()) {
            return null;
        }
        return elements[first];
    }

    public E peekLast() {
        if (isEmpty()) {
            return null;
        }
        if (last == 0) {
            return elements[elements.length - 1];
        }
        return elements[last - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    void resize(int newCapacity) {
        E[] newArr = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            // 这里的取余不能是 size，否则会有大 bug
            newArr[i] = elements[(first + i) % elements.length];
        }
        elements = newArr;
        first = 0;
        last = size;
    }
}

class MyCircularDeque {
    MyArrayCycleDeque<Integer> deque;
    final int maxCap;

    public MyCircularDeque(int k) {
        deque = new MyArrayCycleDeque<>(k);
        maxCap = k;
    }

    public boolean insertFront(int value) {
        if (isFull()) {
            return false;
        }
        deque.addFirst(value);
        return true;
    }

    public boolean insertLast(int value) {
        if (isFull()) {
            return false;
        }
        deque.addLast(value);
        return true;
    }

    public boolean deleteFront() {
        if (isEmpty()) {
            return false;
        }
        deque.removeFirst();
        return true;
    }

    public boolean deleteLast() {
        if (isEmpty()) {
            return false;
        }
        deque.removeLast();
        return true;
    }

    public int getFront() {
        if (isEmpty()) {
            return -1;
        }
        return deque.peekFirst();
    }

    public int getRear() {
        if (isEmpty()) {
            return -1;
        }
        return deque.peekLast();
    }

    public boolean isEmpty() {
        return deque.isEmpty();
    }

    public boolean isFull() {
        return deque.size() == maxCap;
    }

    public static void main(String[] args) {
        MyCircularDeque instance = new MyCircularDeque(71);
        instance.insertFront(47);
        instance.deleteLast();
        instance.deleteLast();
        instance.insertLast(66);
        System.out.println(instance.getRear());
        instance.insertLast(72);
        System.out.println(instance.isFull());
        System.out.println(instance.getRear());
        System.out.println(instance.isFull());
        instance.deleteLast();

        System.out.println(instance.isEmpty());
        instance.insertFront(53);
        instance.isEmpty();
        instance.insertLast(15);
        System.out.println(instance.getRear());
    }
}
