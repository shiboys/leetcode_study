package org.swj.leet_code.array;

import java.util.Arrays;
import java.util.PriorityQueue;

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

        arr = new int[] {12,24,8,32};
        int [] arrq = new int[] {13,25,32,11};
        System.out.println(Arrays.toString(instance.advantageCount(arr, arrq)));
    }

}
