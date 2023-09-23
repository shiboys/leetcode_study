package org.swj.leet_code.data_structure_rewrite;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 自定义的 ArrayList
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/22 10:29
 */
public class MyArrayList<E> implements Iterable<E> {
    private static final int MAX_CAPACITY = Integer.MAX_VALUE - 8;
    private static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Object[] elements;

    public MyArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public void add(E item) {
        addLast(item);
    }

    /**
     * 在 index 位置增加元素，
     * 
     * @param item
     * @param index
     */
    public void add(E item, int index) {
        // 检查插入位置的正确性
        checkPositionIndex(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = item;
        size++;
    }

    // 没有编写 addAll 方法

    public void addLast(E item) {
        ensureCapacity(size + 1);
        elements[size++] = item;
    }

    public void addFirst(E item) {
        add(item, 0);
    }

    public E removeFirst() {
        return remove(0);
    }

    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("list is emtpy");
        }
        E last = (E) elements[size - 1];
        size--;
        elements[size] = null;
        return last;
    }

    public E remove(int index) {
        if (isEmpty()) {
            throw new NoSuchElementException("list is emtpy");
        }
        checkElementIndex(index);

        E item = (E) elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
        return item;
    }

    public E get(int index) {
        if (isEmpty()) {
            throw new NoSuchElementException("list is emtpy");
        }
        checkElementIndex(index);
        return (E) elements[index];
    }

    public E set(int index, E element) {
        E oldElement = get(index);
        elements[index] = element;
        return oldElement;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity(int requestCap) {
        if (requestCap < 0 || requestCap <= elements.length) {
            return;
        }
        int oldCap = elements.length;
        int newCap = oldCap + (oldCap >> 1);
        if (newCap < requestCap) {
            newCap = requestCap;
        }

        if (newCap > MAX_CAPACITY || newCap < 0) {
            newCap = MAX_CAPACITY;
        }
        elements = Arrays.copyOf(elements, newCap);
    }

    /**
     * 检查 index 位置是否可以存放元素
     * 
     * @param index
     * @return
     */
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index)) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index)) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int idx = 0;

            @Override
            public boolean hasNext() {
                return idx < size;
            }

            @Override
            public E next() {
                return (E) elements[idx++];
            }

        };
    }

}
