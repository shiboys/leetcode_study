package org.swj.leet_code.tree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/13 14:07
 *        TrieTree 字符串前缀树的实现
 */
public class TrieMap<V> implements TrieTree<V> {

    // 节点个数最多 256;
    private static final int E = 256;

    TrieNode<V> root;

    private int size;

    @Override
    public void put(String key, V val) {
        TrieNode<V> node = getNode(root, key);
        if (node != null && node.val != null) { // 表明 key 是 trie 树的一个节点，而不是前缀
            node.val = val;
        } else {
            root = addTrieNodes(root, key, val, 0);
            size++;
        }
    }

    TrieNode<V> addTrieNodes(TrieNode<V> node, String key, V val, int i) {
        if (node == null) {
            // 节点不存在则新建
            node = new TrieNode<>();
        }
        // 到达 key 最后一个节点
        if (i == key.length()) {
            node.val = val;
            // 返回 node，结束递归
            return node;
        }
        char c = key.charAt(i);
        // 递归插入子节点，并接收返回值
        node.children[c] = addTrieNodes(node.children[c], key, val, i + 1);

        return node;
    }

    /**
     * 删除节点，Trie 节点的删除在递归中是通过返回 null 来实现的
     */
    @Override
    public void remove(String key) {
        if (key == null || key.isEmpty()) {
            return;
        }
        root = remove(root, key, 0);
        size--;
    }

    TrieNode<V> remove(TrieNode<V> node, String key, int i) {
        if (node == null) {
            return null;
        }
        if (i == key.length()) {
            node.val = null;
        } else {
            // 通过递归的方式去子节点删除
            char c = key.charAt(i);
            node.children[c] = remove(node.children[c], key, i + 1);
        }
        // 后序遍历位置
        if (node.val != null) { // 递归回来路径上的父节点, 不能删除
            return node;
        }
        // 如果当前节点是更节点，即 当前节点的所有子节点都为 null ，则可以把父节点删除
        for (int s = 0; s < E; s++) {
            // 只要有一个不为 null ，则不能删除
            if (node.children[s] != null) {
                return node;
            }
        }
        // 删除 node 节点
        return null;
    }

    @Override
    public V get(String key) {
        TrieNode<V> node = getNode(root, key);
        // node 为null 或者 node.val 为 null，说明 key 没有对应的值。
        return node != null ? node.val : null;
    }

    /**
     * containsKey 需要注意 不能仅仅判断 getNode() != null ，还需要判断 node.val != null
     * 因为 仅仅是 node != null ，只能说这个前缀存在，但是这个前缀不一定是一个 key，
     * 如果是一个 key，必须判断加上 node.val != null
     */
    @Override
    public boolean containsKey(String key) {
        return get(key) != null;
    }

    TrieNode<V> getNode(TrieNode<V> root, String query) {
        return traverseTreeWithQuery(root, query, 0);
    }

    TrieNode<V> traverseTreeWithQuery(TrieNode<V> root, String query, int i) {
        if (root == null) {
            return null;
        }
        if (i == query.length()) {
            return root;
        }
        char c = query.charAt(i);
        return traverseTreeWithQuery(root.children[c], query, i + 1);
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        List<String> res = new LinkedList<>();
        TrieNode<V> node = getNode(root, prefix);
        if (node == null) {
            return res;
        }
        traverseWithPrefix(node, new StringBuilder(prefix), res);
        return res;
    }

    void traverseWithPrefix(TrieNode<V> node, StringBuilder path, List<String> res) {
        if (node == null) {
            // 到达 Trie 树的叶子结点
            return;
        }
        // 碰到一个匹配前缀的字符串
        if (node.val != null) {
            res.add(path.toString());
        }

        // 使用回溯法去暴力搜索
        for (int i = 0; i < E; i++) {
            // 做选择。这里感觉效率不高。最好是应该加个判断 if(node.children[i] != null) ，但是这里为了代码简洁，判断是在递归的前序做的。
            path.append((char) i);
            traverseWithPrefix(node.children[i], path, res);
            // 撤销选择
            path.deleteCharAt(path.length() - 1);
        }
    }

    /**
     * 这个方法的实现，刚好可以使用 getNode() !=null 来实现。
     * 
     */
    @Override
    public boolean hasKeysWithPrefix(String prefix) {
        return getNode(root, prefix) != null;
    }

    @Override
    public List<String> keysWithPattern(String pattern) {
        List<String> res = new LinkedList<>();
        traverseWithPattern(root, new StringBuilder(), res, pattern, 0);
        return res;
    }

    void traverseWithPattern(TrieNode<V> node, StringBuilder path, List<String> res, String pattern, int i) {
        if (node == null) {
            return;
        }
        if (i == pattern.length()) { // 匹配完成
            // 找到一个匹配这个模式的 key
            if (node.val != null) {
                res.add(path.toString());
            }
            return;
        }
        char c = pattern.charAt(i);
        if (c == '.') { // 匹配 . 操作。. 字符可以匹配任何字符。使用回溯算法
            for (int s = 0; s < E; s++) {
                path.append((char) i);
                traverseWithPattern(node.children[i], path, res, pattern, i + 1);
                path.deleteCharAt(path.length() - 1);
            }
        } else { // 匹配普通字符
            path.append(c);
            traverseWithPattern(node.children[c], path, res, pattern, i + 1);
            path.deleteCharAt(path.length() - 1);
        }
    }

    /**
     * 简单的实现就是 !keysWithPattern().isEmpty(), 但是这样的复杂度太高，
     * 只要有一个匹配就满足
     */
    @Override
    public boolean hasKeysWithPattern(String pattern) {
        return hasKeysWithPattern(root, pattern, 0);
    }

    private boolean hasKeysWithPattern(TrieNode<V> node, String pattern, int i) {
        if (node == null) {
            return false;
        }
        if (i == pattern.length()) {
            return node.val != null;
        }
        char c = pattern.charAt(i);
        if (c != '.') {
            return hasKeysWithPattern(node.children[c], pattern, i + 1);
        } else {
            // 匹配 . 字符，任何一个字符匹配都可以
            for (int s = 0; s < E; s++) { // 任何一个匹配都可以返回 true
                if (hasKeysWithPattern(node.children[s], pattern, i + 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String shortestPrefixOf(String query) {
        return shortOrLongPrefixOf(root, query, true);
    }

    String shortOrLongPrefixOf(TrieNode<V> node, String query, boolean shortOrLong) {
        if (node == null) {
            return null;
        }
        int llen = 0, i = 0;
        for (int len = query.length(); i < len; i++) {
            if (node == null) {
                break;
            }
            if (node.val != null) {
                if (shortOrLong) { // short
                    return query.substring(0, i);
                }
                llen = i;
            }
            char c = query.charAt(i);
            node = node.children[c];
        }
        // query 本身就是 key
        if (i == query.length() && node.val != null) {
            return query;
        }
        // 如果 求最长字符串
        if (!shortOrLong && llen > 0) {
            return query.substring(0, llen);
        }
        return null;
    }

    @Override
    public String longestPrefixOf(String query) {
        return shortOrLongPrefixOf(root, query, false);
    }

    public static void main(String[] args) {
        TrieTree<Integer> tree = new TrieMap<>();
        int i = 1;
        String key = "hello";
        tree.put(key, i++);

        tree.put("hellx", i++);

        if (tree.containsKey(key)) {
            System.out.println(tree.get(key));
        } else {
            System.out.println("not exists");
        }
        List<String> keys = tree.keysWithPrefix("hell");
        System.out.println(keys);
        // keysWithPattern

        // shortestPrefix, longestPrefix
        tree.put("the", i++);
        tree.put("them", i++);

        String query = "themxyz";
        System.out.println(tree.shortestPrefixOf(query));
        System.out.println(tree.longestPrefixOf(query));

        // 测试 query 本身就是 key 的情况
        query = "hello";
        System.out.println(tree.shortestPrefixOf(query));
        System.out.println(tree.longestPrefixOf(query));
        // remove
        tree.remove(query);
        System.out.println(tree.get(query));
        System.out.println(tree.get("hellx"));

        String[] ss = new String[] { "the", "cat", "was", "rat" };
        List<String> strList = new LinkedList<>(Arrays.asList(ss));
        System.out.println(Arrays.toString(ss));
        // strList.set(i, query)
        String.join(" ", ss);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * leetcode 1804 题，实现前缀树
     * 1804 题需要 vip 权限，这里就手工写出来
     */
    static class TestTrieTreeLeetcode1084 {
        TrieMap<Integer> tree = new TrieMap<>();

        public void insert(String word) {
            Integer val = tree.get(word);
            if (val == null) {
                tree.put(word, 1);
            } else {
                tree.put(word, val + 1);
            }
        }

        public int countWordsEqualTo(String word) {
            Integer val = tree.get(word);
            return val == null ? 0 : val.intValue();
        }

        public int countWordsStaringWith(String prefix) {
            List<String> keys = tree.keysWithPrefix(prefix);
            int sum = 0;
            for (String key : keys) {
                Integer val = tree.get(key);
                sum += val == null ? 0 : val.intValue();
            }
            return sum;
        }

        private void erase(String word) {
            Integer val = tree.get(word);
            if (val == null) {
                return;
            } else if (val - 1 == 0) {
                tree.remove(word);
            } else {
                tree.put(word, val - 1);
            }
        }

    }
}
