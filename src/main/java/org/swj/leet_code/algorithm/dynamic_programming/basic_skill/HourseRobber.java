package org.swj.leet_code.algorithm.dynamic_programming.basic_skill;

import org.swj.leet_code.binary_tree.TreeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 打家劫舍系列问题
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/24 18:21
 */
public class HourseRobber {
    public int rob(int[] nums) {
        // 打家劫舍系列最入门的一道题
        /**
         * leetcode 198. House Robber
         * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
         * 
         * 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
         */

        memo = new int[nums.length];
        Arrays.fill(memo, -1);
        return dp(nums, 0);
    }

    int[] memo;

    int dp(int[] nums, int i) {
        // base case
        if (i >= nums.length) { // 最后一个房子遍历过了，则得到的钱数为 0
            return 0;
        }
        if (memo[i] != -1) {
            return memo[i];
        }
        // nums[i] 抢或者不抢
        memo[i] = Math.max(nums[i] + dp(nums, i + 2), dp(nums, i + 1));
        return memo[i];
    }

    /**
     * 打家劫舍 数组迭代解法2
     * 
     * @param nums
     * @return
     */
    public int rub2(int[] nums) {
        // dp[i]=x 定义为到第 i 间屋子，打劫的最大金额。
        int n = nums.length;
        int[] dp = new int[n + 2];
        // dp[n]=0
        for (int i = n - 1; i >= 0; i--) {
            dp[i] = Math.max(dp[i + 1], nums[i] + dp[i + 2]);
        }
        return dp[0];
    }


    /**
     * 打家劫舍 数组迭代解法2
     *
     * @param nums
     * @return
     */
    public int rub22(int[] nums) {
        // dp[i]=x 定义为到第 i 间屋子，打劫的最大金额。
        int n = nums.length;
        int[] dp = new int[n + 2];
        // dp[n]=0
        for (int i = 2; i < n+2 ; i++) {
            dp[i] = Math.max(dp[i - 1], nums[i-2] + dp[i - 2]);
        }
        return dp[n+1];
    }

    /**
     * 打家劫舍解法3，数组压缩法
     * 
     * @param nums
     * @return
     */
    int rub3(int[] nums) {
        int n = nums.length;
        int dp_i1 = 0, dp_i2 = 0;
        int dp_i = 0;
        for (int i = n - 1; i >= 0; i--) {
            dp_i = Math.max(dp_i1, nums[i] + dp_i2);
            // 这里为什么不让 dp_i1= dp_i2; dp_i2=dpi 这是因为 dp_i 不能连续参与运算
            // 也就是小偷不能进行连续打劫
            dp_i2 = dp_i1;
            dp_i1 = dp_i;
        }
        return dp_i;
    }

    /**
     * leetcode 213. House Robber II
     * 力扣第 213 题「打家劫舍 II」和上一道题描述基本一样，强盗依然不能抢劫相邻的房子，
     * 输入依然是一个数组，但是告诉你这些房子不是一排，而是围成了一个圈。
     * 
     * @param nums
     * @return
     */
    public int robII(int[] nums) {
        /**
         * 之前单调栈的时候，解决过首尾相连的问题，是通过复制一个相同的数组，将两个相同的数组首尾相连。
         * 但是打家劫舍不能这么搞，这么搞会重复计算，就是大 bug。
         * 首先，首尾房间不能同时被抢，那么只可能有三种不同情况：要么都不被抢；要么第一间房子被抢最后一间不抢；要么最后一间房子被抢第一间不抢。
         * 那就简单了啊，这三种情况，哪种的结果最大，就是最终答案呗！不过，其实我们不需要比较三种情况，只要比较情况二和情况三就行了，
         * 因为这两种情况对于房子的选择余地比情况一大呀，房子里的钱数都是非负数，所以选择余地大，最优决策结果肯定不会小。
         */
        return Math.max(robCycle(nums, 0, nums.length - 2),
                robCycle(nums, 1, nums.length - 1));
    }

    int robCycle(int[] nums, int start, int end) {
        int dp_i1 = 0, dp_i2 = 0;
        int dp_i = 0;
        for (int i = end; i >= start; i--) {
            dp_i = Math.max(dp_i1, nums[i] + dp_i2);
            dp_i2 = dp_i1;
            dp_i1 = dp_i;
        }
        return dp_i;
    }

    /**
     * leetcode 62. 不同路径
     * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为 “Start” ）。
     * 
     * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为 “Finish” ）。
     * 
     * 问总共有多少条不同的路径？
     * 示例 2：
     * 
     * 输入：m = 3, n = 2
     * 输出：3
     * 解释：
     * 从左上角开始，总共有 3 条路径可以到达右下角。
     * 1. 向右 -> 向下 -> 向下
     * 2. 向下 -> 向下 -> 向右
     * 3. 向下 -> 向右 -> 向下
     * 
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths2(int m, int n) {
        int[][] grid = new int[m][n];
        AtomicInteger counter = new AtomicInteger();
        uniquePathBackTrack(grid, 0, 0, counter);
        return counter.get();
    }

    public int uniquePaths(int m, int n) {
        /**
         解题思路
         我看到向下和向右，我想起来 leetcode 22 题，一些列 n 对合法的小括号的 回溯解法
         先尝试放左括号，然后尝试放右括号，组成所有合法的括号对，
         然后就尝试着用回溯法解决，没想到确实可以。
         但是但是，回溯法超时了
         当然动态规划也能解决,但是动态规划没有超时，当然了，O(N) 的时间，肯定比回溯法的时间复杂度更低
         */
        memoPath = new int[m][n];
        for(int[] arr : memoPath) {
            Arrays.fill(arr,-1);
        }

        return dp(m-1,n-1);
    }
    int[][] memoPath;

    //dp(i,j) 的定义为 从 (0,0) 到 (i,j) 的所有唯一路径数
    int dp(int i,int j) {
        if(i<0 || j <0) {
            return 0;
        }
        if(i==0 && j==0) {
            return 1;
        }
        if(memoPath[i][j] != -1) {
            return memoPath[i][j];
        }
        //状态转移公式，到 finish 的路径数=到 finish.left 的路径数+到 finish.top 的路径数
        // 这道题的解法跟 跳楼梯 是一模一样的，都是将两种可能方式的动态规划结果进行相加
        memoPath[i][j] = dp(i-1,j) + dp(i,j-1);
        return memoPath[i][j];
    }

    void uniquePathBackTrack(int[][] grid, int i, int j, AtomicInteger counter) {
        if (i > grid.length || j > grid[0].length) {
            return;
        }
        if (i == grid.length - 1 && j == grid[0].length - 1) {
            counter.incrementAndGet();
        }
        // 向右
        uniquePathBackTrack(grid, i + 1, j, counter);
        // 向下
        uniquePathBackTrack(grid, i, j + 1, counter);
    }


    /**
     * 打家劫舍 III
     * 小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为 root 。
     *
     * 除了 root 之外，每栋房子有且只有一个“父“房子与之相连。一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
     * 如果 两个直接相连的房子在同一天晚上被打劫 ，房屋将自动报警。
     *
     * 给定二叉树的 root 。返回 在不触动警报的情况下 ，小偷能够盗取的最高金额 。
     * @param root
     * @return
     */
    public int rob3(TreeNode root) {
        /**
         沿二叉树进行抢劫
         */
        return robTree(root);
    }
    // 使用 map 作为备忘录
    Map<TreeNode,Integer> memoMap = new HashMap<>();

    int robTree(TreeNode node) {
        if(node == null ) {
            return 0;
        }
        int res =0;
        if(memoMap.containsKey(node)) {
            return memoMap.get(node);
        }

        // 抢
        int doIt = node.val + (node.left == null? 0 : robTree(node.left.left)+ robTree(node.left.right))
            + (node.right == null ? 0 : robTree(node.right.left) + robTree(node.right.right));
        // 当前节点不抢劫
        int notDo = robTree(node.left) + robTree(node.right);
        res = Math.max(doIt, notDo);
        memoMap.put(node, res);
        return res;
    }

    /**
     * 
     * 2560. 打家劫舍 IV
     * 沿街有一排连续的房屋。每间房屋内都藏有一定的现金。现在有一位小偷计划从这些房屋中窃取现金。
     * 
     * 由于相邻的房屋装有相互连通的防盗系统，所以小偷 不会窃取相邻的房屋 。
     * 
     * 小偷的 窃取能力 定义为他在窃取过程中能从单间房屋中窃取的 最大金额 。
     * 
     * 给你一个整数数组 nums 表示每间房屋存放的现金金额。形式上，从左起第 i 间房屋中放有 nums[i] 美元。
     * 
     * 另给你一个整数 k ，表示窃贼将会窃取的 最少 房屋数。小偷总能窃取至少 k 间房屋。
     * 
     * 返回小偷的 最小 窃取能力。
     * 
     * 输入：nums = [2,3,5,9], k = 2
     * 输出：5
     * 解释：
     * 小偷窃取至少 2 间房屋，共有 3 种方式：
     * - 窃取下标 0 和 2 处的房屋，窃取能力为 max(nums[0], nums[2]) = 5 。
     * - 窃取下标 0 和 3 处的房屋，窃取能力为 max(nums[0], nums[3]) = 9 。
     * - 窃取下标 1 和 3 处的房屋，窃取能力为 max(nums[1], nums[3]) = 9 。
     * 因此，返回 min(5, 9, 9) = 5 。
     * 
     * @param nums
     * @param k
     * @return
     */
    public int minCapability(int[] nums, int k) {
        /**
         * 解题思路，这道题我看了半天，都没看明白题意是啥，瞬间就给我整懵逼了。
         * 后来再看 油管的 花花解题视频，才明白了其中的深意。
         * 总体来说是一个升序的区间内，窃取能力越大，能够窃取的房间越多，但总数不超过 (nums.length+1)/2;
         * k 的取值范围是 0 =< k <=(nums.length+1)/2
         * 现在就是让你求 房间数为 k 时的最小窃取能能力。当然 k 越大，能力越大
         * 最终的效果图就是在坐标轴上，横坐标为 能力，纵坐标为窃取的房间数，房间数 >=k 时的最小盗窃能力
         * 这不就用上了最左侧的二分搜索了吗？
         * 然后最后一个问题，如何在我的抢劫能力=m 的情况下，抢劫最多的房子，这正好是动态规划的拿手好戏
         */
        if(nums == null || nums.length<1) {
            return 0;
        }
        cache4 = new int[nums.length];
        int max =0;
        //1 <= nums[i] <= 109
        for(int num: nums) {
            if(num > max) {
                max = num;
            }
        }
        int left =0, right = max+1;
        // 使用最左侧的二分查找来找到 满足 k 的最小 偷窃能力
        while(left < right) {
            int mid = left +(right-left)/2;
            Arrays.fill(cache4, -1);
            // 在二分法里面调用动态规划查找 mid 能力下的最大能够抢劫的房屋数量
            int res = dpOfMaxHousesWithGivenRobberPower(nums, mid, 0);
            if(res > k) {
                right = mid;
            } else if(res < k) {
                left = mid+1;
            } else if(res == k) {// 根据最左原则
                right = mid;
            }
        }
        return left;
    }


    int[] cache4;

    /**
     * 在给定的 抢劫能力 m 下，返回最多能抢劫的房子的数量。
     * 
     * @param nums
     * @param m
     * @param i
     * @return
     */
    int dpOfMaxHousesWithGivenRobberPower(int[] nums, int m, int i) {
        // base case 抢劫到最后一个房子之后
        if (i >= nums.length) {
            return 0;
        }
        // 动态规划的去重功能
        if (cache4[i] >= 0) {
            return cache4[i];
        }
        int res = -1;
        if (nums[i] > m) { // 房屋 nums[i] 的财富大于当前的抢劫能力，则不与抢劫
            res = dpOfMaxHousesWithGivenRobberPower(nums, m, i + 1);
        } else {
            // 此时的房子 nums[i] 是可以被抢劫的，有两种方案，抢劫或者不抢劫，这就是打家劫舍第一个最入门的问题
            res = Math.max(1 + dpOfMaxHousesWithGivenRobberPower(nums, m, i + 2), // 抢劫
                    dpOfMaxHousesWithGivenRobberPower(nums, m, i + 1) // 不抢劫
            );
        }
        cache4[i] = res;
        return res;
    }

    public static void main(String[] args) {
        HourseRobber hr = new HourseRobber();
        System.out.println(hr.uniquePaths(2, 2));
        System.out.println(hr.uniquePaths(3, 2));
        System.out.println(hr.uniquePaths(3, 7));

        int[] arr = new int[] {2,3,5,9};
        System.out.println(hr.minCapability(arr, 2));
    }
}
