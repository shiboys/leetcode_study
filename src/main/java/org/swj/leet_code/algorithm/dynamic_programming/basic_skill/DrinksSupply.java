package org.swj.leet_code.algorithm.dynamic_programming.basic_skill;

/**
 * 饮料供应问题
 * 这是编程之美的 1.6 题，我看完题目就懵逼了。
 * 从阿姨们的统计中的数据中，我们可以知道每一种饮料的满意度。
 * 每天饮料的总供应容量为 V，每种饮料的单个容量的上限为 2 的幂次方，比如 王老吉，都是 2^3=8 升，可乐是 2^5 =32 升。
 * 统计数据中用饮料名字、容量、数量、满意度描述每一种饮料。
 * 求：
 * 保证最大满意度的购买量
 */
public class DrinksSupply {
    /**
     * 懵逼吧？不懵逼是不可能的，除非你比如说参加过比赛之类的，对动态规划的海量题目都做过，对其原理有深刻的理解，了如指掌
     * 否则，你都不知道怎么下手这道题
     * 这里我们还是写上作者的解题思路吧
     */
    /**
     * 解法1：先把这个问题“数学化”一下
     * 假设阿姨提供 n 种饮料，用 (Si,Vi, Ci, Hi, Bi) 表示第 i 种饮料的名字，容量、最大数量、满意度，实际购买数量
     * 饮料总容量为 sum(Vi*Bi)
     * 满意度为 sum(Hi*Bi)
     * 那么题目要求是，在满足条件 sum(Vi*Bi) == V 的前提下，求 Max(Hi*Bi) 的最大值
     * 求最优解的问题，首先想到的就是动态规划，但是这道题目的动态转移方程，我是真想不出来
     * 参考作者的思路，dp(V,i) 表示从第 i,i+1,i+2...n-1,n 种饮料中, 算出总量为 V 的满意度之和的最大值
     * 那么状态转移方程可以为 dp(V,i) = max{ dp(V-Si*k, i-1) + k*Hi } (0<=k<=Ci)， 其中 k 表示购买的数量,
     * i 表示饮料的种类
     * 该方程解释为：最优化的结果 = 第 i 种饮料购买 k 个的满意度 + 减去第 i 种饮料 k 个的容量 ， 求其最大值。
     * 那么其边界条件为:
     * dp(0, n) = 0 ，即容量为 0 的情况下，最优化的结果为 0
     * dp(x,n) = -INF(x != 0) ，即在容量不为 0 的情况下，最优化的结果为负无穷，并把它作为初始值
     */

    static final int INF = 10_000;

    // 每种饮料名称
    final String[] drinks;
    // 每种音量的容量
    final int[] volumes;
    // 每种饮料的最大容量上限
    final int[] capacity;

    final int[] satisfactions;

    // 容量的总上限
    final int V;

    /**
     * @param drinks       饮料名称
     * @param volumes      饮料的容量. 当前饮料种类的容量，比如每瓶 500ml，那么 volumes 就是 500
     * @param capacity     饮料的最大容量上限，每种饮料可以购买的上限，题目要求是 2^n
     * @param satisfactions 饮料的满意度
     * @param V            饮料的总容量上限.比如说 500ml 的饮料，总容量上限为 1000ml
     */
    public DrinksSupply(String[] drinks, int[] volumes, int[] capacity, int[] satisfactions, int V) {
        this.drinks = drinks;
        this.volumes = volumes;
        this.capacity = capacity;
        this.satisfactions = satisfactions;
        this.V = V;
    }

    int getMaxSatisfaction() {
        int n = drinks.length;
        int[][] dp = new int[V + 1][n];

        // 初始化 dp 数组
        for (int i = 0; i <= V; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = -INF;
            }
        }
        dp[0][n] = 0;
        // 种类是倒序遍历
        for (int j = n - 1; j < n; j--) {
            // 容量是正序遍历
            for (int i = 0; i <= V; i++) {
                for (int k = 0; k <= capacity[j]; k++) { // 遍历第 j 种饮料选取数量 k
                    if (volumes[j] * k >= i) { // 第 j 种饮料的容量大于当前容量 i
                        break;
                    }
                    // dp[i-volumes[j]*k][j+1] 这个非常难以理解，其实就是当前饮料之前的最大满意度
                    int prevSatis = dp[i - volumes[j] * k][j + 1];
                    if (prevSatis != -INF) { // 表示之前的饮料状态被动态规划更新了
                        // 这里是关键，需要理解。之前的满意度 + 当前饮料的满意度
                        dp[i][j] = Math.max(dp[i][j], prevSatis + satisfactions[j] * k);
                    }
                }
            }
        }
        return dp[V][0];
    }
}
