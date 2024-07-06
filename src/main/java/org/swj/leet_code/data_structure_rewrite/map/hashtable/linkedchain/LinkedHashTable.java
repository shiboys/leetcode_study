package org.swj.leet_code.data_structure_rewrite.map.hashtable.linkedchain;

import java.util.Iterator;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/05 15:03
 * 实现基于链表+数组的哈希表, 使用拉链法解决哈希冲突
 * 属于轻量级的哈希表，没有对拉链法使用的链表转化为红黑树
 */
public class LinkedHashTable<K, V> {
  // 采用 jdk 同样的数组大小
  private static final int DEFAULT_CAPACITY = 4;
  private static final int MAX_CAPACITY = Integer.MAX_VALUE - DEFAULT_CAPACITY;

  private HashLinkedList<K, V>[] table;
  private int size;

  public LinkedHashTable(int size) {
    size = ensurePowerOf2(size);
    table = new HashLinkedList[size];
  }

  public LinkedHashTable() {
    this(DEFAULT_CAPACITY);
  }

  private int ensurePowerOf2(int size) {
    int n = size - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    // n 最终的结果是 0000...111111... 这样的数组，n+1 实现 2 的幂次方
    return n >= MAX_CAPACITY ? MAX_CAPACITY : (n < 1 ? 1 : n + 1);
  }

  /**
   * 使用 Hash 函数计算 key 的 哈希值，使用 JDK7 的扰动方式，并没有特别复杂的哈希计算方式
   *
   * @param key key
   * @return hash value
   */
  private int hash(K key) {
    int h = key.hashCode();
    h ^= h >>> 20;
    h ^= h >>> 12;
    h ^= h >>> 7;
    h ^= h >>> 4;
    return h;
  }

  // contains， get ,put ,remove, ensureCapacity(), resize, size(), keys()

  public boolean containsKey(K key) {
    int idx = hash(key) & (table.length - 1);
    if (table[idx] == null) {
      return false;
    }
    return table[idx].containsKey(key);
  }

  public int size() {
    return size;
  }

  public V get(K key) {
    int idx = hash(key) & (table.length - 1);
    if (table[idx] == null) {
      return null;
    }
    return table[idx].get(key);
  }

  public V put(K key, V value) {
    if (key == null) {
      throw new IllegalArgumentException("key is null");
    }
    int idx = hash(key) & (table.length - 1);
    HashLinkedList<K, V> linkedList = table[idx];
    if (linkedList != null && linkedList.containsKey(key)) { // 只是替换
      return linkedList.put(key, value);
    }

    // 新增节点，先进行容量确认
    if (ensureCapacity()) { // 扩容成功，则更新引用
      idx = hash(key) & (table.length - 1);
      linkedList = table[idx];
    }
    if (linkedList == null) {
      linkedList = table[idx] = new HashLinkedList<>();
    }
    linkedList.put(key, value);
    size++;
    return null;
  }

  private boolean ensureCapacity() {
    if (size < table.length * 8) { // 链表节点的长度均值为 8，大于 8 则进行扩容
      return false;
    }
    int len = ensurePowerOf2(table.length << 1);
    resize(len);
    return true;
  }

  private void resize(int newSize) {
    HashLinkedList<K, V>[] newTable = new HashLinkedList[newSize];
    int idx = -1, mask = newTable.length - 1;
    HashLinkedList<K, V> currList = null;
    for (HashLinkedList<K, V> linkedList : table) {
      if (linkedList == null) {
        continue;
      }
      for (HashLinkedList.Node<K, V> node : linkedList.entrySet()) {
        if (node == null) {
          continue;
        }
        currList = newTable[(idx = hash(node.key) & mask)];
        if (currList == null) {
          newTable[idx] = new HashLinkedList<>(node.key, node.value);
        } else {
          currList.put(node.key, node.value);
        }
      }
    }
    // resize 完成, table 指向新的数组
    this.table = newTable;
  }

  public V remove(K key) {
    if (key == null) {
      throw new IllegalArgumentException("the key to be deleted is null");
    }
    int idx = hash(key) & (table.length - 1);
    HashLinkedList<K, V> linkedList = null;
    if ((linkedList = table[idx]) == null) { // slot 不存在
      return null;
    }
    size -= linkedList.size();
    V val = linkedList.remove(key);
    size += linkedList.size();
    // 因为使用了链表，load factor 本来就 > 1 了，所以这里使用 length/2 就非常合适了
    if (size == table.length / 2 && size > 1) {
      resize(table.length / 2);
    }
    return val;
  }

  public Iterable<K> keySet() {
    return () -> new TableKeyIterator(table);
  }

  class TableKeyIterator implements Iterator<K> {

    private final HashLinkedList<K, V>[] arr;
    private Iterator<K> it;
    private int idx = 0;

    public TableKeyIterator(HashLinkedList<K, V>[] array) {
      this.arr = array;
      if (this.arr != null) {
        if (arr[idx] != null) {
          this.it = arr[idx].keySet().iterator();
        }
      }
    }

    @Override
    public boolean hasNext() {
      if (this.arr == null) {
        return false;
      }
      while (idx < arr.length && (it == null || !it.hasNext())) {
        idx++;
        if (idx < arr.length) {
          if (arr[idx] == null) {
            continue;
          }
          it = arr[idx].keySet().iterator();
        }
      }
      return it != null && it.hasNext();
    }

    @Override
    public K next() {
      if (!hasNext()) {
        return null;
      }
      return it.next();
    }
  }


}
