package org.swj.leet_code.algorithm.dynamic_programming.playing_games;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/13 14:55
 *        股票买卖问题，动态规划求解
 */
public class StockExchange {

    /**
     * leetcode 121
     * 
     * @param prices
     * @return
     */
    int maxProfit_k_1(int[] prices) {
        int n = prices.length;
        int dp[][] = new int[n][2];
        for (int i = 0; i < n; i++) {
            if (i - 1 == -1) {
                dp[i][0] = 0;
                dp[i][1] = -prices[i];
                continue;
            }
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], -prices[i]); // -prices[i] 参考状态转回方程
        }
        return dp[n - 1][0];
    }

    int maxProfit_k_1_2(int[] prices) {
        int n = prices.length;
        int dp_i_0 = 0;
        int dp_i_1 = 0;
        for (int i = 0; i < n; i++) {
            if (i - 1 == -1) {
                dp_i_0 = 0;
                dp_i_1 = -prices[i];
                continue;
            }
            dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i]);
            dp_i_1 = Math.max(dp_i_1, -prices[i]);
        }
        return dp_i_0;
    }

    /**
     * 求 k 为不限制交际次数的最大利润。 leetcode 122
     * 
     * @param prices
     * @return
     */
    int maxProfit_k_inf(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n][2];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                dp[i][0] = 0;
                dp[i][1] = -prices[i];
                continue;
            }
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
        }
        return dp[n - 1][0];
    }

    int maxProfit_k_inf2(int[] prices) {
        int n = prices.length;
        int dp_i_0 = 0;
        int dp_i_1 = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                dp_i_0 = 0;
                dp_i_1 = -prices[i];
            }
            dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i]);
            dp_i_1 = Math.max(dp_i_1, dp_i_0 - prices[i]);
        }
        return dp_i_0;
    }

    /**
     * 含有 1 天冷冻期的股票最大交易
     */
    int maxProfit_freeze1(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n][2];
        for (int i = 0; i < n; i++) {
            if (i - 1 == -1) {
                // base case 1
                dp[i][0] = 0;
                dp[i][1] = -prices[i];
                continue;
            }
            if (i - 1 == 0) {
                // base case 2
                dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
                dp[i][1] = Math.max(dp[i - 1][1], -prices[i]);
                continue;
            }
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            // 再次交易的冷冻期为 1 天
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 2][0] - prices[i]);
        }
        return dp[n - 1][0];
    }

    int maxProfit_freeze1_2(int[] prices) {
        int dp_i_0 = 0, dp_i_1 = Integer.MIN_VALUE;
        int prev_exchange_max_val = 0; // 代表 dp[i-2][0]
        int n = prices.length;
        for (int i = 0; i < n; i++) {
            if (i - 1 == -1) {
                dp_i_0 = 0;
                dp_i_1 = -prices[i];
                continue;
            }
            if (i - 1 == 0) {
                dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i]);
                dp_i_1 = Math.max(dp_i_1, -prices[i]);
                continue;
            }
            int temp = dp_i_0;
            dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i]);
            dp_i_1 = Math.max(dp_i_1, prev_exchange_max_val - prices[i]);
            // 上上个交易日的
            prev_exchange_max_val = temp;
        }
        return dp_i_0;
    }

    int maxProfit_with_fee(int[] prices, int fee) {
        int n = prices.length;
        int[][] dp = new int[n][2];
        for (int i = 0; i < n; i++) {
            if (i - 1 == -1) {
                dp[i][0] = 0;
                dp[i][1] = -prices[i] - fee;
                continue;
            }
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i] - fee);
        }
        return dp[n - 1][0];
    }

    int maxProfit_k_2(int[] prices) {
        int n = prices.length;
        int maxK = 2;
        int[][][] dp = new int[n][maxK + 1][2];
        for (int i = 0; i < n; i++) {
            // 把 交易次数考虑进去，得到状态转移的完整公式
            // 至于这里为什么采用 -- 的递减方式，请参考 play_game.md 的文档详细说明
            for (int k = maxK; k >= 1; k--) { // 价格取第一个价格时
                if (i - 1 == -1) {
                    dp[i][k][0] = 0; // 卖出只能获取 0 收益
                    dp[i][k][1] = -prices[i]; // 买入只能获取负收益
                    continue;
                }
                dp[i][k][0] = Math.max(dp[i - 1][k][0], dp[i - 1][k][1] + prices[i]);// 卖出股票，可用最大交易次数不减少
                dp[i][k][1] = Math.max(dp[i - 1][k][1], dp[i - 1][k - 1][0] - prices[i]);// 买入股票，最大可用交易次数必须保证上次买入时的最大交易次数为
                                                                                         // k-1
            }
        }
        // f(k) 是通过 f(k-1) 过来的，所以最大的值是 k == maxK 的时候
        return dp[n - 1][maxK][0];
    }

    /**
     * 使用一维空间将 k=2 的空间复杂度打下来
     * 
     * @param prices
     * @return
     */
    int maxProfit_k_2_2(int[] prices) {

        /**
         * 状态转移方程：
         * dp[i][2][0]=max(dp[i-1][2][0], dp[i-1][2][1] + price[i]);
         * dp[i][2][1]=max(dp[i-1][2][1], dp[i][1][0] - price[i])
         * dp[i][1][0]=max(dp[i-1][1][0], dp[i-1][1][1] + price[i]);
         * dp[i][1][1]=max(dp[i-1][1][1], -price[i])
         */
        int dp_i20 = 0, dp_i21 = Integer.MIN_VALUE;
        int dp_i10 = 0, dp_i11 = Integer.MIN_VALUE;
        for (int price : prices) {
            // 这里为何先算 dp_i20 和 dp_i21 那？这是因为上面的代码中 10 和 11 算完之后 continue了，就是为了给下一轮的 20 和 21
            // 使用
            // 所以这里尽管先初始化 20 和 21 ，但是首次的 max 求值因为存在极值，是没问题的
            dp_i20 = Math.max(dp_i20, dp_i21 + price);
            dp_i21 = Math.max(dp_i21, dp_i10 - price);
            dp_i10 = Math.max(dp_i10, dp_i11 + price);
            dp_i11 = Math.max(dp_i11, -price);
        }
        return dp_i20;
    }

    int maxProfit_k_any(int[] prices, int max_k) {
        int n = prices.length;
        if (n <= 0) {
            return 0;
        }
        if (max_k > n / 2) { // 可以将 k 视为无限大，调用 inf 方法
            return maxProfit_k_inf(prices);
        }
        // 否则，走常规的动态转换方程
        int[][][] dp = new int[n][max_k + 1][2];
        // k==0 是的 base case
        for (int i = 0; i < n; i++) {
            dp[i][0][0] = 0;
            dp[i][0][1] = Integer.MIN_VALUE;
        }

        for (int i = 0; i < n; i++) {
            for (int k = max_k; k >= 1; k--) {
                if (i - 1 == -1) {
                    dp[i][k][0] = 0;
                    dp[i][k][1] = -prices[i];
                    continue;
                }
                dp[i][k][0] = Math.max(dp[i - 1][k][0], dp[i - 1][k][1] + prices[i]);
                dp[i][k][1] = Math.max(dp[i - 1][k][1], dp[i - 1][k - 1][0] - prices[i]);
            }
        }
        return dp[n - 1][max_k][0];
    }

    public static void main(String[] args) {
        StockExchange instance = new StockExchange();
        int[] arr = new int[] { 7, 1, 5, 3, 4, 6 };
        System.out.println(instance.maxProfit_k_1(arr));
        System.out.println(instance.maxProfit_k_1_2(arr));
        System.out.println(instance.maxProfit_k_inf(arr));
        System.out.println(instance.maxProfit_k_inf2(arr));

        arr = new int[] { 7, 6, 4, 3, 1 };
        System.out.println(instance.maxProfit_k_1(arr));
        System.out.println(instance.maxProfit_k_1_2(arr));

        arr = new int[] { 1, 2, 3, 4, 5 };
        System.out.println(instance.maxProfit_k_inf(arr));

        System.out.println("含有冷冻期为 1 天的股票交易：");
        arr = new int[] { 1, 2, 3, 0, 2 };
        System.out.println(getFormattedMsgInfo(arr) + instance.maxProfit_freeze1(arr));
        System.out.println(instance.maxProfit_freeze1_2(arr));

        System.out.println("含有手续费的不限制交易次数的最大股票交易收益：");
        arr = new int[] { 1, 3, 2, 8, 4, 9 };
        System.out.println(getFormattedMsgInfo(arr) + instance.maxProfit_with_fee(arr, 2));

        System.out.println("带有交易次数限制最大为 2 的股票交易获取收益最大：");
        arr = new int[] { 3, 3, 5, 0, 0, 3, 1, 4 };
        System.out.println(getFormattedMsgInfo(arr) + instance.maxProfit_k_2(arr));
        System.out.println(instance.maxProfit_k_2_2(arr));

        System.out.println("交易次数没有限制的股票交易获取收益最大：");
        arr = new int[] { 3, 2, 6, 5, 0, 3 };
        System.out.println(getFormattedMsgInfo(arr) + instance.maxProfit_k_any(arr, 2));
    }

    static String getFormattedMsgInfo(int[] arr) {
        return "array is :" + Arrays.toString(arr) + ", result is: ";
    }
}
