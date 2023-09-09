package org.swj.leet_code.binary_tree.bst;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.swj.leet_code.binary_tree.TreeNode;
import org.swj.leet_code.binary_tree.TreeNodeP;

import lombok.val;

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

    /**
     * 若干节点的公共最小祖先
     * @param root
     * @param nodes
     * @return
     */
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

    boolean foundP1 = false;
    boolean foundP2 = false;

    /**
     * 查找 p1 和 p2 的最近公共自祖先节点，p1 和 p2 可能不存在于二叉树上，这时候应该返回 null，如果存在则返回 LCA
     * 该解法需要借助外部变量
     * 
     * @param root
     * @param p1
     * @param p2
     * @return
     */
    TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p1, TreeNode p2) {
        TreeNode node = find2(root, p1.val, p2.val);
        if (foundP1 && foundP2) { // p1 和 p2 都找到了
            return node;
        }
        // 只要有一个没找到 ，就返回 Null
        return null;
    }

    TreeNode find2(TreeNode root, int val1, int val2) {
        if (root == null) {
            return null;
        }
        TreeNode leftNode = find2(root.left, val1, val2);
        TreeNode rightNode = find2(root.right, val1, val2);

        if (leftNode != null && rightNode != null) {
            return root; // 找到 LCA
        }
        if (root.val == val1 || root.val == val2) {
            if (root.val == val1)
                foundP1 = true;
            if (root.val == val2)
                foundP2 = true;
            return root;
        }
        return leftNode != null ? leftNode : rightNode;
    }

    /**
     * 查找 bst 的最小公共祖先，leetcode 235 题
     * 
     * @param root
     * @param p1
     * @param p2
     * @return
     */
    TreeNode lowestCommonAncestorOfBst(TreeNode root, TreeNode p1, TreeNode p2) {
        if (root == null) {
            return null;
        }
        int val1 = Math.min(p1.val, p1.val);
        int val2 = Math.max(p1.val, p2.val);

        return findOfBst(root, val1, val2);
    }

    TreeNode findOfBst(TreeNode root, int val1, int val2) {
        if (root == null) {
            return null;
        }
        // 比 最小值 val1 小，去又子节点查找
        if (root.val < val1) {
            return findOfBst(root.right, val1, val2);
        }
        // 比 最大值 val2 大，去左子节点查找
        if (root.val > val2) {
            return findOfBst(root.left, val1, val2);
        }
        // 这里必须是后续遍历才能根据先这个判断返回 root
        // val1 <= root.val <= val2
        return root;
    }

    TreeNodeP lowestCommonAncestor(TreeNodeP p, TreeNodeP q) {
        // 通过双链表指针技巧解决
        TreeNodeP p1 = p;
        TreeNodeP q1 = q;
        while (p1 != q1) {
            // p1 走一步，如果走到根节点，则转到 q 节点
            if (p1 == null) {
                p1 = q;
            } else {
                p1 = p1.parent;
            }
            
            // q1 同样走一步，如果走到 根节点，指向 p 节点
            
            if (q1 == null) {
                q1 = p;
            } else {
                q1 = q1.parent;
            }            
        }
        return p1;
    }

}
