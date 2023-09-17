package org.swj.leet_code.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/11 12:07
 *        不借助于 LinkedHashMap 手动实现 LRU。
 *        leetcode 146 题
 */
public class CustomLRU {

    interface LruCache {
        void put(int key, int val);

        int get(int key);
    }

    static class Pair<K, V> {
        K key;
        V val;

        public Pair(K key, V val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public String toString() {
            return "(" + key + "," + val + ")";
        }

    }

    static class LruCacheImpl implements LruCache, Iterable<Pair<Integer, Integer>> {

        private int cap;

        Map<Integer, DLNode> map;
        DoubleLink list;

        public LruCacheImpl(int n) {
            this.cap = n;
            map = new HashMap<>();
            list = new DoubleLink();
        }

        @Override
        public void put(int key, int val) {
            // 1、前当前 key 不存在，则增加 key,val
            if (!map.containsKey(key)) {
                // 1.1 如果此时 cache 已满
                while (list.size() >= cap) {
                    removeLeastUsed();
                }
                // 1.2 cache 未满，则将ke-val 封装成 Node 加入队列尾部。
                DLNode node = new DLNode(key, val);
                map.put(key, node);
                list.addLast(node);
            } else { // 2、cache 中存储当前 key 元素
                DLNode node = map.get(key);
                // 2.1 更新 val 值
                node.val = val;
                // 2.2 将 node 在链表中的位置改为最后一个元素，表示最近访问
                list.resetLastAccessNode(node);
            }
        }

        @Override
        public int get(int key) {
            DLNode node = map.get(key);
            if (node == null) {
                return -1;
            }
            list.resetLastAccessNode(node);

            return node.val;
        }

        /**
         * 当cache 满的时候，移除最近找使用的元素
         * 
         * @return
         */
        DLNode removeLeastUsed() {
            DLNode lruNode = list.removeFirst();
            if (lruNode != null) {
                map.remove(lruNode.key);
            }
            return lruNode;
        }

        @Override
        public Iterator<Pair<Integer, Integer>> iterator() {
            return new LruIterator(list);
        }
    }

    static class LruIterator implements Iterator<Pair<Integer, Integer>> {

        private DLNode currNode;
        DoubleLink list;

        public LruIterator(DoubleLink list) {
            this.list = list;
            currNode = list.tail;
        }

        @Override
        public boolean hasNext() {
            return currNode.prev != null && currNode.prev != list.head;
        }

        @Override
        public Pair<Integer, Integer> next() {
            if (!hasNext()) {
                throw new IllegalStateException("the iterator has ended");
            }
            currNode = currNode.prev;
            return new Pair<Integer, Integer>(currNode.key, currNode.val);
        }

    }

    static class DoubleLink {
        DLNode head;
        DLNode tail;
        // 链表元素个数
        private int size;

        public DoubleLink() {
            this.head = new DLNode(0, 0);
            this.tail = new DLNode(0, 0);
            head.next = tail;
            tail.prev = head;
        }

        public void addLast(DLNode node) {
            DLNode prev = tail.prev;
            node.next = tail;
            tail.prev = node;

            prev.next = node;
            node.prev = prev;
            size++;
        }

        public void removeNode(DLNode node) {
            if (size == 0) {
                throw new IllegalStateException("lis is empty and can not removeNode");
            }

            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;

            size--;
        }

        public DLNode removeFirst() {
            DLNode firstNode = head.next;
            removeNode(firstNode);
            return firstNode;
        }

        public int size() {
            return size;
        }

        public void resetLastAccessNode(DLNode node) {
            // 如果当前节点不是队尾元素
            if (node != head && node.next != tail) {
                removeNode(node);
                addLast(node);
            }
        }
    }

    static class DLNode {
        public int key;
        public int val;
        public DLNode next;
        public DLNode prev;

        public DLNode() {

        }

        public DLNode(int key, int val) {
            this.val = val;
            this.key = key;
        }
    }

    public static void main(String[] args) {
        LruCache cache = new LruCacheImpl(2);
        cache.put(1, 1);
        cache.put(2, 2);
        printLruCache(cache);
        System.out.println(cache.get(1));
        printLruCache(cache);

        cache.put(3, 3);
        printLruCache(cache);

        System.out.println(cache.get(2));

        cache.put(1, 4);
        printLruCache(cache);
    }

    static void printLruCache(LruCache cache) {
        LruCacheImpl cacheImpl = (LruCacheImpl) cache;
        StringBuilder sb = new StringBuilder("[");
        for (Pair<Integer, Integer> pair : cacheImpl) {
            sb.append(pair + ",");
        }
        sb.deleteCharAt(sb.length() - 1).append("]");
        System.out.println(sb.toString());
    }
}
