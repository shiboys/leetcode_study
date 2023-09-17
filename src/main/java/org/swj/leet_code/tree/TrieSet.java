package org.swj.leet_code.tree;

import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/13 20:47
 *        TrieTree 字符串前缀树的实现。Set 方式
 */
public class TrieSet {
    TrieTree<Object> tree = new TrieMap<>();

    public void put(String key) {
        tree.put(key, new Object());
    }

    public void remove(String key){
        tree.remove(key);
    }

    public int size() {
        return tree.size();
    }

    public List<String> keysWithPrefix(String prefix) {
        return tree.keysWithPrefix(prefix);
    }

    public List<String> keysWithPattern(String pattern) {
        return tree.keysWithPattern(pattern);
    }

    public boolean hasKeysWithPrefix(String prefix) {
        return tree.hasKeysWithPrefix(prefix);
    }

    public boolean hasKeysWithPattern(String pattern) {
        return tree.hasKeysWithPattern(pattern);
    }

    public String shortestPrefixOf(String query) {
        return tree.shortestPrefixOf(query);
    }
    
    public String longestPrefixOf(String query) {
        return tree.longestPrefixOf(query);
    }
}
