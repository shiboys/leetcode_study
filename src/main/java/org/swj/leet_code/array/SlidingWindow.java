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
     * leetcode 567 题 字符串 s2 中包含 s1 的异位词
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
     * 最长不重复的子序列
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
                //窗口数据更新
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
        System.out.println(instance.minWindow(s, t));

        String s1 = "ab", s2 = "eidbaooo";
        System.out.println(instance.checkInclusion(s1, s2));
        s1 = "ab";
        s2 = "eidboaoo";
        System.out.println(instance.checkInclusion(s1, s2));

        s = "cbaebabacd";
        t = "abc";
        System.out.println(instance.findAnagrams(s, t));

        s = "abab";
        t = "ab";
        System.out.println(instance.findAnagrams(s, t));

        s = "abcabcbb";
        System.out.println(instance.lengthOfLongestSubstring(s));
        System.out.println(instance.lengthOfLongestSubstring("bbbbb"));
        System.out.println(instance.lengthOfLongestSubstring("pwwkew"));
    }
}
