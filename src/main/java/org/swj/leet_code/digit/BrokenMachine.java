package org.swj.leet_code.digit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 找出机器故障的机器编号
 * 来自 leetcode 2243 机器故障？这个存疑
 * 编程之美第 1.5 题
 * 题目描述1：在某个时间，如果得到一个数据文件 ID 的列表，是否能够快速地找出这个表中仅出现一次的 ID。
 * 前提条件：每份数据保存两个备份，假设ID 是小于 10 亿的整数
 * 问题2：如果如果是 2 台机器死机那？
 */
public class BrokenMachine {
    /**
     * 我看这道题的时候，第一反应是用 2 个 bitmap, 然后比较两个 bitmap 的差异，找出差异的 ID。
     * 虽然时间复杂度是O(n), 空间复杂度就会省很多。
     * 作者给了第二种实现方式，用 HashMap 来维护，出现两次的数据就删除这个 key
     * 但是这个算法的最大空间复杂度为 O(N), 最小为 O(1), 因此作者给出了另外一个我其实没有想到的方式：位运算
     * 之前我使用过位运算，也知道 ^ 异或 可以消除相同的数字，但是没想到用在这个地方
     * 
     */

    // static final int MAX = 1_000_000_000;

    static final int MAX = 100;

    static final Random random = new Random();

    static final int len = 10;

    int[] getFilledArray() {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = random.nextInt(MAX) + 1;
        }
        return arr;
    }

    int findBrokenMachine(int[] arr) {
        int result = arr[0];
        for (int i = 1; i < arr.length; i++) {
            result ^= arr[i];
        }
        return result;
    }

    /**
     * 使用位运算的方式，找出 2 台机器故障的机器编号。
     * 这个比较麻烦，首先需要使用位运算计算出 2 台机器的故障编号的异或值 A
     * 然后查询 A 的某个位置的比特位是 1，将该位置记下来，记为 bitIndex
     * 然后将原数组根据 bitIndex 是否为 1 进行分组，得到 2 个新数组 arr1 和 arr2
     * 然后再分别对 arr1 和 arr2 跟 A 进行异或，得到 2 个新值 A1 和 A2，就是那两个坏掉的机器
     * 
     * @param arr
     * @return
     */
    int[] fine2BrokenMachines(int[] arr) {
        // 先求异或值
        int xorResult = findBrokenMachine(arr);
        // 分割数组
        List<Integer> list1 = new ArrayList<>(arr.length / 2);
        List<Integer> list2 = new ArrayList<>(arr.length / 2);
        int firstBit1Index = 0;
        int firstBit1Result = 0;
        while (firstBit1Index < 32) {
            if (((firstBit1Result = 1 << firstBit1Index) & xorResult) != 0) {
                break;
            }
            firstBit1Index++;
        }
        for (int i = 0; i < arr.length; i++) {
            if ((firstBit1Result & arr[i]) != 0) {
                list1.add(arr[i]);
            } else {
                list2.add(arr[i]);
            }
        }
        // return new List[] { list1, list2 };

        // 分别对 list1 和 list2 进行异或
        int xorResult1 = findBrokenMachine(list1.stream().mapToInt(x -> x).toArray());
        int xorResult2 = findBrokenMachine(list2.stream().mapToInt(x -> x).toArray());
        return new int[] { xorResult1, xorResult2 };
    }

    int[] generateSingleBrokenMachineArr(int[] arr, int rndIndx) {
        int[] sourceArr = new int[arr.length * 2 - 1];

        System.arraycopy(arr, 0, sourceArr, 0, arr.length);
        System.arraycopy(arr, 0, sourceArr, arr.length, rndIndx);
        System.arraycopy(arr, rndIndx + 1, sourceArr, arr.length + rndIndx, arr.length - rndIndx - 1);
        return sourceArr;
    }

    public static void main(String[] args) {
        BrokenMachine instance = new BrokenMachine();
        int[] arr = instance.getFilledArray();
        int rndIndx = random.nextInt(arr.length);
        // int[] sourceArr = instance.generateSingleBrokenMachineArr(arr, rndIndx);

        // System.out.println(
        // "the single number is:" + arr[rndIndx] + ", rndIdx is " + rndIndx + ", and
        // the broken machine id is:"
        // + instance.findBrokenMachine(sourceArr));
        int rndIndx2 = random.nextInt(arr.length);
        while (rndIndx2 == rndIndx) {
            rndIndx2 = random.nextInt(arr.length);
        }
        int[] sourceArr2 = new int[arr.length * 2 - 2];
        System.arraycopy(arr, 0, sourceArr2, 0, arr.length);
        int startLen = arr.length;
        for (int i = 0; i < arr.length; i++) {
            if (i == rndIndx || i == rndIndx2) {
                continue;
            }
            sourceArr2[startLen++] = arr[i];
        }

        System.out.println(
                "2 original broken machine ids are: " + Arrays.toString(new int[] { arr[rndIndx], arr[rndIndx2] }));
        System.out.println(
                "2 calculated broken machine ids are: " + Arrays.toString(instance.fine2BrokenMachines(sourceArr2)));

        instance.quickSortOnceAgain(arr);
        System.out.println(Arrays.toString(arr));
    }

    void quickSortOnceAgain(int[] arr) {
        quickSortRecur(arr, 0, arr.length - 1);
    }

    void quickSortRecur(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int pos = partition(arr, start, end);
        quickSortRecur(arr, start, pos - 1);
        quickSortRecur(arr, pos + 1, end);
    }

    int partition(int[] arr, int start, int end) {

        int pivotPos = start + random.nextInt(end - start + 1);
        int pivot = arr[pivotPos];
        swap(arr, pivotPos, start);
        int l = start + 1, r = end;
        // 再次编写插入排序，这里面的条件 l<=r 不能少, 否则会导致死循环。里层和外层的 条件都不能少
        while (l <= r) {
            while (l <= r && arr[l] <= pivot) {
                l++;
            }
            while (l <= r && arr[r] >= pivot) {
                r--;
            }
            if (l < r) {
                swap(arr, l, r);
                // 交换之后，需要继续前进 l 和 r，可写可不写
                // l++;
                // r--;
            }
        }
        // 此时 r+1 = l ,r 是正确的 pivot 位置
        // 需要将 start 位置的 pivot 交换到 r 位置
        swap(arr, start, r);
        return r;
    }

    void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
