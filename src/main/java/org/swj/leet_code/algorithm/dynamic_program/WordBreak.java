package org.swj.leet_code.algorithm.dynamic_program;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/31 21:07
 *        单词拆分 leetcode 第 139 和 140 题。这里的拆分和
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
        // 这里的小优化，将 wordDict 转化为 hashSet ，该循环可以降维 set.contains(prefix)
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

    /**
     * 回溯算法的优缺点：
     * 递归函数的时间复杂度的粗略估算方法时用递归函数调用次数(递归树的节点数) * 递归函数本身的复杂度
     * 对于上述回溯法而言，递归树的每个节点其实就是对 s 进行一次切割，那么最坏情况下 s 能有多少种切割那？长度为 N 的字符串 s 中
     * 有 n-1 个「缝隙」可供切割，每个缝隙可以选择「切」或者「不切」，所以 s 最多有 O(2^N) 个节点(比如 abc有有 2 个缝隙 有2^2=4
     * 中切法，
     * 分别为 a b c,ab c,a bc, abc 这 4 种)。
     * 当然，实际情况肯定会好一些，比较存在剪枝的逻辑，但是从最坏的角度来看，递归树的节点个数确实是指数级的。
     * 
     * 回溯算法的时间复杂度是多少？主要的时间消耗是遍历 wordDict 寻找匹配 s[i..] 的前缀时间
     * 设 wordDict 的长度为 M，字符串 s 的长度为 N，那么这段代码的最坏时间复杂度是 O(MN)(for 循环 O(M),Java 的
     * Substring 方法 O(n),
     * 所以宗的时间复杂度为 O(2^N * MN))
     */

    public static void main(String[] args) {
        // testBasicBackTrack();
        WordBreak instance = new WordBreak();
        String s = "applepenapple";
        String[] arr = new String[] { "apple", "pen" };
        System.out.println(instance.wordBreak_dp(s, Arrays.asList(arr)));
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

    private Set<String> wordSet;
    private int[] memo;

    boolean dp(String s, int i) {
        // base case
        if (i == s.length()) {
            return true;
        }
        // 防止冗余计算
        if (memo[i] != -1) {
            return true;
        }
        // 遍历 s[i..] 的所有前缀
        for (int k = 1; k + i <= s.length(); k++) {
            String prefix = s.substring(i, i + k);
            // 看哪些前缀存在 wordSet 中
            if (wordSet.contains(prefix)) {
                boolean subProblem = dp(s, i + prefix.length());
                // 找到一个单词匹配 s[i..i+k]
                // 只要 s[i+len..] 可以被必配出来，则 s[i..] 就能被整体匹配成功
                if (subProblem) { // 从 prefix 到 subProblem 都匹配成功，则 i 位置整体匹配成功
                    memo[i] = 1;
                    return true;
                }
            }
        }
        // s[i..] 无法从字典中被拼出一个
        memo[i] = 0;
        return false;
    }

    boolean wordBreak_dp(String s, List<String> wordDict) {
        this.wordSet = new HashSet(wordDict);
        memo = new int[s.length()];
        Arrays.fill(memo, -1);
        return dp(s, 0);
    }
}
