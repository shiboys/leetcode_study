package org.swj.leet_code.algorithm.dynamic_programming.subsequence;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/04 19:14
 *        最长回文子序列
 *        leetcode 516 题 和 1312 题
 *        回文字符串 比如 上海自来水来自海上
 *        但是这里要求是回文子序列，中间可以有不是回文的字符串，这跟回文字符串也不一样，
 *        firstMac 项目中有个 com.swj.ics.dataStructure.strings.Palindrome 算法
 *        实现了判断是否是回文，以及返回回文子字符串的方法，当时写的还是感觉很精巧的，但是跟今天的
 *        动态规划一比，感觉弱爆了，不得不感叹动态规划的强大
 * 
 */
public class LongestPalindromeSubSeq {

  /**
   * 使用空间复杂度降维方式的回文子序列方法来实现将 dp[i][j] 压缩投影到 dp[j]
   * @param s
   * @return
   */
  int longestPalindromeSubseq(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    int n = s.length();
    int[] dp = new int[s.length()];
    // base case 0, dp[i][i] 初始化为 1
    Arrays.fill(dp, 1);
    for (int i = n - 2; i >= 0; i--) {
      int pre = 0;// pre 每次都初始化为 0，也是 base case 之一
      for (int j = i + 1; j < n; j++) {
        int temp = dp[j];
        // 状态转移方程
        if (s.charAt(i) == s.charAt(j)) {
          dp[j] = pre + 2;
        } else {
          dp[j] = Math.max(dp[j], dp[j - 1]);
        }
        
        pre = temp;
        // pre 就是下次循环的里面的 dp[i+1][j-1]
      }
    }
    return dp[n - 1];
  }

  /**
   * 使用 dp table 来解决回文子序列
   * @param s
   * @return
   */
  int longestPalindromeSubseqDpDoubleArray(String s) {
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
      for (int j = i + 1; j < n; j++) {// j==i 的位置值为 1
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

  /**
   * 将一个字符串变成回文最少需要几步
   * @param s
   * @return
   */
  int minInsertion(String s) {
    int n = s.length();
    int[][] dp = new int[n][n];
    for (int i = 0; i < n; i++) {
      dp[i][i] = 0;
    }
    // 状态转移方程
    for (int i = n - 1; i >= 0; i--) {
      for (int j = i + 1; j < n; j++) {
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
    return s.length() - longestPalindromeSubseqDpDoubleArray(s);
  }

  /**
   * 使用普通的 for 循环来判断回文子序列的长度
   *  todo:leetcode 的用例 「bbbab」没有通过，尴尬了！！！调试下
   * @param s
   * @return
   */
  int longestPalindromeSubSeqNoDp(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    int n = s.length();
    int max = 0,tempMax=0;
    for (int i = 0; i < n; i++) {
      // 以 i 为中心，如果回文长度是奇数，并判断是否回文，进而取得回文长度
      // abcea
      for (int j = 0; j <= i && i + j < n; j++) {
        if (s.charAt(i - j) == s.charAt(i + j)) { // 遇到相等则求回文长度
          max = Math.max(max, j * 2 + 1); // j 在这里不是作为 字符索引，不能理解为 charAt(j),就是一个普通变量, 表示一段长度为 j
        } else { // 不相等的字符，则跳出本次内层循环
          break;
        }
      }
      max = Math.max(max, tempMax);
      // 回文长度为 偶数
      for (int j = 0; j <= i && i + j < n - 1; j++) {
        if (s.charAt(i - j) == s.charAt(i + j + 1)) {
          max = Math.max(max,j * 2 + 2);
        } else {
          break;
        }
      }
    }
    return max;
  }

  public static void main(String[] args) {
    String s = "sssss";
    LongestPalindromeSubSeq instance = new LongestPalindromeSubSeq();
    System.out.println(instance.longestPalindromeSubSeqNoDp(s));
    s = "acaaba";
    System.out.println(instance.longestPalindromeSubSeqNoDp(s));
    System.out.println("min insertion:");
    System.out.println(instance.minInsertion(s));
    System.out.println(instance.minInsertion2(s));
    s = "bbbab";
    System.out.println(instance.longestPalindromeSubseqDpDoubleArray(s));
    System.out.println(instance.longestPalindromeSubSeqNoDp(s));
    s="leetcode"; // ece 是回文子序列
    System.out.println(instance.longestPalindromeSubseqDpDoubleArray(s));
  }
}
