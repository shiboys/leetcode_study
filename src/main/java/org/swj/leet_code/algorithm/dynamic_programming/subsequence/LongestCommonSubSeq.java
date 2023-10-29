package org.swj.leet_code.algorithm.dynamic_programming.subsequence;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/05 22:24
 *        最长公共子序列
 *        leetcode 第 1143，583，712 题
 *        详细截图思路请看考 note.md 有关公共子序列 LCS 相关章节
 */
public class LongestCommonSubSeq {

  int[][] memo;

  int longestCommonSubSequence(String s1, String s2) {
    memo = new int[s1.length()][s2.length()];
    for (int i = 0; i < memo.length; i++) {
      Arrays.fill(memo[i], -1);
    }
    return dp(s1, 0, s2, 0);
  }

  int dp(String s1, int i, String s2, int j) {
    // base case
    if (i == s1.length() || j == s2.length()) {
      return 0;
    }
    if (memo[i][j] != -1) {
      return memo[i][j];
    }
    if (s1.charAt(i) == s2.charAt(j)) {
      memo[i][j] = 1 + dp(s1, i + 1, s2, j + 1);
    } else {
      memo[i][j] = Math.max(
          dp(s1, i, s2, j + 1),
          dp(s1, i + 1, s2, j));
    }
    return memo[i][j];
  }

  /**
   * 采用自底向上的 dp table 数组实现，原理是类似的。
   * 
   * @param s1
   * @param s2
   * @return
   */
  public int longestCommonSubSequence2(String s1, String s2) {
    if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()) {
      return 0;
    }
    int m = s1.length(), n = s2.length();
    int[][] dp = new int[m + 1][n + 1];

    // 状态转移方程，这里的遍历跟 递归的解法多少有点差异，这是因为数组的索引是从 0 开始的，存在索引偏移
    /**
     * 当 S1[j-1] == S2[i-1] , dp[i][j] = dp[i-1][j-1] +1; // 自底向上
     * 
     * 当 S1[j-1] != S2[i-1] , dp[i][j] = max(dp[i][j-1],dp[i-1][j])
     * 跟最小编辑距离的逻辑判断完全相同，执行逻辑不同而已
     */
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        // 编辑距离也是相同的逻辑 if s1[i-1] == s2[j-1] then dp[i][j]=f(dp[i-1][j-1])
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          dp[i][j] = 1 + dp[i - 1][j - 1];
        } else {
          // 至少有 1 个字符不等
          dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
        }
      }
    }
    return dp[m][n];
  }

  public int minimumDeleteSum(String s1, String s2) {
    return dpMinDelSum(s1, 0, s2, 0);
  }

  /**
   * 函数定义为 s1[i..] 和 s2[j..] 的最小删除 ascii 和
   * Input: s1 = "sea", s2 = "eat"
   * Output: 231
   * Input: s1 = "delete", s2 = "leet"
   * Output: 403
   * 
   * @param s1
   * @param i
   * @param s2
   * @param j
   * @return
   */
  int dpMinDelSum(String s1, int i, String s2, int j) {
    int[][] memo = new int[s1.length()][s2.length()];
    int res = 0;
    // base case
    for (int[] arr : memo) {
      Arrays.fill(arr, -1);
    }
    if (i == s1.length()) {
      for (; j < s2.length(); j++) {
        res += s2.charAt(j);
      }
      return res;
    }
    if (j == s2.length()) {
      for (; i < s1.length(); i++) {
        res += s1.charAt(i);
      }
      return res;
    }
    if (memo[i][j] != -1) {
      return memo[i][j];
    }
    if (s1.charAt(i) == s2.charAt(j)) {
      // 如果字符相等，则不需要求删除和
      res += dpMinDelSum(s1, i + 1, s2, j + 1);
    } else {
      // 否则求 ascii 删除和最小的
      res += Math.min(s1.charAt(i) + dpMinDelSum(s1, i + 1, s2, j),
          s2.charAt(j) + dpMinDelSum(s1, i, s2, j + 1));
    }
    memo[i][j] = res;
    return memo[i][j];
  }

  /**
   * 获取最长公共子序列的内容
   * 用 动态规划可以求出最长 LCS 的长度，但是内容目前好像求不出来，只能反推 ？坑爹了
   * 同一个字符会被计算多次
   * 
   * @param s1
   * @param s2
   * @return
   */
  List<Character> getLcsContent(String s1, String s2) {
    int m = s1.length();
    int n = s2.length();
    Set<LcsChar> rs = new LinkedHashSet<>();
    int[][] dp = new int[m + 1][n + 1];

    // base case
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          rs.add(new LcsChar(s1.charAt(i - 1), i - 1));
          dp[i][j] = 1 + dp[i - 1][j - 1];
        } else {
          dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
        }
      }
    }
    for (LcsChar ch : rs) {
      System.out.print(ch + " ");
    }
    System.out.println();
    return rs.stream().map(x -> x.ch).collect(Collectors.toList());
  }

  @AllArgsConstructor
  @NoArgsConstructor
  class LcsChar {
    Character ch;
    int idx;

    @Override
    public boolean equals(Object obj) {
      LcsChar other = (LcsChar) obj;
      return idx == other.idx;
    }

    @Override
    public int hashCode() {
      return ch.hashCode() + idx;
    }

    @Override
    public String toString() {
      return ch + "_" + idx;
    }
  }

  public static void main(String[] args) {
    LongestCommonSubSeq lcs = new LongestCommonSubSeq();
    String s1 = "zabcde";
    String s2 = "acez";
    System.out.println(lcs.longestCommonSubSequence2(s1, s2));
    System.out.println(lcs.getLcsContent(s1, s2));
    s1 = "delete";
    s2 = "leet";
    System.out.println(lcs.getLcsContent(s1, s2));
    System.out.println(lcs.minimumDeleteSum(s1, s2));
  }
}
