package org.swj.leet_code.binary_tree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import lombok.val;

/**
 * 二叉树相关的序列化
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/27 14:27
 */
public class SerializeBinaryTree {

    /**
     * 寻找重复的子树。 leetcode 652 题
     * 这道题的解法思考：
     * 要找到重复的子树，必须知道某棵子树长什么样，其他子树长什么样
     * 那怎么才能知道我长什么样？那就要用到序列化了，将子树序列化成字符串，
     * 通过比较字符串来比较子树，如果有相同的，则放入集合中。
     * 
     * @param root
     * @return
     */
    List<TreeNode> findDuplicateTree(TreeNode root) {
        keyCountMap = new HashMap<>();
        duplicateNodeList = new LinkedList<>();
        serialize(root);
        return duplicateNodeList;
    }

    Map<String, Integer> keyCountMap;
    List<TreeNode> duplicateNodeList;

    private String serialize(TreeNode node) {
        if (node == null) {
            return "#";
        }
        String left = serialize(node.left);
        String right = serialize(node.right);
        // 后续遍历的方式
        String serializeVal = left + "," + right + "," + node.val;
        int keyCount = keyCountMap.getOrDefault(serializeVal, 0);
        if (keyCount == 1) {
            duplicateNodeList.add(node);
        }

        keyCountMap.put(serializeVal, keyCount + 1);
        return serializeVal;
    }

    private static final String SEP = ",";
    private static final String NULL = "#";

    public String serializeCodec(TreeNode root) {
        // 默认前序
        return serializeCodec(root, 1);
    }

    /**
     * 
     * @param root
     * @param way  序列化方式，1：前序，2：中序，3：后序，4：水平。ps: 中序实现不了
     * @return
     */
    public String serializeCodec(TreeNode root, int way) {
        StringBuilder sb = new StringBuilder();
        if (way == 1)
            serializeCodecPre(root, sb);
        else if (way == 3)
            serializeCodecPost(root, sb);
        else if (way == 4) {
            serializeCodecLevel(root, sb);
        } else { // 默认还是前序序列化
            serializeCodecPre(root, sb);
        }
        return sb.toString();
    }

    /**
     * 序列化一颗二叉树
     * 所谓序列化不过就是把结构化的数据「打平」，本质就是在考察二叉树的遍历方式。
     * 二叉树的遍历方式有哪些？递归遍历分为分为前、中、后序；迭代方式一般都是层级遍历。
     * 
     * @param root
     * @return
     */
    private void serializeCodecPre(TreeNode root, StringBuilder sb) {
        // 使用前序遍历
        if (root == null) {
            sb.append(NULL).append(SEP);
            return;
        }
        sb.append(root.val).append(SEP);
        serializeCodecPre(root.left, sb);
        serializeCodecPre(root.right, sb);
    }

    /*
     * 后续遍历解法
     * 
     */
    private void serializeCodecPost(TreeNode root, StringBuilder sb) {
        if (root == null) {
            sb.append(NULL).append(SEP);
            return;
        }
        serializeCodecPost(root.left, sb);
        serializeCodecPost(root.right, sb);
        sb.append(root.val).append(SEP);
    }

    /**
     * 水平方向序列化二叉树
     * 
     * @param root
     * @param sb
     */
    private void serializeCodecLevel(TreeNode root, StringBuilder sb) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int sz = queue.size();
            for (int i = 0; i < sz; i++) {
                TreeNode node = queue.poll();
                if (node == null) {
                    sb.append(NULL).append(SEP);
                    continue;
                }
                sb.append(node.val).append(SEP);
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
    }

    /*
     * 如何反序列化那？
     * 首先我们知道，序列化是前序遍历得到的，那么反序列化自然也是前序遍历
     * 反序列化就是序列化的回放。
     * 默认使用前序的遍历结果反序列化
     */

    TreeNode deserializeToTreeCodec(String serializedTree) {
        return deserializeToTreeCodec(serializedTree, 1);
    }

    /**
     * 
     * @param serializedTree
     * @param way            反序列化遍历方式，1：前序，2：中序，3：后序，4：水平
     * @return
     */
    TreeNode deserializeToTreeCodec(String serializedTree, int way) {
        LinkedList<String> valList = new LinkedList<>();
        String[] splitArr = serializedTree.split(",");
        valList.addAll(Arrays.asList(splitArr));
        if (way == 1)
            return deserializeToTreeCodecPre(valList);
        else if (way == 3) {
            return deserializeToTreeCodecPost(valList);
        } else if (way == 4) {
            return deserializeToTreeCodecLevel(splitArr);
        }
        return null;
    }

    TreeNode deserializeToTreeCodecPre(LinkedList<String> valueList) {
        if (valueList.isEmpty()) {
            return null;
        }
        String str = valueList.removeFirst();
        if (str.equals(NULL)) {
            return null;
        }
        TreeNode root = new TreeNode(Integer.parseInt(str));
        root.left = deserializeToTreeCodecPre(valueList);
        root.right = deserializeToTreeCodecPre(valueList);
        return root;
    }

    /**
     * 采用后续遍历的结果来反序列化二叉树
     * 后序遍历的结果是，集合最后一个元素是根节点的值，然后从后到前依次是右子树和左子树，所以反序列化的顺序不能错了
     * 
     * @param valueList
     * @return
     */
    TreeNode deserializeToTreeCodecPost(LinkedList<String> valueList) {
        if (valueList.isEmpty()) {
            return null;
        }
        // 从后往前
        String valStr = valueList.removeLast();
        if (NULL.equals(valStr)) {
            return null;
        }
        TreeNode root = new TreeNode(Integer.parseInt(valStr));
        // 先反序列化右子树，再反序列化左子树
        root.right = deserializeToTreeCodecPost(valueList);
        root.left = deserializeToTreeCodecPost(valueList);
        return root;
    }

    /*
     * 中序遍历的方式行不通，因为无法实现反序列化放 deserialize
     * 序列化方法 serialize 很容易，在中序位置调用 sb.append(root.val).append(SEP);
     * 但是如果实现反序列化，要首先构造 root 节点。前序遍历得到的 nodes 列表中，第一个元素是 root 节点的值；
     * 后续遍历得到的 nodes 列表中，最后一个元素是 root 节点值。然而，中序遍历的代码，root 的值被夹在两棵子树中间，也就是 nodes
     * 列表中间，
     * 我们不知道确切的索引位置，所以无法找到 root 节点，也就无法正常地反序列化。
     */

    TreeNode deserializeToTreeCodecLevel(String[] valueArr) {
        if (valueArr == null || valueArr.length < 1) {
            return null;
        }
        int rootVal = Integer.parseInt(valueArr[0]);
        TreeNode root = new TreeNode(rootVal);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int index = 1;// 0 的位置已经被 root 占用
        while (!queue.isEmpty()) {
            int sz = queue.size();
            for (int i = 0; i < sz; i++) {
                TreeNode node = queue.poll();
                String val = valueArr[index++];
                // 左子节点， 如果为 null ，不需要设置 node.left
                if (!NULL.equals(val)) {
                    TreeNode leftNode = new TreeNode(Integer.parseInt(val));
                    node.left = leftNode;
                    queue.offer(leftNode);
                }
                // 右子节点
                val = valueArr[index++];
                if (!NULL.equals(val)) {
                    TreeNode righNode = new TreeNode(Integer.parseInt(val));
                    node.right = righNode;
                    queue.offer(righNode);
                }
            }
        }
        return root;
    }

    public static void main(String[] args) {
        SerializeBinaryTree instance = new SerializeBinaryTree();
        // findDuplicateSubTree(instance);
        TreeNode root = new TreeNode(1);
        TreeNode left = new TreeNode(2);
        TreeNode right = new TreeNode(3, new TreeNode(4), new TreeNode(5));
        root.left = left;
        root.right = right;
        int way = 4;
        String serializedVal = instance.serializeCodec(root, way);
        System.out.println(serializedVal);
        TreeNode deserializedRoot = instance.deserializeToTreeCodec(serializedVal, way);
        BinaryTreeArch.printBinaryTreeSimple(deserializedRoot);
        System.out.println();
    }

    private static void findDuplicateSubTree(SerializeBinaryTree instance) {
        TreeNode root = new TreeNode(1);

        TreeNode left = new TreeNode(2, new TreeNode(4), null);
        TreeNode right = new TreeNode(3,
                new TreeNode(2, new TreeNode(4), null),
                new TreeNode(4));
        root.left = left;
        root.right = right;

        List<TreeNode> nodeList = instance.findDuplicateTree(root);
        for (TreeNode node : nodeList) {
            BinaryTreeArch.printBinaryTreeSimple(node);
            System.out.println();
        }
    }
}
