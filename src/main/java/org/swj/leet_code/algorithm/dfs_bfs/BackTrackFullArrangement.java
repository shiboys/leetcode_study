package org.swj.leet_code.algorithm.dfs_bfs;

import java.util.LinkedList;
import java.util.List;

/**
 * 全排列，leetcode 46 题
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/17 16:29
 */
public class BackTrackFullArrangement {

    boolean[] visited;
    List<List<Integer>> res;

    public List<List<Integer>> permute(int[] nums) {
        res = new LinkedList<>();
        visited = new boolean[nums.length];
        traverseBackTrack(nums, new LinkedList<>());
        return res;
    }

    void traverseBackTrack(int[] nums, List<Integer> list) {
        if (list.size() == nums.length) {
            res.add(new LinkedList<>(list));
            return;
        }

        // 回溯框架
        for (int i = 0; i < nums.length; i++) {
            // 排除路径上已经访问过的元素，避免向 1,1,1 或者 1,1,2 这种
            if (visited[i]) {
                continue;
            }
            int num = nums[i];
            list.add(num);
            visited[i] = true;

            traverseBackTrack(nums, list);

            list.remove(list.size() - 1);
            visited[i] = false;
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[] { 1, 2, 3 };
        BackTrackFullArrangement instance = new BackTrackFullArrangement();
        List<List<Integer>> res = instance.permute(arr);
        for (List<Integer> list : res) {
            System.out.println(list);
        }
    }
}
