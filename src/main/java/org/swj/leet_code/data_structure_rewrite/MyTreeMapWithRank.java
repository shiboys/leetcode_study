package org.swj.leet_code.data_structure_rewrite;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/22 21:29
 * 
 *        TreeMap 结构重写，底层是 二叉搜索树，而不是 JDK 那样的自平衡的红黑树
 *        这样重写的工作量就会小些。
 */
public class MyTreeMapWithRank<K extends Comparable<K>, V> {
    /**
     * 这个类主要解决 TreeMap 的 rand 和 select 方法
     * rank 和 select 方法由于要对 Node 进行改造，所以使用一个全新的类
     * 不再污染原始的类
     */
    private TreeNode<K, V> root;

    static class TreeNode<K extends Comparable<K>, V> {
        public K key;
        public V val;
        // 表示当前节点的子节点个数+1(自身)。默认个数为1
        public int size = 1;
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

        @Override
        public String toString() {
            return "{key:" + key + ", size:" + size + "}\t";
        }

    }

    public V get(K key) {
        TreeNode<K, V> node = getNode(key);
        return node != null ? node.val : null;
    }

    public V put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        V oldVal = get(key);
        root = put(root, key, val);
        return oldVal;
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
        // node.size++;
        // 算法4 和 阿东方式是如下，个人感觉也可以，但是我这个也不错
        // 这样都会递归的方式计算每个节点的 size
        node.size = size(node.left) + size(node.right) + 1;
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
        // 维护每个节点的 size
        // node.size--;
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    public V remove(K key) {
        // 不存在
        if (!containsKey(key)) {
            return null;
        }
        return remove(root, key).val;
    }

    TreeNode<K, V> remove(TreeNode<K, V> node, K key) {
        // 不存在
        if (node == null) {
            return null;
        }
        int cmp = node.key.compareTo(key);
        if (cmp < 0) {
            node.right = remove(node.right, key);
        } else if (cmp > 0) {
            node.left = remove(node.left, key);
        } else { // node.key == key

            // 要被删除的 node 分 3 中情况
            // 1、node 是叶子节点。2，node 有左子节点或者右子节点
            // 3、node 的左右子节点都是存在的
            // 其中 情况1 和 情况2 可以合并
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
                // leftMaxNode.size = size(node.left) + size(node.right) + 1;
                // return leftMaxNode;
                // 不使用上面的方式更简洁
                node = leftMaxNode;
            }
        }

        // node.size--;
        node.size = size(node.left) + size(node.right) + 1;
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

    public void visitWithSize() {
        visitWithSize(root);
        System.out.println();
    }

    private void visitWithSize(TreeNode<K, V> node) {
        if (node == null) {
            return;
        }
        visitWithSize(node.left);
        System.out.println(node);
        visitWithSize(node.right);
    }

    public boolean isEmpty() {
        return size(root) == 0;
    }

    public int size() {
        return size(root);
    }

    private int size(TreeNode<K, V> node) {
        if (node == null) {
            return 0;
        }
        return node.size;
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
            TreeNode<K, V> x = floor(node.right, key);
            if (x != null) {
                return x;
            }
            return node;
        }
        // node.key == key
        return node;
    }

    public int rank(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return rank(root, key);
    }

    // 从 0 开始的索引
    int rank(TreeNode<K, V> node, K key) {
        int cmp = node.key.compareTo(key);
        if (cmp > 0) { // node.key 大于 key，左子树寻找
            return rank(node.left, key);
        } else if (cmp < 0) {
            // 右子树寻找有些特别
            return size(node.left) + 1 + rank(node.right, key);
        } else {
            return size(node.left);
        }
    }

    public static void main(String[] args) {
        // TreeNode<Integer,Integer> root = new TreeNode<Integer,Integer>(3, 3);
        MyTreeMapWithRank<Integer, Integer> treeMap = new MyTreeMapWithRank<>();
        int[] arr = new int[] { 3, 2, 0, 1, 7, 5, 9 };
        for (int key : arr) {
            treeMap.put(key, key * 10);
        }
        treeMap.visitWithSize();
        // System.out.println("delete 0 ");

        // // 删除节点 0,代表哦删除带有 右子节点1 的节点
        // treeMap.remove(0);
        // treeMap.visitWithSize();
        // // 删除节点 7,代表删除左右子节点都不为空的节点
        // System.out.println("delete 7 ");
        // treeMap.remove(7);
        // // 此时遍历结果应该是 1 2 3 4 6
        // treeMap.visitWithSize();
        System.out.println(treeMap.rank(7));
    }

}
