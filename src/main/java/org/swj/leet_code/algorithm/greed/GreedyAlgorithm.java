package org.swj.leet_code.algorithm.greed;

import java.util.Arrays;

/**
 * 贪心算法
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/25 21:29
 */
public class GreedyAlgorithm {

    /**
     * leetcode 435. 无重叠区间
     * 
     * @param intervals
     * @return
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        if (intervals == null || intervals.length < 1) {
            return 0;
        }
        return intervals.length - intervalScheduleCount(intervals);
    }

    /**
     * 求 intervals 不相交区间的个数
     * 
     * @param intervals
     * @return
     */
    int intervalScheduleCount(int[][] intervals) {
        // 先按照 end 进行排序
        Arrays.sort(intervals, (a, b) -> {
            // return a[1] - b[1];
            // 上面这种排序算法对于 case  [[-2147483646,-2147483645],[2147483646,2147483647]]
            // 是运算失败的
            return a[1] > b[1] ? 1 : -1;
        });

        // 至少一个不想交区间
        int xcount = 1;
        int xend = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            int xstart = intervals[i][0];
            if (xstart >= xend) {
                xcount++;
                // 更新 xend
                xend = intervals[i][1];
            }
        }
        return xcount;
    }

    public static void main(String[] args) {
        int[][] intervals = new int[][] { { -2147483646, -2147483645 }, { 2147483646, 2147483647 } };
        GreedyAlgorithm ga = new GreedyAlgorithm();
        System.out.println(ga.intervalScheduleCount(intervals));
    }
}
