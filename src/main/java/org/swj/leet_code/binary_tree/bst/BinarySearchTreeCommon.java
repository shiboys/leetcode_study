package org.swj.leet_code.binary_tree.bst;

import org.swj.leet_code.binary_tree.BinaryTreeArch;
import org.swj.leet_code.binary_tree.TreeNode;

/**
 * 二叉搜索树的基本操作
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/25 22:27
 *        二叉树框架
 */
public class BinarySearchTreeCommon {

    /**
     * leetcode 98 题，是否合法的 bst
     * 
     * @param root
     * @return
     */
    boolean isValidBST(TreeNode root) {
        return isValidBST(root, null, null);
    }

    boolean isValidBST(TreeNode root, TreeNode min, TreeNode max) {
        if (root == null) {
            return true;
        }
        // root 的左子树节点应该更小，
        if (min != null && root.val <= min.val) // leetcode 给的 usercase 有 [2,2,2] 这种情况，所以改为 <=
            return false;
        // root 的右子树节点应该更大。
        if (max != null && root.val >= max.val)
            return false;

        return isValidBST(root.left, min, root) && // 在这个地柜里面 root 作为 max 会一直传递下去
                isValidBST(root.right, root, max); // 在这个递归里面，root 作为 min 会一直传递下去

    }

    TreeNode targetNode;

    TreeNode searchBST(TreeNode root, int target) {
        if (root == null) {
            return null;
        }
        if (root.val == target) {
            return root;
        }
        if (root.val > target) {
            return searchBST(root.left, target);
        } else {
            return searchBST(root.right, target);
        }
    }

    TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) { // 找到了插入的位置，
            return new TreeNode(val);
        }
        // if(root.val = =val) BST 的插入一般不会插入重复值
        if (root.val > val) {// 去左子树寻找插入点
            root.left = insertIntoBST(root.left, val);
        } else if (root.val < val) { // 去右子树寻找插入点
            root.right = insertIntoBST(root.right, val);
        }
        return root;
    }

    TreeNode kthNode;
    int counter;

    TreeNode kthSmallest(TreeNode root, int k) {
        traverseMidBST(root, k);
        return kthNode;
    }

    void traverseMidBST(TreeNode root, int k) {
        if (root == null) {
            return;
        }
        traverseMidBST(root.left, k);
        // 中序遍历
        counter++;
        if (counter == k) {
            kthNode = root;
        }
        traverseMidBST(root.right, k);
    }

    // 记录累加和
    int sum = 0;

    /**
     * 转换成累加树，leetcode 538 & 1038
     * 
     * @param root
     * @return
     */
    void convertToSumBST(TreeNode root) {
        if (root == null) {
            return;
        }
        /**
         * 根据图片，我们知道要逆中序遍历，就是 右中左的方向
         */
        convertToSumBST(root.right);
        sum += root.val;
        // 将原 bst 转化为累加和
        root.val = sum;
        convertToSumBST(root.left);
    }

    /**
     * leetcode 450, 删除二叉搜索树中的节点
     */
    TreeNode deleteBstNode(TreeNode root, int target) {
        if (root == null) {
            return root;
        }
        if (root.val == target) {
            // 找到元素，进行删除
            // 覆盖情况1 和 情况2
            if (root.left == null)
                return root.right;
            if (root.right == null)
                return root.left;
            // 情况 3 ，左右子节点都不为空
            // 查找右子树的最小节点
            TreeNode minNode = getMinNode(root.right);
            // 转化成删除该节点右子树的最小节点
            root.right = deleteBstNode(root.right, minNode.val);
            // 将 右子树的最小节点替换为 root 节点
            minNode.left = root.left;
            minNode.right = root.right;
            root = minNode;
        } else if (root.val > target) {
            // 去左子节点查找并删除
            root.left = deleteBstNode(root.left, target);
        } else if (root.val < target) {
            // 去右子节点查找并删除
            root.right = deleteBstNode(root.right, target);
        }
        return root;
    }

    private TreeNode getMinNode(TreeNode right) {
        TreeNode p = right;
        while (p.left != null) {
            p = p.left;
        }
        return p;
    }

    public static void main(String[] args) {
        BinarySearchTreeCommon instance = new BinarySearchTreeCommon();

        // testValidBST(instance);

        TreeNode node2 = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        TreeNode root = new TreeNode(4, node2, new TreeNode(7));
        BinaryTreeArch.printBinaryTreeSimple(instance.searchBST(root, 2));

        BinaryTreeArch.printBinaryTreeSimple(instance.searchBST(root, 5));
    }

    private static void testValidBST(BinarySearchTreeCommon instance) {
        TreeNode root = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        System.out.println(instance.isValidBST(root));

        TreeNode node4 = new TreeNode(4, new TreeNode(3), new TreeNode(6));
        root = new TreeNode(5, new TreeNode(1), node4);
        System.out.println(instance.isValidBST(root));
    }
}
