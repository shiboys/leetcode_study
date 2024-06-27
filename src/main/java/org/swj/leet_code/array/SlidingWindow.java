package org.swj.leet_code.array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlidingWindow {

    /**
     * 最小覆盖字符串，leetcode 第 76 题
     *
     * @param s 原字符串
     * @param t 目标字符串
     * @return 匹配的最小覆盖字符串
     */
    String minWindow(String s, String t) {
        // 目标字符及其出现次数的统计
        Map<Character, Integer> need = new HashMap<>();
        // 窗口内字符以及字符个数统计
        Map<Character, Integer> window = new HashMap<>();
        for (char ch : t.toCharArray()) {
            need.put(ch, need.getOrDefault(ch, 0) + 1);
        }

        int left = 0, right = 0;
        int slen = s.length();
        int valid = 0;
        int minLen = Integer.MAX_VALUE;
        int startIndex = 0;
        while (right < slen) {
            // 当前字符加入窗口
            Character ch = s.charAt(right);
            if (need.containsKey(ch)) {
                window.put(ch, window.getOrDefault(ch, 0) + 1);
                // 如果当前字符在 t 和 窗口中的数量一致，则表示匹配成功一个字符
                if (window.get(ch).equals(need.get(ch))) {
                    valid++;
                }
            }
            right++;

            while (valid == need.size()) { // 字符个数和每个字符的数量都匹配上了
                // 求最短匹配字符串
                int len = right - left;
                if (len < minLen) {
                    startIndex = left;
                    minLen = len;
                }
                // 缩小窗口
                Character d = s.charAt(left);
                left++;
                // 将 d 字符在窗口内做反向操作
                if (need.containsKey(d)) {
                    // 反向操作，步骤也得是反向的。
                    if (window.get(d).equals(need.get(d))) {
                        valid--;
                    }
                    window.put(d, window.get(d) - 1);
                }

            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(startIndex, startIndex + minLen);
    }

    /**
     * leetcode 567 题 字符串 s2 中包含 s1 的异位词；或者叫 s1 的排列之一是 s2 的子串。
     * 输入：s1 = "ab" s2 = "eidbaooo"
     * 输出：true
     * 解释：s2 包含 s1 的排列之一 ("ba").
     * 
     * @param s1
     * @param s2
     * @return
     */
    public boolean checkInclusion(String s1, String s2) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();
        for (Character ch : s1.toCharArray()) {
            need.put(ch, need.getOrDefault(ch, 0) + 1);
        }
        int left = 0, right = 0;
        int valid = 0;
        int s2len = s2.length();
        int s1len = s1.length();
        while (right < s2len) {
            Character ch = s2.charAt(right);
            right++;
            if (need.containsKey(ch)) {
                window.put(ch, window.getOrDefault(ch, 0) + 1);
                if (window.get(ch).equals(need.get(ch))) {
                    valid++;
                }
            }

            while (right - left >= s1len) {
                if (valid == need.size()) {
                    return true;
                }
                // 缩小窗口
                Character d = s2.charAt(left);
                left++;
                if (need.containsKey(d)) {
                    int windowDSize = window.get(d);
                    if (need.get(d).equals(windowDSize)) {
                        valid--;
                    }
                    window.put(d, windowDSize - 1);
                }
            }
        }
        return false;
    }

    /**
     * leetcode 438 题，寻找字符串中所有字母异位词
     * 
     * @param s
     * @param t
     * @return
     */
    List<Integer> findAnagrams(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();
        for (Character ch : t.toCharArray()) {
            need.put(ch, need.getOrDefault(ch, 0) + 1);
        }

        int left = 0, right = 0;
        int valid = 0;
        int slen = s.length();
        int tlen = t.length();
        List<Integer> res = new ArrayList<>();
        while (right < slen) {
            Character ch = s.charAt(right);
            right++;
            if (need.containsKey(ch)) {
                window.put(ch, window.getOrDefault(ch, 0) + 1);
                if (window.get(ch).equals(need.get(ch))) {
                    valid++;
                }
            }

            while (right - left >= tlen) {
                // 符合条件时，把起始索引加入 res
                if (valid == need.size()) {
                    res.add(left);
                }
                Character d = s.charAt(left);
                left++;
                if (need.containsKey(d)) {
                    if (window.get(d).equals(need.get(d))) {
                        valid--;
                    }
                    window.put(d, window.get(d) - 1);
                }
            }
        }
        return res;
    }

    /**
     * leetcode 3.最长不重复的子序列
     *
     * @param s
     * @return
     */
    int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> window = new HashMap<>();
        int left = 0, right = 0;
        int slen = s.length();
        int rlen = 0;
        while (right < slen) {
            Character ch = s.charAt(right);
            window.put(ch, window.getOrDefault(ch, 0) + 1);
            right++;
            // 如果遇到了重复字符，则缩小窗口
            while (window.get(ch) > 1) {
                Character d = s.charAt(left);
                left++;
                // 窗口数据更新
                if (window.containsKey(d)) {
                    window.put(d, window.get(d) - 1);
                }
            }
            int len = right - left;
            rlen = Math.max(rlen, len);
        }
        return rlen;
    }

    public static void main(String[] args) {
        String s = "aaaaaaaaaaaabbbbbcdd";
        String t = "abcdd";
        // abbbbbcdd
        SlidingWindow instance = new SlidingWindow();
        System.out.println(instance.minWindowNew(s, t));

        // String s1 = "ab", s2 = "eidbaooo";
        // System.out.println(instance.checkInclusion(s1, s2));
        // s1 = "ab";
        // s2 = "eidboaoo";
        // System.out.println(instance.checkInclusion(s1, s2));
        //
        // s = "cbaebabacd";
        // t = "abc";
        // System.out.println(instance.findAnagrams(s, t));
        //
        // s = "abab";
        // t = "ab";
        // System.out.println(instance.findAnagrams(s, t));
        //
        // s = "abcabcbb";
        // System.out.println(instance.lengthOfLongestSubstring(s));
        // System.out.println(instance.lengthOfLongestSubstring("bbbbb"));
        // System.out.println(instance.lengthOfLongestSubstring("pwwkew"));
    }

    private static final int MAX = Integer.MAX_VALUE;

    public String minWindowNew(String s, String t) {
        // t 中 前 128 个 ascii 字符的字符:数量映射
        final int[] need = new int[128];
        // 目标窗口 字符串中每个字符的统计
        final int[] window = new int[128];
        // t 中不同字符的数量
        int targetCharSize = 0;
        // 获取 s 的子字符串的关键变量
        int minLen = MAX, startIndex = 0;

        // 先遍历 t 中的字符，更新目标统计变量
        char ch;
        for (int i = 0, len = t.length(); i < len; i++) {
            ch = t.charAt(i);
            // 首次遇到该字符
            if (need[ch] == 0) {
                targetCharSize++;
            }
            need[ch]++;
        }
        // 窗口的前后指针
        int left = 0, right = 0;
        int sLen = s.length();
        // 字符数量相等的字符数。
        int matched = 0;
        while (right < sLen) {
            ch = s.charAt(right);
            if (need[ch] > 0) { // 在目标字符串中
                window[ch]++;
                if (window[ch] == need[ch]) {
                    // 匹配到一个字符
                    matched++;
                }
            }
            right++;
            // 当满足覆盖要求时
            while (matched == targetCharSize) {
                char d = s.charAt(left);
                // 更新统计目标
                int strLen = right - left;
                if (strLen < minLen) {
                    // 更新最小覆盖字符串的长度和起始位置
                    minLen = strLen;
                    startIndex = left;
                }
                // 缩小窗口，d 字符被移出窗口，判断 d 字符是否是目标字符
                left++;
                if (need[d] > 0) {
                    if (window[d] == need[d]) { // d 字符数量窗口和 need 将要不一致，这个逻辑必须跟窗口的逻辑相反
                        matched--;// 匹配的数量 --
                    }
                    window[d]--;
                }
            }
        }
        return minLen == MAX ? "" : s.substring(startIndex, startIndex + minLen);
    }
}
