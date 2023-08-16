package org.swj.leet_code.algorithm.dynamic_programming.playing_games;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/13 14:50
 *        使用动态规划解析简单的正则表达式
 *        leetcode 第 10 题
 */
public class RegexWithDp {
    int[][] memo;

    boolean isMatch(String s, String p) {
        memo = new int[s.length() + 1][p.length() + 1];
        for (int[] arr : memo) {
            Arrays.fill(arr, -1);
        }
        return dp(s, 0, p, 0);
    }

    boolean dp(String s, int i, String p, int j) {
        int m = s.length();
        int n = p.length();
        if (j == n) {
            return i == m;
        }
        if (i == m) { // s 已经匹配完毕，p 是否匹配完毕
            if (j == n) {
                return true;
            } else {
                // 这种情况下，要看 s 的结尾是否 a*b*c* 这种形式
                int llen = n - j;
                if ((llen & 1) != 0) { // 不是偶数
                    return false;
                }
                char[] lcharArr = p.substring(j).toCharArray();
                for (int k = 0, len = lcharArr.length; k + 1 < len; k += 2) {
                    if (lcharArr[k + 1] != '*') {
                        return false;
                    }
                }
                return true;
            }
        }

        if (memo[i][j] != -1) {
            return memo[i][j] == 1 ? true : false;
        }
        boolean res = false;
        // 匹配
        if (s.charAt(i) == p.charAt(j) || p.charAt(j) == '.') {
            // p[j+1] 是否是 *
            if (j < p.length() - 1 && p.charAt(j + 1) == '*') {
                res = dp(s, i + 1, p, j) // 匹配 多 次
                        || dp(s, i, p, j + 2); // 匹配 0 次
            } else {
                // 老老实实匹配
                i++;
                j++;
                res = dp(s, i, p, j);
            }
        } else {
            // 仍然需要判断 p[j+1] 是否是 *
            if (j < p.length() - 1 && p.charAt(j + 1) == '*') {
                // 匹配 0 次
                res = dp(s, i, p, j + 2);
            } else {
                res = false;
            }
        }
        memo[i][j] = res ? 1 : 0;
        return res;
    }

    public static void main(String[] args) {
        RegexWithDp instance = new RegexWithDp();
        String s = "aa";
        String p = "a";
        System.out.println(instance.isMatch(s, p));

        p = "a*";
        System.out.println(instance.isMatch(s, p));

        s = "ab";
        p = ".*";
        System.out.println(instance.isMatch(s, p));
    }
}
