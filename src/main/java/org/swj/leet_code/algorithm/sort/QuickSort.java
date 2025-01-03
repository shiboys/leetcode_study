package org.swj.leet_code.algorithm.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/14 11:58
 */
public class QuickSort {
  /**
   * 快速排序算法，是 平均时间复杂度为 O(nlogn) ，最坏O(n^2) 的一种算法，之所以叫快速排序，是因为绝大多数时间都比 归并排序块
   * 更不用说现在还有双轴快速排序算法
   * 因此这里对快速排序重新进行一次梳理和重写，主要分为简单快速排序 和复杂快速排序；其中简单快速排序就是我们常见的快排算法
   * 但是即使常见的，我们快排也分为两端扫描法和一端扫描方式
   * 复杂的快排主要是三项切分快速排序和双轴快速排序，双轴快排也是 jdk Arrays.sort 使用的
   * java.util.DualPivotQuicksort 和排序思想
   * 只不过 jdk 还有其他的一些额外的优化，在这里我们只研究快速排序
   * 最后我们还要编写一个快速选择算法选择 top k 的元素，虽然这个算法有小顶堆的解法，但是快速选择的效率还是要高于小顶堆
   */
  static final Random random = new Random();

  public void quickSortSimple(int[] array) {
    quickSortSimpleRecur(array, 0, array.length - 1);
  }

  private void quickSortSimpleRecur(int[] array, int start, int end) {
    if (start >= end) {
      return;
    }
    // 先分区
    // int partitionIdx = partitionSimple2(array, start, end);
    int partitionIdx = partitionWithSingleEndPoint2(array, start, end);
    quickSortSimpleRecur(array, 0, partitionIdx - 1);
    quickSortSimpleRecur(array, partitionIdx + 1, end);
  }

  /**
   * 交换 pivot 到尾部
   *
   * @param array
   * @param start
   * @param end
   * @return
   */
  private int partitionSimple(int[] array, int start, int end) {
    // 这里我们选择随机数的方式，来选择 pivot 的值，而不是用 shuffle 的方式，这种方式更加简单，虽然可能不高效
    int rndIdx = start + random.nextInt((end - start + 1));
    int pivot = array[rndIdx];
    // 将 rnd 跟 end 交换，将 pivot 交换到数组的尾部
    swap(array, rndIdx, end);
    int i = start, j = end - 1;
    while (i <= j) {
      // 左右指针的方式找到左边第一个 >=pivot 和 右边第一个 < pivot 的指针，交换
      while (i <= j && array[i] <= pivot) {
        i++;
      }
      while (i <= j && array[j] >= pivot) {
        j--;
      }
      if (i < j) {
        swap(array, i, j);
      }
    }
    // 出循环之后， i>j , array[i] > pivot, 将 pivot 交换到 i 位置
    swap(array, i, end);
    return i;
  }

  /**
   * 交换 pivot 到头部
   *
   * @param array
   * @param start
   * @param end
   * @return
   */
  private int partitionSimple2(int[] array, int start, int end) {
    int rndIdx = start + random.nextInt((end - start + 1));
    int pivot = array[rndIdx];
    // 将 rnd 跟 start 交换，将 pivot 交换到数组的头部
    swap(array, rndIdx, start);
    // 这里的起始位置也很重要
    int i = start + 1, j = end;
    while (i <= j) {
      while (i <= j && array[i] <= pivot) {
        i++;
      }
      // 左右指针的方式找到左边第一个 >=pivot 和 右边第一个 < pivot 的指针，交换
      while (i <= j && array[j] >= pivot) {
        j--;
      }
      if (i < j) {
        swap(array, i, j);
      }
    }
    // 出循环之后， i>j , array[i] > pivot, array[j] 小于 pivot, 因此将 j 跟 start 换位置，将比 pivot
    // 小的元素换到 pivot 的左边，pivot 换到正确的位置
    // 选 i 还是 j 进行教化， 关键是看两点
    // 1 、pivot 是被交换到头部还是尾部， 如果是头部，数组选取范围是 [start+1,end], 则这里需要跟 j 交换，因为 arr[j] <
    // pivot, 然后返回 j
    // 2、 pivot 被交换到尾部，数组选取范围是 [start,end-1], 则这里需要跟 i 交换，因为 arr[i] > pivot, 刚好可以将 i
    // 元素交换到队尾，最后返回 i
    swap(array, j, start);
    return j;
  }

  /**
   * 单端快速选择。
   * 这个方法是我在看 双轴快速排序和剑指参考得到
   *
   * @param array
   * @param start
   * @param end
   * @return
   */
  int partitionWithSingleEndPoint(int[] array, int start, int end) {
    // 总体思路是从左到右遍历，遇到比 pivot 小的元素，放 ++left 这里
    int left = start - 1, right = start;
    int rndIdx = start + random.nextInt(end - start + 1);
    int pivot = array[rndIdx];
    // 交换到最后
    swap(array, rndIdx, end);
    while (right < end) {
      if (array[right] < pivot) {
        // 找到 < pivot 的值,将其放置到 ++left 的位置
        // 将 ++left 和 right 交换，表示第几个小于 pivot 的元素，然后 right++ ，right 指针继续向后走一步
        swap(array, ++left, right);
      }
      right++;
    }
    // 遍历完一遍，此时的 ++left 就是 pivot 的位置
    swap(array, ++left, end);
    return left;
  }

  /**
   * 单方向快速分区,非交换的方式
   *
   * @param array
   * @param start
   * @param end
   * @return
   */
  int partitionWithSingleEndPoint2(int[] array, int start, int end) {
    int left = start, right = left + 1;
    int pivot = array[start];
    while (right <= end) {
      if (array[right] < pivot) {
        left++;
        swap(array, left, right);
      }
      right++;
    }
    // 交换 pivot,此时因为 left < pivot, left 被交换换到 start 的位置， 是符合要求的
    swap(array, start, left);
    return left;
  }

  /**
   * 3 项快速排序法的基本思想，用 i,j,k 三个指针将数组切分成 4 部分，a[start,i-1] 表示小于 pivot1 的部分，a[i,k-1]
   * 表示等于 pivot 的部分
   * a[j+1,end] 表示大于 pivot 的部分，而a[k,j] 表示未判定的元素(即不知道比 pivot 大还是比它小)。我们要注意 a[i]
   * 始终位于等于 pivot 的部分
   * 的第一个元素，a[i] 的左边是小于等于 pivot 的部分的第一个元素， a[i] 的左边是小于 pivot 的部分
   * 我们选取最左边的元素作为 pivot, 初始化时，i=start, k = start+1, j = end
   * 详情见 quicksort.md 文档
   *
   * @param array
   */
  public void quickSort3Way(int[] array) {
    quickSort3WayRecur(array, 0, array.length - 1);
  }

  void quickSort3WayRecur(int[] array, int start, int end) {
    if (start >= end) {
      return;
    }
    Partition3Way p3way = partition3Way(array, start, end);
    // A[left, right] 位置的等于 pivot ，不需要参与排序
    quickSort3WayRecur(array, start, p3way.left - 1);
    quickSort3WayRecur(array, p3way.right + 1, end);
  }

  private Partition3Way partition3Way(int[] array, int start, int end) {

    int i = start, k = i + 1, j = end;
    int pivot = array[start];

    OUT_LOOP: while (k <= j) {
      if (array[k] < pivot) {
        swap(array, i, k); // 符合单方向遍历的规则，交换i 和 k，将第 k 小保存下来
        i++;
        k++;
      } else if (array[k] == pivot) {
        k++;
      } else { // array[k] > pivot
        while (array[j] > pivot) { // 注意次数是 while 循环，找到第一个 <= pivot 的 j。此处找到正确的 j 是为了满足下面的 if .. else
          // 这样整体 while 循环就会基本每次都进行交换
          j--;
          if (j < k) {
            break OUT_LOOP;
          }
        }

        if (array[j] == pivot) {
          // 将 k 位置与 j 位置交换，使得 == pivot 的值 切换到 k 位置, j 位置的被更换为 > pivot 的值，因此 j 不更新，k++
          swap(array, k, j);
          k++;
        } else { // array[j] < pivot
          // 交换 i，j；将 < pivot 的 array[j] 交换到 i 位置，表示小于 pivot 的值，
          swap(array, j, i);
          // array[k] 现在是 大于 Pivot，因此需要放到 array[j] 的位置
          swap(array, j, k);
          i++;
          k++;
          j--;
        }
      }
    }
    return new Partition3Way(i, j);
  }

  /**
   * 双轴快速排序，跟 3 项目快速排序非常类似，具体说明参见说明文档
   *
   * @param array
   */
  public void dualPivotQuickSort(int[] array) {
    dualPivotQuickSortRecur(array, 0, array.length - 1);
  }

  void dualPivotQuickSortRecur(int[] array, int start, int end) {
    if (start >= end) {
      return;
    }
    Partition3Way partition3Way = partitionDualPivot(array, start, end);
    dualPivotQuickSortRecur(array, start, partition3Way.left - 1);
    dualPivotQuickSortRecur(array, partition3Way.left + 1, partition3Way.right - 1);
    dualPivotQuickSortRecur(array, partition3Way.right + 1, end);
  }

  /**
   * 双走方法非常复杂，但是写完之后，非常牛逼
   * 
   * @param array
   * @param start
   * @param end
   * @return
   */
  Partition3Way partitionDualPivot(int[] array, int start, int end) {
    // 这里的起始点一定是不包括 pivot 的，否则会导致死循环
    int i = start + 1, k = start + 1, j = end - 1;

    // 保证 pivot1 <= pivot2
    if (array[start] > array[end]) {
      swap(array, start, end);
    }

    int pivot1 = array[start], pivot2 = array[end];

    OUT_LOOP: while (k <= j) {
      if (array[k] < pivot1) {
        swap(array, i, k);
        i++; // 这都符合单轴的 单向排序
        k++;
      } else if (array[k] >= pivot1 && array[k] <= pivot2) {
        k++;
      } else {
        while (array[j] > pivot2) {
          j--;
          if (j <= k) {
            break OUT_LOOP;
          }
        }

        if (array[j] >= pivot1 && array[j] <= pivot2) {
          swap(array, k, j);
          k++;
          j--;
        } else { // array[j] < pivot1
          // array[k] > pivot2
          swap(array, j, k);
          j--;
          // 此时 k 不动，因为需要用 array[k]< pivot1 来推进 i++ 和 k++
        }
      }
    }

    // 一遍循环完毕，找到 pivot1 和 pivot2 的正确位置
    // i 和 j 需要找到正确的位置
    i--;
    j++;
    swap(array, start, i);
    swap(array, end, j);
    return new Partition3Way(i, j);
  }

  static class Partition3Way {
    public int left;
    public int right;

    public Partition3Way(int left, int right) {
      this.left = left;
      this.right = right;
    }
  }

  /**
   * leetcode 215 & lcr 076 计算第 k 大的元素
   *
   * @param nums
   * @param k
   * @return
   */
  public int findKthLargest(int[] nums, int k) {
    if (nums == null || nums.length < 1) {
      return -1;
    }
    if (nums.length < k) {
      return -1;
    }
    int target = nums.length - k;
    int p = partitionSimple(nums, 0, nums.length - 1);
    while (p != target) {
      if (p > target) {
        p = partitionSimple(nums, 0, p - 1);
      } else {
        p = partitionSimple(nums, p + 1, nums.length - 1);
      }
      if (p < 0) {
        return -1;
      }
    }
    return nums[p];
  }

  /**
   * kth 的终极 bfprt 算法
   * 
   * @param nums
   * @param k
   * @return
   */
  public int bfprt(int[] arr, int k) {
    return bfprt(arr, 0, arr.length - 1, k);
  }

  int bfprt(int[] arr, int start, int end, int k) {
    if (end == start) {
      return arr[start];
    }
    // 查找近似中位数, 使用中位数的方式，来让每次分区都尽量平均
    int pivot = medianOfMedian(arr, start, end);
    // 执行荷兰国旗分割。把数据量尽量砍掉一半，根据知乎的说法，使用 5 个一组的分组方式， 最少能砍去 3/10
    int[] partitions = partition(arr, start, end, pivot);
    if (k >= partitions[0] && k <= partitions[1]) {
      return arr[k];
    } else if (k > partitions[1]) {
      return bfprt(arr, partitions[1] + 1, end, k);
    } else {
      return bfprt(arr, start, partitions[0] - 1, k);
    }
  }

  /**
   * 中位数的中位数
   * 
   * @param arr
   * @param start
   * @param end
   * @return
   */
  int medianOfMedian(int[] arr, int start, int end) {
    int n = end - start + 1;
    int modLen = (n % 5 == 0 ? 0 : 1);
    int[] medianArr = new int[n / 5 + modLen];
    int arrayStart = 0;
    for (int i = 0; i < medianArr.length; i++) {
      // 我之前这里位置算错了，坑爹!! 我自己算错了
      // arrayStart = i * 5 ;
      // medianArr[i] = computeMedian(arr, arrayStart, Math.min(arrayStart + 4,
      // medianArr.length - 1));
      arrayStart = i * 5 + start;
      medianArr[i] = computeMedian(arr, arrayStart, Math.min(arrayStart + 4, end));
    }
    // 这里就是很玄妙的地方，这里就开始递归 bfprt 方法，之前 B 站的 UP 主使用的 computeMedian 对 medianArr
    // 进行排序后取中位数的算法，进行了描述，说明他的 bug 所在
    // 知乎博主这里是正确的, 最后的参数 k 位置传递的是 n/2 ，真正实现了 medianOfMedian。这就实现了中位数的中位数
    return bfprt(medianArr, 0, medianArr.length - 1, medianArr.length / 2);
  }

  int computeMedian(int[] arr, int start, int end) {
    insertSort(arr, start, end);
    return arr[start + (end - start) / 2];
  }

  /**
   * 写个简单的插入排序, 这里的插入排序，是对数组的局部数组进行排序
   * 
   * @param arr
   */
  void insertSort(int[] arr, int start, int end) {
    // 外层循环 从 1...n-1
    for (int i = start + 1; i <= end; i++) {
      // 将 插入位置 j 到 i, 就是 [start..i) 的所有元素后移
      int j = i - 1;
      int target = arr[i];// 抽出这张牌
      for (; j >= start && arr[j] > target; j--) { // 寻找正确的插入位置，方式是将 所有 [start,i) 中大于 target 的元素后移一位
        arr[j + 1] = arr[j];
      }
      // 找到插入位置，插入元素 target
      arr[j + 1] = target;
    }
  }

  /**
   * 使用荷兰国旗算法类的分区方式，将一个数组分为 0....[pivot...]....n 这 3 个区间, 返回等于 pivot 的区间的首末两位
   * 
   * @param arr
   * @param start
   * @param end
   * @param pivot
   * @return
   */
  int[] partition(int[] arr, int start, int end, int pivot) {
    int left = start - 1;
    int right = end + 1;
    int curr = start;
    while (curr < right) {
      if (arr[curr] < pivot) { // 换到左边，然后左指针 ++
        swap(arr, curr++, ++left);
      } else if (arr[curr] == pivot) {
        curr++;
      } else { // 换到右边，curr 不变，下一轮继续比较 cur，右指针 --
        swap(arr, curr, --right);
      }
    }
    return new int[] { left + 1, right - 1 };
  }

  private void swap(int[] array, int i, int j) {
    if (i == j) {
      return;
    }
    int tmp = array[i];
    array[i] = array[j];
    array[j] = tmp;
  }

  public static void main(String[] args) {
    int[] arr = { 6, 1, 2, 7, 3, 4, 5, 10, 8 };
    QuickSort instance = new QuickSort();
    System.out.println("before sort:" + Arrays.toString(arr));
    // instance.quickSortSimple(arr);
    instance.dualPivotQuickSort(arr);
    System.out.println(Arrays.toString(arr));
  }
}
