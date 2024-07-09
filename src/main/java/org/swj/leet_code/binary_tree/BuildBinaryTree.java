package org.swj.leet_code.binary_tree;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/26 22:27
 *        二叉树的构造
 */
public class BuildBinaryTree {

    Map<Integer, Integer> valToIndex;

    /**
     * 从前序和中序遍历结果中构造二叉树
     * 
     * @param preOrder 前序遍历结果
     * @param inOrder  中序遍历结果
     * @return 二叉树的头指针。
     */
    public TreeNode buildTreeFromPreOrderAndInOrder(int[] preOrder, int[] inOrder) {
        valToIndex = new HashMap<>();
        for (int i = 0; i < inOrder.length; i++) {
            valToIndex.put(inOrder[i], i);
        }
        return buildTreeNodeFromPreIn(preOrder, 0, preOrder.length - 1,
                inOrder, 0, inOrder.length - 1);
    }

    TreeNode buildTreeNodeFromPreIn(int[] preOrder, int preStart, int preEnd,
            int[] inOrder, int inStart, int inEnd) {
        if (preStart > preEnd || inStart > inEnd || preEnd < 0 || preStart < 0) {
            return null;
        }
        int rootVal = preOrder[preStart];
        Integer rootIdx = valToIndex.get(rootVal);
        if (rootIdx == null) {
            return null;
        }
        int leftLength = rootIdx - inStart;
        TreeNode root = new TreeNode(rootVal);
        // 这里的索引一旦错了，二叉树就会丢节点
        root.left = buildTreeNodeFromPreIn(preOrder, preStart + 1, leftLength + preStart,
                inOrder, inStart, rootIdx - 1);
        root.right = buildTreeNodeFromPreIn(preOrder, leftLength + preStart + 1, preEnd,
                inOrder, rootIdx + 1, inEnd);
        return root;
    }

    public TreeNode buildTreeFromPostOrderAndInOrder(int[] inOrder, int[] postOrder) {
        valToIndex = new HashMap<>();
        for (int i = 0; i < inOrder.length; i++) {
            valToIndex.put(inOrder[i], i);
        }
        return buildTreeNodeFromPostIn(inOrder, 0, inOrder.length - 1,
                postOrder, 0, postOrder.length - 1);
    }

    TreeNode buildTreeNodeFromPostIn(int[] inOrder, int inStart, int inEnd,
            int[] postOrder, int postStart, int postEnd) {
        if (inStart > inEnd) {
            return null;
        }
        int rootVal = postOrder[postEnd];
        Integer rootIdx = valToIndex.get(rootVal);
        if (rootIdx == null) {
            return null;
        }
        TreeNode root = new TreeNode(rootVal);
        int leftLength = rootIdx - inStart;
        root.left = buildTreeNodeFromPostIn(inOrder, inStart, rootIdx - 1, postOrder, postStart,
                postStart + leftLength - 1);

        root.right = buildTreeNodeFromPostIn(inOrder, rootIdx + 1, inEnd,
                postOrder, postStart + leftLength, postEnd - 1); // 这里的 postEnd -1 很重要，错误就有可能造成死循环
        return root;
    }

    /**
     * 从数组中构造二叉树，leetcode 654 题
     * 从数组中找出一个最大值作为二叉树的根，根左边的元素作为二叉树的左子树，根右边的元素作为右子树。
     * 
     * @param nums
     * @return
     */
    TreeNode buildTreeFromArray(int[] nums) {
        return buildTreeNodeFromArray(nums, 0, nums.length - 1);
    }

    TreeNode buildTreeNodeFromArray(int[] nums, int start, int end) {
        if (nums == null || nums.length < 1) {
            return null;
        }
        if (start > end) {
            return null;
        }
        int rootVal = Integer.MIN_VALUE;
        int rootIdx = 0;
        // 注意，这里是 i<=end, 范围错了就会丢失数据
        for (int i = start; i <= end; i++) {
            if (nums[i] > rootVal) {
                rootVal = nums[i];
                rootIdx = i;
            }
        }
        TreeNode root = new TreeNode(rootVal);
        root.left = buildTreeNodeFromArray(nums, start, rootIdx - 1);
        root.right = buildTreeNodeFromArray(nums, rootIdx + 1, end);
        return root;
    }

    /**
     * 从前序遍历和后续遍历结果中构造二叉树
     * leetcode 889 题
     * 
     * @param preOrder  前序遍历结果
     * @param postOrder 后续遍历结果
     * @return 二叉树根节点
     */
    TreeNode buildTreeFromPreOrderAndPostOrder(int[] preOrder, int[] postOrder) {
        /**
         * 用前序遍历和后序遍历结果还原二叉树，解法逻辑上和上面的前中序和后中序差别不是很大，也是通过控制左右子树的索引来构建
         * 1、首先将前序遍历的第一个元素或者后序遍历的最后一个元素作为根节点的值。
         * 2、把前序遍历的第二个元素作为左子树根节点的值
         * 3、在后序遍历结果中寻找左子树根节点的值，从而确定了左子树的索引边界，进而确定右子树的索引边界，递归构造左右子树即可。
         */
        valToIndex = new HashMap<>();
        for (int i = 0; i < postOrder.length; i++) {
            valToIndex.put(postOrder[i], i);
        }
        return buildTreeNodeFromPrePost(preOrder, 0, preOrder.length - 1, postOrder, 0, postOrder.length - 1);
    }

    TreeNode buildTreeNodeFromPrePost(int[] preOrder, int preStart, int preEnd,
            int[] postOrder, int postStart, int postEnd) {
        if (preStart > preEnd) {
            return null;
        }
        if (preStart == preEnd) {
            return new TreeNode(preOrder[preStart]);
        }
        int rootVal = preOrder[preStart];
        TreeNode root = new TreeNode(rootVal);
        if (preStart + 1 > preOrder.length - 1) {
            return root;
        }
        int leftRootVal = preOrder[preStart + 1];

        int leftRootIdx = valToIndex.get(leftRootVal);
        // 左子树长度, 需要包含左子树的根节点这个长度 1
        int leftLength = leftRootIdx - postStart + 1;
        root.left = buildTreeNodeFromPrePost(preOrder, preStart + 1, preStart + leftLength, postOrder, postStart,
                leftRootIdx);
        root.right = buildTreeNodeFromPrePost(preOrder, preStart + leftLength + 1, preEnd, postOrder, leftRootIdx + 1,
                postEnd - 1);
        return root;
    }

    public static void main(String[] args) {
        // buildTreeTest();
        // buildTreeByArray();
        int[] preOrder = new int[] { 1, 2, 4, 5, 3, 6, 7 };
        int[] postOrder = new int[] { 4, 5, 2, 6, 7, 3, 1 };
        BuildBinaryTree instance = new BuildBinaryTree();
        TreeNode root = instance.buildTreeFromPreOrderAndPostOrder(preOrder, postOrder);
        BinaryTreeArch.printBinaryTreeSimple(root);
        System.out.println();
        BinaryTreeArch.printBinaryTreeBack(root);
        System.out.println();

    }

    private static void buildTreeByArray() {
        int[] arr = new int[] { 3, 2, 1, 6, 0, 5 };
        BuildBinaryTree instance = new BuildBinaryTree();
        TreeNode root = instance.buildTreeFromArray(arr);
        BinaryTreeArch.printBinaryTreeBack(root);
        System.out.println();
    }

    private static void buildTreeTest() {
        int[] preOrder = new int[] { 3, 9, 20, 15, 7 };
        int[] inOrder = new int[] { 9, 3, 15, 20, 7 };
        int[] postOrder = new int[] { 9, 15, 7, 20, 3 };
        BuildBinaryTree instance = new BuildBinaryTree();
        // TreeNode root = instance.buildTreeFromPreOrderAndInOrder(preOrder, inOrder);
        // BinaryTreeArch.printBinaryTreeBack(root);
        TreeNode root = instance.buildTreeFromPostOrderAndInOrder(inOrder, postOrder);
        BinaryTreeArch.printBinaryTreeBack(root);
        System.out.println();
    }

}
