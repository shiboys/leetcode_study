package org.swj.leet_code.data_structure_rewrite;

/**
 * 用数组实现的循环队列功能
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/08 10:29
 */
public class MyArrayCycleQueue<E> {
    private static final int DEFAULT_CAPACITY = 2;
    private int size;
    private E[] elements;
    private int first, last;

    public MyArrayCycleQueue(int cap) {
        elements = (E[]) new Object[cap];
    }

    public MyArrayCycleQueue() {
        this(DEFAULT_CAPACITY);
    }

    public void addLast(E e) {
        if (isFull()) {
            resize(size * 2);
        }
        elements[last] = e;
        last++;
        if (last == elements.length) {
            last = 0;
        }
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        E e = elements[first];
        elements[first] = null;
        first++;
        if (first == elements.length) {
            first = 0;
        }
        size--;
        if (size <= elements.length / 4) {
            resize(elements.length / 2);
        }
        return e;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == elements.length;
    }

    public int size() {
        return size;
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

    void resize(int newCapacity) {
        E[] newArr = (E[]) new Object[newCapacity];
        // 将原数组的元素，转移到新数组中
        // 因为是循环数组，因此没有办法使用 System.arrayCopy(), 只能一个一个元素拷贝
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[(first + i) % elements.length];
        }
        first = 0;
        last = size;
        elements = newArr;
    }
}

/**
 * leecode 622 设计循环队列
 */
class MyCircularQueue {

    MyArrayCycleQueue<Integer> queue;
    private int maxCapacity;

    public MyCircularQueue(int k) {
        maxCapacity = k;
        queue = new MyArrayCycleQueue<>(k);
    }

    public boolean enQueue(int value) {
        if (isFull()) {
            return false;
        }
        queue.addLast(value);
        return true;
    }

    public boolean deQueue() {
        if (isEmpty()) {
            return false;
        }
        queue.removeFirst();
        return true;
    }

    public int Front() {
        if (isEmpty()) {
            return -1;
        }
        return queue.peekFirst();
    }

    public int Rear() {
        if (isEmpty()) {
            return -1;
        }
        return queue.peekLast();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean isFull() {
        return queue.size() == maxCapacity;
    }
}