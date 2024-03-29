package org.swj.leet_code.algorithm.dynamic_programming.basic_skill;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/02 19:14
 *        不同的子序列。其实就是子序列的个数
 *        leetcode 115
 *        给 2 个字符串 s 和 t，统计并返回在 s 中子序列 t 出现的个数。
 *        示例1：
 *        s="babgbag",t="bag"
 *        输出为 5.因为有 5 种得到 bag 的方案。
 */
public class DistinctSubSequence {
    int[][] memo;

    /**
     * 解题思路：
     * 动态规划组合拳，
     * 首先，经过前面的刷题心得，我们刷的算题问题的本质是「穷举」，动态规划也不例外，你必须想办法列出所有可能的解，然后从中筛选出符合题目要求的解
     * 然后，我们需要暴力求解转化为效率更高的动态规划解法。
     * 然后，想要写出暴力解需要依据动态转移方程，动态转移方程是动态规划的核心，可不是那么容易想出来的。不过，经过前文的学习，我们知道
     * 思考动态转义方程的一个基本方法是数学归纳法，即明确 dp 函数或者数组的定义，然后使用这个定义，从已知的「状态」中推导出未知的「状态」
     * 
     * 接下就是本文要着重套路你的问题了：就算 dp 函数/数组的定义相同，如果使用不同的「视角」进行穷举，效率也不见得是相同的。
     * 关于穷举「视角」的问题，后面会有一篇专门的 「子集规划问题」来专门讨论。
     * 排列组合的 2 种视角
     * 请参考 note.md 排列组合的两种视角
     * 
     */

    /**
     * 我们要数一数 s 的子序列有多少个 t，说白了就是穷举嘛，那么首先想到的就是能不能把原问题分解成规模更小的子问题，然后通过子问题的答案推导出原问题的答案
     * 我们首先定义一个 dp 函数
     * // 定义：s[i..]的子序列中 t[j..] 出现的次数为 dp(s,i,t,j)
     * int dp(String s,int i,String t,int j)
     * 在阅读完 note.md 中排列组合的两种视角后，
     * 
     * @param s
     * @param t
     * @return
     */
    int numDistinct(String s, String t) {
        memo = new int[s.length()][t.length()];
        for (int i = 0; i < memo.length; i++) {
            Arrays.fill(memo[i], -1);
        }
        return dp_s(s, 0, t, 0);
    }

    /**
     * 定义：s[i..]的子序列中 t[j..] 出现的次数为 dp(s,i,t,j)
     * 接下来思考如何利用 dp 函数将大问题分解成小问题，即如何写出状态转移方程进行穷举
     * 
     * @param s
     * @param i
     * @param t
     * @param j
     * @return
     */
    int dp(String s, int i, String t, int j) {
        // base case 1
        // j == t.length-1 说明 j 指针已经走到了 t 字符串的末尾，完成了一次 s 到 t 的匹配。
        if (j == t.length()) {
            return 1;
        }
        // base case2: s.length-i < t.length() -j ，因为 i 和 j 是从 0 开始，如果出现 s 的剩余字符长度小于 t
        // 的剩余字符长度，说明已经没有匹配的可能了
        if (s.length() - i < t.length() - j) {
            return 0;
        }
        // 查询备忘录
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        // 执行状态转移方程
        int res = 0;
        // 在 s[i...] 中寻找 k，使得 s[k] == t[j]
        for (int k = i; k < s.length(); k++) { // k 最好是从 1 开始
            if (s.charAt(k) == t.charAt(j)) {
                // 累加结果
                res += dp(s, k + 1, t, j + 1);
            }
        }
        memo[i][j] = res;
        return res;
    }

    public static void main(String[] args) {
        DistinctSubSequence instance = new DistinctSubSequence();
        String s = "rabbbit", t = "rabbit";
        System.out.println(instance.dp_array(s, t));
        s="babgbag" ;
         t= "xyz";
         System.out.println(instance.dp_array2(s, t));
    }

    /**
     * 有关复杂度的讨论请查看考 note.md 有关动态规划&排列组合的算法复杂度计算
     */

    int dp_s(String s, int i, String t, int j) {
        // base case 0
        // 找到一个匹配
        if (j == s.length()) {
            return 1;
        }
        if (s.length() - i < t.length() - j) {
            return 0;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        int res = 0;
        // 执行状态转移方程
        if (s.charAt(i) == t.charAt(j)) {
            // 匹配，两种情况累加
            res += dp(s, i + 1, t, j + 1) + dp(s, i + 1, t, j);
        } else {
            // 不匹配，在 s[i+1..] 中的子序列中计算 t[j..] 的出现次数
            res += dp(s, i + 1, t, j);
        }
        memo[i][j] = res;
        return res;
    }

    /**
     * 上述解法中的 dp 函数递归次数，即状态 i,j 的不同组合个数为 O(MN), 而 dp 函数本身没有 for 循环，即时间复杂度为 O(1)，
     * 所以算法总的时间复杂度为 O(MN), 比上一个算法的时间复杂度要好一些。
     * 
     * 至此，这道题分析完了。我们分别站在 s 和 t 的视角运用 dp 函数定义进行穷举，得出两种完全不同的但都是正确的状态转移逻辑
     * 不过两种逻辑在代码实现上有效率的差异。
     * 我们不妨思考什么样的动态规划问题能抽象成经典的「球盒模型」那？
     */

    /**
     * 使用 dp 数组的解法，这个是我参考 leetcode 的解法
     * leetcode 的dp方程是 dp[i][j] = dp[i-1][j-1] + dp[i-1][j] where s[i-1]=t[j-1]
     * dp[i][j]= dp[i-1][j] where s[i-1] != t[i-1]
     * leetcode 的解法是字符串从后向前匹配，按照方向是自底向上迭代。
     * leetcode 这个方法太难理解了。
     * @param s
     * @param t
     * @return
     */
    int dp_array(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[][] dp = new int[m + 1][n + 1];
        // s="babgbag" , t= "bag"
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                // base case 
                if (j == 0) { // 这个的意思跟递归判断 j==n 是一样的，就是表示匹配到一个完整的子序列
                    dp[i][j] = 1;
                } else if (i==0) { // 防止溢出. 也可以理解为 s 为空字符串 
                    dp[i][j] = 0;
                } else {
                    // 因为 dp 的 i 和 j 比字符串的索引位偏移 1 位，所以要使用 i-1 和 j-1
                    if (s.charAt(i-1) == t.charAt(j-1)) {
                        // 仍然是 j 不动，i 回退一步，有关参考图如下 note.md 所示
                        dp[i][j] = dp[i - 1][j - 1] + dp[i-1][j];
                    } else {
                        dp[i][j] = dp[i-1][j];
                    }
                }
            }
        }
        return dp[m][n];
    }

    /**
     * 从后往前循环。等于是从前往后递归，
     * 但是 dp 的思想仍然是 dp[i][j] = dp[i+1][j+1] + dp[i+1][j]
     * 也就是 如果当前 s[i]=t[j], 则 dp 向后传递，要么 dp[i+1][j+1],要么 t[j] 保持不动
     * 让 s[i+1] 来匹配
     * 我这里采用 if s[i] == t[j] then dp[i][j] = dp[i+1][j+1] + dp[i+1][j] else dp[i][j]
     * = dp[i+1][j] 的解法更容易理解
     * 我这里仍然采用 labuladong 的字符串从前往后匹配。自顶向下
     * @param s
     * @param t
     * @return
     */
    int dp_array2(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[][] dp = new int[m + 1][n + 1];
        // s="babgbag" , t= "bag"
        for (int i = m; i >= 0; i--) {
            for (int j = n; j >= 0; j--) {
                // base case 
                if (j == n) { // 要匹配的 t 为 空字符串，
                    dp[i][j] = 1;
                } else if (i==m) { // 防止溢出. 也可以理解为 s 为空字符串 
                    dp[i][j] = 0;
                } else {
                    if (s.charAt(i) == t.charAt(j)) {
                        // 上面 dp 递归的循环版本，也是两种情况累加
                        dp[i][j] = dp[i + 1][j + 1] + dp[i+1][j];
                    } else {
                        dp[i][j] = dp[i+1][j];
                    }
                }
            }
        }
        return dp[0][0];
    }
}
