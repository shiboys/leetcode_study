package org.swj.leet_code.array;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/21 10:27
 *        前缀和数组
 */
public class DifferentArray {

    class Different {
        // 差分数组
        private int[] diff;

        public Different(int[] nums) {
            diff = new int[nums.length];
            diff[0] = nums[0];
            for (int i = 1; i < nums.length; i++) {
                diff[i] = nums[i] - nums[i - 1];
            }
        }

        /**
         * 给区间 [i,j] 增加 val (可以是负数)
         * 
         * @param i
         * @param j
         * @param val
         */
        public void increment(int i, int j, int val) {
            if (i < 0 || i > j) {
                return;
            }
            diff[i] += val;
            if (j + 1 < diff.length) {
                diff[j + 1] -= val;
            }
        }

        public int[] result() {
            int[] res = new int[diff.length];
            res[0] = diff[0];
            for (int i = 1, len = res.length; i < len; i++) {
                res[i] = res[i - 1] + diff[i];
            }
            return res;
        }
    }

    /**
     * leetcode 370 区间加法（ps：这道题 leetcode 需要付费会员才能打开）
     * @param length
     * @param updates
     * @return
     */
    public int[] getModifiedArray(int length, int[][] updates) {
        int[] nums = new int[length];
        Different diff = new Different(nums);
        for (int i = 0, len = updates.length; i < len; i++) {
            int start = updates[i][0];
            int end = updates[i][1];
            int val = updates[i][2];
            diff.increment(start, end, val);
        }
        return diff.result();
    }

    /**
     * leetcode 1094 题 拼车
     * @param trips 旅程和旅程信息
     * @param capacity 公交车的容量
     * @return 是否能容下
     */
    boolean canPooling(int[][] trips,int capacity) {
    // 根据题目给出的限制信息，最多 1001 个车站，车站编号 0-1000。
        int n = 1001;
        int[] nums = new int[n];
        Different diff = new Different(nums);
        for(int i =0,len=trips.length;i<len;i++) {
            // 乘客数量
            int val = trips[i][0];
            int fromi = trips[i][1];
            // 第 trip[2] 站乘客已下车，
            // 即乘客在车上的车程区间为 [trip[1], trip[2]-1]
            int toi = trips[i][2] - 1;
            diff.increment(fromi, toi, val);
        }
        int[] res = diff.result();
        for(int cap : res) {
            // 只要任何一站地人数超出 capacity ，就表示超载
            if(cap > capacity) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int length = 5;
        int[][] updates = new int[][] {
                new int[] { 1, 3, 2 },
                new int[] { 2, 4, 3 },
                new int[] { 0, 2, -2 }
        };

        DifferentArray instance = new DifferentArray();
        System.out.println(Arrays.toString(instance.getModifiedArray(length, updates)));

        int[][] trips = new int[][] {
            new int[] {2,1,5},
            new int[] {3,5,7}
        };
        System.out.println(instance.canPooling(trips, 3));
    }
}
