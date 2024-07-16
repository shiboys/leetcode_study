package org.swj.leet_code.tree;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/13 18:47
 */
public class TrieTreeLeetCode {
  static final int E = 26;
  static final char a = 'a';
  final TrieTree<String> tree;

  public TrieTreeLeetCode() {
    tree = new TrieTree<>();
  }

  public void buildDict(String[] dictionary) {
    for (String dict : dictionary) {
      tree.put(dict, dict);
    }
  }

  public boolean search(String searchWord) {
    return tree.search(searchWord, 1);
  }

  static class TrieTree<V> {
    TrieNode<V> root;

    public void put(String key, V value) {
      if (root == null) {
        root = new TrieNode();
      }
      TrieNode p = root;
      char ch;
      for (int i = 0, len = key.length(); i < len; i++) {
        ch = key.charAt(i);
        if (p.children[ch - a] == null) {
          p.children[ch - a] = new TrieNode();
        }
        if (i == len - 1) {
          p.children[ch - a].value = value;
        }
        p = p.children[ch - a];
      }
    }

    /**
     * k 是变更字符串的次数
     */
    public boolean search(String searchWord, int k) {
      if (searchWord == null || searchWord.isEmpty()) {
        return false;
      }
      TrieNode node = getNode(root, searchWord, 0, k);

      return node != null && node.value != null;
    }

    TrieNode getNode(TrieNode node, String key, int i, int changeCount) {
      if (key == null || key.isEmpty()) {
        return null;
      }
      if (node == null) {
        return null;
      }
      if (changeCount < 0) {
        return null;
      }
      if (i == key.length()) {
        if (changeCount == 0 && node.value != null) {
          return node;
        }
        return null;
      }
      char ch = key.charAt(i);
      // 优先找不匹配的
      TrieNode resNode = null;
      for (int j = 0; j < E; j++) {
        if (node.children[j] == null) {
          continue;
        }
        if (ch == a + j) { // 匹配字符
          resNode = getNode(node.children[j], key, i + 1, changeCount);
        } else {
          // 不匹配字符
          resNode = getNode(node.children[j], key, i + 1, changeCount - 1);
        }
        if (key.equals("bcb")) {
          if (resNode != null) {
            System.out.println("resNode is not null and char is " + (char) (a + j));
          }

        }
        if (resNode != null) { // 找到完全匹配字符串了
          return resNode;
        }
      }

      return null;
    }
  }


  static class TrieNode<V> {
    public V value;
    public TrieNode<V>[] children;

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
    TrieTreeLeetCode instance = new TrieTreeLeetCode();
    instance.buildDict(dictionary);
    String keyWord = "bcb";
    boolean result = instance.search(keyWord);
    System.out.println(result);
  }
}
