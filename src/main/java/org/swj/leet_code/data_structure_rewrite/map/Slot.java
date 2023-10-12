package org.swj.leet_code.data_structure_rewrite.map;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/28 16:02
 *        拉链法实现 Map 功能的链表结构
 *        相当于 Map.Entry<K,V>
 */
public class Slot<K, V> {

    private int size;
    private Node<K, V> head, tail;

    public Slot() {
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
    }

    boolean containsKey(K key) {
        if (isEmpty()) {
            return false;
        }
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        return getNode(key) != null;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        Node<K, V> node = getNode(key);
        if (node == null) {
            return null;
        }
        return node.val;
    }

    public V put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        Node<K, V> node = getNode(key);
        if (node == null) {
            // 新节点，采用头插法的方式
            Node<K, V> next = head.next;
            Node<K, V> newNode = new Node<>(key, val);
            head.next = newNode;
            newNode.next = next;

            size++;
            return null;
        } else {
            V oldVal = node.val;
            node.val = val;
            return oldVal;
        }
    }

    V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        Node<K, V> prev = head;
        Node<K, V> node = head.next;
        while (node != tail) {
            if (Objects.equal(key, node.key)) {
                V oldVal = node.val;
                Node<K, V> next = node.next;
                prev.next = next;
                node.next = null;
                return oldVal;
            }
            prev = node;
            node = node.next;
        }
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> node = head.next;
        while (node != tail) {
            if (Objects.equal(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    public List<Map.Entry<K, V>> entries() {
        List<Map.Entry<K, V>> entries = new LinkedList<>();
        Node<K, V> node = head.next;
        while (node != tail) {
            entries.add(new SlotEntry<>(node));
            node = node.next;
        }
        return entries;
    }

    static class SlotEntry<K, V> implements Map.Entry<K, V> {

        final Node<K, V> node;

        public SlotEntry(Node<K, V> node) {
            this.node = node;
        }

        @Override
        public K getKey() {
            return node.key;
        }

        @Override
        public V getValue() {
            return node.val;
        }

        @Override
        public V setValue(V value) {
            V oldVal = node.val;
            node.val = value;
            return oldVal;
        }
    }

    static class Node<K, V> {
        public K key;
        public V val;
        public Node<K, V> next;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }

        public Node() {

        }
    }
}
