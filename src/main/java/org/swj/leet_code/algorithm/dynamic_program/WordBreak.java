package org.swj.leet_code.algorithm.dynamic_program;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/31 21:07
 *        单词拆分 leetcode 第 139 和 140 题
 *        本篇同时给出「遍历」和「分解问题」两种思路。其中「遍历」的思路扩展延伸一下就是回溯算法，「分解问题」的思路可以扩展成动态规划算法。
 *        题目要求：
 *        给一个字符串 s 和一个字符串列表 wordDict 作为字典。请判断是否可以利用字典中出现的单词拼接处 s。
 *        注意：不要求字典中出现的单词全部使用，并且字典中的单词可以重复使用。
 *        示例1：
 *        输入 s="leetcode", wordDict=["leet","code"]
 *        返回 true
 *        因为 leetcode 可以有 leet 和 code 拼接而成
 *        示例2：
 *        输入 s="applepenapple",workDict=["apple","pen"]
 *        输出 true
 *        解释：返回 true 是因为 applepenapple 可以由 apple,pen,apple 拼接而成。注意，可以重复使用字典中的单词。
 */
public class WordBreak {

    private static List<List<Integer>> resultList;

    /**
     * 基本的回溯类
     */
    static class BasicBackTrack {
        LinkedList<Integer> trackList = new LinkedList<>();
        List<List<Integer>> res = new LinkedList<>();

        public void basicTrack(int[] nums) {
            if (nums == null || nums.length < 1) {
                return;
            }
            if (trackList.size() == nums.length) {
                res.add(new LinkedList(trackList)); // 满足条件的 回溯元素个数，则回收一波到 res 里面
                return;
            }
            // 下面就是标准的回溯算法
            // 加入集合，然后递归，直到 trackList.size() == nums.length
            // 然后就 removeLast，最终的结果是 1 1 1，1 1 2。。。。3 3 3 这 3^3=27 个结果
            for (int num : nums) {
                trackList.add(num);
                basicTrack(nums);
                trackList.removeLast();
            }
        }

        public List<List<Integer>> getResult() {
            return res;
        }
    }

    boolean found = false;
    List<String> wordDict;
    LinkedList<String> backTrackList = new LinkedList<>();

    /**
     * 使用回溯法来实现 word break
     * 
     * @param s
     * @param wordDict
     * @return
     */
    boolean wordBreak_bt(String s, List<String> wordDict) {
        this.wordDict = wordDict;
        wordBreak_backTrackStyle(s, 0);
        return found;
    }

    void wordBreak_backTrackStyle(String s, int i) {
        if (found) { // 如果已经找在字典中到了匹配该字符的单词，则返回
            return;
        }
        if (i == s.length()) { // i 指针已经匹配到最后，说明完全匹配，不匹配指针不走
            found = true;
            return;
        }
        // 回溯算法框架/核心
        for (String word : wordDict) {
            // word 是否匹配字符串 s[i...] 的前缀
            if (i + word.length() <= s.length() &&
                    s.substring(i, i + word.length()).equals(word)) {
                // 找到一个单词匹配 s[i...i+len]
                // 做选择
                backTrackList.add(word);
                // 递归查看 s[i+length...] 是否匹配。进入回溯树下一层，继续匹配 s[i+length...]
                wordBreak_backTrackStyle(s, i + word.length());
                // 撤销选择
                backTrackList.removeLast();
            }
        }
    }

    public static void main(String[] args) {
        // testBasicBackTrack();
        WordBreak instance = new WordBreak();
        String s = "applepenapple";
        String[] arr = new String[] { "apple", "pen" };
        System.out.println(instance.wordBreak_bt(s, Arrays.asList(arr)));
    }

    private static void testBasicBackTrack() {
        int[] nums = new int[] { 1, 2, 3 };
        BasicBackTrack backTrack = new BasicBackTrack();
        backTrack.basicTrack(nums);
        resultList = backTrack.getResult();
        int nextLine = 9;
        int counter = 0;
        for (List<Integer> itemList : resultList) {
            System.out.print(String.join(" ",
                    itemList.stream()
                            .map(x -> String.valueOf(x))
                            .collect(Collectors.toList()))
                    + "\t");
            counter++;
            if (counter % nextLine == 0) {
                System.out.println();
            }
        }
    }
}
