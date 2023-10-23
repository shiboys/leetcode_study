package org.swj.leet_code.data_structure_rewrite;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/22 11:29
 * 
 *        TreeMap 结构重写，底层是 二叉搜索树，而不是 JDK 那样的自平衡的红黑树
 *        这样重写的工作量就会小些。
 */
public class MyTreeMap<K extends Comparable<K>, V> {
    /**
     * 主要的 API 分 3 大部分
     * 1、map 相关的，put，get，delete/remove，containsKey。
     * 2、tree 相关的，ceiling，floor，
     * 3、额外有用的 rank, select
     * 4、辅助函数 size,delMin,delMax,isEmpty,
     */
    private TreeNode<K, V> root;
    private int size;

    static class TreeNode<K extends Comparable<K>, V> {
        public K key;
        public V val;
        TreeNode<K, V> left;
        TreeNode<K, V> right;

        public TreeNode() {

        }

        public TreeNode(K key, V val) {
            this.key = key;
            this.val = val;
        }

        public TreeNode(K key, V val, TreeNode<K, V> left, TreeNode<K, V> right) {
            this.key = key;
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public V get(K key) {
        TreeNode<K, V> node = getNode(key);
        return node != null ? node.val : null;
    }

    public V put(K key, V val) {
        TreeNode<K, V> currNode;
        if ((currNode = getNode(key)) != null) {
            V oldVal = currNode.val;
            currNode.val = val;
            return oldVal;
        }
        root = put(root, key, val);
        size++;
        return null;
    }

    private TreeNode<K, V> put(TreeNode<K, V> node, K key, V val) {
        if (node == null) {
            return new TreeNode<>(key, val);
        }
        if (node.key.equals(key)) {
            return node;
        }
        int cmp = node.key.compareTo(key);
        // node < key
        if (cmp < 0) {
            node.right = put(node.right, key, val);
        } else {
            node.left = put(node.left, key, val);
        }
        return node;
    }

    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    private TreeNode<K, V> getNode(K key) {
        return getNode(root, key);
    }

    TreeNode<K, V> getNode(TreeNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        if (node.key.equals(key)) {
            return node;
        }
        int cmp = node.key.compareTo(key);
        // node < key
        if (cmp < 0) {
            return getNode(node.right, key);
        } else {
            return getNode(node.left, key);
        }
    }

    /**
     * 获取以 node 为根节点的最小 key 的节点
     * 
     * @param node
     * @return
     */
    TreeNode<K, V> getMin(TreeNode<K, V> node) {
        TreeNode<K, V> p = node;
        while (p.left != null) {
            p = p.left;
        }
        return p;
    }

    TreeNode<K, V> getMax(TreeNode<K, V> node) {
        TreeNode<K, V> p = node;
        while (p.right != null) {
            p = p.right;
        }
        return p;
    }

    TreeNode<K, V> delMin(TreeNode<K, V> node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node.right;
        }
        node.left = delMin(node.left);
        return node;
    }

    TreeNode<K, V> delMax(TreeNode<K, V> node) {
        if (node == null) {
            return null;
        }
        if (node.right == null) {
            return node.left;
        }
        node.right = delMax(node.right);
        return node;
    }

    public V remove(K key) {
        TreeNode<K, V> node = remove(root, key);
        // 不存在
        if (node == null) {
            return null;
        }
        return node.val;
    }

    TreeNode<K, V> remove(TreeNode<K, V> node, K key) {
        // 不存在
        if (node == null) {
            return null;
        }
        if (node.key.equals(key)) {
            // 要被删除的 node 分 3 中情况
            // 1、node 是叶子节点。2，node 有左子节点或者右子节点
            // 3、node 的左右子节点都是存在的
            // 其中 情况1 和 情况2 可以合并
            size--;
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            // 左右子节点都不为 null，则从 左子节点的最大子节点或者右子节点的最小子节点任选一个作为新的节点
            // 替换当前被删除的节点
            if (node.left != null && node.right != null) {
                TreeNode<K, V> leftMaxNode = getMax(node.left);
                // 将当前节点删除，主要是将 leftMaxNode 的相关子节点进行转移安置
                node.left = delMax(node.left);
                leftMaxNode.left = node.left;
                leftMaxNode.right = node.right;
                return leftMaxNode;
            }
        }
        int cmp = node.key.compareTo(key);
        if (cmp < 0) {
            node.right = remove(node.right, key);
        } else {
            node.left = remove(node.left, key);
        }
        return node;
    }

    /**
     * 返回基于 BST 的有序键的列表
     * 
     * @return
     */
    public Iterable<K> keys() {
        /**
         * BST 中序遍历的话，key 就是有序的
         */
        List<K> keys = new LinkedList<>();
        traverse(keys, root);
        return keys;
    }

    void traverse(List<K> keyList, TreeNode<K, V> node) {
        if (node == null) {
            return;
        }
        traverse(keyList, node.left);
        keyList.add(node.key);
        traverse(keyList, node.right);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 找到 大于等于 key 且最接近 key 的值
     * 循环的方式，目前不能很好地解决 ceiling 的问题
     * 最后还是采用了递归的方式，参见 ceiling 方法
     * 
     * @deprecated
     * @param key
     * @return
     */
    public K ceiling2(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        TreeNode<K, V> node = root;
        if (node == null) {
            return null;
        }
        if (node.key.equals(key)) {
            return node.key;
        }
        int cmp = 0;
        // 父节点
        TreeNode<K, V> pNode = null;

        while (node != null) {
            cmp = node.key.compareTo(key);
            if (cmp > 0) {
                pNode = node;
                node = node.left;
            } else if (cmp < 0) {
                pNode = node;
                node = node.right;
            } else { // 相等，判断是否有右子节点， 如果有，则返回右子节点，否则返回父节点
                return node.key;
            }
        }
        // 没找到 key 相等的节点，如果最后一次遍历的节点比 key 大，则返回最后一次遍历的节点
        // 否则比 key 小，遍历完了都没找到比 key 小的元素，说明所有元素都小于 key，也就是说不存在
        return cmp > 0 ? pNode.key : null;
    }

    public K ceiling(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        TreeNode<K, V> node = ceiling(root, key);
        return node != null ? node.key : null;
    }

    TreeNode<K, V> ceiling(TreeNode<K, V> node, K key) {
        if (node == null) { // key 不存在
            return null;
        }
        int cmp = node.key.compareTo(key);
        // node.key > key
        if (cmp > 0) {
            TreeNode<K, V> x = ceiling(node.left, key);
            if (x != null) {
                return x;
            }
            return node;
        } else if (cmp < 0) { // node.key < key
            return ceiling(node.right, key);
        }
        // node.key == key
        return node;
    }

    public K floor(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        TreeNode<K, V> node = floor(root, key);
        return node != null ? node.key : null;
    }

    TreeNode<K, V> floor(TreeNode<K, V> node, K key) {
        if (node == null) { // key 不存在
            return null;
        }
        int cmp = node.key.compareTo(key);
        // node.key > key
        if (cmp > 0) {
            return floor(node.left, key);
        } else if (cmp < 0) { // node.key < key
            TreeNode<K, V> x =  floor(node.right, key);
            if( x != null) {
                return x;
            }
            return node;
        }
        // node.key == key
        return node;
    }

    public static void main(String[] args) {
        // TreeNode<Integer,Integer> root = new TreeNode<Integer,Integer>(3, 3);
        MyTreeMap<Integer, Integer> treeMap = new MyTreeMap<>();
        int[] arr = new int[] { 3, 2, 0, 1, 7, 5, 9 };
        for (int key : arr) {
            treeMap.put(key, key * 10);
        }
        for (Integer key : treeMap.keys()) {
            System.out.print(key + " ");
        }
        System.out.println();
        // // 删除节点 0,代表哦删除带有 右子节点1 的节点
        // map.remove(0);
        // for (Integer key : map.keys()) {
        // System.out.print(key + " ");
        // }
        // System.out.println();
        // // 删除节点 5,代表删除左右子节点都不为空的节点
        // map.remove(5);
        // // 此时遍历结果应该是 1 2 3 4 6
        // for (Integer key : map.keys()) {
        // System.out.print(key + " ");
        // }
        // System.out.println();
        // 存在的 key，返回存在的右子节点
        //testCeiling(treeMap);
        int key = 0;
        Integer val = treeMap.floor(key);
        System.out.println("the floor value of " + key + " is " + val);
        // 不存在的 key，返回大于等于 key 的键
        key = 6;
        val = treeMap.floor(key);
        System.out.println("the floor value of " + key + " is " + val);

        // 不存在的 key，，返回大于等于 key 的键
        key = 8;
        val = treeMap.floor(key);
        System.out.println("the floor value of " + key + " is " + val);

        // 不存在的 key，比所有的 key 都大
        key = 10;
        val = treeMap.floor(key);
        System.out.println("the floor value of " + key + " is " + val);
    }

    private static void testCeiling(MyTreeMap<Integer, Integer> treeMap) {
        int key = 0;
        Integer val = treeMap.ceiling(key);
        System.out.println("the ceiling value of " + key + " is " + val);
        // 不存在的 key，返回大于等于 key 的键
        key = 6;
        val = treeMap.ceiling(key);
        System.out.println("the ceiling value of " + key + " is " + val);

        // 不存在的 key，，返回大于等于 key 的键
        key = 8;
        val = treeMap.ceiling(key);
        System.out.println("the ceiling value of " + key + " is " + val);

        // 不存在的 key，比所有的 key 都大
        key = 10;
        val = treeMap.ceiling(key);
        System.out.println("the ceiling value of " + key + " is " + val);
    }

}
