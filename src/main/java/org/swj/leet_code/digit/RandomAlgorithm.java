package org.swj.leet_code.digit;

import java.util.Arrays;
import java.util.Random;

import org.swj.leet_code.linked_list.ListNode;

/**
 * 游戏中的随机算法
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/30 10:07
 */
public class RandomAlgorithm {

    private final Random random;
    int[] original;
    ListNode head;

    public RandomAlgorithm() {
        random = new Random();
    }

    public RandomAlgorithm(ListNode head) {
        random = new Random();
        this.head = head;
    }

    public RandomAlgorithm(int[] nums) {
        original = nums;
        random = new Random();
    }

    public int[] reset() {
        return original;
    }

    /**
     * 随机算法的第一个：洗牌算法
     * 如果我们先想随机初始化 k 颗雷的位置，你可以先把这 k 颗雷放在 board 开头，然后把 board 数组随机打乱
     * 这样雷不就不就随机分不到 board 数组的各个地方了吗？洗牌算法，或者叫随机乱置算法，就是专门解决这个问题的
     * leetcode 384. Shuffle an Array
     * 给你一个整数数组 nums ，设计算法来打乱一个没有重复元素的数组。打乱后，数组的所有排列应该是 等可能 的。
     * 
     * 实现 Solution class:
     * 
     * Solution(int[] nums) 使用整数数组 nums 初始化对象
     * int[] reset() 重设数组到它的初始状态并返回
     * int[] shuffle() 返回数组随机打乱后的结果
     * 
     * @return
     */
    public int[] shuffle() {
        int n = original.length;
        int[] copy = Arrays.copyOf(original, n);
        for (int i = 0; i < n; i++) {
            // 生成一个 [i...n) 之间的随机数
            int j = i + random.nextInt(n - i);
            // 交换 nums[i] 和 nums[j]
            swap(copy, i, j);
        }
        return copy;
    }

    void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    class Game {
        // 棋盘的行数和列数(非常大)
        int m, n;
        // 长度为 k 的数组，记录 k 个雷的一维索引
        int[] mines;

        // 将二维数组中的坐标 (x,y) 转化为一维数组中的索引
        int encode(int x, int y) {
            return x * n + y;
        }

        // 将一维数组中的索引转化为二维数组中的坐标(x,y)
        int[] decode(int pos) {
            return new int[] { pos / n, pos % n };
        }
    }

    /**
     * leetcode 382. 链表随机节点
     * 给你一个单链表，随机选择链表的一个节点，并返回相应的节点值。每个节点 被选中的概率一样 。
     * 
     * 实现 Solution 类：
     * 
     * Solution(ListNode head) 使用整数数组初始化对象。
     * int getRandom() 从链表中随机选择一个节点并返回该节点的值。链表中所有节点被选中的概率相等
     * 
     * @return
     */
    public int getRandom() {
        ListNode p = head;
        int i = 0;
        int res = 0;
        while (p != null) {
            i++;
            // 生成一个 [0,i) 的随机数，这个随机数等于 0 的概率是 1/i。
            // 如何证明，请参考 digit.md 文档中有关 随机数的描述
            if (0 == random.nextInt(i)) {
                res = p.val;
            }
            p = p.next;
        }
        return res;
    }

    /**
     * leetcode 398. 随机数索引
     * 给你一个可能含有 重复元素 的整数数组 nums ，请你随机输出给定的目标数字 target 的索引。你可以假设给定的数字一定存在于数组中。
     * 
     * 实现 Solution 类：
     * 
     * Solution(int[] nums) 用数组 nums 初始化对象。
     * int pick(int target) 从 nums 中选出一个满足 nums[i] == target 的随机索引 i
     * 。如果存在多个有效的索引，则每个索引的返回概率应当相等。
     * 示例：
     * 
     * 输入
     * ["Solution", "pick", "pick", "pick"]
     * [[[1, 2, 3, 3, 3]], [3], [1], [3]]
     * 输出
     * [null, 4, 0, 2]
     * 
     * 解释
     * Solution solution = new Solution([1, 2, 3, 3, 3]);
     * solution.pick(3); // 随机返回索引 2, 3 或者 4 之一。每个索引的返回概率应该相等。
     * solution.pick(1); // 返回 0 。因为只有 nums[0] 等于 1 。
     * solution.pick(3); // 随机返回索引 2, 3 或者 4 之一。每个索引的返回概率应该相等。
     * 
     * @param target
     * @return
     */
    public int pick(int target) {
        /**
         * 
         * 这道题的解法，我感觉完全不是 单链表中随机返回 k 个随机节点的值
         * 我的思路是，先排序，在二分法找到 target 的 leftbound 和 rightbound
         * 然后 在 left_bound 和 right_bound 之间找到。
         * 后来又发现这种方式太费时又不讨好，一时半会想不到好的办法。
         * 其实笨办法就是将 等于 target 的所有元素和索引都放到一个集合里面，
         * 然后 random 这个集合，返回元素的原始索引。
         * 参考了阿东的思路。更加精妙
         */
        int count = 0;
        int res = 0;
        for (int i = 0; i < original.length; i++) {
            if (original[i] != target) {
                continue;
            }
            count++;
            // 等于 count 的每个元素的被返回 概率都是 1/sum(nums[i]==count)
            if (0 == random.nextInt(count)) {
                res = i;
            }
        }
        return res;
    }

    int[] getKofRandomVal(ListNode head, int k) {
        ListNode p;
        // 前 k 个元素先选上
        int[] res = new int[k];
        p = head;
        for (int i = 0; i < k && p != null; i++) {
            res[k] = p.val;
            p = p.next;
        }

        int count = k;
        int j = 0;
        // while 循环遍历链表
        while (p != null) {
            count++;
            // 生成一个 [0...count) 之间的随机数 j
            // 这个整数 j 小于 k 的概率就是 k/count
            if ((j = random.nextInt(count)) < k) {
                res[j] = p.val;
            }
            p = p.next;
        }
        return res;
    }

    /**
     * 在区间 [lo..hi] 之间随机选取 k 个数
     * 
     * @param k
     * @param lo
     * @param hi
     * @return
     */
    public int[] getRandomRange(int k, int lo, int hi) {
        int[] res = new int[k];
        // 先填充前 k 个数
        for (int i = 0; i < k; i++) {
            res[i] = lo + i;
        }
        int count = k;
        int j;
        while (count < hi - lo) {
            count++;
            // 生成一个[0...count) 之间的随机数
            // 如果 小于 k
            if ((j = random.nextInt(count)) < k) {
                res[j] = lo + count - 1;
            }
        }
        return res;
    }

    public int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1);
            count++;
        }
        return count;
    }

    /**
     * leetcode 231. 2 的幂
     * 给你一个整数 n，请你判断该整数是否是 2 的幂次方。如果是，返回 true ；否则，返回 false 。
     * 
     * 如果存在一个整数 x 使得 n == 2x ，则认为 n 是 2 的幂次方。
     * 
     * @param n
     * @return
     */
    public boolean isPowerOfTwo(int n) {
        if (n < 1) {
            return false;
        }
        // 2 的幂次只有 1 个 1.
        return (n & (n - 1)) == 0;
    }

    /**
     * 136. 只出现一次的数字
     * 示例 1 ：
     * 
     * 输入：nums = [2,2,1]
     * 输出：1
     * 示例 2 ：
     * 
     * 输入：nums = [4,1,2,1,2]
     * 输出：4
     * 
     * @param nums
     * @return
     */
    public int singleNumber(int[] nums) {
        // 解题思路，就是利用 a&a=0 这个牛逼的条件，数组中只有 1 个数是单独的，其他数都是成对出现的，那么
        // 对所有的元素进行异或运算，最终得到的那个数就是 单独的数
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            res ^= nums[i];
        }
        return res;
    }

    /**
     * 丢失的数字 leetcode 268
     * 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
     * 
     * 示例 1：
     * 
     * 输入：nums = [3,0,1]
     * 输出：2
     * 解释：n = 3，因为有 3 个数字，所以所有的数字都在范围 [0,3] 内。2 是丢失的数字，因为它没有出现在 nums 中。
     * 
     * @param nums
     * @return
     */
    public int missingNumber(int[] nums) {
        /**
         * 解题思路，利用异或运算的交换律和结合律
         * 2 ^ 3 ^ 2 = 3 ^ (2 ^ 2) = 3 ^ 0 = 3
         */
        int res = 0;
        int n = nums.length;
        res ^= n;
        for (int i = 0; i < n; i++) {
            // 和其他的元素、索引做异或
            res ^= nums[i] ^ i;
        }
        return res;
    }
}
