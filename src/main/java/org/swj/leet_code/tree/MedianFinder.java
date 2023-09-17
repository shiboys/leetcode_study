package org.swj.leet_code.tree;

import java.util.PriorityQueue;

/**
 * 查找数组中位数
 * leetcode 295 题
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/15 21:07
 */
public class MedianFinder {

    private static final int PQ_SIZE = 2 * 100_000;
    private PriorityQueue<Integer> large;

    private PriorityQueue<Integer> small;

    private MinPq<Integer> largePq = new MinPq<>(PQ_SIZE);

    private MaxPq<Integer> smallPq = new MaxPq<>(PQ_SIZE);

    public MedianFinder() {
        // large 为小顶堆，里面存储的都是比较大的数，PriorityQueue 默认小顶堆
        large = new PriorityQueue<>();
        // small 为大顶堆，里面都存储的是相对较小的数据
        small = new PriorityQueue<>((a, b) -> {
            return b - a;
        });
    }

    public void addNum(int num) {
        if (small.size() >= large.size()) {
            // 需要向 large 增加元素
            small.offer(num);
            large.offer(small.poll());
        } else {
            large.offer(num);
            small.offer(large.poll());
        }
    }

    /**
     * 剑指 offer 的解法
     * 
     * @param num
     */
    public void addNum2(int num) {
        if (small.size() == large.size()) {// 元素个数相同，插入小顶堆，也就是 large
            Integer sp = small.peek();
            if (sp != null && sp > num) { // 比大顶堆的最大值小
                small.offer(num); // num 留在 small 里面
                num = small.poll(); // 取大顶堆的最大值 作为交换
            }
            large.offer(num);
            // 这样对对比就会发现 addNum 的解法更优雅，或许 addNum2 的性能好那么一丢丢把
        } else { // 插入大顶堆, 也就是 small
            Integer lp = large.peek();
            if (lp != null && lp < num) { // num 留在 large 里面
                large.offer(num);
                num = large.poll();// num 交换得到 large 的堆顶最小元素
            }
            small.offer(num);
        }
    }

    public void addNumC2(int num) {
        if (smallPq.size() >= largePq.size()) {
            // 向 laregePq 新增元素
            smallPq.insert(num);
            largePq.insert(smallPq.delMax());
        } else {
            largePq.insert(num);
            smallPq.insert(largePq.delMin());
        }
    }

    public double findMedian() {
        if (small.size() > large.size()) {
            return small.peek();
        } else if (small.size() < large.size()) {
            return large.peek();
        }
        // 二者 size 相等
        return (small.peek() + large.peek()) / 2.0;
    }

    public double findMedianC2() {
        if (smallPq.size() > largePq.size()) {
            return smallPq.max();
        } else if (smallPq.size() < largePq.size()) {
            return largePq.min();
        }
        // 二者 size 相等
        return (smallPq.max() + largePq.min()) / 2.0;
    }

    public static void main(String[] args) {
        int[] arr = new int[] { 7, 8, 10, 1, 3, 5,12 };
        MedianFinder mf = new MedianFinder();
        for (int item : arr) {
            mf.addNumC2(item);
        }
        System.out.println(mf.findMedianC2());
    }
}
