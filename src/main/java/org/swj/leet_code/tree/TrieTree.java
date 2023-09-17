package org.swj.leet_code.tree;

import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/13 11:07
 * TrieTree 字符串前缀树
 */
public interface TrieTree<V> {
    // put,get, containsKey,remove,size
    //  keysWithPrefix, keysWithPattern, hasKeysWithPrefix, hasKeysWithPattern
    // shortestPrefixOf(query), longestPrefixOf(query)

    // 在 Map 中添加 Key
    void put(String key,V val);

    // 删除键 key 以及其对应的值
    void remove(String key);

    // 搜索 key 对应的值，不存在则返回 null。
    V get(String key);

    // 判断 key 是否存在 Trie 树中
    boolean containsKey(String key);

    /**
     * TrieTree 中的键值对
     * @return
     */
    int size();

    /**
     * 搜索前缀为 prefix 的所有键
     * @param prefix
     * @return
     */
    List<String> keysWithPrefix(String prefix);

    /**
     * 是否存在前缀为 prefix 的键
     * @param prefix
     * @return
     */
    boolean hasKeysWithPrefix(String prefix);

    /**
     * 返回指定符合指定 pattern 的所有key
     * @param pattern pattern 目前进支持简单的 . 字符匹配
     * @return
     */
    List<String> keysWithPattern(String pattern);

    /**
     * 是否存在符合指定 pattern 的键
     * @param pattern
     * @return
     */
    boolean hasKeysWithPattern(String pattern);

    /**
     * 在 Trie 树中搜索 query 的最短前缀。比如 shortestPrefixOF("themxyz") -> "the"
     * @param query
     * @return
     */
    String shortestPrefixOf(String query);

    /**
     * 在 Trie 树中搜索匹配 query 的最长 key。比如 longestPrefixOF("themxyz") -> "them"
     * @param query
     * @return
     */
    String longestPrefixOf(String query);
}
