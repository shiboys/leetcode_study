package org.swj.leet_code.tree;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/13 18:47
 */
public class TrieTreeLeetCode {
  static final int E = 2;
  private int maxSum = Integer.MIN_VALUE;
  TrieTree<Boolean> tree;

  // 这个解法是错误的，正确的在 leetcode lrc67
  public int findMaximumXOR(int[] nums) {
    tree = new TrieTree<>();
    for (int num : nums) {
      tree.put(num, true);
    }
    return maxSum;
  }

  class TrieTree<V> {
    TrieNode<V> root;
    private int maxCounter = 0;
    private int size;

    public void put(int key, V value) {
      int v = key, r, sum = 0, counter = 0;

      if (root == null) {
        root = new TrieNode<>();
      }
      TrieNode node = root, prevNode = null;
      while (v != 0) {
        r = v & 1;
        if (node.children[r] == null) {
          node.children[r] = new TrieNode<>();
          if (size > 0) {
            sum = sum | (1 << counter);
          }
        } else {
          //sum = sum << 1;
        }

        prevNode = node;
        node = node.children[r];

        v = v >> 1;

        counter++;
      }
      if (prevNode != null) {
        prevNode.value = value;
      }

      if (counter < maxCounter) {
        // 当前 value 已经被左移成为 0，但是前缀树还有其他数字没有遍历完，
        // value 此时的 bit 全部为 0，跟其他前缀树的bit 进行比对，比如 [8,10,2] case, 8 xor 2 == 10
        // 2 先被左移为 0 ，剩下的需要跟 8 跟 10的 bit 位， 需要 跟 0 进行异或
        while (counter < maxCounter && node != null) {
          // 剩下的 bit 位只有1 的情况下，才有意义
          if (node.children[1] != null) {
            sum = sum | (1 << counter);
            node = node.children[1];
          } else {
            node = node.children[0];
          }

          counter++;
        }
      } else {
        maxCounter = counter;
      }
      maxSum = Math.max(sum, maxSum);
      size++;
    }
  }


  class TrieNode<V> {
    public V value;
    public TrieNode[] children;

    public TrieNode() {
      children = new TrieNode[E];
    }

    public TrieNode(V value) {
      this.value = value;
      children = new TrieNode[E];
    }
  }

  public static void main(String[] args) {
    String[] dictionary = new String[] {
        "a", "b", "ab", "abc", "abcabacbababdbadbfaejfoiawfjaojfaojefaowjfoawjfoawj", "abcdefghijawefe",
        "aefawoifjowajfowafjeoawjfaow", "cba", "cas", "aaewfawi", "babcda", "bcd", "awefj"
    };
    //int[] arr = new int[] {14,70,53,83,49,91,36,80,92,51,66,70};
    int[] arr = new int[] {49, 91, 36, 80};
    int len = arr.length;
    int max = 0;
    TrieTreeLeetCode instance = new TrieTreeLeetCode();
    for (int i = 0; i < len; i++) {

      for (int j = i + 1; j < len; j++) {
        if ((arr[i] ^ arr[j]) == 127 || (arr[i] ^ arr[j]) == 126) {
          System.out.println(arr[i] + "," + arr[j]);
        }
      }
    }
    max = instance.findMaximumXOR(arr);

    System.out.println(max);
  }
}
