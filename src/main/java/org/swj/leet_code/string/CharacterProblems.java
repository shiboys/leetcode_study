package org.swj.leet_code.string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class CharacterProblems {

    /**
     * 387. 字符串中的第一个唯一字符
     * 输入: s = "loveleetcode"
     * 输出: 2
     * 
     * @param s
     * @return
     */
    public int firstUniqChar(String s) {
        int right = 0, left = -1;
        // s 只包含小写字符
        int[] charIdx = new int[26];
        char[] chs = s.toCharArray();
        char ch;
        while (right < chs.length) {
            ch = chs[right];
            charIdx[ch - 'a']++;
            right++;
        }
        for (int i = 0; i < chs.length; i++) {
            if (charIdx[chs[i] - 'a'] == 1) {
                return i;
            }
        }
        // 没找到
        return -1;
    }

    /**
     * @deprecated
     *             解法2是错误的
     */
    public int firstUniqChar2(String s) {
        int right = s.length() - 1, left = -1;
        // s 只包含小写字符
        int[] charIdx = new int[26];
        char[] chs = s.toCharArray();
        char ch;
        while (right >= 0) {
            ch = chs[right];
            charIdx[ch - 'a']++;
            if (charIdx[ch - 'a'] == 1) {
                left = right;
            }
            right--;
        }
        // for (int i = 0; i < chs.length; i++) {
        // if (charIdx[chs[i] - 'a'] == 1) {
        // return i;
        // }
        // }
        // 没找到
        return left;
    }

    /**
     * leetcode 763. 划分字母区间
     * 给你一个字符串 s 。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。
     * 
     * 注意，划分结果需要满足：将所有划分结果按顺序连接，得到的字符串仍然是 s 。
     * 
     * 返回一个表示每个字符串片段的长度的列表。
     * 示例 1：
     * 输入：s = "ababcbacadefegdehijhklij"
     * 输出：[9,7,8]
     * 解释：
     * 划分结果为 "ababcbaca"、"defegde"、"hijhklij" 。
     * 每个字母最多出现在一个片段中。
     * 像 "ababcbacadefegde", "hijhklij" 这样的划分是错误的，因为划分的片段数较少。
     * 示例 2：
     * 
     * 输入：s = "eccbbbbdec"
     * 输出：[10]
     * @deprecated 这个方法时错误的，过不了测试用例
     * @param s
     * @return
     */
    public List<Integer> partitionLabels(String s) {
        Map<Character, LinkedList<Integer>> posMap = new HashMap<>();
        char[] chs = s.toCharArray();
        // 将各个字符的所有位置都记录下来
        for (int i = 0, len = chs.length; i < len; i++) {
            posMap.putIfAbsent(chs[i], new LinkedList<>());
            posMap.get(chs[i]).add(i);
        }
        int pos = 0, rightPos = 0, lastPos = 0;
        List<Integer> res = new ArrayList<>();
        while (pos < chs.length) {
            if ((rightPos = getValidSplitPos(pos, chs, posMap)) > pos) {
                // 本次位置减去上次索引开始的位置
                res.add(rightPos - lastPos + 1);
                pos = lastPos = (rightPos + 1);
            } else {
                pos++;
            }
        }

        return res;
    }

    int getValidSplitPos(int pos, char[] chs, Map<Character, LinkedList<Integer>> posMap) {
        if (posMap.get(chs[pos]).size() == 1) {
            return pos;
        }
        List<Integer> posList = posMap.get(chs[pos]);
        int lastPos = posList.get(posList.size() - 1);

        for (int i = pos + 1; i < lastPos; i++) {
            if (posMap.get(chs[i]).getLast() > lastPos) {
                return pos;
            }
        }
        return lastPos;
    }

    public List<Integer> partitionLabels2(String s) {
        LinkedList<Integer>[] chPosArr = new LinkedList[26];
        char[] chs = s.toCharArray();
        // 将各个字符的所有位置都记录下来
        int idx;
        for (int i = 0, len = chs.length; i < len; i++) {
            idx = chs[i] - 'a';
            if (chPosArr[idx] == null) {
                chPosArr[idx] = new LinkedList<>();
                chPosArr[idx].add(i);
            } else {
                if (chPosArr[idx].size() > 1) {
                    chPosArr[idx].set(1, i);
                } else {
                    chPosArr[idx].add(i);
                }
            }
        }
        int pos = 0, rightPos = 0, lastPos = 0;
        List<Integer> res = new ArrayList<>();
        while (pos < chs.length) {
            if ((rightPos = getValidSplitPos(pos, rightPos, chs, chPosArr)) > pos) {
                // 本次位置减去上次索引开始的位置
                if (isValidLastPos(pos, lastPos, chs, chPosArr)) {
                    // 分割为两个
                    res.add(pos - lastPos);
                    res.add(rightPos - pos + 1);
                } else {
                    res.add(rightPos - lastPos + 1);
                }
                pos = lastPos = (rightPos + 1);
            } else {
                pos++;
            }
        }
        // 尾部字符串
        if (rightPos < chs.length - 1) {
            res.add(chs.length - 1 - rightPos);
        }

        return res;
    }

    int getValidSplitPos(int pos, int rightPos, char[] chs, LinkedList<Integer>[] chPosArr) {
        int idx = chs[pos] - 'a';
        if (chPosArr[idx].size() == 1) {
            // 返回上一次发现正确重复字符的位置
            return rightPos;
        }
        int lastPos = chPosArr[idx].getLast();

        for (int i = pos + 1; i < lastPos; i++) {
            if (chPosArr[chs[i] - 'a'].getLast() > lastPos) {
                return rightPos;
            }
        }
        return lastPos;
    }

    // pos 和 lastPos 之间是否可以在分割
    boolean isValidLastPos(int pos, int lastPos, char[] chs, LinkedList<Integer>[] chPosArr) {
        int idx = chs[pos] - 'a';
        int rightPos = chPosArr[idx].getLast();
        // pos ... rightPos 之间包含 lastPos...pos 之间的字符，则不符合规定
        for (int i = lastPos; i < pos; i++) {
            if (chPosArr[chs[i] - 'a'].getLast() > pos && chPosArr[chs[i] - 'a'].getLast() < rightPos) {
                return false;
            }
        }
        // 符合规定，需要分割为 lastPos,pos 和 pos...rightPos
        return pos > lastPos;
    }

    public static void main(String[] args) {
        String s = "loveleetcode";
        CharacterProblems cp = new CharacterProblems();
        // System.out.println(cp.firstUniqChar2(s));
        // System.out.println(cp.firstUniqChar2("leetcode"));
        // System.out.println(cp.firstUniqChar2("aabb"));

        // System.out.println(cp.partitionLabels2("ababcbacadefegdehijhklij"));
        // System.out.println(cp.partitionLabels2("eccbbbbdec"));
        // System.out.println(cp.partitionLabels2("caedbdedda"));
        // System.out.println(cp.partitionLabels2("cfaedbdedda"));
        // System.out.println(cp.partitionLabels2("eaaaabaaec"));

        System.out.println(cp.partitionLabels2("aebbedaddc"));
    }
}
