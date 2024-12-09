package org.swj.leet_code.tree;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/08/04 18:03
 */
public class MedianFinderLeetCode {
  /**
   * 数据流的中位数，跟 1670 前中后队列思想基本一致，实现在中位 O(1) 的时间内
   * 插入一个元素，和获取一个元素
   * 不过这里我们实现的是通过两个由下级队列或者说叫大顶堆+小顶堆的方式，
   * 插入数据是 O(logN) 的时间复杂度，
   * 同时为了维护中间节点的特点，两个堆中的数据个数之差不能不能超过 1
   */

  // 因为我们自己写的大小堆不支持动态扩容，因此我们这里再将堆初始化的时候，会进行初始化很大
  // 最多 5 * 10^4 次调用 addNum 和 findMedian
  static final int DEFAULT_SIZE = 50_000;
  // 小顶堆中存储的是比较大的数据
  final SmallTopHeap<Integer> large;
  // 大顶堆中存储的是比较小的数据，这样才能 O(1) 的时间存取中间的数据
  final LargeTopHeap<Integer> small;

  public MedianFinderLeetCode() {
    large = new SmallTopHeap<>(DEFAULT_SIZE);
    small = new LargeTopHeap<>(DEFAULT_SIZE);
  }

  public void addNum(int num) {
    if (small.size() >= large.size()) {
      // 先让数据加入大顶堆，最后再把数据放入小顶堆中
      small.offer(num);
      large.offer(small.poll());
    } else {
      large.offer(num);
      small.offer(large.poll());
    }
  }

  public double findMedian() {
    if (large.size() > small.size()) {
      return large.peek();
    } else if (large.size() < small.size()) {
      return small.peek();
    }
    if (large.size() == 0) {
      return 0;
    }

    return (large.peek() + small.peek()) / 2.0;
  }

  void swap(Object[] arr, int i, int j) {
    Object tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

  boolean less(Comparable[] arr, int i, int j) {
    return arr[i].compareTo(arr[j]) < 0;
  }

  boolean lessThan(Comparable a, Comparable c) {
    return a.compareTo(c) < 0;
  }

  class SmallTopHeap<K extends Comparable<K>> {
    K[] arr;
    int size;

    public SmallTopHeap(int n) {
      arr = (K[]) new Comparable[n + 1];
    }

    public int size() {
      return size;
    }

    // 有了上浮和下沉函数，我们在写 poll 和 offer 就简单多了
    public void offer(K e) {
      if (e == null) {
        return;
      }
      // 因为我们的元素是从 1 开始计数的
      arr[++size] = e;
      swimUp(size);
    }

    // 取堆顶元素
    public K poll() {
      if (size == 0) {
        return null;
      }
      K e = arr[1];
      // 替换为最后一个元素
      arr[1] = arr[size];
      // 移除最后一个元素
      arr[size] = null;
      // 数量 --
      size--;
      // 将替换后的最后一个元素进行按堆的规则下沉
      sink(1);

      return e;
    }

    public K peek() {
      return arr[1];
    }

    /**
     * 将 k 索引位的元素进行 sink 操作。
     */
    void sink(int k) {
      int child, right;
      // 当前节点的子节点也在堆内，如果不在堆内，就无处下沉 sink
      K target = arr[k];
      while ((child = left(k)) <= size) {
        // 当前是小顶堆，如果 k 比左右任何一个子节点大，则将 k 下沉，跟子节点交换
        // child = left(k);
        right = child + 1;
        // 寻找最小的子节点
        if (right <= size && less(arr, right, child)) {
          child = right;
        }
        // 如果父节点小于 最小的子节点，则当前位置就是合适的 k 位，跳出循环
        if (lessThan(target, arr[child])) {
          // 找到正确的位置
          break;
        }
        // 否则交换 k 位，并继续递归
        // 重新改写，不再使用 swap
        //swap(arr, k, child);
        arr[k] = arr[child];
        k = child;
      }
      //找到正确的位置
      arr[k] = target;
    }

    void swimUp(int k) {
      int p;
      // 为了简化二叉堆的使用， 我们索引是从 1 开始
      K target = arr[k];
      while (k > 1) {
        p = parent(k);
        // 小顶堆，看谁小，谁上浮
        if (lessThan(target, arr[p])) {
          // parent 下降
          arr[k] = arr[p];
          k = p;
        } else { // 找到正确的位置
          break;
        }
      }
      // k == 1 或者找到正确的位置
      arr[k] = target;
    }

    int parent(int k) {
      return k >> 1;
    }

    // 左子节点
    int left(int k) {
      return k << 1;
    }
  }


  /**
   * 大顶堆
   */
  class LargeTopHeap<K extends Comparable<K>> {
    K[] arr;
    private int size;

    public LargeTopHeap(int n) {
      arr = (K[]) new Comparable[n + 1];
    }

    public int size() {
      return size;
    }

    public K peek() {
      return arr[1];
    }

    // 有了 swimUp 和 sink ，我们完成 poll 和 offer 就比较简单
    public void offer(K e) {
      if (e == null) {
        return;
      }

      arr[++size] = e;
      swimUp(size);
    }

    public K poll() {
      K e = arr[1];
      arr[1] = arr[size];
      arr[size] = null;
      size--;
      sink(1);
      return e;
    }

    /**
     * 大顶堆的 sink ，父节点比任何一个子节点小，则需要 sink
     */
    void sink(int k) {
      int child, right;
      K target = arr[k];
      while ((child = left(k)) <= size) {
        right = child + 1;
        if (right <= size && less(arr, child, right)) {
          child = right;
        }
        // 如果最大的子节点都比父节点小，则跳出循环，k 的位置是当前位置
        if (lessThan(arr[child], target)) {
          break;
        }
        // 否则交换位置
        // 改用更优的直接复制的算法， 而不用性能更低的交换方式
        //swap(arr, k, child);
        arr[k] = arr[child];
        k = child;
      }
      // 找到目标位置，替换
      arr[k] = target;
    }

    /**
     * 将 k 位置元素上浮
     */
    void swimUp(int k) {
      int p;
      K target = arr[k];
      while (k > 1) {
        // 如果当前位置 k 比父节点位置的元素大，则要上浮
        p = parent(k);
        if (lessThan(arr[p], target)) {
          //swap(arr, p, k);
          // k 上浮，就是 p 下沉，k 已经有 target 引用了，所以 p 不能被覆盖
          arr[k] = arr[p];
          k = p;
        } else { // 找到正确的位置
          break;
        }
      }
      arr[k] = target;
    }

    int parent(int k) {
      return k >> 1;
    }

    // 左子节点
    int left(int k) {
      return k << 1;
    }
  }

  public static void main(String[] args) {
    MedianFinderLeetCode instance = new MedianFinderLeetCode();
    //int[] arr = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
    int[] arr = new int[] {6, 10, 2, 6, 5, 0, 6, 3, 1};
    for (int val : arr) {
      instance.addNum(val);
      System.out.println(instance.findMedian());
    }


    System.out.println(1e9 == Math.pow(10,9));
  }
}
