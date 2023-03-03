package org.swj.leet_code.string;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/03/03 22:13
 * 力扣第 5 题，最长回文字符串
 * 回文： 所谓回文，是指正读和反读都是一样的字符串，比如madam,我爱我， 上海自来水来自海上
 * 黄河小浪底浪小河黄，前门出租车租出门前，黄山落叶松叶落山黄，山东落花生花落东山
 * 思路也是来自 hellMac 项目的数据结构一节的最长回文长度
 */
public class MaxPalindromeString {
  public String longestPalindrome(String s) {
    if (s == null || s.isEmpty()) {
      return null;
    }
    int startIndex = 0;
    int maxLength = 0;
    int currLength = 0;
    for (int i = 0, len = s.length(); i < len; i++) {
      for (int j = 0; j <= i; j++) {
        // 如果当前回文的长度是奇数的，以 i 为中心
        if ((i + j) < len && s.charAt(i - j) == s.charAt(i + j)) {
          currLength = j * 2 + 1;
        } else {
          // 否则不是回文
          break;
        }
      }
      if (currLength > maxLength) {
        maxLength = currLength;
        //奇数长度求 startIndex
        startIndex = i - maxLength / 2;
      }
      // 以 i 为中心的偶数的回文长度
      for (int j = 0; j <= i; j++) {
        if ((i + j + 1) < len && s.charAt(i - j) == s.charAt(i + j + 1)) {
          currLength = j * 2 + 2;
        } else {
          // 否则不是回文
          break;
        }
      }
      if (currLength > maxLength) {
        maxLength = currLength;
        startIndex = (i + 1) - maxLength / 2;
        //偶数长度求 startIndex
      }
    }
    if (startIndex < 0 || startIndex + maxLength > s.length()) {
      return s;
    }
    return s.substring(startIndex, maxLength + startIndex);
  }

  public static void main(String[] args) {
    String ss = "cbbd";
    MaxPalindromeString mps = new MaxPalindromeString();
    /*System.out.println(mps.longestPalindrome(ss));
    ss = "caba";
    System.out.println(mps.longestPalindrome(ss));
    ss = "babad";
    System.out.println(mps.longestPalindrome(ss));*/

    ss = "aacabdkacaa";
    System.out.println(mps.longestPalindrome(ss));
  }
}
