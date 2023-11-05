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
            // 上面这种排序算法对于 case [[-2147483646,-2147483645],[2147483646,2147483647]]
            // 是运算失败的
            return a[1] > b[1] ? 1 : -1;
        });

        // 至少一个不相交区间
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

    /**
     * leetcode 134. 加油站
     * 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
     * 
     * 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。你从其中的一个加油站出发，开始时油箱为空。
     * 
     * 给定两个整数数组 gas 和 cost ，如果你可以按顺序绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1 。如果存在解，则 保证 它是 唯一
     * 的。
     * 
     * 示例 1:
     * 
     * 输入: gas = [1,2,3,4,5], cost = [3,4,5,1,2]
     * 输出: 3
     * 解释:
     * 从 3 号加油站(索引为 3 处)出发，可获得 4 升汽油。此时油箱有 = 0 + 4 = 4 升汽油
     * 开往 4 号加油站，此时油箱有 4 - 1 + 5 = 8 升汽油
     * 开往 0 号加油站，此时油箱有 8 - 2 + 1 = 7 升汽油
     * 开往 1 号加油站，此时油箱有 7 - 3 + 2 = 6 升汽油
     * 开往 2 号加油站，此时油箱有 6 - 4 + 3 = 5 升汽油
     * 开往 3 号加油站，你需要消耗 5 升汽油，正好足够你返回到 3 号加油站。
     * 因此，3 可为起始索引。
     * 
     * @param gas
     * @param cost
     * @return
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int sum = 0, minSum = 0;
        int start = 0, n = gas.length;
        for (int i = 0; i < n; i++) {
            sum += gas[i] - cost[i];
            if (sum < minSum) {
                // 记录最小值
                minSum = sum;
                // 经过第 i 个站点后，使 sum 到达新低，
                // 所以 站点 i+1 就是最低点，因为 i 站点是最小点， 处于下降趋势中，会导致如果你在 i 点出发，加油 < 消耗(i->i+1)，
                // 最终导致无法到达 i+1 点
                start = i + 1;
            }
        }
        if (sum < 0) { // 表明加的总油量 < 消耗的总油量，无法兜一圈
            return -1;
        }
        return start == n ? 0 : start;
    }

    /**
     * 加油站加油 贪心算法版
     * 
     * @param gas
     * @param cost
     * @return
     */
    public int canCompleteCircuit2(int[] gas, int[] cost) {
        int sum = 0;
        int n = gas.length;
        for (int i = 0; i < n; i++) {
            sum += gas[i] - cost[i];
        }
        if (sum < 0) { // 总加油数量小于总消耗数量，无解的
            return -1;
        }
        int start = 0;
        // 记录油量
        int oil = 0;
        for (int i = 0; i < n; i++) {
            oil += gas[i] - cost[i];
            if (oil < 0) {
                start = i + 1;
                // 将 sum 重置为0，表示 从 start 开始出发
                oil = 0;
            }
        }
        return start == n ? 0 : start;
    }

    public static void main(String[] args) {
        int[][] intervals = new int[][] { { -2147483646, -2147483645 }, { 2147483646, 2147483647 } };
        GreedyAlgorithm ga = new GreedyAlgorithm();
        System.out.println(ga.intervalScheduleCount(intervals));
    }
}
