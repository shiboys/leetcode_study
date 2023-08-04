package org.swj.leet_code.algorithm.dynamic_program.subsequence;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/04 19:14
 *        最长回文字符串
 *        leetcode 516 题 和 1312 题
 *        回文字符串 比如 上海自来水来自海上
 *        但是这里要求是回文子序列，中间可以有不是回文的字符串，这跟回文字符串也不一样，
 *        firstMac 项目中有个 com.swj.ics.dataStructure.strings.Palindrome 算法
 *        实现了判断是否是回文，以及返回回文子字符串的方法，当时写的还是感觉很精巧的，但是跟今天的
 *        动态规划一比，感觉弱爆了，不得不感叹动态规划的强大
 * 
 */
public class LongestPalindromeSubSeq {

  int longestPalindromeSubeq(String s) {
    int n = s.length();
    // base case：一维 dp 数组全部初始化为 1
    int[] dp = new int[n];
    Arrays.fill(dp, 1);

    for (int i = n - 2; i >= 0; i--) {
      int pre = 0;
      for (int j = i + 1; j < n; j++) {
        int temp = dp[j];
        // 状态转移方程
        if (s.charAt(i) == s.charAt(j))
          dp[j] = pre + 2;
        else
          dp[j] = Math.max(dp[j], dp[j - 1]);
        pre = temp;
      }
    }
    return dp[n - 1];
  }

  int longestPalindromeSubeqDpDoubleArray(String s) {
    int n = s.length();
    int[][] dp = new int[n][n];
    for (int i = 0; i < n; i++) {
      // 同一个字符的 dp[i..i] 肯定是 1
      dp[i][i] = 1;
    }
    // 状态转移方程核心
    // 总体从下向上遍历，i--
    for (int i = n - 1; i >= 0; i--) {
      // 列的遍历方向为正常的从左至右，从小到大哦
      for (int j = i + 1; j < n; j++) {
        if (s.charAt(i) == s.charAt(j)) {
          dp[i][j] = dp[i + 1][j - 1] + 2;
        } else {
          dp[i][j] = Math.max(dp[i][j - 1], dp[i + 1][j]);
        }
      }
    }
    // 打完收工
    return dp[0][n - 1];
  }

  int minInsertion(String s) {
    int n = s.length();
    int[][] dp = new int[n][n];
    for (int i = 0; i < n; i++) {
      dp[i][i] = 0;
    }
    // 状态转移方程
    for (int i = n - 1; i >= 0; i--) {
      for (int j = i+1; j < n; j++) {
        if (s.charAt(i) == s.charAt(j)) {
          dp[i][j] = dp[i + 1][j - 1];
        } else {
          dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
        }
      }
    }
    return dp[0][n - 1];
  }

  int minInsertion2(String s) {
    return s.length() - longestPalindromeSubeqDpDoubleArray(s);
  }

  public static void main(String[] args) {
    String s = "sssss";
    LongestPalindromeSubSeq instance = new LongestPalindromeSubSeq();
    System.out.println(instance.longestPalindromeSubeqDpDoubleArray(s));
    s = "abcea";
    System.out.println(instance.longestPalindromeSubeqDpDoubleArray(s));
    System.out.println("min insertion:");
    System.out.println(instance.minInsertion(s));
    System.out.println(instance.minInsertion2(s));
    s ="abcdefg";
    System.out.println(instance.minInsertion(s));
    System.out.println(instance.minInsertion2(s));
    System.out.println(instance.longestPalindromeSubeqDpDoubleArray(s));
  }
}
