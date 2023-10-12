package org.swj.leet_code.array.binarysearch;

import java.util.Random;

public class BinarySearchSkill {

    /**
     * 最基本的二分搜索
     * 
     * @param nums
     * @param target
     * @return
     */
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

    int leftBound(int[] nums, int target) {
        int left = 0;
        int right = nums.length;// 注意

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                mid = right;//
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                mid = right; // 注意
            }
        }
        return left;// 注意
    }

    int leftBound2(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        // 因为右侧搜索区间是闭的，因此改动逻辑如下：
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] == target) { // 收缩右边界
                right = mid - 1;
            }
        }
        // 越界判断
        if (left < 0 || left >= nums.length - 1) {
            return -1;
        }
        // 目标判断
        return nums[left] == target ? left : -1;
    }

    int rightBound(int[] nums, int target) {
        int left = 0, right = nums.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > target) {
                right = mid; // right 是开区间
            } else if (nums[mid] == target) {
                left = mid + 1; // 这里注意， mid 已经搜索过了，下次需要排除，left 是开区间。
            } else if (nums[mid] < target) {
                left = mid + 1;
            }
        }
        int pos = right - 1;
        if (pos < 0 || pos > nums.length - 1) {
            return -1;
        }
        return nums[pos] == target ? pos : -1; // 或者是 left -1 因为 left == right 是退出条件
    }

    int rightBound2(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) { // while 结束的条件为 right = left -1
            int mid = left + (right - left) / 2;
            if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] == target) {
                left = mid + 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            }
        }

        // int pos = left - 1; 根据 right 的结束条件，可得出 pos == left -1 == right
        int pos = right;
        if (pos < 0 || pos > nums.length - 1) {
            return -1;
        }
        return nums[pos] == target ? pos : -1;
    }

    /**
     * 带权重的随机选择算法
     */

    int[] getPreSumArr(int[] weights) {
        int n = weights.length;
        int[] preSum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            preSum[i] = preSum[i - 1] + weights[i - 1];
        }
        return preSum;
    }

    int[] preSum;
    Random random = new Random();

    public BinarySearchSkill(int[] w) {
        this.preSum = getPreSumArr(w);
    }

    public BinarySearchSkill() {

    }

    public int pickIndex() {
        int n = preSum.length;
        // preSum[0] 是占位符, 所以这里要 +1，越过占位符
        int target = random.nextInt(preSum[n - 1]) + 1;
        int left = leftBoundForWeight(target, preSum);
        return left - 1;
    }

    int leftBoundForWeight(int target, int[] preSum) {
        int left = 0, right = preSum.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (preSum[mid] > target) {
                right = mid - 1;
            } else if (preSum[mid] < target) {
                left = mid + 1;
            } else { // 相等
                right = mid - 1;
            }
        }
        // preSum 数组比 weights 数组的索引偏移量多 1
        return left;
    }

    public static void main(String[] args) {
        BinarySearchSkill instance = new BinarySearchSkill();
        // int target = 5;
        // int[] preSum = new int[] { 0, 2, 4, 6, 7 };
        // int left = instance.leftBoundForWeight(target, preSum);
        // System.out.println(left);

        // left = instance.leftBoundForWeight(1, preSum);
        // System.out.println(left);

        int[] piles = new int[] { 3, 6, 7, 11 };
        int h = 8;
        System.out.println("珂珂吃香蕉的最小速度1是：" + instance.koko_eat_bananas(piles, h));

        // 30,11,23,4,20
        piles = new int[] { 30, 11, 23, 4, 20 };
        h = 5;
        System.out.println("珂珂吃香蕉的最小速度2是：" + instance.koko_eat_bananas(piles, h));

        h = 6;
        System.out.println("珂珂吃香蕉的最小速度3是：" + instance.koko_eat_bananas(piles, h));

        int[] weights = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        // 15 的速度运输，需要 5 天。
        System.out.println(instance.shipWithinDays(weights, 5));

      
        weights = new int[] {1,2,3,1,1};
        System.out.println(instance.shipWithinDays(weights, 4));

        weights = new int[] { 3,2,2,4,1,4};
        System.out.println(instance.shipWithinDays(weights, 3));

    }

    // 定义：速度为 x 时，需要 f(x) 小时吃完所有香蕉
    // f(x) 随着 x 的增加单调递减
    long f_keke_eat_all_bananas_time(int[] bananas, int eat_speed_x) {
        long hours = 0;
        for (int banana_pile : bananas) {
            hours += banana_pile / eat_speed_x;
            if (banana_pile % eat_speed_x != 0) {
                hours += 1;
            }
        }
        // 返回 long 是因为 题目给出的数据范围和 f 函数的逻辑
        // piles 数组中元素的最大值是 10^9，最多有 10 ^4 个元素；那么当 x 取 1 时，hours 变量就会被加到 10^13 这个数量级，
        // 超过了 int 类型的最大值 （大概 2*10^9 21 亿嘛），所以用 long 避免可能出现的类型溢出。
        return hours;
    }

    /**
     * 求在 h 时间内，吃完 piles 的香蕉，最低的速度每小时吃多少。
     * 
     * @param piles
     * @param h
     * @return 每小时最少吃多少香蕉
     */
    int koko_eat_bananas(int[] piles, int h) {
        int left = 1, right = (int) Math.pow(10, 9) + 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            long midEatHour = f_keke_eat_all_bananas_time(piles, mid);
            // 函数是递减的
            if (midEatHour > h) {
                left = mid + 1;
            } else if (midEatHour < h) {
                right = mid-1;
            } else if (midEatHour == h) { // 收缩右边界
                right = mid-1;
            }
        }
        return left;
    }

    /**
     * 货物装船你的速度
     * 
     * @param weights 货物总重量
     * @param x       运送速度，单位为 日
     * @return
     */
    int f_weight(int[] weights, int x) {
        int days = 0;
        for (int i = 0; i < weights.length;) {
            if(x  < weights[i]) { // 根本运不走这个货物，说明 x 的容量有问题
                return Integer.MAX_VALUE; 
            }
            int cap = x;
            while (cap > 0 && i < weights.length) {
                if (cap < weights[i]) {
                    // 本次装不下，第二天再运
                    break;
                } else {
                    cap -= weights[i];// weight[i] 装船
                    i++;// 下一个货物
                }
            }
            days++;
        }
        return days;
    }

    /**
     * 求船的最低运载量
     * @param weights
     * @param days
     * @return
     */
    public int shipWithinDays(int[] weights, int days) {
        int left = 1;
        // 根据题目要求，1 <= weights[i] <= 500，1 <= days <= weights.length <= 5 * 104
        int right = 25 * (int)Math.pow(10, 6);
        // 仍然是降序排列, 船的载重越大，则运输的天数越少
        while(left <= right) {
            int mid = left + (right-left)/2;
            int midDays = f_weight(weights, mid);
            if(midDays > days) {
                left = mid + 1; // 增大运载量
            } else if( midDays < days) {
                right = mid-1 ; // 减少运载量 
            } else if(midDays == days) { // 收缩右边界
                right = mid-1;
            }
        }
        return left;
    }

}
