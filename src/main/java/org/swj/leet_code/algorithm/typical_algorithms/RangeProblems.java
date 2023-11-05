package org.swj.leet_code.algorithm.typical_algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 三道区间问题
 */
public class RangeProblems {

    /**
     * leetcode 1288. Remove Covered Intervals
     * 删除被覆盖的区间
     * 给你一个区间列表，请你删除列表中被其他区间所覆盖的区间。
     * 
     * 只有当 c <= a 且 b <= d 时，我们才认为区间 [a,b) 被区间 [c,d) 覆盖。
     * 
     * 在完成所有删除操作后，请你返回列表中剩余区间的数目。
     * 
     * @param intervals
     * @return
     */
    public int removeCoveredIntervals(int[][] intervals) {
        // 先排序。按 start 升序排列，start 相同的按 end 降序排列
        Arrays.sort(intervals, (a, b) -> {
            return a[0] == b[0] ? b[1] - a[1] : a[0] - b[0];
        });
        int left = intervals[0][0];
        int right = intervals[0][1];
        int coverCount = 0;
        int start, end;
        for (int i = 1, len = intervals.length; i < len; i++) {
            start = intervals[i][0];
            end = intervals[i][1];
            // 1、覆盖
            if (start >= left && end <= right) {
                coverCount++;
                continue;
            }
            // 2 、交叉
            else if (right >= start) {
                right = Math.max(right, end);
            } else { // 3、无任何交集 right < start
                left = start;
                right = end;
            }
        }
        return intervals.length - coverCount;
    }

    /**
     * 986. 区间列表的交集
     * 给定两个由一些 闭区间 组成的列表，firstList 和 secondList ，其中 firstList[i] = [starti, endi] 而
     * secondList[j] = [startj, endj] 。
     * 每个区间列表都是成对 不相交 的，并且 已经排序 。
     * 
     * 返回这 两个区间列表的交集 。
     * 形式上，闭区间 [a, b]（其中 a <= b）表示实数 x 的集合，而 a <= x <= b 。
     * 
     * 两个闭区间的 交集 是一组实数，要么为空集，要么为闭区间。例如，[1, 3] 和 [2, 4] 的交集为 [2, 3] 。
     * 
     * 输入：firstList = [[0,2],[5,10],[13,23],[24,25]], secondList =
     * [[1,5],[8,12],[15,24],[25,26]]
     * 输出：[[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
     * 
     * @param firstList
     * @param secondList
     * @return
     */
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        /**
         * 
         * 解题思路：
         * 什么情况下，两个集合是没有交集的？比如说两个集合为 [a1,a2],[b1,b2]
         * 
         * 通过高中数学只是我们知道， if (a2 < b1 || b2 < a1) 的时候是没有交集的
         * 那所以有交集的情况就是这个条件取反
         * if(a2>=b1 && b2 >= a1)
         * 
         * 另外 i++ 还是 j++ , 取决于什么？取决于 a2 小还是 b2 小，哪个小，哪个要走一步
         */
        List<int[]> res = new ArrayList<>();

        int i = 0, j = 0;
        while (i < firstList.length && j < secondList.length) {
            int a1 = firstList[i][0], a2 = firstList[i][1];
            int b1 = secondList[j][0], b2 = secondList[j][1];
            if (a2 >= b1 && b2 >= a1) {
                int[] re = new int[] {
                        Math.max(a1, b1),
                        Math.min(a2, b2)
                };
                res.add(re);
            }
            if (a2 > b2) {
                j++;
            } else {
                i++;
            }
        }
        return res.toArray(new int[res.size()][]);
    }

    /**
     * leetcode 659. 分割数组为连续子序列
     * 示例 1：
     * 
     * 输入：nums = [1,2,3,3,4,5]
     * 输出：true
     * 解释：nums 可以分割成以下子序列：
     * [1,2,3,3,4,5] --> 1, 2, 3
     * [1,2,3,3,4,5] --> 3, 4, 5
     * 
     * 示例 3：
     * 
     * 输入：nums = [1,2,3,4,4,5]
     * 输出：false
     * 解释：无法将 nums 分割成长度至少为 3 的连续递增子序列。
     * 
     * 但是这个方法提交超时
     * 
     * @param nums
     * @return
     */
    public boolean isPossible(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        for (int val : nums) {
            List<Integer> subList = getListOfPrev(val - 1, res);
            if (subList == null) {
                subList = new ArrayList<>();
                res.add(subList);
            }
            subList.add(val);
        }
        for (List<Integer> subList : res) {
            if (subList.size() < 3) {
                return false;
            }
        }
        return true;
    }

    public boolean isPossible2(int[] nums) {
        /**
         * 两个 map 的解决方案。
         */
        Map<Integer, Integer> need = new HashMap<>();
        Map<Integer, Integer> freq = new HashMap<>();
        for (int val : nums) {
            freq.put(val, freq.getOrDefault(val, 0) + 1);
        }
        Integer needs = 0;
        for (int val : nums) {
            // 如果可以匹配，则匹配
            if (freq.get(val) == 0) {// 该元素已经被使用
                continue;
            }
            // 跟在别人后面
            if (need.containsKey(val) && (needs = need.get(val)) > 0) {
                // val 被用上
                freq.put(val, freq.get(val) - 1);
                // val 的需求数量减 1。
                need.put(val, needs - 1);
                // val+1 的需求数量 +1
                need.put(val + 1, need.getOrDefault(val + 1, 0) + 1);
            } else if ((needs = freq.get(val)) != null && needs > 0 &&
                    (needs = freq.get(val + 1)) != null && needs > 0 &&
                    (needs = freq.get(val + 2)) != null && needs > 0) { // 成立一个新的门派

                freq.put(val, freq.get(val) - 1);
                freq.put(val + 1, freq.get(val + 1) - 1);
                freq.put(val + 2, freq.get(val + 2) - 1);
                // val+3 的需求量加 1。
                need.put(val + 3, need.getOrDefault(val + 3, 0) + 1);
            } else {
                // 自成体系
                // 否则返回 false
                return false;
            }
        }
        return true;
    }

    public List<List<Integer>> isPossible3(int[] nums) {
        /**
         * 两个 map 的解决方案 且返回必须要的顺子。
         */
        Map<Integer, List<List<Integer>>> need = new HashMap<>();
        Map<Integer, Integer> freq = new HashMap<>();
        List<List<Integer>> res = new LinkedList<>();
        for (int val : nums) {
            freq.put(val, freq.getOrDefault(val, 0) + 1);
        }
        List<List<Integer>> needSubLsit;
        Integer needs = 0;
        for (int val : nums) {
            // 如果可以匹配，则匹配
            if (freq.get(val) == 0) {// 该元素已经被使用
                continue;
            }
            // 跟在别人后面
            if (need.containsKey(val) && (needSubLsit = need.get(val)).size() > 0) {
                // val 被用上
                freq.put(val, freq.get(val) - 1);
                List<Integer> subList = needSubLsit.remove(needSubLsit.size() - 1);
                subList.add(val);
                need.putIfAbsent(val + 1, new LinkedList<>());
                need.get(val + 1).add(subList);
            } else if ((needs = freq.get(val)) != null && needs > 0 &&
                    (needs = freq.get(val + 1)) != null && needs > 0 &&
                    (needs = freq.get(val + 2)) != null && needs > 0) { // 成立一个新的门派

                freq.put(val, freq.get(val) - 1);
                freq.put(val + 1, freq.get(val + 1) - 1);
                freq.put(val + 2, freq.get(val + 2) - 1);
                List<Integer> newList = new LinkedList<>();
                newList.addAll(Arrays.asList(val, val + 1, val + 2));

                // val+3 的需求量加 1。
                need.putIfAbsent(val + 3, new LinkedList<>());
                need.get(val + 3).add(newList);
            } else {
                // 自成体系
                // 否则返回 false
                return null;
            }
        }
        for(List<List<Integer>> subList : need.values()) {
            res.addAll(subList);
        }
        return res;
    }

    List<Integer> getListOfPrev(int prev, List<List<Integer>> resList) {
        if (resList == null || resList.isEmpty()) {
            return null;
        }
        List<Integer> res = null;
        // 1 <= nums.length <= 10^4
        int minSize = 10001;
        for (List<Integer> list : resList) {
            // 找到能接上的子序列连接，且是最短的那一个，是最需要拼接的集合/链表
            if (list.get(list.size() - 1) == prev && list.size() < minSize) {
                minSize = list.size();
                res = list;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        RangeProblems instance = new RangeProblems();
        int[][] range = new int[][] {
                { 1, 4 }, { 3, 6 }, { 2, 8 }
        };
        System.out.println(instance.removeCoveredIntervals(range));

        // int[][] r1 = new int[][] { { 0, 2 }, { 5, 10 }, { 13, 23 }, { 24, 25 } };
        // int[][] r2 = new int[][] { { 1, 5 }, { 8, 12 }, { 15, 24 }, { 25, 26 } };
        // int[][] res = instance.intervalIntersection(r1, r2);
        // for (int[] arr : res) {
        // System.out.println(Arrays.toString(arr));
        // }

        int[] arr = new int[] { 1, 2, 3, 3, 4, 5 };
        System.out.println(instance.isPossible(arr));
        arr = new int[] { 1, 2, 3, 3, 4, 4, 5, 5 };
        System.out.println(instance.isPossible3(arr));
        arr = new int[] { 1, 2, 3, 4, 4, 5 };
        //System.out.println(instance.isPossible(arr));
    }
}
