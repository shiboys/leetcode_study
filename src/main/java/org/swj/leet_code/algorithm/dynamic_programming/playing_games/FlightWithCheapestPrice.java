package org.swj.leet_code.algorithm.dynamic_programming.playing_games;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/13 14:51
 *        查找最便宜的中转站，使用动态规划来实现加权有向图的查找，找到 K 次中转范围内的最便宜的路线
 *        There are n cities connected by some number of flights.
 *        You are given an array flights where flights[i] = [fromi, toi, pricei]
 *        indicates that there is a flight from city from i to city to i with
 *        cost price i.
 *        You are also given three integers src, dst, and k, return the cheapest
 *        price from src to dst
 *        with at most k stops. If there is no such route, return -1.
 */
public class FlightWithCheapestPrice {
    int src;
    int dst;

    Map<Integer, List<int[]>> indegreeMap;

    int[][] memo;
    /**
     * 
     * @param n       城市数量
     * @param src     出发地
     * @param dst     目的地
     * @param flights 整个航班列表
     * @param K       K 次中转限制
     * @return 最便宜的中转路径
     */
    int findCheapestPrice(int n, int src, int dst, int[][] flights, int K) {
        // K 次中转，转换成有向图的边的数量是 K ++
        K++;
        this.src = src;
        this.dst = dst;
        memo = new int[n][K+1];
        for(int[] arr: memo) {
            Arrays.fill(arr, -100);
        }
        indegreeMap = new HashMap<>();
        // 初始化入度 map ，将 航班信息转换成 入度信息
        for (int[] flight : flights) {
            int from = flight[0];
            int to = flight[1];
            int price = flight[2];
            indegreeMap.putIfAbsent(to, new LinkedList<>());
            indegreeMap.get(to).add(new int[] { from, price });
        }
        return dp(dst, K);
    }

    /**
     * 动态规划解决从 目的地 src 到 dst 最便宜路径
     * 
     * @param dst2
     * @param k
     * @return
     */
    private int dp(int dst, int k) {
        // 如果起点和重点相同，则不需要任何中转。这里的 if 不能和下面的 if 位置颠倒，要不然逻辑错误。
        // 如果 k 刚好用完且 此时 dst == src 是满足要求的，说明此时恰好到达目标节点
        if (dst == this.src) {
            return 0;
        }

        if (k == 0) { // 中转次数用完了。
            return -1;
        }
        if(memo[dst][k] != -100) {
            return memo[dst][k];
        }
        int res = Integer.MAX_VALUE;
        if (indegreeMap.containsKey(dst)) {
            List<int[]> indegreeList = indegreeMap.get(dst);
            for (int[] indegree : indegreeList) {
                int from = indegree[0];
                int price = indegree[1];
                int subProblem = dp(from, k - 1);
                // 如果 中转次数用完的路线，则忽略
                if (subProblem != -1) {
                    res = Math.min(res, price + subProblem);
                }
            }
        }
        // 如果 res == Integer.MAX_VALUE 说明 des 节点不在图中，也就是航班无法到达
        memo[dst][k] = res == Integer.MAX_VALUE ? -1 : res;
        return memo[dst][k];
    }

    public static void main(String[] args) {
        int n = 4;
        int[][] flights = new int[][] {
                new int[] { 0, 1, 100 },
                new int[] { 1, 2, 100 },
                new int[] { 2, 0, 100 },
                new int[] { 1, 3, 600 },
                new int[] { 2, 3, 200 }
        };
        int src = 0;
        int dst = 3;
        int k = 1;
        FlightWithCheapestPrice instance = new FlightWithCheapestPrice();
        System.out.println(instance.findCheapestPrice(n, src, dst, flights, k));

        n = 3;
        dst = 2;
        k = 1;
        flights = new int[][] {
                new int[] { 0, 1, 100 },
                new int[] { 1, 2, 100 },
                new int[] { 0, 2, 500 }
        };
        System.out.println(instance.findCheapestPrice(n, src, dst, flights, k));

        k=0;
        System.out.println(instance.findCheapestPrice(n, src, dst, flights, k));
    }

}
