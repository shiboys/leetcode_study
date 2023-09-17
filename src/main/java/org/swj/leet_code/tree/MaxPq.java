package org.swj.leet_code.tree;

/**
 * 大顶堆
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/15 17:07
 */
public class MaxPq<K extends Comparable<K>> {
    // 存储底层数据的数组
    private K[] arr;
    // 队列中的元素个数
    private int size;

    public MaxPq(int n) {
        // 索引 0 的位置不用，因此这里的容量需要 加 1。
        arr = (K[]) new Comparable[n + 1];
    }

    // 辅助函数
    private int parent(int k) {
        return k / 2;
    }

    private int left(int k) {
        return k * 2;
    }

    private int right(int k) {
        return k * 2 + 1;
    }

    /**
     * 将 k 位置的元素下沉，以维护二叉堆性质
     * 
     * @param k
     */
    private void sink(int k) {
        // 下沉的逻辑稍微复杂些
        // 之所以要下沉，是因为 父节点小于子节点，父节点需要沉下去做子节点，子节点浮上来做父节点
        // 注意这里的 while 循环条件，能够下沉的条件必须是 k 所在层和 left(k) 层 都存在
        // 如果 left(k) 不存在，则没有交换的必要。说人话就是如果沉到底了，就没有必要沉下去了
        while (left(k) <= size) {
            int child = left(k);
            // 左子节点 小于 右子节点，则 child 指向右节点
            if (right(k) <= size && less(child, right(k))) {
                child = right(k);
            }
            // 如果此时 满足 当前节点大于 左右子节点，则跳出循环
            if (less(child, k)) {
                break;
            }
            // 否则进行交换式的下沉
            swap(k, child);
            k = child;
        }
    }

    /**
     * 将 k 位置的元素上浮，以维护二叉堆性质
     * 
     * @param k
     */
    private void swim(int k) {
        // 如果浮到堆顶，就不能再上浮了
        while (k > 1 && less(parent(k), k)) {
            // 如果 第 k 个元素比父节点元素大，则将第 k 个元素跟父节点交换以达到上浮的目的
            swap(parent(k), k);
            k = parent(k);
        }
    }

    public K max() {
        return arr[1];
    }

    /**
     * 向二叉堆中插入一个元素
     * 
     * @param k
     */
    public void insert(K k) {
        size++;
        // 先把元素加到数组尾部，
        arr[size] = k;
        // 然后再让它上浮到合适的位置。
        swim(size);
        //ps: 这里并没有像 jdk 那样实现的自动扩容那么完美
    }

    /**
     * 删除并返回堆顶的最大元素
     * 
     * @return
     */
    public K delMax() {
        K k = arr[1];
        // 将 1 位置和数组尾部元素交换，把要删除的元素换到尾部
        swap(1, size);
        // 删除之前的堆顶元素
        arr[size] = null;
        // 元素个数减 1。
        size--;
        // 将交换后的堆顶元素下沉到正确的位置
        sink(1);
        return k;
    }

    private void swap(int i, int j) {
        K tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private boolean less(int i, int j) {
        K a = arr[i];
        K b = arr[j];
        return a != null && a.compareTo(b) < 0;
    }

    public int size() {
        return size;
    }
}
