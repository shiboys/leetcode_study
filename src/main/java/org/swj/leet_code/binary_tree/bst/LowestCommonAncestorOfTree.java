package org.swj.leet_code.binary_tree.bst;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.swj.leet_code.binary_tree.TreeNode;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/31 10:27
 *        树的最小公共祖先。这里主要是二叉树，最小公共祖先——LowestCommonAncestor，简称 LCA 。
 *        相关描述请参考 lca.md
 */
public class LowestCommonAncestorOfTree {

    /**
     * leetcode 236 题，给定一个二叉树，找到该树中两个指定节点的最近公共祖先。
     * p 和 q 都是树中的节点，这点很重要
     * 
     * @param root
     * @param p
     * @param q
     * @return
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        return find(root, p.val, q.val);
    }

    public TreeNode find(TreeNode root, int val1, int val2) {
        if (root == null) {
            return null;
        }
        if (root.val == val1 || root.val == val2) {
            // 找到了符合要求的节点
            // 这也符合情况1 ，就是 p 本身就是 q 的最近公共祖先，先找到p 则返回
            return root;
        }
        // 后续遍历
        TreeNode leftTargetNode = find(root.left, val1, val2);
        TreeNode rightTargetNode = find(root.right, val1, val2);
        // 这里判断左右目标子节点都不为空，说明找到了 2 个目标子节点，则 root 就是最近公共祖先
        // 满足情况 2 左右子节点分别分布在左右子树上。
        if (leftTargetNode != null && rightTargetNode != null) {
            return root;
        }
        // 将找到的目标节点一级级的返回。
        return leftTargetNode != null ? leftTargetNode : rightTargetNode;
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode[] nodes) {
        Set<Integer> values = Arrays.stream(nodes).map(x -> x.val).collect(Collectors.toSet());

        return find(root, values);
    }

    public TreeNode find(TreeNode node, Set<Integer> values) {
        if (node == null) {
            return null;
        }
        if (values.contains(node.val)) {
            return node;
        }
        TreeNode leftTargetNode = find(node.left, values);
        TreeNode rightTargetNode = find(node.right, values);
        if (leftTargetNode != null && rightTargetNode != null) {
            // 找到其中两个节点的最小公共子节点
            return node;
        }
        // 继续向上回溯，向上查找最小公共祖先
        return leftTargetNode != null ? leftTargetNode : rightTargetNode;
    }
}
