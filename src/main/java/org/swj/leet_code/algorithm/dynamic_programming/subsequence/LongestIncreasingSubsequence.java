package org.swj.leet_code.algorithm.dynamic_programming.subsequence;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/07/27 15:16
 *        Leecode 第 300 题 和 354 题——俄罗斯套娃信封问题
 *        最长增长子序列 LongestIncreasingSubsequence，简称 LIS
 *        ，是非常经典的一个算法问题，比较容易想到的是动态规划解法，时间
 *        复杂度是O(N^2), 我们借这个问题由浅入深讲解下状态转移方程，如果写出动态规划解法。
 *        另外一个解法是，利用二分查找，该想法是比较难想到的，此时的时间复杂度是O(NlogN)，我们通过一种简单的纸牌游戏来辅助理解这种巧妙的解法
 *        纸牌游戏的过程参考 note.md
 *        比如说输入 nums=[10,9,2,5,3,7,101,18]，其中最长的递增子序列是 [2,3,7,101], 所以算法的输出应该是 4
 *        注意「子序列」和「子串」这两个名词的区别，子串一定是连续的，而子序列不一定是连续的。
 *        下面我们来设计动态规划算法来解决这个问题
 */
public class LongestIncreasingSubsequence {
    /**
     * dp[i] 定义为 表示以 nums[i] 这个数结尾的最长递增子序列的长度
     * 0 1 2 3 4 5
     * nums 1 4 3 4 2 3
     * dp 1 2 2 3 2 ？
     * max(1+1,2+1) 就是从前面的 2 起则为 1，从最前面的 1 起则为 2，
     * 观察上面这个数组，发现 nums[5]=3,要怎么算 dp[5] 那？
     * 既然是递增子序列，我们只要找到前面哪些结尾比 3 小的子序列，然后把 3 接到这些子序列的末尾，就可以形成一个新的
     * 递增自序列，而这个新的子序列的长度加 1
     * 以我们举的例子来看，nums[0] 和 nums[4] 都是小于 nums[5] 的，然后 可以的得出
     * dp[5] = max(dp[1]+1,dp[4]+1) = max(1+1,2+1)
     * nums[5] 前面有哪些元素小于 nums[5]? 这个好算，用 for 循环就能比较一波把这些元素找出来
     * for (int j=0;j<i; j++) {
     * if(nums[i] > nums[j]) {
     * dp[i] = Math.max(dp[i],dp[j]+1)
     * }
     * }
     * 当 i=5 时，这段代码的逻辑就可以算出 dp[5]。其实到这里，这道算法题我们基本已经实现了
     * dp[4], dp[3] 怎么算 ？ 类似数学归纳法，我们已经可以算出 dp[5] 了，其他的就可以算出来。
     */

    int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        // 每个元素的最长自序列长度至少是 1（元素本身的长度）
        Arrays.fill(dp, 1);
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return Arrays.stream(dp).max().getAsInt();
    }

    /**
     * 上述解法是标准的动态规划，但是对于最长递增子序列来说，这个解法不是最优的，可能无法通过所有测试用例
     * 下面我们使用 二分法来查找最长子序列。二分法查找的详细表述在 note.md 中最长子序列查找——二分法章节
     * 
     * @param nums
     * @return
     */
    int lengthOfLisBs(int[] nums) {
        // 新建一个跟 nums 长度一致的堆顶，堆顶的元素个数最坏情况跟 nums 的长度一致。
        // top 的元素默认初始化为 0
        int[] top = new int[nums.length];
        int pile = 0; // 默认的堆为 0,对相当于数组的 top 堆顶有元素个数的 length
        // 将 nums 数组中的数字看作扑克牌
        for (int pork : nums) {
            // 将 top 数组的 0-pile 区间看作是堆顶的二分区间
            int left = 0;
            int right = pile;
            // 找到合适最左侧放置的位置
            while (left < right) { // 循环退出条件是 left==right
                // 因此每次搜索的区间是 [left,pile) 左闭右开。而终止条件是 left == right。
                int middle = left + (right - left) / 2;
                if (top[middle] > pork) {
                    right = middle; // 此处跟二分法稍微有些区别，这里为 middle，而不是 middle - 1，
                    // 是因为下面使用了 left 变量作为堆顶元素的访问下标，为了使 left 计算正确
                    // 之所以这里让 right = middle, 是因为在这个扑克牌的逻辑中，当前 pork 只能落在比它大的且是最左边的元素上。
                    // 其实是为了找到一个比当前元素大的最左边的元素，落上去或者说叫替换
                    // 所以比 pork 小的话, 左指针 left 可以 ++，定位到最后一个比它大的元素时和跟他相等的元素时，right 右指针需要在此停住
                    // 以满足停在手中的扑克牌最左侧的上面的要求，请查看考 md 文件中的扑克牌摆放示意图
                } else if (top[middle] < pork) {
                    left = middle + 1;
                } else {
                    right = middle;
                }
            }
            // 没有找到合适的牌堆，如果二分法遍历没有开始或者遍历完也没有找到 pork 的位置，则新增一个堆
            if (left == pile) { // left > 0 说明且 left == pile 说明left 索引 此时已经在新堆上了
                pile++; // pile 是个数，left 是索引
            }
            // 将堆顶的该位置设置为当前的 pork，也就是覆盖操作，覆盖之后，只能看到当前 pork
            // 之前的元素就会丢失，这个跟我们肉眼看到的扑克图片效果稍微有点不一样，但是结果是一致的
            top[left] = pork;
        }
        return pile;
    }

    /**
     * 再次使用二分法来解决最长递增子序列的长度，一把梭了，不带注释
     * 
     * @param nums
     * @return
     */
    int lisUsingBinarySearch(int[] nums) {
        int pilo = 0;
        // 存放堆顶元素的数组，默认长度跟 nums 长度一致。
        int[] tops = new int[nums.length];
        for (int pork : nums) {
            int left = 0, right = pilo;
            while (left < right) {
                int middle = left + (right - left) / 2;
                if (tops[middle] > pork) {
                    right = middle;
                } else if (tops[middle] < pork) {
                    left = middle + 1;
                } else {
                    right = middle;
                }
            }
            if (left == pilo) {
                pilo++;
            }
            tops[left] = pork;
        }
        return pilo;
    }

    int lisWithBinarySearch(int[] nums) {
        int[] tops = new int[nums.length];
        int pilo = 0;
        for (int pork : nums) {
            int left = 0, right = pilo;
            while (left < right) {
                int middle = left + (right - left) / 2;
                if (tops[middle] > pork) {
                    right = middle;
                } else if (tops[middle] < pork) {
                    left = middle + 1;
                } else {
                    right = middle;
                }
            }
            if (left == pilo) {
                pilo++;
            }
            tops[left] = pork;
        }
        return pilo;
    }

    /**
     * 给你一个二维数组 envelops, 其中 envelopes[i]=[wi,hi]，表示第 i 个信封的宽度和高度
     * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，就如同俄罗斯套娃一样。
     * 请计算 最多能有多少个 信封能组成一组 “俄罗斯套娃”信封（即把一个信封放到另一个信封里面）。
     * 输入 [[5,4],[6,4],[6,7],[2,3]]
     * 输出 3
     * 解释：最多信封的个数为 3，组合为 [2,3]=>[5,4]=>[6,7]。
     * 详细解析过程请参加 note.md 有关俄罗斯信封部分
     * 
     * @param nums 该方法是对 LIS 方法的一个具体运用
     * @return
     */
    int russia_envelope(int[][] envelopes) {
        int n = envelopes.length;
        // envelopes 的形式为 [[w:h],[w:h]....]
        // 对数组 envelops 按照 w 排序，如果 w 相同则按照 h 进行倒序排序
        Arrays.sort(envelopes, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                // 0 为 width，1 为 height
                return o1[0] == o2[0] ? o2[1] - o1[1] : o1[0] - o2[0];
            }
        });
        // 排完序之后，则将 h 提取出来，调用 LIS 方法返回 最多嵌套序列
        int[] h = new int[n];
        int counter = 0;
        for (int[] subArr : envelopes) {
            h[counter++] = subArr[1];
        }
        return lengthOfLisBs(h);
    }

    public static void main(String[] args) {
        int[] arr = new int[] { 10, 9, 2, 5, 3, 7, 101, 18 };
        int[] arr2 = new int[] { 6, 3, 5, 10, 11, 2, 9, 14, 13, 7, 4, 8, 12 };
        LongestIncreasingSubsequence instance = new LongestIncreasingSubsequence();
        System.out.println(instance.lisWithBinarySearch(arr));
        System.out.println(instance.lisWithBinarySearch(arr2));
        int[][] arrays = new int[][] {
                new int[] { 5, 4 },
                new int[] { 6, 4 },
                new int[] { 6, 7 },
                new int[] { 2, 3 }
        };
        System.out.println(instance.lisRussiaEnvelops2(arrays));
    }

    int lisRussiaEnvelops2(int[][] envelopes) {
        // 将二维数组先按照 w 进行升序排列，然后按照 h 降序排列，最后取所有信封的 h 放入一维数组，利用一维数组的求最长递增子序列的方式求解
        if (envelopes == null || envelopes.length < 1) {
            return 0;
        }
        Arrays.sort(envelopes, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] == o2[0] ? o2[1] - o1[1] : o1[0] - o2[0];
            }
        });
        int[] h = new int[envelopes.length];
        int i = 0;
        for (int[] arr : envelopes) {
            h[i++] = arr[1];
        }
        // todo 使用 dp 方法对数组求 最小递增子序列
        System.out.println(Arrays.toString(h));
        return lisWithDp(h);
    }

    int lisWithDp(int[] nums) {
        // dp[i] 代表 i 位置的最大递增子序列长度
        int[] dp = new int[nums.length];
        // base case 0：每个字符的递增子序列至少包括它自己，因此长度至少为 0
        Arrays.fill(dp, 1);
        for (int i = 0, len = nums.length; i < len; i++) {
            // max(dp[j]) + 1 where j<1
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return Arrays.stream(dp).max().getAsInt();
    }
}
