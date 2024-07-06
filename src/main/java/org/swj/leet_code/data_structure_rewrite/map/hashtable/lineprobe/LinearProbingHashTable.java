package org.swj.leet_code.data_structure_rewrite.map.hashtable.lineprobe;

import org.apache.commons.codec.digest.MurmurHash3;

import java.util.Iterator;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/05 15:33
 * 使用线性探查法的哈希表
 */
public class LinearProbingHashTable<K, V> {
  /**
   * 线性探查法的哈希表，仍然使用 Node<K,V> 封装，但是因为不再使用链表，全部是基于数组来实现
   * 因此就没有链表这个数据结构了
   */

  private static final int DEFAULT_CAPACITY = 16;
  private static final int MAX_CAPACITY = Integer.MAX_VALUE - DEFAULT_CAPACITY;

  private Node[] table;
  private int size;


  static class Node<K, V> {
    public K key;
    public V value;

    public Node(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }

  public LinearProbingHashTable(int size) {
    int len = upperBoundOfPower2(size);
    table = new Node[len];
  }

  public LinearProbingHashTable() {
    this(DEFAULT_CAPACITY);
  }

  private int upperBoundOfPower2(int size) {
    int n = size - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;

    return n > MAX_CAPACITY ? MAX_CAPACITY : (n < 1 ? 1 : n + 1);
  }

  /**
   * 普通的 JDK8 Hash 会造成比较大的 Hash 冲突，
   * 这里采用 Murmur Hash
   *
   * @param key
   * @return
   */
  int hash(K key) {
    return MurmurHash3.hash32(key.hashCode());
  }

  public V get(K key) {
    if (key == null) {
      throw new IllegalArgumentException("key is null");
    }
    int mask = table.length - 1;
    int idx = hash(key) & mask;
    Node<K, V> node = table[idx];
    if (node == null) {
      return null;
    }
    if (key.equals(node.key)) {
      return node.value;
    }
    // 没找到，开始线性探查
    int oldIdx = idx;
    while (node != null) {
      idx = (idx + 1) & mask;
      if (idx == oldIdx) { // 遍历了一周
        break;
      }
      node = table[idx];
      // 遇到 null 元素
      if (node == null) {
        return null;
      }

      if (key.equals(node.key)) {
        return node.value;
      }
    }
    return null;
  }

  public V put(K key, V value) {
    if (key == null) {
      throw new IllegalArgumentException("key is null");
    }
    int mask = table.length - 1;
    int idx = hash(key) & mask;
    Node<K, V> node = table[idx];
    if (node == null) {
      addNewNode(key, value, idx);
      return null;
    }
    if (key.equals(node.key)) {// 更新元素的值
      V oldValue = node.value;
      node.value = value;
      return oldValue;
    }
    // 开始线性探查
    int oldIdx = idx;
    while (node != null) {
      idx = (idx + 1) & mask;
      if (idx == oldIdx) {
        throw new IllegalStateException("the hashtable is full, can not offer any space for new element.");
      }
      node = table[idx];
      if (node != null && key.equals(node.key)) {// 找到相同元素
        V oldValue = node.value;
        node.value = value;
        return oldValue;
      }
    }
    // 找到位置
    addNewNode(key, value, idx);
    return null;
  }

  private void addNewNode(K key, V value, int idx) {
    if (ensureCapacity()) {
      idx = hash(key) & (table.length - 1);
    }
    table[idx] = new Node(key, value);
    size++;
  }

  boolean ensureCapacity() {
    // 达不到使用量的 2/3 则不扩容，否则需要扩容，要不然线性探查的冲突就会非常多
    // 2/3 这个值是参考 ThreadLocalMap 的线性探查设置
    if (size * 2 / 3 < table.length) {
      return false;
    }
    resize(table.length << 1);
    return true;
  }

  private void resize(int newSize) {
    Node[] newTable = new Node[newSize];
    int mask = newTable.length - 1;
    for (int i = 0; i < table.length; i++) {
      if (table[i] == null) {
        continue;
      }
      rehash(newTable, table[i], mask);
    }
    // rehash 完成
    this.table = newTable;
  }

  private void rehash(Node[] newTable, Node<K, V> node, int mask) {
    if (node == null) {
      return;
    }
    int idx = hash(node.key) & mask;
    if (newTable[idx] != null) {
      int oldIdx = idx;
      while (newTable[idx] != null) {
        idx = (idx + 1) & mask;
        if (idx == oldIdx) {
          throw new IllegalStateException("hash table is full when resizing.it should not be happen!");
        }
      }
    }
    newTable[idx] = node;
  }

  public int size() {
    return size;
  }

  public Iterable<K> keySet() {
    return () -> new KeyIterator(table);
  }

  // 最后一个最重要的方法，remove

  public V remove(K key) {
    if (key == null) {
      return null;
    }
    int idx = hash(key) & (table.length - 1);
    Node<K, V> node = table[idx];
    if (key.equals(node.key)) { // 找到了
      doRemove(idx);
      return node.value;
    } else {
      int oldIdx = idx;
      while ((node = table[idx]) != null) {
        if (node.key.equals(key)) {
          doRemove(idx);
          return node.value;
        }
        idx = (idx + 1) & (table.length - 1);
        if (idx == oldIdx) {
          break;
        }
      }
      // 没找到该 key
    }
    return null;
  }

  /**
   * 将 idx 到下一个 Null 元素之间的所有元素都重新 rehash
   *
   * @param idx
   */
  void doRemove(int idx) {
    table[idx] = null;
    idx = (idx + 1) & table.length - 1;
    Node<K, V> node = null;
    while (table[idx] != null) {
      node = table[idx];
      table[idx] = null;
      // 重新 放入哈希表中
      put(node.key, node.value);
      idx = (idx + 1) & table.length - 1;
    }
  }

  class KeyIterator implements Iterator<K> {

    private Node<K, V>[] arr;
    private int cursor;

    public KeyIterator(Node<K, V>[] array) {
      this.arr = array;
    }

    @Override
    public boolean hasNext() {
      while (cursor < arr.length) {
        if (arr[cursor] != null) {
          return true;
        }
        cursor++;
      }
      return false;
    }

    @Override
    public K next() {
      if (!hasNext()) {
        return null;
      }
      return arr[cursor].key;
    }
  }
}
