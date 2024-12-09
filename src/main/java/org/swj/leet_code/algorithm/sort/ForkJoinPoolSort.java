package org.swj.leet_code.algorithm.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/03/20 18:26
 *        使用 ForkJoinPool 实现归并排序
 */
@Slf4j
public class ForkJoinPoolSort {

  private static final MergeSort mergeSort = new MergeSort();
  static final Random random = new Random();

  public static void main(String[] args) throws Exception {
    // testCompletableFuture();
    ForkJoinPoolSort instance = new ForkJoinPoolSort();
    // int[] arr = new int[] { 32, 11, 46, 8, 25, 47, 25, 47, 49, 6, 19, 20, 47, 36,
    // 45, 39, 42, 6, 21, 37, 4, 26, 7, 3,
    // 23, 6, 12, 23, 16, 41, 9, 34, 34, 26, 25, 16, 43, 25, 38, 17, 37, 7, 7, 1,
    // 48, 43, 31, 35, 3, 25 };
    // instance.sortSimple(arr);
    // instance.testForkJoinSortSimple();
    int n = 100;
    String prefix = "1+2+...." + n;
    /**
     * 0010 
     * ^
     * 1010
     * =1000 还真是 8
     */
    System.out.println("2^10 = " + (2 ^ 10));
    System.out.println(prefix + " multi thread is " + instance.getSumAccumulator(n)
        + "\n" + prefix + " simple calc is " + instance.getSumAccumulatorSimple(n)
        + "\n" + prefix + " simple calcWithFJ is " + instance.getSumAccumulatorFJ(n)
    // getSumAccumulatorFJ
    );
  }

  void testForkJoinSortSimple() {
    int len = 50;
    int[] arr = new int[len];
    for (int i = 0; i < len; i++) {
      arr[i] = random.nextInt(len);
    }
    System.out.println("before multi thread sort with forkjionpool : " + Arrays.toString(arr));
    SortTask sortTask = new SortTask(arr, new QuickSort());
    ForkJoinPool pool = new ForkJoinPool();
    pool.execute(sortTask);
    int[] res = sortTask.join();
    System.out.println("after multi thread sort with forkjionpool : " + Arrays.toString(res));
  }

  /**
   * SortTask 继承自 RecursiveTask<int[]> 来自定义实现
   * 使用 FokrJoinPool 的多线程排序逻辑
   */
  class SortTask extends RecursiveTask<int[]> {
    static final int MIN_SORT_THRESHOLD_SIZE = 20;

    SortFunc sortFunc;
    int[] sourceArr;

    public SortTask(int[] targetArr, SortFunc sortFunc) {
      if (targetArr == null) {
        throw new IllegalArgumentException("targetArr array is null");
      }
      this.sourceArr = targetArr;
      this.sortFunc = sortFunc;
    }

    @Override
    protected int[] compute() {
      // 如果数组的长度太小，则不需要要使用多线程排序，使用单线程的快速排序性能就很好
      if (sourceArr.length <= MIN_SORT_THRESHOLD_SIZE) {
        sortFunc.sort(sourceArr);
        return sourceArr;
      }
      // 否则使用 ForkJoin 的多线程排序
      int minLen = sourceArr.length >>> 1;
      int[] leftArr = Arrays.copyOfRange(sourceArr, 0, minLen);
      int[] rightArr = Arrays.copyOfRange(sourceArr, minLen, sourceArr.length);

      SortTask leftTask = new SortTask(leftArr, sortFunc);
      leftTask.fork();
      SortTask rightTask = new SortTask(rightArr, sortFunc);
      rightTask.fork();

      // invokeAll(leftTask, rightTask);

      // 通过调用 leftTask.join() 和 rightTask.join() 获取排序后的数组，进行归并
      // 都是通过归并排序/快速排序已经有序的数组，然后最终归并到一起
      return mergeSort.merge(leftTask.join(), rightTask.join());
    }

  }

  void sortSimple(int[] arr) {
    SortFunc instance = new ForkJoinPoolSort.QuickSort();
    instance.sort(arr);
    System.out.println(Arrays.toString(arr));
  }

  interface SortFunc {
    /**
     * 在数据规模比较小的时候进行的内排序
     * 
     * @param arr
     */
    void sort(int[] arr);
  }

  static class MergeSort {
    /**
     * 归并排序的归并，在多线程的归并排序中，归并排序并不参与排序，只参与归并
     * 
     * @param leftArr
     * @param rightArr
     */
    public int[] merge(int[] leftArr, int[] rightArr) {
      int[] dest = new int[leftArr.length + rightArr.length];
      int i = 0, j = 0;
      for (int p = 0; p < dest.length; p++) {
        if (i == leftArr.length) {
          dest[p] = rightArr[j++];
        } else if (j == rightArr.length) {
          dest[p] = leftArr[i++];
        } else if (leftArr[i] < rightArr[j]) {
          dest[p] = leftArr[i++];
        } else {
          dest[p] = rightArr[j++];
        }
      }
      return dest;
    }
  }

  // 默认使用快速排序进行内排序
  class QuickSort implements SortFunc {

    @Override
    public void sort(int[] arr) {
      doSort(arr, 0, arr.length - 1);
    }

    void doSort(int[] arr, int start, int end) {
      if (start >= end) {
        return;
      }
      int pivot = start + random.nextInt(end - start + 1);
      int pv = arr[pivot];
      // 将队首元素跟 pivot 交换, 来达到每次 选取的轴元素都是队首的元素
      swap(arr, pivot, start);
      int i, j;
      for (i = start + 1, j = end; i <= j;) {
        while (i <= j && arr[i] <= pv) {
          i++;
        }
        while (i <= j && arr[j] >= pv) {
          j--;
        }
        if (i <= j) {
          swap(arr, i, j);
        }
      }
      // 跳出循环，i==j+1 ，此时就是 pivot 的正确位置 arr[i-1] <= pv 的
      // 这里要记住，将 start 位置的 Pivot 值换回正确的位置
      swap(arr, start, j);

      // 一遍快排之后，进行递归, 递归是从 [0..j-1] 和 [j+1...end] 的返回
      doSort(arr, start, j - 1);
      doSort(arr, j + 1, end);
    }
  }

  class BubbleSort implements SortFunc {

    @Override
    public void sort(int[] arr) {
      for (int i = 0, len = arr.length; i < len; i++) {
        // 冒泡到 arr[len-1] 的位置
        for (int j = 0; j < len - i - 1; j++) {
          if (arr[j] > arr[j + 1]) {
            swap(arr, j, j + 1);
          }
        }
      }
    }

  }

  void swap(int[] arr, int i, int j) {
    if (i == j) {
      return;
    }
    int tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  private static void testCompletableFuture() throws Exception {
    // 打印结果 1 2 3
    testCompletableFuture1();

    // 打印结果为 1
    testCompletableFuture2();

    testCompletableFuture3();
  }

  private static void testCompletableFuture1() throws Exception {
    CompletableFuture<String> base = new CompletableFuture<>();
    CompletableFuture<String> future = base
        .thenApply(s -> s + " 2")
        .thenApply(s -> s + " 3");
    base.complete("1");

    System.out.println(future.get());
    System.out.print("base.get() = ");
    System.out.println(base.get());
  }

  private static void testCompletableFuture2() throws Exception {
    CompletableFuture<String> base = new CompletableFuture<>();
    CompletableFuture<String> future = base.thenApply(s -> s + " 2").thenApply(s -> s + " 3");
    future.complete("1");
    System.out.println(future.get());
    // base.get 调用将被挂起，因为调用方没有调用 base.complete() 方法
  }

  private static void testCompletableFuture3() throws Exception {
    CompletableFuture<String> base = new CompletableFuture<>();
    CompletableFuture<String> future = base.thenApply(s -> {
      log.info(s);
      return s + " 2";
    });

    base.thenAccept(s -> log.info(s + "3-1")).thenAccept(Void -> log.info("3-2"));
    base.thenAccept(s -> log.info(s + "4-1")).thenAccept(Void -> log.info("4-2"));

    base.complete("1");
    log.info("base result : {}", base.get());
    log.info("future result:{} ", future.get());
    /**
     * 打印结果如下
     * 20:52:08,147 INFO org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort - 14-1
     * 20:52:08,149 INFO org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort - 4-2
     * 20:52:08,149 INFO org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort - 13-1
     * 20:52:08,149 INFO org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort - 3-2
     * 20:52:08,149 INFO org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort - 1
     * 20:52:08,151 INFO org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort - base
     * result : 1
     * 20:52:08,151 INFO org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort - future
     * result:1 2
     * 说明 CompletableFuture 确实是以栈的形式存储任务的，最后的任务，最先被执行，其内部的结构图如 md 文档所示
     */
  }

  public long getSumAccumulatorSimple(int n) {
    return (n + 1) * n / 2;
  }

  /**
   * 使用多线程实现 1到 100 求和的类
   * 这道题没有 leetcode ，是我看到一道面试题，就是让你用多线程的思维计算 1 到 100 的和
   * 就这么简单的需求，我在微医的时候也出过这道题，让别人手写，哈哈，今天我来手写下
   */
  public long getSumAccumulator(int n) {
    long sum = 0;
    if (n < 10) {
      for (int i = 1; i <= n; i++) {
        sum += i;
      }
      return sum;
    }
    int threads = 10, step = n / 10;
    // CountDownLatch latch = new CountDownLatch(11);
    ExecutorService threadPool = Executors.newFixedThreadPool(threads);
    List<LevelAccumulator> tasks = new ArrayList<>(threads);
    for (int i = 0; i < threads; i++) {
      if (i == threads - 1) {
        tasks.add(new LevelAccumulator(i * step + 1, step + (n % 10)));
      } else {
        tasks.add(new LevelAccumulator(i * step + 1, step));
      }

    }
    try {
      List<Future<Long>> futureList = threadPool.invokeAll(tasks);
      for (Future<Long> future : futureList) {
        sum += future.get();
      }
      threadPool.shutdownNow();
      threadPool.awaitTermination(1, TimeUnit.SECONDS);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return sum;
  }

  /**
   * 使用 ForkJoinPool 多线程计算 1...n 的累加和
   * 
   * @param n
   * @return
   */
  long getSumAccumulatorFJ(int n) {
    ForkJoinPool pool = new ForkJoinPool(10);
    ForkJoinAccumulatorTask calcTask = new ForkJoinAccumulatorTask(1, n);
    pool.execute(calcTask);
    try {
      return calcTask.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return -1;
  }

  class LevelAccumulator implements Callable<Long> {

    int start;
    int k;

    public LevelAccumulator(int start, int k) {
      if (k < 0) {
        throw new IllegalArgumentException("k is invalid, k must be greater than 0");
      }
      this.start = start;
      this.k = k;
    }

    @Override
    public Long call() throws Exception {
      long sum = 0;
      for (int i = start; i < start + k; i++) {
        sum += i;
      }
      return sum;
    }
  }

  class ForkJoinAccumulatorTask extends RecursiveTask<Long> {
    final int minLen = 10;
    int start;
    int k;

    public ForkJoinAccumulatorTask(int start, int k) {
      if (k < 1) {
        throw new IllegalArgumentException("k must be greater than 0!");
      }
      this.k = k;
      this.start = start;
    }

    @Override
    protected Long compute() {
      long sum = 0;
      if (k <= minLen) {
        for (int i = start; i < start + k; i++) {
          sum += i;
        }
        return sum;
      }
      // 任务拆分
      int mid = k >>> 1;
      ForkJoinAccumulatorTask leftTask = new ForkJoinAccumulatorTask(start, mid);
      leftTask.fork();
      ForkJoinAccumulatorTask rightTask = new ForkJoinAccumulatorTask(start + mid, k - mid);
      rightTask.fork();
      // invokeAll(leftTask,rightTask);
      long left = leftTask.join();
      long right = rightTask.join();
      return left + right;
    }

  }
}
