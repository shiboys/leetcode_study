package org.swj.leet_code.algorithm.dynamic_programming.playing_games;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/13 14:53
 *        高楼扔鸡蛋
 *        leetcode 第 887 题
 */
public class DropEggsFromTopStairs {

    int[][] memo;

    /**
     * 从 N 层楼扔 k 个鸡蛋，最坏情况下确认在哪层扔鸡蛋不会导致摔碎鸡蛋
     * 
     * @param k 鸡蛋个数
     * @param N 楼层高度
     * @return
     */
    int superEggDrop(int k, int N) {
        memo = new int[k + 1][N + 1];
        for (int[] arr : memo) {
            Arrays.fill(arr, -1);
        }
        this.n = N;
        return dp(k, N);
    }

    int n;

    int dp(int k, int n) {
        // base case
        if (n == 0) {
            return 0;
        }
        if (k == 1) { // 只有 1 个鸡蛋，则只能采用线性扫描方式
            return n;
        }
        // if(n == this.n) {
        // return 0;
        // }
        if (memo[k][n] != -1) {
            return memo[k][n];
        }
        int res = Integer.MAX_VALUE;
        // for (int i = 1; i <= n; i++) {
        // // 取所有楼层扔鸡蛋的最小个数
        // res = Math.min(res,
        // // 碎或者没碎取最坏情况
        // 1 + // 在 i 层扔了 1 次
        // Math.max(dp(k - 1, i - 1), //鸡蛋碎了，则降低一层尝试
        // dp(k, n-i)) // 鸡蛋没碎，则剩余空间尝试
        // );
        // }
        // 下面是 二分法，只要是符合序列的单调性，就可以使用而二分法。
        
        int lo = 1, hi = n;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int broken = dp(k - 1, mid - 1);
            int not_broken = dp(k, n - mid);
            // res = min(res,1+max(broken,not_brokedn))
            if (broken > not_broken) {
                hi = mid - 1;
                res = Math.min(res, 1 + broken);
            } else {
                lo = mid + 1;
                res = Math.min(res, 1 + not_broken);
            }
        }
        memo[k][n] = res;
        return res;
    }

    public static void main(String[] args) {
        DropEggsFromTopStairs instance = new DropEggsFromTopStairs();
        int k = 1, n = 2;
        System.out.println(instance.superEggDrop(k, n));
        k = 2;
        n = 6;
        System.out.println(instance.superEggDrop(k, n));

        k = 3;
        n = 14;
        System.out.println(instance.superEggDrop(k, n));

        k = 3;
        n = 10;
        System.out.println(instance.superEggDrop(k, n));
    }
}
