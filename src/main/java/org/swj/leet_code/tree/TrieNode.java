package org.swj.leet_code.tree;

public class TrieNode<V> {
    public V val;
    public TrieNode<V>[] children = new TrieNode[256];
}
