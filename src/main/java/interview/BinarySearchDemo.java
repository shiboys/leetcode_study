package interview;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2025/03/31 16:23
 */
public class BinarySearchDemo {

  public static void main(String[] args) {

    testBinarySort();

  }

  static int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length;
    Arrays.sort(arr);

    while (left < right) {
      int mid = left + (right - left) / 2;
      int midValue = arr[mid];
      if (midValue == target) {
        right = mid;
      } else if (midValue < target) {
        left = mid+1;
      } else {
        right = mid;
      }
    }
    if (arr[left] == target) {
      return left;
    }
    return arr.length;
  }

  static void testBinarySort() {
    int[] arr = new int[] {1, 2, 2, 3, 5, 5, 6};
    int target = 5;

    System.out.println("binary search result for target 5 is " + binarySearch(arr, target));
  }
}
