package org.swj.leet_code.data_structure_rewrite;

import java.security.interfaces.ECKey;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 自定义的双链表结构的 List
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/22 11:29
 */
public class MyLinkedList<E> implements Iterable<E> {
    private int size;
    // 使用 dummy 的头尾结点的双链表结构，相比 JDK 的 LinkedList 空间上多了 2 个节点的内存，但是操作上便利了很多。
    Node<E> head;
    Node<E> tail;

    public MyLinkedList() {
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.prev = head;
    }

    static class Node<E> {
        E val;
        Node<E> prev;
        Node<E> next;

        public Node(E e) {
            this.val = e;
        }

        public Node() {

        }
    }

    public void add(E e) {
        addLast(e);
    }

    public void add(E e, int index) {
        Node<E> node = getNode(index);
        Node<E> prev = node.prev;
        Node<E> newNode = new Node<>(e);
        prev.next = newNode;
        newNode.prev = prev;
        newNode.next = node;
        node.prev = newNode;
        size++;
    }

    public void addLast(E e) {
        // 尾插法
        Node<E> prev = tail.prev;
        Node<E> x = new Node<>(e);
        prev.next = x;
        tail.prev = x;

        x.next = tail;
        x.prev = prev;

        size++;
    }

    public void addFirst(E e) {
        // 头插法
        Node<E> next = head.next;
        Node<E> node = new Node<>(e);
        head.next = node;
        next.prev = node;

        node.next = next;
        node.prev = head;

        size++;
    }

    public E removeFirst() {
        checkEmpty();
        Node<E> first = head.next;
        Node<E> next = first.next;
        head.next = next;
        next.prev = head;

        first.next = first.prev = null;

        size--;

        return first.val;
    }

    public E removeLast() {
        checkEmpty();
        Node<E> node = tail.prev;
        Node<E> prev = node.prev;

        prev.next = tail;
        tail.prev = prev;

        node.next = node.prev = null;
        size--;
        return node.val;
    }

    public E remove(int index) {
        Node<E> node = getNode(index);
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        prev.next = next;
        next.prev = prev;

        node.next = node.prev = null;
        size--;

        return node.val;
    }

    public void set(int index, E e) {
        Node<E> node = getNode(index);
        if (node != null) {
            node.val = e;
        }
    }

    public E get(int index) {
        Node<E> node = getNode(index);
        return node != null ? node.val : null;
    }

    public Node<E> getNode(int index) {
        checkElementIndex(index);

        Node<E> p = null;
        int idx = 0;
        if (index < size / 2) { // index 在前半段
            p = head.next;
            while (idx < index) {
                p = p.next;
                idx++;
            }
        } else { // idx 在后半段
            index = size - index - 1;
            p = tail.prev;
            while (idx < index) {
                p = p.prev;
                idx++;
            }
        }
        return p;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("list is emtpy");
        }
    }

    public void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> p = head.next;

            @Override
            public boolean hasNext() {
                return p != tail;
            }

            @Override
            public E next() {
                if (hasNext()) {
                    E e = p.val;
                    p = p.next;
                    return e;
                }
                return null;
            }
        };
    }

    public static void main(String[] args) {
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 1; i <= 20; i++) {
            list.add(i * 2);
        }
        printList(list);

        list.remove(10);
        printList(list);

        MyLinkedList<Integer> list2 = new MyLinkedList<>();
        for (int i = 1; i <= 20; i++) {
            list2.add(i * 2);
        }
        printList(list2);
        list2.remove(15);
        printList(list2);

    }

    static <T> void printList(Iterable<T> items) {
        for (T val : items) {
            System.out.print(val + " ");
        }
        System.out.println();
    }
}
