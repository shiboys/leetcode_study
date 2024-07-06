package org.swj.leet_code.data_structure_rewrite.map.hashtable;

import org.swj.leet_code.data_structure_rewrite.map.hashtable.linkedchain.HashLinkedList;
import org.swj.leet_code.data_structure_rewrite.map.hashtable.linkedchain.LinkedHashTable;

import java.lang.reflect.Field;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/05 18:03
 */
public class TestHashTable {

  static final int max = 33;

  public static void main(String[] args) throws Exception {
    int multi = 10;
    LinkedHashTable<Integer, Integer> hashTable = new LinkedHashTable<>();
    // 测试 put , 并测试 扩容
    for (int i = 1; i <= max; i++) {
      hashTable.put(i, i * multi);
    }

    //测试 get 和 迭代器
    iterateHashTable(hashTable);


    Field f = LinkedHashTable.class.getDeclaredField("table");
    f.setAccessible(true);
    HashLinkedList[] list = (HashLinkedList[]) f.get(hashTable);
    System.out.println("the base array length of hashtable is " + list.length);
    // 测试删除
    for(int i=1;i<=max-4;i++) {
      hashTable.remove(i);
    }
    System.out.println("after deleted " + (max-4) + " items");
    iterateHashTable(hashTable);
    list = (HashLinkedList[]) f.get(hashTable);
    System.out.println("the base array length of hashtable is " + list.length);
    // 缩容之后，重新遍历，测试迭代器和 get
    iterateHashTable(hashTable);
  }

  static void iterateHashTable(LinkedHashTable<Integer, Integer> hashTable) {
    int i = 0;
    for (Integer key : hashTable.keySet()) {
      if (i == 0) {
        System.out.println("first key is " + key + " ,value is " + hashTable.get(key));
      } else if (i == hashTable.size() - 1) {
        System.out.println("last key is " + key + " ,value is " + hashTable.get(key));
      } else {
        System.out.println("key is " + key);
      }
      i++;
    }
  }
}
