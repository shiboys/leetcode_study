package org.swj.leet_code.data_structure_rewrite.map.hashtable.linkedchain;

import java.util.Iterator;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/05 12:36
 * 拉链法的 hash 链表节点
 */
public class HashLinkedList<K, V> {

  Node<K, V> head;
  private int size;


  protected final static class Node<K, V> {
    public K key;
    public V value;
    public Node<K, V> next;

    public Node(K key, V value) {
      this(key, value, null);
    }

    public Node(K key, V value, Node<K, V> next) {
      this.key = key;
      this.value = value;
      if (next != null) {
        this.next = next;
      }
    }
  }


  public HashLinkedList() {

  }

  public HashLinkedList(K key, V value) {
    head = new Node<>(key, value);
    size=1;
  }

  public int size() {
    return size;
  }

  public Iterable<K> keySet() {
    final Node<K, V>[] p = new Node[] {head};
    return () -> new Iterator<K>() {

      @Override
      public boolean hasNext() {
        return p[0] != null;
      }

      @Override
      public K next() {
        K key = p[0].key;
        p[0] = p[0].next;
        return key;
      }
    };
  }

  public Iterable<Node<K, V>> entrySet() {
    return () -> new NodeIterator(head);
  }


  public boolean containsKey(K key) {
    return get(key) != null;
  }

  // 拉链法查找 key == key 的值
  public V get(K key) {
    Node<K, V> node = getNode(key);
    return node != null ? node.value : null;
  }


  private Node<K, V> getNode(K key) {
    if (key == null) {
      return null;
    }
    Node<K, V> p = head;
    while (p != null) {
      if (p.key.equals(key)) {
        return p;
      }
      p = p.next;
    }
    return null;
  }

  public V put(K key, V value) {
    Node<K, V> existingNode = getNode(key);
    if (existingNode != null) { // 现存的节点
      V oldVal = existingNode.value;
      existingNode.value = value;
      return oldVal;
    } else { // 新增节点
      // 采用头插法
      head = new Node<>(key, value, head);
      size++;
      return null;
    }
  }

  public V remove(K key) {
    Node<K, V> p = head;
    Node<K, V> prev = null;
    while (p != null) {
      if (p.key.equals(key)) {
        if (prev == null) {
          head = p.next;
        } else {
          prev.next = p.next;
        }
        p.next = null;
        size--;
        return p.value;
      }
      prev = p;
      p = p.next;
    }
    return null;
  }



  class NodeIterator implements Iterator<Node<K, V>> {

    private Node<K, V> cursor;

    public NodeIterator(Node<K, V> head) {
      this.cursor = head;
    }

    @Override
    public boolean hasNext() {
      return cursor != null;
    }

    @Override
    public Node<K, V> next() {
      if (!hasNext()) {
        return null;
      }
      Node<K, V> newNode = new Node<>(cursor.key, cursor.value);
      cursor = cursor.next;
      return newNode;
    }

    // 这个迭代器不支持删除，想要删除节点，可以通过 remove 方法
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

}
