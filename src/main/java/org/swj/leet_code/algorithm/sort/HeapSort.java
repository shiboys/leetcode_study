package org.swj.leet_code.algorithm.sort;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/07/14 11:59
 */
public class HeapSort {
    /**
     * 堆排序，
     * 堆排序的主要思路是先建堆,
     * 然后将堆顶的元素交换到 队列末尾，堆的 size--, 堆顶元素下沉
     * 从这里我们可以看到这里的堆是大顶堆，每次都将最大的元素交换到数组尾部，然后 size--
     */

    public void sort(int[] arr) {
        if (arr == null) {
            return;
        } else if (arr.length <= 1) {
            return;
        }
        // 使用堆排序
        MaxTopHeap heap = new MaxTopHeap(arr);
        // 其实跟 delMax 或者 poll 是相同的方式，只不过我们这里写到外面了
        for (int i = arr.length - 1; i >= 0; i--) {
            // swap(arr,0,len-i-1);
            // heap.size--;
            // heap.sinkDown(0, arr[0]);
            arr[i] = heap.poll();
        }
    }

    public static void main(String[] args) {
        int[] arr = { 6, 1, 2, 7, 3, 4, 5, 10, 8 };
        HeapSort heapSort = new HeapSort();
        // 居然一次写对了
        heapSort.sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    class MaxTopHeap {

        int[] arr;
        int size;

        public MaxTopHeap(int[] array) {
            // this.arr = new int[array.length+1];
            // System.arraycopy(array, 0, arr, 1, arr.length);
            this.arr = array;
            this.size = array.length;
            buildHeap();
        }

        void buildHeap() {
            for (int i = (arr.length >>> 1); i >= 0; i--) {
                sinkDown(i, arr[i]);
            }
        }

        // offer,poll 可以暂时不写

        // 这里是为了堆排序考虑，没有将 arr[--size] = null 置为 null
        // 正常的 Poll 是应该置为 null 的
        int poll() {
            int val = arr[0];
            arr[0] = arr[--size];
            sinkDown(0, arr[0]);
            return val;
        }

        /**
         * 
         * 元素上浮
         * 
         * @param k 索引位
         * @param e 当前索引位的元素值。之所以传入 2 个参数，也是参考了 jdk 的做法，减少了交换的次数
         */
        void swimUp(int k, int e) {
            int p;
            while (k > 0) {
                p = parent(k);
                if (arr[p] <= e) { // 比父节点大
                    arr[k] = arr[p];
                } else { // 如果 e < arr[p] 则停止上浮
                    break;
                }
                k = p;
            }
            // 跳出循环，当前k 的位置就是 e 的正确位置，只交换一次
            arr[k] = e;
        }

        /**
         * 元素下沉，如果比父节点小，则交换父节点
         * 
         * @param k
         * @param e
         */
        void sinkDown(int k, int e) {
            int left, right, child;
            while (k < size) {
                child = left = left(k);
                right = left + 1;
                if (right < size && arr[right] > arr[left]) {
                    child = right;
                }
                // 如果无法计算 child，或者 已经比最大的子元素大，则停止下潜
                if (child >= size || arr[k] > arr[child]) {
                    break;
                }
                if (arr[k] < arr[child]) {
                    arr[k] = arr[child];
                }
                k = child;
            }
            // 跳出循环的位置k 就是正确的位置
            arr[k] = e;
        }

        /**
         * 
         * 这里使用 JDK 的左右子节点及父节点的计算方法
         * 
         * @param k
         * @return
         */
        int left(int k) {
            return k * 2 + 1;
        }

        int right(int k) {
            return k * 2 + 2;
        }

        int parent(int k) {
            return (k - 1) >>> 1;
        }
    }
}
