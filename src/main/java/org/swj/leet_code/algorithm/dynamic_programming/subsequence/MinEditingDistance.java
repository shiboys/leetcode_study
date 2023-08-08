package org.swj.leet_code.algorithm.dynamic_programming.subsequence;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/29 21:34
 *        编辑距离
 *        leetcode 第 72 题
 *        编辑距离这类问题，因为看起来十分困难，解法却出奇地简单，而且是少有的比较使用的算法(其实很多算法都不怎么实用)
 *        给 2 个额单词 s1 和 s2，请返回将 s1 转换成 s2 最少的操作次数。
 *        你可以对一个单词进行如下 3 种操作：
 *        1、插入一个字符
 *        2、删除一个字符
 *        3、替换一个字符
 *        示例 1：
 *        输入：s1="horse", s2="ros"
 *        输出:3
 *        hourse->rorse(h->r)
 *        rorse->rose(删除 r)
 *        rose->ros(删除 e)
 *        示例2：
 *        Input: word1 = "intention", word2 = "execution"
 *        Output: 5
 *        Explanation:
 *        intention -> inention (remove 't')
 *        inention -> enention (replace 'i' with 'e')
 *        enention -> exention (replace 'n' with 'x')
 *        exention -> exection (replace 'n' with 'c')
 *        exection -> execution (insert 'u')
 * 
 *        为什么说这个问题难那？因为显而易见，它就是难，让人无法下手，望而生畏
 *        下面说下思路，了解下编辑距离怎么算，既然是属于动态规划范畴的，肯定少不了动态规划三板斧->dp,basecase, 数学归纳法
 * 
 */
public class MinEditingDistance {

    /**
     * 编辑距离问题是给我们两个字符串 s1 和 s2，它只能有 3 种操作，让我们把 s1 变成 s2，求最少的操作数。需要明确的是，不管把
     * s1 变成 s2 还是反过来，结果都是一样的。
     * 最长公共子序列 后面会讲过，解决两个字符串的动态规划问题，一般都是用两个指针i,j 分别指向两个字符串的最后，然后一步步往前移动，缩小问题的规模
     * (从后向前)。具体的步骤说明以及图片展示，请参考 note.md 中有关编辑距离部分
     */

    /**
     * 
     * @param s1
     * @param i  第一个字符的指针
     * @param s2
     * @param j  第二个字符的当前指针
     * @return
     */
    int dp(String s1, int i, String s2, int j) {
        // basecase
        if (i < 0) {
            return j + 1;
        }
        if (j < 0) {
            return i + 1;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        if (s1.charAt(i) == s2.charAt(j)) {
            memo[i][j] = dp(s1, i - 1, s2, j - 1);
        } else {
            memo[i][j] = min(
                    dp(s1, i - 1, s2, j) + 1, // s1 前进一步 s2不动，代表删除 s[i]
                    dp(s1, i, s2, j - 1) + 1, // s1 不动，s2 前进一步，代表插入s1 的 i+1 位置
                    dp(s1, i - 1, s2, j - 1) + 1 // 两者都动代表替换
            );
        }
        return memo[i][j];
    }

    private int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    int[][] memo;

    public int minDistance(String word1, String word2) {
        // 使用备忘录消除子问题
        memo = new int[word1.length()][word2.length()];
        for (int[] arr : memo) {
            Arrays.fill(arr, -1);
        }
        return dp(word1, word1.length() - 1, word2, word2.length() - 1);
    }

    public static void main(String[] args) {
        MinEditingDistance instance = new MinEditingDistance();
        String word1 = "horse";
        String word2 = "ros";
        System.out.println(instance.minDistance(word1, word2));
        word1 = "intention";
        word2 = "execution";
        System.out.println(instance.minDistance(word1, word2));
        System.out.println(instance.minDistance_dpTable(word1, word2));
        System.out.println(instance.minDistance_dpNode(word1, word2));
    }

    int minDistance_dpTable(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];
        // base case
        // 相当于 dp 递归的 if(j<0) {return i+1;}
        // 第二个字符串从空字符串改为第一个字符串需要几步，很明显需要 i 步
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        // 相当于 dp 函数递归的 if(i<0) {return j+1;}
        // 同理第一个字符从空字符改为第二个字符需要几步，需要 j 步
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        // 自底向上求解
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = min(
                            1 + dp[i - 1][j], // s1 前进一步，s2 不动 s1 要被删除一个字符
                            1 + dp[i][j - 1], // s1 不动，s2 前进一步，代表插入 s1 的 i+1 位置 s2 删除一个字符
                            1 + dp[i - 1][j - 1] // s1,s2 均前进一步，代表替换
                    );
                }
            }
        }
        return dp[m][n];
    }

    static class Node {
        int val;
        // 0 代表什么也不做
        // 1 代表插入，
        // 2 代表删除
        // 3 代表替换
        int choice;
        int i; // 第一个字符串的位置
        int j; // 第二个字符串的位置
        static final Map<Integer, String> map;

        static {
            map = new HashMap<>();
            map.put(0, "跳");
            map.put(1, "插");
            map.put(2, "删");
            map.put(3, "替");
        }

        public Node(int val, int choice, int i, int j) {
            this.val = val;
            this.choice = choice;
        }

        public Node() {

        }

        String getChoiceStr() {
            return map.containsKey(choice) ? map.get(choice) : "";
        }

        @Override
        public String toString() {
            return val + getChoiceStr();
        }
    }

    Node[][] dpNodeArr;

    int minDistance_dpNode(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        dpNodeArr = new Node[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dpNodeArr[i][0] = new Node(i, -1, i, 0);
        }
        for (int j = 0; j <= n; j++) {
            dpNodeArr[0][j] = new Node(j, -1, 0, j);
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Node node = new
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dpNodeArr[i][j] = getNothingToDoNode(dpNodeArr[i - 1][j - 1]);
                } else {
                    dpNodeArr[i][j] = min(
                            getInsertedNode(dpNodeArr[i][j - 1]),
                            getDeletedNode(dpNodeArr[i - 1][j]),
                            getReplacedNode(dpNodeArr[i - 1][j - 1]));
                }
            }
        }
        // Node finalNode = dpNodeArr[m][n];
        printDpNodeTableInfo(dpNodeArr);
        return dpNodeArr[m][n].val;
    }

    Node getInsertedNode(Node prevNode) {
        // dp[i][j]=1 + dp[i][j - 1]
        Node node = new Node();
        node.val = prevNode.val + 1;
        node.choice = 1;
        // node.i = prevNode.i;
        // node.j = prevNode.j-1;
        return node;
    }

    Node getDeletedNode(Node prevNode) {
        // dp[i][j]=1 + dp[i - 1][j]
        Node node = new Node();
        node.val = prevNode.val + 1;
        node.choice = 2;
        // node.i= prevNode.i-1;
        // node.j=prevNode.j;
        return node;
    }

    Node getReplacedNode(Node prevNode) {
        Node node = new Node();
        node.val = prevNode.val + 1;
        node.choice = 3;
        // node.i= prevNode.i-1;
        // node.j=prevNode.j-1;
        return node;
    }

    Node getNothingToDoNode(Node prevNode) {
        Node node = new Node();
        node.val = prevNode.val;
        node.choice = 0;
        return node;
    }

    Node min(Node... nodeArray) {
        Arrays.sort(nodeArray, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.val - o2.val;
            }
        });
        return nodeArray[0];
    }

    void printDpNodeTableInfo(Node[][] dpNodeArr) {
        int llen = dpNodeArr[0].length;
        for (int i = 0; i < dpNodeArr.length; i++) {
            for (int j = 0; j < llen; j++) {
                if (j == 0) {
                    System.out.print(i + "\t");
                }
                if (dpNodeArr[i][j] != null) {
                    System.out.print(dpNodeArr[i][j]);
                } else {
                    System.out.print("NULL");
                }
                if (j < llen) {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }
    }

}