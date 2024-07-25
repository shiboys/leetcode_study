package org.swj.leet_code.algorithm;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/03/03 19:34
 * 力扣第 4 题，求2个有序数组的中位数
 */

/**
 * 求两个已排序的数组的中位数
 *
 * <p>
 * 输入：nums1 = [1,3], nums2 = [2] 输出：2.00000 解释：合并数组 = [1,2,3]，中位数 2
 *
 * <p>
 * 输入：nums1 = [1,2], nums2 = [3,4] 输出：2.50000 解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) /
 * 2 = 2.5
 */
public class MedianOfTwoSortedArrays {

  /**
   * 我解这道题的解题思路来自于归并排序，借鉴了归并的思想，也就是：
   * 1、如果 nums1 的当前值小于 nums2，则 nums1 的指针++，否则 nums2 的指针++
   * 2、如果达到跳出跳进条件，也就是 有一边的数组已经遍历完毕，则继续判断剩下的 index1 和 index2
   * 3、剩下的 index1 和 index2 的判断条件是 index1+index2 > middle
   * ，一旦超过了总长度的一半则立马跳出，是为了保证上一次的 index1+index2 == middle
   * 如果 index1 未达到数组的末尾，则 index1++，否则 index2++ 总之找到一个 index1+index2 == middle 的时候
   * 4、根据数组长度之和是否是偶数，如果是奇数，则使用当前最后一次遍历的数组以及 index 求解
   * 5、如果是偶数，使用最后一次遍历的数组以及 index 跟 最后一次的上一次遍历的数组和 index 之和除以2
   * 这道题我解了 2 个小时左右，主要浪费在之前没有考虑上一次遍历的位置，只是粗暴的记录了 index1 的上一个位置和 index2
   * 之间谁大取谁，这个逻辑有问题的
   * 后来换成记录前一个被遍历的的索引和数组，本来我是打算用面向对象建立一个类来标识当前的正在遍历的 数组和索引，以及一个old 对象来记录上次遍历的数组和索引
   * 后来想到 力扣比较要求性能，我就没有用 oop，而是使用了 int 的数组指针和数组的对象引用，共 2 对4个对象，后来发现用了
   * 上一个遍历的数组和索引之后，
   * 代码量立马变得简单了，我刚开始是这么想的，后来推翻了我正确的想法，再后来发现没有通过一些用例，仔细研究了下，发现我当初的想法是对的。
   * ps：这道题的 Level 为 hard。经过我的实践证明确实不是几分真到半小时内能想清楚并写出来的。难度等级比较符合我的预期
   * 
   * @param nums1 数组1
   * @param nums2 数组2
   * @return
   */
  public double findMedianSortedArrays(int[] nums1, int[] nums2) {
    if (nums1.length < 1 && nums2.length < 1) {
      return 0;
    }
    int[] currArr = null;
    int currIdx = 0;
    int[] prevArr = null;
    int prevIdx = 0;

    int currIndex1 = 0;
    int currIndex2 = 0;

    int middle = (nums1.length + nums2.length) / 2;
    boolean even = ((nums1.length + nums2.length) % 2) == 0;

    while (currIndex1 < nums1.length && currIndex2 < nums2.length) {
      if ((currIndex1 + currIndex2) > middle) {
        break;
      }
      prevIdx = currIdx;
      prevArr = currArr;
      if (nums1[currIndex1] < nums2[currIndex2]) {
        currIdx = currIndex1;
        currArr = nums1;
        currIndex1++;
      } else {
        currIdx = currIndex2;
        currArr = nums2;
        currIndex2++;
      }
    }
    // middle 我这里取的是 length 的长度的 1/2
    // 偶数的时候取 middle 和 middle-1，因此 index1 之和可以为 middle，大于 middle 则跳出
    while ((currIndex1 + currIndex2) <= middle) {
      prevIdx = currIdx;
      prevArr = currArr;
      // 数组1 的遍历已经结束，则继续遍历数组2
      if (currIndex1 >= nums1.length) {
        currIdx = currIndex2;
        currArr = nums2;
        currIndex2++;
      } else {
        // 否则遍历数组1
        currIdx = currIndex1;
        currArr = nums1;
        currIndex1++;
      }
    }

    if (!even) {
      return currArr[currIdx];
    } else {
      int val1 = currArr[currIdx];
      int val2 = prevArr[prevIdx];
      return (val1 + val2) / 2.0;
    }

    // ps: 如果没有空间复杂度的要求，将两个排序数组合并成一个数组，
    // 然后求合并后新数组的中位数 岂不更简单
    // 10-23 日，我再看到这题，发现跟两个链表或者两个数组的合并很相似，很 easy 呀
    // 可能是我当初数据结构基础知识比较差把
  }

  public static void main(String[] args) {
    int[] nums1 = new int[] { 1, 3 };
    int[] nums2 = new int[] { 2 };
    MedianOfTwoSortedArrays mta = new MedianOfTwoSortedArrays();

    /*
     * System.out.println(mta.findMedianSortedArrays(nums1, nums2));
     * 
     * nums1= new int[] {1,2};
     * nums2= new int[] {3,4};
     * 
     * System.out.println(mta.findMedianSortedArrays(nums1,nums2)) ;
     * 
     * nums1 = new int[] {1, 5, 10, 30, 111};
     * nums2 = new int[] {3, 4};
     * 
     * System.out.println(mta.findMedianSortedArrays(nums1, nums2));
     * 
     * nums2 = new int[] {50};
     * 
     * System.out.println(mta.findMedianSortedArrays(nums1, nums2));
     */

    nums1 = new int[] {};
    nums2 = new int[] { 1 };
    System.out.println(mta.findMedianSortedArrays(nums1, nums2));

  }
}
