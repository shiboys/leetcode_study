package org.swj.leet_code.algorithm.sort;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/14 11:59
 * 归并排序，我本次重写归并排序主要包括一下几大项：
 * 1、使用循环非递归方式的归并排序
 * 2、使用传统递归方式的归并排序
 * 2.1 使用带传入 拷贝数组的 归并排序
 * 2.2 使用外置拷贝数组的 归并排序
 * 3、外排序 使用 归并示例
 */
public class MergeSort {

  /**
   * 正常的迭代排序，非递归法
   *
   * @param array
   */
  public int[] mergeSortNormal(int[] array) {
    if (array == null || array.length < 1) {
      return null;
    }
    int len = array.length;
    int[] dest = new int[array.length];
    // seg 代表了归并一半的步长，1，2，4，8 这种，表示每次归并的元素个数,
    // 从这里也可以得出归并排序的时间复杂度是 O(NlogN)
    for (int seg = 1; seg <= len; seg += seg) {
      // 每 2*seg 个元素会被归并一次
      for (int i = 0; i < len; i += seg * 2) {
        int lo = i, k = i;
        int mid = Math.min(lo + seg, len);
        int hi = mid, end = Math.min(lo + seg * 2, len);
        while (k < end) {
          /**
           * 其实下面这 4 个 if 可以被整合为两个 if
           * if(hi==end || (lo<mid && array[lo] < array[hi]) {
           *   temp[k++] = array[lo++];
           * } else {
           *    temp[k++] = array[hi++];
           * }
           */
          if (lo == mid) {
            dest[k++] = array[hi++];
          } else if (hi == end) {
            dest[k++] = array[lo++];
          } else if (array[lo] < array[hi]) {
            dest[k++] = array[lo++];
          } else {
            dest[k++] = array[hi++];
          }
        }
      }// 归并完一遍， 交换 temp 和 array
      int[] tmp = array;
      array = dest;
      dest = tmp;
    }
    return dest;
  }

  int[] temp;

  public void mergeSort(int[] array) {
    /**
     * 1、使用 成员变量版本
     */
    //temp = new int[array.length];
    //mergeSort(array, 0, array.length - 1);
    /**
     * 2、使用参数变量版本，在归并过程中发生拷贝
     */
    //int[] dest = new int[array.length];
    //mergeSort2(array, dest, 0, array.length - 1);

    /**
     3、使用参数变量版本，在归并过程中部分发生拷贝只交换引用，归并结束，将 dest 复制回 array
     *这个归并方法只会复制 2 次，是复制次数最少的方式，如果只需返回排序后的数组，那只需要复制一次
     */
    int[] dest = Arrays.copyOf(array, array.length);
    mergeSort3(array, dest, 0, array.length - 1);
    System.arraycopy(dest, 0, array, 0, array.length);
  }

  void mergeSort2(int[] src, int[] dest, int start, int end) {
    if (start >= end) {
      return;
    }
    int mid = (start + end) >>> 1;
    mergeSort2(src, dest, start, mid);
    mergeSort2(src, dest, mid + 1, end);
    merge2(src, dest, start, mid, end);
  }

  private void merge2(int[] src, int[] dest, int start, int mid, int end) {
    int i = start, j = mid + 1, k = start;
    System.arraycopy(src, start, dest, start, end - start + 1);
    while (k <= end) {
      if (j == end + 1 || (i <= mid && src[i] < src[j])) {
        src[k++] = dest[i++];
      } else {
        src[k++] = dest[j++];
      }
    }
  }

  void mergeSort(int[] src, int start, int end) {
    // 这里的边界一定要注意,如果 end 是索引，则边界为 start>= end, 如果 end 是 length，则边界为是 start+1 >= end
    if (start >= end) {
      return;
    }
    // 先分再合并
    int mid = (start + end) >>> 1;
    // 递归调用的时候，将 temp 作为 src 参数传入, 而 src 作为 temp, 这样的 zigzag 方式写入
    mergeSort(src, start, mid);
    mergeSort(src, mid + 1, end);
    // 进行合并
    merge(src, start, mid, end);
  }

  private void merge(int[] src, int start, int mid, int end) {
    int i = start, j = mid + 1, k = start;
    System.arraycopy(src, start, temp, start, end - start + 1);
    // 卧槽，在 i,j, mid 的边界上，我浪费了好多时间，边界没有找对，坑呀
    while (k <= end) {
      if (i == mid + 1) {
        src[k++] = temp[j++];
      } else if (j == end + 1) {
        src[k++] = temp[i++];
      } else if (src[i] < src[j]) {
        src[k++] = temp[i++];
      } else {
        src[k++] = temp[j++];
      }
    }
  }

  void mergeSort3(int[] src, int[] dest, int start, int end) {
    if (start >= end) {
      return;
    }
    int mid = (start + end) >>> 1;
    mergeSort3(dest, src, start, mid);
    mergeSort3(dest, src, mid + 1, end);
    merge3(src, dest, start, mid, end);
  }

  private void merge3(int[] src, int[] dest, int start, int mid, int end) {
    int i = start, j = mid + 1, k = start;
    //System.arraycopy(src, start, dest, start, end - start + 1);
    while (k <= end) {
      if (j == end + 1 || (i <= mid && src[i] < src[j])) {
        dest[k++] = src[i++];
      } else {
        dest[k++] = src[j++];
      }
    }
  }

  public static void main(String[] args) {
    int[] arr = {6, 1, 2, 7, 3, 4, 5, 10, 8};
    MergeSort instance = new MergeSort();
    instance.temp = new int[arr.length];
    System.out.println("before sort:" + Arrays.toString(arr));
    //instance.quickSortSimple(arr);
    instance.mergeSort(arr);
    System.out.println(Arrays.toString(arr));
  }
}
