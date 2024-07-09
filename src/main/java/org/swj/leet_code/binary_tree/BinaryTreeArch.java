package org.swj.leet_code.binary_tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/25 22:27
 *        二叉树框架
 * 
 */
public class BinaryTreeArch {

    int maxDepth;
    int depth;

    /**
     * 求一个二叉树的最大深度，最大深度是从根节点到叶子节点所经过的数量最多的节点
     * leetcode 104 题
     * 树的高度和深度，来自维基百科的解释
     * 深度：对于任意节点n,n的深度为从根到n的唯一路径长，根的深度为0；
     * 高度：对于任意节点n,n的高度为从n到一片树叶的最长路径长，所有树叶的高度为0；
     * 
     * @param node
     * @return
     */
    int maxDepth(TreeNode root) {
        traverse(root);
        return maxDepth;
    }

    void traverse(TreeNode node) {
        if (node == null) {
            return;
        }
        // 前序位置
        depth++;
        // 到达叶子结点，计算最大深度
        // if (node.left == null && node.right == null) {
        // maxDepth = Math.max(maxDepth, depth);
        // }
        // 上面的 if 判断可以不写，因为 null 的节点 return 了，不会被前序的递归逻辑计算到
        // 加上之后，逻辑更严谨
        maxDepth = Math.max(maxDepth, depth);
        traverse(node.left);
        traverse(node.right);
        // 后续位置
        depth--;
    }

    /**
     * 通过分解问题，找到最大深度
     * 
     * @param node
     * @return
     */
    int maxDepth2(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int maxLeftDepth = maxDepth2(node.left);
        int maxRightDepth = maxDepth2(node.right);
        return Math.max(maxLeftDepth, maxRightDepth) + 1;
    }

    int maxHeight(TreeNode node) {
        if (node == null) {
            return -1;
        }
        // 叶子结点的高度为 0。
        if (node.left == null && node.right == null) {
            return 0;
        }
        int maxLeftHeight = maxHeight(node.left);
        int maxRightHeight = maxHeight(node.right);
        return Math.max(maxLeftHeight, maxRightHeight) + 1;
    }

    /**
     * 110. Balanced Binary Tree
     * AVL树（Adelson-Velsky and Landis Tree）是计算机科学中最早被发明的自平衡二叉查找树。
     * 在AVL树中，任一节点对应的两棵子树的最大高度差为1，因此它也被称为高度平衡树。
     * 
     * @param root
     * @return
     */
    public boolean isBalanced(TreeNode root) {
        return isBalancedTree(root);
    }

    boolean isBalancedTree(TreeNode node) {
        if (node == null) {
            return true;
        }
        int leftHight = maxHeight(node.left);
        int rightHeight = maxHeight(node.right);
        int diff = Math.abs(leftHight - rightHeight);
        if (diff > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }

    private static void maxLevelVal(BinaryTreeArch instance) {
        TreeNode root = new TreeNode(1);
        TreeNode node1 = new TreeNode(2, null, new TreeNode(9));
        TreeNode node2 = new TreeNode(3, new TreeNode(5), new TreeNode(3));
        root.right = node1;
        root.left = node2;

        // System.out.println(instance.traverseMaxLevel(root));
        System.out.println(instance.traverseMaxLevel2(root));

        instance.printBinaryTreeSimple(root);
    }

    private static void testGetDiameter(BinaryTreeArch instance) {
        TreeNode root = new TreeNode(9);
        TreeNode node1 = new TreeNode(1, null, new TreeNode(3));
        TreeNode node2 = new TreeNode(2, new TreeNode(4), new TreeNode(5));
        root.right = node1;
        node1.left = node2;

        System.out.println(instance.diameterOfBinaryTree(root));
    }

    private static void testMaxDepth(BinaryTreeArch instance) {
        TreeNode root = new TreeNode(3);
        TreeNode rightChild = new TreeNode(20);
        rightChild.left = new TreeNode(15);
        rightChild.right = new TreeNode(7);

        root.left = new TreeNode(9);
        root.right = rightChild;

        System.out.println(instance.maxDepth(root));

        System.out.println(instance.maxDepth2(root));
    }

    int maxDiameter = 0;

    /**
     * 计算树的最大直径
     * leetcode 第 543 题。
     * 
     * @param root
     * @return
     */
    int diameterOfBinaryTree(TreeNode root) {
        maxDepth3(root);
        return maxDiameter;
    }

    int maxDepth3(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftMaxDepth = maxDepth3(node.left);
        int rightMaxDepth = maxDepth3(node.right);
        // 在统计最大深度的同时，把最大直径顺便统计了
        // 后续位置，顺便计算最大直径
        // 对每个节点计算直径
        int diameter = rightMaxDepth + leftMaxDepth;
        // 更新全局最大直径
        maxDiameter = Math.max(maxDiameter, diameter);

        return Math.max(leftMaxDepth, rightMaxDepth) + 1;
    }

    /**
     * 遍历写法获取每层的最大值
     * leetcode
     * 
     * @param root
     * @return
     */
    public List<Integer> traverseMaxLevel(TreeNode root) {
        List<Integer> maxLevelValRes = new LinkedList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int sz = queue.size();
            // List<Integer> levelValues = new ArrayList<>(sz);
            int maxLevel = Integer.MIN_VALUE;
            for (int i = 0; i < sz; i++) {
                TreeNode node = queue.poll();
                maxLevel = Math.max(maxLevel, node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            // maxLevelValRes.add(levelValues.stream().max((a, b) -> {
            // return a >= b ? 1 : -1;
            // }).get());
            maxLevelValRes.add(maxLevel);
        }
        return maxLevelValRes;
    }

    public List<Integer> traverseMaxLevel2(TreeNode root) {
        if (root == null) {
            return null;
        }
        List<TreeNode> list = new LinkedList<>();
        list.add(root);
        maxLevelValListRes = new LinkedList<>();
        traverseLevel(list);
        return maxLevelValListRes;
    }

    List<Integer> maxLevelValListRes;

    /**
     * 不依靠队列且水平的方式实现宽度优先遍历二叉树。在遍历过程中求每一层的最大值
     * leetcode 102 题
     * 
     * @param list
     */
    private void traverseLevel(List<TreeNode> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        // 直接用 int maxVal = Integer.MIN_VALUE. maxVal = Math.max(maxVal,node.val);
        List<Integer> currVals = new ArrayList<>(list.size());
        List<TreeNode> nextLevelList = new LinkedList<>();
        for (TreeNode node : list) {
            currVals.add(node.val);
            if (node.left != null) {
                nextLevelList.add(node.left);
            }
            if (node.right != null) {
                nextLevelList.add(node.right);
            }
        }
        // 把这行放到 traverseLevel 后面就是后序遍历，则是自下而上的，从最底层开始算起
        if (!currVals.isEmpty()) {
            maxLevelValListRes.add(currVals.stream().mapToInt(Integer::intValue).max().getAsInt());
            // maxLevelValListRes.add(currVals.stream().max((a, b) -> {
            // return a >= b ? 1 : -1;
            // }).get());
        }

        traverseLevel(nextLevelList);
    }

    /**
     * leetcode 第 114 题，将链表拉平成全部是右节点的子树。
     * 
     * @param root
     */
    void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        // 方法1 ，使用队列的方式
        Queue<TreeNode> queue = new LinkedList<>();
        flattenNode(root, queue);
        TreeNode p = queue.poll();

        while (!queue.isEmpty() && p != null) {
            TreeNode rightNode = queue.poll();
            p.left = null;
            p.right = rightNode;
            p = p.right;
        }
        // 方法2 使用中序遍历，参见 leetcode 中文网的该题解决思路

        // 方法3 ，采用后续遍历的方式。
        // flattenNode3(root);
    }

    void flattenNode3(TreeNode node) {
        if (node == null) {
            return;
        }
        // 采用分解问题的方式，将左右子树拉平
        flattenNode3(node.left);
        flattenNode3(node.right);
        // 采用后续遍历的方式，拉平子树
        // 1. 左右子树被拉平成一条链表
        TreeNode left = node.left;
        TreeNode right = node.right;

        // 2. 将左子树作为右子树
        node.left = null;
        node.right = left;

        // 将原来的右子树连接到新的右子树的最后
        TreeNode p = node;
        while (p.right != null) {
            p = p.right;
        }
        p.right = right;
    }

    void flattenNode(TreeNode node, Queue<TreeNode> queue) {
        if (node == null) {
            return;
        }
        queue.add(node);
        flattenNode(node.left, queue);
        flattenNode(node.right, queue);
    }

    /**
     * 简单打印二叉树，按照前序遍历的思路
     * 
     * @param root
     */
    public static void printBinaryTreeSimple(TreeNode root) {
        doPrintBinaryTreeSimple(root);
        System.out.println();
    }

    static void doPrintBinaryTreeSimple(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.print(root.val + " ");
        doPrintBinaryTreeSimple(root.left);
        doPrintBinaryTreeSimple(root.right);
    }

    /**
     * 后续遍历
     * 
     * @param root
     */
    public static void printBinaryTreeBack(TreeNode root) {
        if (root == null) {
            return;
        }
        printBinaryTreeBack(root.left);
        printBinaryTreeBack(root.right);
        System.out.print(root.val + " ");
    }

    /**
     * 反转二叉树
     * leetcode 226
     * 
     * @param root
     */
    void invertBinaryTree(TreeNode root) {
        if (root == null) {
            return;
        }
        // 二叉树的翻转，前序和后续都行
        // 这里需要看仔细，反转的代码不是简单的值交换，而是引用交换
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        invertBinaryTree(root.left);
        invertBinaryTree(root.right);
    }

    /**
     * 分解方式实现翻转
     * 
     * @param root
     */
    TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);

        // 实现反转
        root.left = right;
        root.right = left;

        return root;
    }

    /**
     * 填充每个二叉树节点的右指针
     * leetcode 116
     * 
     * @param root
     * @return
     */
    public Node connect(Node root) {
        /**
         * 这道题我们需要发散下思维，
         * 除了设置我们常规的 node.left.next = node.right;
         * 我们还要将 node1.right = node2.left，就是图中的 5 节点指向 6 节点
         * 因此我们递归的参数需要两个: node1, node2
         */
        // root.next = root.left;
        traverseConnnectNode(root.left, root.right);
        return root;
    }

    void traverseConnnectNode(Node node1, Node node2) {
        if (node1 == null) {
            return;
        }
        // 前序遍历
        node1.next = node2;
        traverseConnnectNode(node1.left, node1.right);
        // 将 node2 跟 Node1 链起来
        if (node2 != null) {
            traverseConnnectNode(node1.right, node2.left);
            traverseConnnectNode(node2.left, node2.right);
        }
    }

    /**
     * 101. Symmetric Tree
     * 二叉树是否对称
     * 
     * @param root
     * @return
     */
    public boolean isSymmetric(TreeNode root) {
        /**
         * 对称二叉树，这个是我从剑指offer上获取到的思路
         * 我们常见的二叉树的前后后遍历全部都是 先遍历左子节点，然后遍历右子节点，为什么这么遍历，因为这是规定，那么我们有没有想过，
         * 可以先遍历右子节点，然后再遍历左子节点， 这成啥了？哟，你别说，对称的算法就用到了这个
         * 如果前序遍历的中左右 和 对称前序遍历的 中右左节点的值完全相同，那就是对称的了。
         * 不过有种特殊情况，比如说 节点的值都是相同的，但是不对称，这种如何处理？
         * 这里就用到了序列化二叉树中讲到的 null 值表示了，把 null 叶子结点也算上，就可以完全判断出二叉树是否对称
         */
        return isSymmetric(root, root);
        // 其实把树序列化(带 null)出来以后，比对字符串的值也可以, 可能更直观
    }

    boolean isSymmetric(TreeNode node1, TreeNode node2) {
        if (node1 == null && node2 == null) {
            return true;
        }
        if (node1 == null || node2 == null) {
            return false;
        }
        if (node1.val != node2.val) {
            return false;
        }
        return isSymmetric(node1.left, node2.right) && isSymmetric(node1.right, node2.left);
    }

    /**
     * 1325. Delete Leaves With a Given Value
     * 
     * @param root
     * @param target
     * @return
     */
    public TreeNode removeLeafNodes(TreeNode root, int target) {
        root = deleteNodeWithTarget(root, target);
        return root;
    }

    TreeNode deleteNodeWithTarget(TreeNode node, int target) {
        if (node == null) {
            return null;
        }
        node.left = deleteNodeWithTarget(node.left, target);
        node.right = deleteNodeWithTarget(node.right, target);
        // 当前节点是叶子结点，且值为 target，表示可以删掉
        if (node.val == target && node.left == null && node.right == null) {
            return null;
        }
        return node;
    }

    /**
     * leetcode 103 锯齿状遍历二叉树
     * 
     * @param root
     * @return
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        Stack<TreeNode> stack2 = new Stack<>();
        stack.push(root);
        TreeNode node = null;
        int size = 0;
        boolean inOrder = true;
        // 使用两个栈来实现 zigzag 遍历，使用 inOrder 变量决定先访问左子节点还是右子节点
        while (!stack.isEmpty()) {
            size = stack.size();
            List<Integer> subList = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                node = stack.pop();
                subList.add(node.val);
                if (inOrder) {// 正序
                    if (node.left != null) {
                        stack2.push(node.left);
                    }
                    if (node.right != null) {
                        stack2.push(node.right);
                    }
                } else {
                    if (node.right != null) {
                        stack2.push(node.right);
                    }
                    if (node.left != null) {
                        stack2.push(node.left);
                    }
                }
            }
            res.add(subList);
            inOrder = !inOrder;
            // 交换 stack;
            Stack<TreeNode> tmp = stack;
            stack = stack2;
            stack2 = tmp;
        }
        return res;
    }

    public static void main(String[] args) {
        BinaryTreeArch instance = new BinaryTreeArch();

        // testMaxDepth(instance);
        // testGetDiameter(instance);
        // maxLevelVal(instance);

        testInvertTree(instance);
        // Node root = new Node(1);
        // Node node2 = new Node(2, new Node(4), new Node(5), null);
        // Node node3 = new Node(3, new Node(6), new Node(7), null);
        // root.left = node2;
        // root.right = node3;
        // printNodeChain(instance.connect(root));

        // root.val = 3;
        // node2.val=9;
        // node3.val = 20;
        // node3.left.val = 15;node3.right.val= 7;
    }

    static void printNodeChain(Node node) {

        Node n = node;
        Node p = node;
        while (n != null) {

            while (p != null) {
                System.out.print(p.val + " ");
                p = p.next;
            }
            n = n.left;
            p = n;
            System.out.println();
        }
    }

    private static void testInvertTree(BinaryTreeArch instance) {
        TreeNode root = new TreeNode(3);
        TreeNode node1 = new TreeNode(9, new TreeNode(5), new TreeNode(6));
        TreeNode node2 = new TreeNode(20, new TreeNode(15), new TreeNode(7));

        root.left = node1;
        root.right = node2;

        // instance.printBinaryTreeSimple(root);
        // System.out.println();
        // // instance.invertBinaryTree(root);
        // instance.invertTree(root);
        // instance.printBinaryTreeSimple(root);
        // System.out.println();

        List<List<Integer>> res = instance.zigzagLevelOrder(root);
        System.out.println(res);

    }

}
