package org.swj.complex.beauty_programming;

/**
 * 编程之美-买书打折问题，求买 n 本书最大的折扣
 * 每本书都价格相同，但是 2 本折扣5%, 3 本折扣10%，4本20%，5本 25%
 * 这道题看完就懵逼，没办法去 google 上找，网上的答案也很坑爹，
 * 最后试了下豆包，字节果然强呀，这道题给的回复看着还不错
 * 这里就是参考豆包 AI 给的解法
 */
public class BookBuyingProblem {


    /**
     * 这是豆包给出的解答，根本不对嘛
     * @param books
     * @return
     */
    public static int minCost(int[] books) {
        // 动态规划数组，dp[i][j]表示购买前i种书，剩余j张优惠券时的最低价格
        int[][] dp = new int[books.length + 1][books.length + 1];

        // 初始化边界条件
        for (int i = 0; i <= books.length; i++) {
            dp[i][0] = i * 8; // 没有优惠券时，每本书价格为8元
            dp[0][i] = 0; // 没有书时，价格为0
        }

        // 动态规划计算
        for (int i = 1; i <= books.length; i++) {
            for (int j = 1; j <= books.length; j++) {
                // 不使用优惠券购买当前书
                dp[i][j] = dp[i - 1][j] + 8;

                // 使用优惠券购买当前书
                if (j >= 2) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 2] + 8 * Math.max(0, books[i - 1] - 2));
                }

                // 使用一张优惠券购买当前书和之前的一本书
                if (j >= 1 && books[i - 1] >= 1) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1] + 8 * Math.max(0, books[i - 1] - 1));
                }
            }
        }

        return dp[books.length][books.length];
    }
    public static void main(String[] args) {
        int[] books = {2, 2, 2, 1, 1};
        System.out.println("最低价格: " + minCost(books));
    }
}
