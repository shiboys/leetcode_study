package org.swj.leet_code.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/19 11:27
 *        双指针解决常见的 7 个 leetcode 数组题目
 */
public class DoublePointerHandleArray {

    /**
     * leetcode 26 题，有序数组中，原地删除重复重复项，并返回删除后的数组长度
     * 
     * @param nums
     * @return
     */
    int removeDuplicates(int[] nums) {
        int slow = 0, fast = 0;
        int len = nums.length;
        while (fast < len) {
            // 如果慢指针跟快指针不等
            if (slow < fast && nums[slow] != nums[fast]) {
                slow++;
                // 维护 [0..slow] 无重复。
                nums[slow] = nums[fast];
            }
            fast++;
        }
        // 数组长度为索引长度 + 1
        return slow + 1;
    }

    /**
     * leetcode 27
     * 将重复的元素删除，也就是数组中不包含任何重复的元素
     * 返回剩余元素的数量
     * 
     * @param nums
     * @return
     */
    int removeElement(int[] nums, int val) {
        int slow = 0, fast = 0;
        while (fast < nums.length) {
            // 3 2 2 3, val=3 => 2,2
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
            fast++;
        }
        return slow;
    }

    /**
     * leetcode 283 题
     * 
     * @param nums
     */
    void moveZeros(int[] nums) {
        int pos = removeElement(nums, 0);
        for (; pos < nums.length; pos++) {
            nums[pos] = 0;
        }
    }

    int[] twoSum(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                return new int[] { nums[left], nums[right] };
            }
            if (sum < target) { // 需要增大，left ++
                left++;
            } else {
                right--;
            }
        }
        return null;
    }

    /**
     * leetcode 344 题，反转数组
     * 
     * @param s
     */
    void reverseString(char[] s) {
        int left = 0, right = s.length - 1;
        while (left < right) {
            char tmp = s[left];
            s[left] = s[right];
            s[right] = tmp;
            left++;
            right--;
        }
    }

    boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            right--;
            left++;
        }
        return true;
    }

    /**
     * 返回以 l 和 r 为重点的最长回文子串。如果回文子串为奇数，则 l 和 r 相等，否则 l + 1 = r
     * 
     * @param s 回文
     * @param l 中点的左起点
     * @param r 重点的右起点
     * @return 最长的回文子串
     */
    String palindromeSubString(String s, int l, int r) {
        int n = s.length();
        while (l >= 0 && r <= n - 1 && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        if (l + 1 > r) {
            return null;
        }
        return s.substring(l + 1, r);
    }

    public String longestPalindromeSubstring(String s) {
        String res = "";
        for (int i = 0, len = s.length(); i < len; i++) {
            // 判断以 i 为中心的最长回文子串
            String s1 = palindromeSubString(s, i, i);
            if (s1 != null) {
                res = res.length() >= s1.length() ? res : s1;
            }
            // 再判断以 i,i+1 为中心的偶数的最长字符串回文长度
            String s2 = palindromeSubString(s, i, i + 1);
            if (s2 != null) {
                res = res.length() >= s2.length() ? res : s2;
            }
        }
        return res;
    }

    /**
     * leetcode 870 优势洗牌
     * 
     * @param nums1
     * @param nums2
     * @return
     */
    int[] advantageCount(int[] nums1, int[] nums2) {
        // 齐王的马，将其战力和比赛排位封装进一个数组中
        int n = nums2.length;
        int[] res = new int[n];
        // 小顶堆倒序排列就成了大顶堆，齐王队列
        PriorityQueue<int[]> pqqw = new PriorityQueue<>((o1, o2) -> {
            // 按照战力倒序排列
            return o2[1] - o1[1];
        });
        for (int i = 0, len = nums2.length; i < len; i++) {
            pqqw.offer(new int[] { i, nums2[i] });
        }
        // 田忌的马排序
        Arrays.sort(nums1);
        int left = 0, right = nums1.length - 1;
        while (!pqqw.isEmpty()) {
            int[] qh = pqqw.poll();
            int i = qh[0], hp = qh[1]; // hp:horse power
            if (hp < nums1[right]) { // 打得过
                res[i] = nums1[right];
                right--;
            } else { // 打不过，送人头
                res[i] = nums1[left];
                left++;
            }
        }
        return res;
    }

    /**
     * 202. 快乐数
     * 「快乐数」 定义为：
     * 
     * 对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和。
     * 然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。
     * 如果这个过程 结果为 1，那么这个数就是快乐数。
     * 如果 n 是 快乐数 就返回 true ；不是，则返回 false 。
     * 输入：n = 19
     * 输出：true
     * 解释：
     * 1^2 + 9^2 = 82
     * 8^2 + 2^2 = 68
     * 6^2 + 8^2 = 100
     * 1^2 + 0^2 + 0^2 = 1
     * 
     * @param n
     * @return
     */
    public boolean isHappy(int n) {
        if (n == 1) {
            return true;
        }
        int slow = n, fast = n;
        do {
            slow = calcSquareEachNum(slow);
            fast = calcSquareEachNum(fast);
            fast = calcSquareEachNum(fast);
        } while (slow != fast);

        return slow == 1;
        // 这道题，让我对快慢指针的认知，达到了新的高度
    }

    boolean oldMethond(int n) {
        int m = n;
        int limit = 10000;
        int counter = 0;
        // m 和 n 再次相等，则说明有循环。
        // 这里的无限循环，我理解错了，这里的无限循环跟链表的相交点是相同的意思
        while ((m = calcSquareEachNum(m)) != n && counter < limit) {
            if (m == 1) {
                return true;
            }
            counter++;
        }
        return false;
    }

    int calcSquareEachNum(int n) {
        int square = 0;
        int mod = 0;
        while (n > 0) {
            mod = n % 10;
            square += mod * mod;
            n /= 10;
        }
        return square;
    }

    /**
     * 56. 合并区间
     * 
     * @param intervals
     * @return
     */
    public int[][] merge(int[][] intervals) {
        /**
         * 解题套路，使用左右指针，将右指针所指向的数组跟左指针对比，看是否可以合并。
         * 结果发现，左右指针没有搞定，
         * 最终发现用栈实现相当爽，结果提交一试，通过了，不过需要排序，
         * 用例给的都是有序的例子，坑爹
         */
        if (intervals == null || intervals.length < 1) {
            return null;
        }
        // 排序很有必要
        Arrays.sort(intervals, (a, b) -> {
            return a[0] - b[0];
        });
        Stack<int[]> stack = new Stack<>();
        for (int[] arr : intervals) {
            if (!stack.isEmpty() && stack.peek()[1] >= arr[0]) {
                int[] last = stack.pop();
                arr[0] = Math.min(arr[0], last[0]);
                arr[1] = Math.max(arr[1], last[1]);
            }
            stack.push(arr);
        }
        int[][] res = new int[stack.size()][];
        int counter = stack.size() - 1;
        while (!stack.isEmpty()) {
            res[counter--] = stack.pop();
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = new int[] { 1, 1, 2 };
        DoublePointerHandleArray instance = new DoublePointerHandleArray();
        // System.out.println(instance.removeDuplicates(arr));

        // arr = new int[] { 0, 0, 1, 1, 1, 2, 2, 3, 3, 4 };
        // System.out.println(instance.removeDuplicates(arr));

        // arr = new int[] { 3, 2, 2, 3 };
        // System.out.println(instance.removeElement(arr, 3));

        // arr = new int[] { 0, 1, 2, 2, 3, 0, 4, 2 };
        // System.out.println(instance.removeElement(arr, 2));

        // arr = new int[] { 0, 1, 2, 2, 3, 0, 4, 2 };
        // instance.moveZeros(arr);
        // System.out.println(Arrays.toString(arr));

        // arr = new int[] { 2, 7, 11, 15 };
        // System.out.println(Arrays.toString(instance.twoSum(arr, 9)));

        // String s = "my leetcode";
        // char[] sArr = s.toCharArray();
        // instance.reverseString(sArr);
        // System.out.println(Arrays.toString(sArr));

        // s = "as上海自来水来自海上ha";
        // System.out.println(instance.longestPalindromeSubstring(s));
        // s = "google is a good searching engine.";
        // System.out.println(instance.longestPalindromeSubstring(s));

        // Random rand = new Random();
        // rand.nextInt(10);

        // arr = new int[] { 12, 24, 8, 32 };
        // int[] arrq = new int[] { 13, 25, 32, 11 };
        // System.out.println(Arrays.toString(instance.advantageCount(arr, arrq)));
        System.out.println(instance.isHappy(19));
        System.out.println(instance.isHappy(2));

        int[][] intervals = new int[][] { { 1, 3 }, { 2, 6 }, { 8, 10 }, { 15, 18 } };
        int[][] res = instance.merge(intervals);
        for (int[] array : res) {
            System.out.println(Arrays.toString(array));
        }

        intervals = new int[][] { { 1, 4 }, { 0, 0 } };
        res = instance.merge(intervals);
        for (int[] array : res) {
            System.out.println(Arrays.toString(array));
        }
    }

}
