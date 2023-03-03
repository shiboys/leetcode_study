package org.swj.leet_code.string;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/03/03 11:02 力扣题目3 找出最长子串的长度。
 */

/**
 * 输入: s = "abcabcbb" 输出: 3 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3
 *
 * <p>s输入: s = "pwwkew" 输出: 3 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *
 * <p>请注意，你的答案必须是子串的长度，"pwke" 是一个子序列，不是子串
 *
 * <p>条件： 0 <= s.length <= 5 * 104 s 由英文字母、数字、符号和空格组成
 */
public class LengthOfLongestSubstring {

  /**
   * 算法思路： 启用前后两个指针，后指针用来遍历字符 数组，遍历的时候要把当前字符和字符的位置放入到 一个 Map 中。
   * 判断当前字符是否跟之前的字符重复，如果重复，则计算并保存最大距离，然后更新第一个指针到当前重复字符的后面 1 位。 上述计算都是基于当前已经遍历到的字符的下一个位置来判断的，
   * 所以到最后一个字符之后，需要最后计算一次最大距离，因为最后一个字符串的最大距离并没有计算，
   * 有点滑动窗口的思想在里面
   * @param s 输入的字符串
   * @return 返回字长子字符串的长度
   */
  public int lengthOfLongestSubstring(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    java.util.Map<Character, Integer> container = new java.util.HashMap<>();
    int startIdx = 0;
    int endIdx = 0;
    int subStrLength = 0;
    container.put(s.charAt(0), 0);
    char ch;
    while (endIdx < s.length()) {
      endIdx++;
      if (endIdx == s.length()) {
        break;
      }
      if (container.containsKey((ch = s.charAt(endIdx)))) {
        if (endIdx - startIdx > subStrLength) {
          subStrLength = endIdx - startIdx;
        }
        // 更新开始位置，startIndex 只能前进
        if (startIdx < container.get(ch) + 1) {
          startIdx = container.get(ch) + 1;
        }
      }
      container.put(s.charAt(endIdx), endIdx);
    }
    if (endIdx - startIdx > subStrLength) {
      subStrLength = endIdx - startIdx;
    }
    return subStrLength;
  }

  /**
   * 第二种解法，之前我的 firstMac 数据结构与算法中，采用了面试宝典的解法，LongestSubstringWithoutDuplication 这个，
   * 但是今天看来仍然比较复杂，不太容易理解，但是 其中的节省空间的思路是可以借鉴的，就是用字符数组来代替哈希表来实现 O(1) 的时间内找到某个字符的位置 该种解法就是这个第二个解法？
   */
  public int lengthOfLongestSubstring2(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    int maxLength = 0;
    // 能把所有要求范围内的字符存储进来，实现 O(1) 查找字符位置
    int[] charIndArr = new int[128];
    int startIdx = 0;
    int endIdx = 0;
    int nextIdx = 0;
    int length = s.length();
    char currChar;
    while (endIdx < length) {
      currChar = s.charAt(endIdx);
      if (charIndArr[currChar] > startIdx) {
        // 这里说明是有重复元素了，将 startIdx 移动至重复元素的位置+1处
        startIdx = charIndArr[currChar];
      }
      nextIdx = endIdx + 1;
      maxLength = Math.max(nextIdx - startIdx, maxLength);
      // 数组存储的是当前字符位置的下一个位置，其实存储的是当前字符的距离第一个字符的元素个数或者叫距离
      charIndArr[currChar] = nextIdx;
      endIdx++;
    }
    return maxLength;
  }

  /**
   * 解法 3 是暴力求解
   * 这里主要说下思路：两层 for 循环
   * 两个指针 i，j。j 从 i 开始往后走，每个字符串都存入一个 set 里面，如果 j 此时的字符在 set 里面是重复的，则计算 maxLength = set.size()
   * 然后 break，代表这个 i 这个位置的最长子字符串计算完毕，否则将 j 所在的字符存入 set。
   * 需要注意的是，set 需要在每次 j 的每次循环开始之前重置或者进行 clear
   */

  public static void main(String[] args) {
    String str = "pwwkew";
    LengthOfLongestSubstring ll = new LengthOfLongestSubstring();
    System.out.println(ll.lengthOfLongestSubstring2(str));
    str = "abcabcbb";
    System.out.println(ll.lengthOfLongestSubstring2(str));
    str = "pwwkewx";
    System.out.println(ll.lengthOfLongestSubstring2(str));
    str = "abba";
    System.out.println(ll.lengthOfLongestSubstring2(str));
  }
}
