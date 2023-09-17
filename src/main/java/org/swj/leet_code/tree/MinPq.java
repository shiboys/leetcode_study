package org.swj.leet_code.tree;

/**
 * 小顶堆
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/15 19:07
 */
public class MinPq<K extends Comparable<K>> {

    private K[] pq;
    private int size;

    public MinPq(int n) {
        this.pq = (K[]) new Comparable[n + 1];
    }

    // 仍然是辅助方法
    private int parent(int k) {
        return k / 2;
    }

    private int left(int k) {
        return k * 2;
    }

    private int right(int k) {
        return k * 2 + 1;
    }

    private void swap(int i, int j) {
        K tmp = pq[i];
        pq[i] = pq[j];
        pq[j] = tmp;
    }

    private boolean less(int i, int j) {
        return pq[i] != null && pq[i].compareTo(pq[j]) < 0;
    }

    // 核心方法
    private void sink(int k) {
        while (left(k) <= size) {
            int child = left(k);
            if (right(k) <= size && less(right(k), child)) {
                child = right(k);
            }
            // k 比最小的左右子节点还小，满足条件，退出循环
            if (less(k, child)) {
                break;
            }
            swap(k, child);
            k = child;
        }
    }

    private void swim(int k) {
        while (k > 1 && less(k, parent(k))) {
            swap(k, parent(k));
            k = parent(k);
        }
    }

    public K min() {
        return pq[1];
    }

    public void insert(K k) {
        size++;
        pq[size] = k;
        swim(size);
    }

    public K delMin() {
        K k = pq[1];
        swap(1, size);
        pq[size] = null;
        size--;
        sink(1);
        return k;
    }
    // 没有 buildHeap

    public static void main(String[] args) {
        int[] arr = { 3, 5, 7, 1, 4, 6, 8 };
        MinPq<Integer> minQueue = new MinPq<>(100);
        for (int item : arr) {
            minQueue.insert(item);
        }
        System.out.println(minQueue.min());
    }

    public int size() {
        return size;
    }
}
