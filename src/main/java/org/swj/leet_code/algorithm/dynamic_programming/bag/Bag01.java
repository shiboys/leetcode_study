package org.swj.leet_code.algorithm.dynamic_programming.bag;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/11 20:03
 *        0-1 背包问题
 *        详细描述请参考 bag.md 中有关 0-1 背包的问题。
 */
public class Bag01 {

  /**
   * 
   *
   * @param W   背包的容量
   * @param N   不同物品的数量
   * @param val 各个物品的价值数组
   * @param wt  各个物品的重量数组
   * @return
   */
  int getMaxValueOfBag(int W, int N, int[] val, int[] wt) {
    
    assert N  == val.length;
    // dp[i][j] 表示第 i 个物品,当前容量为 j 的时候，背包能装下的最大的价值
    int dp[][] = new int[N + 1][W + 1];
    // base case

    for (int i = 1; i <= N; i++) {
      for (int j = 1; j <= W; j++) {
        if (j - wt[i-1] < 0) { // 当前的剩余重量已经不足以放下第 i 个物品了
          dp[i][j] = dp[i - 1][j]; // 当前背包的价格只能取上一次装填背包的价格
        } else {
          dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - wt[i-1]] + val[i-1]);
        }
      }
    }
    return dp[N][W];
  }

  public static void main(String[] args) {
    Bag01 instance = new Bag01();
    int[] wt = new int[] {2,1,3};
    int[] val = new int[] {4, 2, 3};
    int w  = 4;
    int n = 3;
    System.out.println(instance.getMaxValueOfBag(w, n, val, wt));
  }
  // int dpMaxValue()
}
