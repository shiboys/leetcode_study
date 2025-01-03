package org.swj.leet_code.algorithm.sort;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/08/13 12:43
 *        排序测试
 */
public class SortTest {

    static final Random random = new Random();

    public static void main(String[] args) {
        SortTest instance = new SortTest();
        int[] arr = instance.randomGenerateArray(10);
        TraditionalSort ts = new TraditionalSort();

        // instance.testSort(arr, ts::insertSort);

        // arr = new int[] {3, 2, 4, 1};
        // instance.testSort(arr, ts::bubbleSort);

        // instance.testSort(arr, ts::insertSort);
        System.out.println(Arrays.toString(arr));

        QuickSort quickSort = new QuickSort();
        for (int i = 1; i <= 10; i++) {
            int[] copiedArr = Arrays.copyOf(arr, arr.length);
            ts.insertSort(copiedArr);
            int res = quickSort.bfprt(arr, i - 1);
            System.out.println("插入排序法：第" + i + "小的元素是" + copiedArr[i - 1]);
            System.out.println("buprt topK 方法：第" + i + "小的元素是" + res);
        }
    }

    void testSort(int[] arr, Consumer<int[]> consumer) {
        System.out.println("before sort:" + Arrays.toString(arr));
        consumer.accept(arr);
        System.out.println("after sort:" + Arrays.toString(arr));
    }

    void testSort(int[] arr, Function<int[], int[]> consumer) {
        System.out.println("before sort:" + Arrays.toString(arr));
        int[] res = consumer.apply(arr);
        System.out.println("after sort:" + Arrays.toString(arr));
        if (res != null)
            System.out.println("res is " + Arrays.toString(res));
    }

    /**
     * 随机产生 n 个数
     * 
     * @param n
     * @return
     */
    int[] randomGenerateArray(int n) {
        if (n > Integer.MAX_VALUE / 10) {
            throw new IllegalArgumentException(String.format("n is invalid range number.[n=%d]", n));
        }
        int[] res = new int[n];
        int max = 10_000;
        for (int i = 0; i < n; i++) {
            res[i] = random.nextInt(max);
        }
        return res;
    }
}
