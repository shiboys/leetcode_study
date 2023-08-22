package org.swj.leet_code.array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

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
     * 将重复的元素删除，也就是数组中不包含任何重复的元素
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

    void moveZeros(int[] nums) {
        int pos = removeElement(nums, 0);
        for (; pos < nums.length; pos++) {
            nums[pos] = 0;
        }
    }

    int binarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (target == nums[mid]) {
                return mid;
            } else if (target > nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
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



    public static void main(String[] args) {
        int[] arr = new int[] { 1, 1, 2 };
        DoublePointerHandleArray instance = new DoublePointerHandleArray();
        System.out.println(instance.removeDuplicates(arr));

        arr = new int[] { 0, 0, 1, 1, 1, 2, 2, 3, 3, 4 };
        System.out.println(instance.removeDuplicates(arr));

        arr = new int[] { 3, 2, 2, 3 };
        System.out.println(instance.removeElement(arr, 3));

        arr = new int[] { 0, 1, 2, 2, 3, 0, 4, 2 };
        System.out.println(instance.removeElement(arr, 2));

        arr = new int[] { 0, 1, 2, 2, 3, 0, 4, 2 };
        instance.moveZeros(arr);
        System.out.println(Arrays.toString(arr));

        arr = new int[] { 2, 7, 11, 15 };
        System.out.println(Arrays.toString(instance.twoSum(arr, 9)));

        String s = "my leetcode";
        char[] sArr = s.toCharArray();
        instance.reverseString(sArr);
        System.out.println(Arrays.toString(sArr));

        s = "as上海自来水来自海上ha";
        System.out.println(instance.longestPalindromeSubstring(s));
        s = "google is a good searching engine.";
        System.out.println(instance.longestPalindromeSubstring(s));

        Random rand  = new Random();
        rand.nextInt(10);

        HashMap map  = new HashMap<>();
        map.compute(map, null);
    }

}
