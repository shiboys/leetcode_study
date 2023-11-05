package org.swj.leet_code.algorithm.dfs_bfs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.swj.leet_code.binary_tree.TreeNode;

/**
 * BFS 框架的使用
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/20 11:29
 */
public class BfsUsage {

    /**
     * 二叉树的最小深度。leetcode 111 题
     * 给定一个二叉树，找出其最小深度。
     * 最小深度是从根节点到最近叶子节点的最短路径上的节点数量
     * 
     * @param root
     * @return
     */
    public int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        AtomicInteger depthCounter = new AtomicInteger();
        traverseBinaryTreeHorizonially(root, depthCounter);
        return depthCounter.get();
    }

    void traverseBinaryTreeHorizonially(TreeNode root, AtomicInteger depthCounter) {
        if (root == null) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            // 深度增加
            depthCounter.incrementAndGet();
            for (int i = 0, len = queue.size(); i < len; i++) {
                TreeNode node = queue.poll();
                // 找到子节点
                if (node.left == null && node.right == null) {
                    return;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

        }
    }

    /**
     * 双向 BFS 解决密码锁的问题
     * @param deadends
     * @param target
     * @return
     */
    public int openLockDual(String[] deadends, String target) {
        Set<String> deadendsSet = new HashSet<>(Arrays.asList(deadends));
        Set<String> s1 = new HashSet<>();
        Set<String> s2 = new HashSet<>();
        s1.add("0000");// 从开头开始遍历的集合
        s2.add(target); // 从结尾开始遍历的集合
        int step = 0;
        Set<String> visitedSet = new HashSet<>();
        // visitedSet.add("0000");
        // 不再使用队列
        while (!s1.isEmpty() && !s2.isEmpty()) {
            Set<String> temp = new HashSet<>();
            // temp 用来记录 s1 的节点的衍生变化
            for (String cur : s1) {
                if (deadendsSet.contains(cur)) {
                    continue;
                }
                if (s2.contains(cur)) { // 正反两方向的遍历出现相交节点，就返回步数，就是这么牛逼！
                    return step;
                }
                visitedSet.add(cur);

                for (int i = 0; i < 4; i++) {
                    String plusOne = plusOneString(cur, i);
                    if (!visitedSet.contains(plusOne)) {
                        // visitedSet.add 放这里不行，这里就会提前包含了 s1 和 s2 的交集，导致 s1 和 s2 无法相交，最后返回 -1。
                        // 取出来之后才叫 visited,跟队列稍微有些区别
                        // visitedSet.add(plusOne);
                        temp.add(plusOne);
                    }
                    String minusOne = minusOneString(cur, i);
                    if (!visitedSet.contains(minusOne)) {
                        // visitedSet.add(minusOne);
                        temp.add(minusOne);
                    }
                }
            }
            // 增加步数
            step++;
            // 下面是重点
            // 此时 s1 已经被遍历完毕，没有用处，将 s2 赋值给 s1，s2 初始化为只含有 target 字符串的集合，
            // 因此下次遍历从 target 开始反方向遍历
            s1 = s2;
            // 把 temp 赋给 s2，temp 是原来 s1 遍历出来的衍生结果，这里 s2 已经赋给 s1 用来下次遍历，s2 也被腾出来，
            // 可以用来存储/指向 temp 这个 s1的遍历衍生结果集
            s2 = temp;
            // 最终结果就像 s1 和 s2 进行了交换
        }
        return -1;
    }

    /**
     * leetcode 752，打开转盘锁
     * 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8',
     * '9' 。
     * 每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9' 。每次旋转都只能旋转一个拨轮的一位数字。
     * 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串
     * 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
     * 字符串 target 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1
     * 
     * @param deadends
     * @param target
     * @return
     */
    public int openLock(String[] deadends, String target) {

        Set<String> deadendsSet = new HashSet<>(Arrays.asList(deadends));
        return bfsOpenLock(deadendsSet, target);
    }

    int bfsOpenLock(Set<String> deadends, String target) {
        String source = "0000";
        if (Objects.equals(source, target)) {
            return 0;
        }
        Queue<String> queue = new LinkedList<>();
        Set<String> visitedSet = new HashSet<>();
        // 每个密码有 8 个相邻密码，也就是 8 个相邻节点， 
        queue.offer(source);
        visitedSet.add(source);
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            // 像二叉树的深度一样的话，是求的节点说，则 ++ 可以放在这里，而这里求的是步骤，步骤是节点数 -1
            // 因此 ++ 操作需要放在循环后面
            // counter.incrementAndGet();
            for (int i = 0; i < size; i++) {
                String candidate = queue.poll();
                if (target.equals(candidate)) {
                    return step;
                }
                if (deadends.contains(candidate)) {
                    continue;
                }
                // 否则，将 candidate 可以变化的 8 中类型加队队列。比如 '0000'的 8 中变化
                // 1000,9000,0100,0900,0010,0090,0001,0009
                char[] canCharArr = candidate.toCharArray();
                for (int j = 0; j < 4; j++) {
                    char oldChar = canCharArr[j];
                    char plusOneChar = lockPlusOne(oldChar);
                    char minusOneChar = lockMinusOne(oldChar);
                    // // 一次放入一个字符的两种方向旋转的变化组成的字符串
                    canCharArr[j] = plusOneChar;
                    // String plusOne = plusOneString(candidate, j);
                    String plusOne = new String(canCharArr);
                    // 遇到死锁字符串或者已经访问过的字符串，则不需要放入队列
                    // 有些建议是 将两个 set 合并成一个，能提升性能，个人感觉这样更好理解
                    if (!visitedSet.contains(plusOne)) {
                        visitedSet.add(plusOne);
                        queue.offer(plusOne);
                    }

                    canCharArr[j] = minusOneChar;
                    // String minusOne = minusOneString(candidate, j);
                    String minusOne = new String(canCharArr);
                    if (!visitedSet.contains(minusOne)) {
                        visitedSet.add(minusOne);
                        queue.offer(minusOne);
                    }
                    // 再将字符还原,以便变换其他位置
                    canCharArr[j] = oldChar;
                }
            }
            // 广度优先 BFS 跑完一层，则表示增加一个步骤
            step++;
        }
        return -1;
    }

    /**
     * leetcode 773 题，「滑动谜题」
     * @param board
     * @return
     */
    public int slidingPuzzle(int[][] board) {
        String target = "123450";
        int[][] neighbors = new int[][] {
                { 1, 3 }, // 2x3 二维数组的 0 位置的上下左右索引在一维空间的映射为 {1,3}, 其中 1 是 右，3 是下
                { 0, 4, 2 }, // 索引 1 的 左，下，右 相邻索引,规律是二维索引从左到右的顺序
                { 1, 5 },
                { 0, 4 },
                { 3, 1, 5 }, // 顺时针
                { 4, 2 }
        };
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.offer(getStringFromBoard(board));
        int step = 0;
        while (!queue.isEmpty()) {
            int sz = queue.size();
            for (int i = 0; i < sz; i++) {
                String candidate = queue.poll();
                if (Objects.equals(candidate, target)) {
                    return step;
                }
                int zeroCharIdx = 0;
                for (int len = candidate.length(); zeroCharIdx < len
                        && candidate.charAt(zeroCharIdx) != '0'; zeroCharIdx++)
                    ;

                for (int neighborIdx : neighbors[zeroCharIdx]) {
                    String changedStr = swapForSlidingPuzzle(candidate, zeroCharIdx, neighborIdx);
                    if (!visited.contains(changedStr)) {
                        visited.add(changedStr);
                        queue.offer(changedStr);
                    }
                }
            }
            // 将 字符 0 和它的相邻位置交换得到新的字符。因为只有字符 0 能和周边部进行移动
            step++;
        }
        return -1;
    }

    private String swapForSlidingPuzzle(String source, int zeroCharIdx, int adjIdx) {
        char[] charArr = source.toCharArray();
        char zeroCh = charArr[zeroCharIdx];
        charArr[zeroCharIdx] = charArr[adjIdx];
        charArr[adjIdx] = zeroCh;
        return new String(charArr);
    }

    // 将 board 元素变成字符串的一维形式，因为 0 <= board[i][j] <= 5
    String getStringFromBoard(int[][] board) {
        int m = board.length;
        int n = board[0].length;

        StringBuilder sb = new StringBuilder(m * n);
        for (int[] arr : board) {
            for (int item : arr) {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    private String plusOneString(String source, int i) {
        char[] chars = source.toCharArray();
        char ch = chars[i];
        if (ch == '9') {
            ch = '0';
        } else {
            ch += 1;
        }
        chars[i] = ch;
        return new String(chars);
    }

    private String minusOneString(String source, int i) {
        char[] chars = source.toCharArray();
        char ch = chars[i];
        if (ch == '0') {
            ch = '9';
        } else {
            ch -= 1;
        }
        chars[i] = ch;
        return new String(chars);
    }

    private char lockPlusOne(char source) {
        if (source == '9') {
            return '0';
        }
        return (char) ((int) source + 1);
    }

    private char lockMinusOne(char source) {
        if (source == '0') {
            return '9';
        }
        return (char) ((int) source - 1);
    }

    public static void main(String[] args) {
        BfsUsage instance = new BfsUsage();

        // TreeNode right = new TreeNode(20, new TreeNode(15), new TreeNode(7));
        // TreeNode root = new TreeNode(3, new TreeNode(9), right);

        // System.out.println(instance.minDepth(root));

        // right = new TreeNode(3, null, new TreeNode(4, null, new TreeNode(5, null, new
        // TreeNode(6))));
        // root = new TreeNode(2, null, right);
        // System.out.println(instance.minDepth(root));

        // String source = "abc";
        // char[] target = new char[] { 'a', 'b', 'c' };
        // System.out.println(target.equals(source.toCharArray()));
        // System.out.println(source.equals(new String(target)));

        String[] deadends = new String[] { "0201", "0101", "0102", "1212", "2002" };
        String targetStr = "0202";
        System.out.println(instance.openLockDual(deadends, targetStr));

        // deadends = new String[] { "8888" };
        // targetStr = "0009";
        // System.out.println(instance.openLockDual(deadends, targetStr));

        deadends = new String[] { "0000" };
        targetStr = "8888";
        System.out.println(instance.openLock(deadends, targetStr));
        System.out.println(instance.openLockDual(deadends, targetStr));

        // deadends = new String[] { "8887", "8889", "8878", "8898", "8788", "8988",
        // "7888", "9888" };
        // targetStr = "8888";
        // System.out.println(instance.openLockDual(deadends, targetStr));

        // int[][] board = new int[][] { { 1, 2, 3 }, { 4, 0, 5 } };

        // System.out.println("sliding puzzle steps =" + instance.slidingPuzzle(board));

        // board = new int[][] { { 4, 1, 2 }, { 5, 0, 3 } };
        // System.out.println("sliding puzzle2 steps = " +
        // instance.slidingPuzzle(board));
    }
}
