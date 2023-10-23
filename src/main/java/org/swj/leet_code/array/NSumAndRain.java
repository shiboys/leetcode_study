package org.swj.leet_code.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.swj.leet_code.string.MaxPalindromeString;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/03/02 19:09 力扣第 1 题，在一个数组中 求 两个数字之和为定值 S 的两个数组的索引
 */
public class NSumAndRain {

  /**
   * 解法 1，使用蛮力法求解，通过获取数组中的第 i 个元素，将 i 个元素和后面的所有元素求和，如果等于目标值，则返回
   *
   * @param nums
   * @param target 目标和
   * @return
   */
  public int[] twoSum(int[] nums, int target) {
    for (int i = 0; i < nums.length; i++) {
      for (int j = i + 1; j < nums.length; j++) {
        if (nums[i] + nums[j] == target) {
          return new int[] { i, j };
        }
      }
    }
    return null;
  }

  /**
   * 算法1 的时间复杂度很明显为 O(n^2), 如何进行优化那？ 如果给定一个数和目标和，需要寻找下一个元素是否在数组中，如果能在 O(1)
   * 的时间里面找到另外一个数，是不是复杂度就讲到
   * O(n) 了？ O(1) 从集合中找到一个数，我们一般想到的数据结构就是 Map<K,V>，因此解法 2 采用 Map 来解
   */
  public int[] twoSum2(int[] nums, int target) {
    java.util.Map<Integer, Integer> map = new java.util.HashMap<>();
    for (int i = 0; i < nums.length; i++) {
      int curr = nums[i];
      int targetNum = target - curr;
      if (map.containsKey(targetNum)) {
        // map 里面的数据是数组的第一个元素
        return new int[] { map.get(targetNum), i };
      }
      map.put(curr, i);
    }
    return null;
  }

  /**
   * 两数之和的泛型版，用于解决 3 数之和，nSum 系列。前提是 nums 是排序数组
   * 
   * @param nums
   * @param target
   * @return
   */
  public List<List<Integer>> twoSumGeneric(int[] nums, int start, int target) {
    List<List<Integer>> res = new ArrayList<>();
    if (nums == null || nums.length < 1 || start >= nums.length) {
      return res;
    }
    int lo = start, hi = nums.length - 1;
    int left, right;
    while (lo < hi) { // 两数之和，一定是两个数，不能是 lo==hi 的时候的 nums[lo] 和 nums[hi] 之和。
      left = nums[lo];
      right = nums[hi];
      int nSum = left + right;

      if (nSum < target) {
        // lo++
        while (lo < hi && nums[lo] == left)
          lo++;
      } else if (nSum > target) {
        // hi--
        while (lo < hi && nums[hi] == right)
          hi--;
      } else {// nSum== target
        ArrayList<Integer> list = new ArrayList<>();
        list.add(left);
        list.add(right);
        res.add(list);
        while (lo < hi && nums[lo] == left)
          lo++;
        while (lo < hi && nums[hi] == right)
          hi--;
      }
    }
    return res;
  }

  public List<List<Integer>> threeSumTarget(int[] nums, int start, int target) {
    if (nums.length < 1) {
      return null;
    }
    // 使用头尾双指针之前，数组必须有序
    Arrays.sort(nums);
    List<List<Integer>> res = new ArrayList<>();
    for (int i = start; i < nums.length; i++) {
      int val = nums[i];
      List<List<Integer>> tuple = twoSumGeneric(nums, i + 1, target - nums[i]);
      for (List<Integer> match : tuple) {
        match.add(0, val);
      }
      if (!tuple.isEmpty()) {
        res.addAll(tuple);
      }
      // 跳过第一个元素的重复项
      while (i + 1 < nums.length && nums[i] == nums[i + 1]) {
        i++;
      }
    }
    return res;
  }

  /**
   * 4 数之和，leetcode 18 题。跟 3Sum 一个套路
   * 
   * @param nums
   * @param target
   * @return
   */
  public List<List<Integer>> fourSum(int[] nums, int target) {
    Arrays.sort(nums);
    int n = nums.length;
    List<List<Integer>> res = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      int first = nums[i];
      List<List<Integer>> triple = threeSumTarget(nums, i + 1, target - first);
      for (List<Integer> tuple : triple) {
        tuple.add(0, first);
      }
      res.addAll(triple);

      while (i + 1 < nums.length && nums[i] == nums[i + 1]) {
        i++;
      }
    }
    return res;
  }

  /**
   * 调用 n nSumTarget 的前提是 nums 已经是排好序的。
   * 
   * @param nums
   * @param n
   * @param start
   * @param target
   * @return
   */
  public List<List<Integer>> nSumTarget(int[] nums, int n, int start, long target) {
    List<List<Integer>> res = new ArrayList<>();
    if (nums == null || nums.length < 1 || n < 2) {
      return res;
    }
    int len = nums.length;
    int sum, left, right;
    if (n == 2) { // twoSum那一套
      int lo = start, hi = len - 1;
      while (lo < hi) {
        left = nums[lo];
        right = nums[hi];
        sum = nums[lo] + nums[hi];
        if (sum < target) {
          // lo++;
          while (lo < hi && nums[lo] == left)
            lo++;
        } else if (sum > target) {
          // hi--
          while (lo < hi && nums[hi] == right)
            hi--;
        } else {// sum == target
          List<Integer> match = new ArrayList<>();
          match.add(left);
          match.add(right);
          res.add(match);
          while (lo < hi && nums[lo] == left)
            lo++;
          while (lo < hi && nums[hi] == right)
            hi--;
        }
      }
    } else { // 3Sum ....nSum
      for (int i = start; i < len - 1; i++) {
        int first = nums[i];
        List<List<Integer>> sub = nSumTarget(nums, n - 1, i + 1, target - first);
        for (List<Integer> list : sub) {
          list.add(first);
          res.add(list);
        }
        // 排重下
        while (i + 1 < len && nums[i] == nums[i + 1]) {
          i++;
        }
      }
    }
    return res;
  }

  /**
   * leetcode 42 接雨水，暴力解法
   * 
   * @param height
   * @return
   */
  public int trapVolient(int[] height) {
    int n = height.length - 1;
    // 为什么从 1 开始,因为外围的遍历只遍历 1..n-2 就行，0 和 n-1 左右 l_max 和 r_max 去遍历获取的
    int res = 0;
    for (int i = 1; i < n - 1; i++) {
      int l_max = 0, r_max = 0;
      // 获取 0...i 的 l_max
      for (int j = 0; j <= i; j++) {
        l_max = Math.max(l_max, height[j]);
      }
      for (int j = i; j < n - 1; j++) {
        r_max = Math.max(r_max, height[j]);
      }
      res += Math.min(l_max, r_max) - height[i];
    }
    return res;
  }

  int trapWithMemo(int[] height) {
    if (height == null || height.length < 1) {
      return 0;
    }
    int n = height.length;
    int[] l_max = new int[height.length];
    int[] r_max = new int[height.length];
    // 初始化
    l_max[0] = height[0];
    r_max[n - 1] = height[n - 1];
    for (int i = 1; i < n; i++) {
      l_max[i] = Math.max(l_max[i - 1], height[i]);
    }
    // 从右向左计算 r_max
    for (int j = n - 2; j >= 0; j--) {
      r_max[j] = Math.max(r_max[j + 1], height[j]);
    }

    int res = 0;
    for (int i = 0; i < n; i++) {
      res += Math.min(l_max[i], r_max[i]) - height[i];
    }
    return res;
  }

  /**
   * 时空复杂度最小的，采用边走边算的方式计算可乘雨水数
   * 
   * @param height
   * @return
   */
  public int trap(int[] height) {
    if (height == null || height.length < 1) {
      return 0;
    }
    int left = 0, right = height.length - 1;
    int l_max = 0, r_max = 0;
    int res = 0;
    while (left < right) {
      l_max = Math.max(l_max, height[left]);
      r_max = Math.max(r_max, height[right]);
      // 以上的 lmax 代表 [0,left] 之间的最高柱子，rmax 代表 [right, end] 之间的最高水柱
      if (l_max < r_max) { // 这列为什么 l_max 小就 left++，这就是我们双指针常用的方式，也符合求最大值的基本原理，
        // 因为我们求的是 min(l_max,r_max) ，如果不让 left++ ，则后面的 min(l_max,r_max) 都永远小于等于 l_max
        // 因此为了求 l_max 最大值，也就是求最大盛雨量，谁小谁先走是符合要求的
        res += l_max - height[left];
        left++;
        ;
      } else {
        res += r_max + -height[right];
        right--;
      }
    }
    return res;
  }

  /**
   * leetcode 11 题，盛最多水的容器
   * 
   * @param height
   * @return
   */
  public int maxArea(int[] height) {
    int left = 0, right = height.length - 1;
    int res = 0;
    while (left < right) {
      if (height[left] < height[right]) {
        res = Math.max(res, height[left] * (right - left));
        left++;
      } else {
        res = Math.max(res, height[right] * (right - left));
        right--;
      }
    }
    return res;
  }

  /**
   * leetcode 16 题，最接近的三数之和
   * Input: nums = [-1,2,1,-4], target = 1
   * Output: 2
   * Explanation: The sum that is closest to the target is 2. (-1 + 2 + 1 = 2)
   */
  public int threeSumClosest(int[] nums, int target) {
    Arrays.sort(nums);
    return nSumCloest(nums, 3, 0, target);
  }

  int nSumCloest(int[] nums, int n, int start, int target) {
    if (n < 2 || start < 0) {
      throw new IllegalArgumentException();
    }
    int deltaSum = 100_0000, minSum = deltaSum;
    int lo = start, len = nums.length, hi = len - 1;
    int left, right, sum;
    if (n == 2) {
      while (lo < hi) {
        left = nums[lo];
        right = nums[hi];
        sum = left + right;
        if (sum < target) {
          if (target - sum < deltaSum) {
            deltaSum = target - sum;
            minSum = sum;
          }
          while (lo < hi && nums[lo] == left)
            lo++;
        } else if (sum > target) {
          // 记录最小差
          if (sum - target < deltaSum) {
            deltaSum = sum - target;
            minSum = sum;
          }
          // 消除右边重复
          while (lo < hi && nums[hi] == right)
            hi--;
        } else { // 相等
          deltaSum = 0;
          minSum = sum;
          while (lo < hi && nums[lo] == left)
            lo++;
          while (lo < hi && nums[hi] == right)
            hi--;
        }
      }
    } else { // n 数最接近之和
      for (int i = 0; i < nums.length; i++) {
        int first = nums[i];
        int closestSum = nSumCloest(nums, n - 1, i + 1, target - first);
        // 还原 closestSum
        closestSum += first;
        if (Math.abs(target - closestSum) < deltaSum) {
          deltaSum = Math.abs(target - closestSum);
          minSum = closestSum;
        }
      }
    }
    return minSum;
  }

  public static void main(String[] args) {
    int[] arr = new int[] { 2, 7, 11, 15 };
    int target = 22;
    NSumAndRain instance = new NSumAndRain();
    // System.out.println(Arrays.toString(instance.twoSum(arr, target)));
    // System.out.println(Arrays.toString(instance.twoSum2(arr, target)));

    // arr = new int[] { -1, 0, 1, 2, -1, -4 };
    // System.out.println(instance.threeSumTarget(arr, 0, 0));

    // arr = new int[] { 1, 0, -1, 0, -2, 2 };
    // System.out.println(instance.fourSum(arr, 0));

    // 使用 nSum 测试 4Sum, 注意，使用之前要先排序，要不让算法会死循环或者计算错误
    // Arrays.sort(arr);
    // System.out.println(instance.nSumTarget(arr, 4, 0, 0));

    arr = new int[] { 1000000000, 1000000000, 1000000000, 1000000000 };
    // target = -294967296;
    // Arrays.sort(arr);
    // System.out.println(instance.nSumTarget(arr, 4, 0, target));
    // System.out.println(target - 2 * arr[0]);

    // arr = new int[] { 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1 };

    // System.out.println(instance.trap(arr));

    // arr = new int[] { 1, 8, 6, 2, 5, 4, 8, 3, 7 };
    // System.out.println(instance.maxArea(arr));

    // int mask = 0x7ffffff;
    // System.out.println(-1 >>> 16);
    // System.out.println((-1 >>> 16) & mask);
    // System.out.println(-1 ^ (-1 >>> 16));

    arr = new int[] { -1, 2, 1, -4 };
    System.out.println(instance.threeSumClosest(arr, 1));
    arr = new int[] { 0, 0, 0 };
    System.out.println(instance.threeSumClosest(arr, 1));
    arr = new int[] { 0, 1, 2 };
    System.out.println(instance.threeSumClosest(arr, 3));
    arr = new int[] { 833, 736, 953, -584, -448, 207, 128, -445, 126, 248, 871, 860, 333, -899, 463, 488, -50, -331,
        903, 575, 265, 162, -733, 648, 678, 549, 579, -172, -897, 562, -503, -508, 858, 259, -347, -162, -505, -694,
        300, -40, -147, 383, -221, -28, -699, 36, -229, 960, 317, -585, 879, 406, 2, 409, -393, -934, 67, 71, -312, 787,
        161, 514, 865, 60, 555, 843, -725, -966, -352, 862, 821, 803, -835, -635, 476, -704, -78, 393, 212, 767, -833,
        543, 923, -993, 274, -839, 389, 447, 741, 999, -87, 599, -349, -515, -553, -14, -421, -294, -204, -713, 497,
        168, 337, -345, -948, 145, 625, 901, 34, -306, -546, -536, 332, -467, -729, 229, -170, -915, 407, 450, 159,
        -385, 163, -420, 58, 869, 308, -494, 367, -33, 205, -823, -869, 478, -238, -375, 352, 113, -741, -970, -990,
        802, -173, -977, 464, -801, -408, -77, 694, -58, -796, -599, -918, 643, -651, -555, 864, -274, 534, 211, -910,
        815, -102, 24, -461, -146 };
        
  target = -7111;
  System.out.println(instance.threeSumClosest(arr, target));
  }
}
