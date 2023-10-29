package org.swj.leet_code.algorithm.dynamic_programming.playing_games;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/12 22:16
 *        leetcode 第 514 题，自由之路
 */
public class FreedomTrail {
    Map<Character, List<Integer>> keyPosMap;

    public int findRotateSteps(String ring, String key) {
        if (ring == null || ring.isEmpty()) {
            return 0;
        }
        if (key == null || key.isEmpty()) {
            return 0;
        }
        keyPosMap = new HashMap<>();
        char[] ringChars = ring.toCharArray();
        for (int i = 0, len = ringChars.length; i < len; i++) {
            char ch = ringChars[i];
            keyPosMap.putIfAbsent(ch, new LinkedList<>());
            keyPosMap.get(ch).add(i);
        }
        memo = new int[ring.length()][key.length()];
        for (int[] arr : memo) {
            Arrays.fill(arr, -1);
        }
        return dp(ring, 0, key, 0);
    }

    int[][] memo;

    /**
     * 当圆盘指针指向 ring[i] 时，输入字符串 key[j..] 至少需要 dp(ring,i,key ,j) 次操作
     * @param ring
     * @param i
     * @param key
     * @param j
     * @return
     */
    int dp(String ring, int i, String key, int j) {
        if (j == key.length()) {
            return 0;
        }
        // 根本拨不到 j 索引位的字符
        if (!keyPosMap.containsKey(key.charAt(j))) {
            return -1;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        int res = Integer.MAX_VALUE;

        for (int ring_pos : keyPosMap.get(key.charAt(j))) {
            int delta = Math.abs(ring_pos - i); // 算出 ring_pos 字母的位置距离当前 i 位置的距离
            delta = Math.min(
                    delta, // 顺时针 j 位置的距离
                    ring.length() - delta // 逆时针 j 位置的距离
            );
            // 写到这里把状态转移方程忘记了，ring_pos 是拨到 key[j] 的最短距离，同时 j 要++
            int subProblem = dp(ring, ring_pos, key, j + 1);
            // + 1 是因为点击中间的按钮要加 1
            res = Math.min(res, delta + subProblem + 1);
        }

        memo[i][j] = res;
        return res;
    }

    public static void main(String[] args) {
        FreedomTrail instance = new FreedomTrail();

        System.out.println(instance.findRotateSteps("godding", "gd"));
    }
}