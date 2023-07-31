package org.swj.leet_code.algorithm.dynamic_program;

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
        if (s1.charAt(i) == s2.charAt(j)) {
            return dp(s1, i - 1, s2, j - 1);
        }
        return min(
                dp(s1, i - 1, s2, j) + 1, // s1 前进一步 s2不懂，代表删除 s[i]
                dp(s1, i, s2, j - 1) + 1, // s1 不动，s2 前进一步，代表插入s1 的 i+1 位置
                dp(s1, i - 1, s2, j - 1) + 1 // 两者都动代表替换
        );
    }

    private int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    public int minDistance(String word1, String word2) {
        return dp(word1, word1.length()-1, word2, word2.length()-1);
    } 

    public static void main(String[] args) {
        MinEditingDistance instance = new MinEditingDistance();
        String word1="horse";
        String word2="ros";
        System.out.println(instance.minDistance(word1, word2));
        word1="intention";
        word2="execution";
        System.out.println(instance.minDistance(word1, word2));
    }
}