package org.swj.leet_code.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javafx.scene.layout.Priority;
import lombok.val;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/12 20:27
 *        堆和优先级队列使用示例
 */
public class HeapPriorityQueue {
    /**
     * leetcode 451. 根据字符出现频率排序
     * 给定一个字符串 s ，根据字符出现的 频率 对其进行 降序排序 。一个字符出现的 频率 是它出现在字符串中的次数。
     * 返回 已排序的字符串 。如果有多个答案，返回其中任何一个。
     * 
     * @param s
     * @return
     */

    public String frequencySort(String s) {
        if (s == null || s.length() < 1) {
            return s;
        }
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char ch : s.toCharArray()) {
            freqMap.put(ch, freqMap.getOrDefault(ch, 0) + 1);
        }
        PriorityQueue<Map.Entry<Character, Integer>> queue = new PriorityQueue<>((a, b) -> {
            return b.getValue() - a.getValue();
        });

        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            queue.offer(entry);
        }
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Map.Entry<Character, Integer> entry = queue.poll();
            for (int i = 0; i < entry.getValue(); i++) {
                sb.append(entry.getKey());
            }
        }
        return sb.toString();
    }

    /**
     * 703. 数据流中的第 K 大元素
     */
    class KthLargest {

        PriorityQueue<Integer> queue;
        private int kth;

        public KthLargest(int k, int[] nums) {
            queue = new PriorityQueue<>();
            kth = k;
            for (int i = 0; i < nums.length; i++) {
                queue.offer(nums[i]);
                if (queue.size() > k) {
                    queue.poll();
                }
            }
        }

        public int add(int val) {
            queue.offer(val);
            if (queue.size() > kth) {
                queue.poll();
            }
            return queue.peek();
        }
    }

    /**
     * 347. 前 K 个高频元素
     * 输入: nums = [1,1,1,2,2,3], k = 2
     * 输出: [1,2]
     * 
     * @param nums
     * @param k
     * @return
     */
    public int[] topKFrequent(int[] nums, int k) {
        /**
         * 有两种解决方案，
         * 第一种:map+priorityQueue
         * 第二种:map+ArrayList<Integer>[] 多个 ArrayList,
         * 每个频率一个ArrayList，ArrayList容器里面的元素是这个频率的 value
         * 
         */
        int[] res = new int[k];
        Map<Integer, Integer> map = new HashMap<>();
        for (int val : nums) {
            map.put(val, map.getOrDefault(val, 0) + 1);
        }
        // 小顶堆，里面存储的数据都是大数
        PriorityQueue<Map.Entry<Integer, Integer>> queue = new PriorityQueue<>((a, b) -> {
            return b.getValue() - a.getValue();
        });
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            queue.offer(entry);
            if (queue.size() > k) {
                queue.poll();
            }
        }
        int counter = 0;
        while (!queue.isEmpty()) {
            Map.Entry<Integer, Integer> entry = queue.poll();
            res[counter++] = entry.getKey();
        }
        return res;
    }

    public int[] topKFrequent2(int[] nums, int k) {
        /**
         * 第二种:map+ArrayList<Integer>[] 多个 ArrayList,
         * 每个频率一个ArrayList，ArrayList容器里面的元素是这个频率的 value
         * todo
         */
        int[] res = new int[k];

        return res;
    }

    /**
     * 692. Top K Frequent Words
     * Input: words = ["i","love","leetcode","i","love","coding"], k = 2
     * Output: ["i","love"]
     * 
     * @param words
     * @param k
     * @return
     */
    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> freqMap = new HashMap<>();
        for (String wd : words) {
            freqMap.put(wd, freqMap.getOrDefault(wd, 0) + 1);
        }
        PriorityQueue<Map.Entry<String, Integer>> queue = new PriorityQueue<>((a, b) -> {
            if (a.getValue().equals(b.getValue())) {
                // 如果频率相同，则按照字典顺序排序
                return b.getKey().compareTo(a.getKey());
            }
            return a.getValue() - b.getValue();
        });
        for (Map.Entry<String, Integer> etnry : freqMap.entrySet()) {
            queue.offer(etnry);
            if (queue.size() > k) {
                queue.poll();
            }
        }
        LinkedList<String> res = new LinkedList<>();
        while (!queue.isEmpty()) {
            res.addFirst((queue.poll().getKey()));
        }
        return res;
    }

    /**
     * 1845. Seat Reservation Manager
     * 座位管理
     * SeatManager seatManager = new SeatManager(5); // Initializes a SeatManager
     * with 5 seats.
     * seatManager.reserve(); // All seats are available, so return the lowest
     * numbered seat, which is 1.
     * seatManager.reserve(); // The available seats are [2,3,4,5], so return the
     * lowest of them, which is 2.
     * seatManager.unreserve(2); // Unreserve seat 2, so now the available seats are
     * [2,3,4,5].
     * seatManager.reserve(); // The available seats are [2,3,4,5], so return the
     * lowest of them, which is 2.
     * seatManager.reserve(); // The available seats are [3,4,5], so return the
     * lowest of them, which is 3.
     * seatManager.reserve(); // The available seats are [4,5], so return the lowest
     * of them, which is 4.
     * seatManager.reserve(); // The only available seat is seat 5, so return 5.
     * seatManager.unreserve(5); // Unreserve seat 5, so now the available seats are
     * [5].
     * 
     */
    class SeatManager {
        PriorityQueue<Integer> queue;

        public SeatManager(int n) {
            queue = new PriorityQueue<>();
            for (int i = 1; i <= n; i++) {
                queue.offer(i);
            }
        }

        public int reserve() {
            return queue.isEmpty() ? 0 : queue.poll();
        }

        public void unreserve(int seatNumber) {
            queue.offer(seatNumber);
        }
    }

    /**
     * 1834. 单线程 CPU
     * 给你一个二维数组 tasks ，用于表示 n​​​​​​ 项从 0 到 n - 1 编号的任务。其中 tasks[i] = [enqueueTimei,
     * processingTimei] 意味着
     * 第 i​​​​​​​​​​ 项任务将会于 enqueueTimei 时进入任务队列，需要 processingTimei 的时长完成执行。
     * 
     * 现有一个单线程 CPU ，同一时间只能执行 最多一项 任务，该 CPU 将会按照下述方式运行：
     * 
     * 如果 CPU 空闲，且任务队列中没有需要执行的任务，则 CPU 保持空闲状态。
     * 如果 CPU 空闲，但任务队列中有需要执行的任务，则 CPU 将会选择 执行时间最短 的任务开始执行。如果多个任务具有同样的最短执行时间，
     * 则选择下标最小的任务开始执行。
     * 一旦某项任务开始执行，CPU 在 执行完整个任务 前都不会停止。
     * CPU 可以在完成一项任务后，立即开始执行一项新任务。
     * 返回 CPU 处理任务的顺序。
     * 
     * 示例1：
     * 输入：tasks = [[1,2],[2,4],[3,2],[4,1]]
     * 输出：[0,2,3,1]
     * 解释：事件按下述流程运行：
     * - time = 1 ，任务 0 进入任务队列，可执行任务项 = {0}
     * - 同样在 time = 1 ，空闲状态的 CPU 开始执行任务 0 ，可执行任务项 = {}
     * - time = 2 ，任务 1 进入任务队列，可执行任务项 = {1}
     * - time = 3 ，任务 2 进入任务队列，可执行任务项 = {1, 2}
     * - 同样在 time = 3 ，CPU 完成任务 0 并开始执行队列中用时最短的任务 2 ，可执行任务项 = {1}
     * - time = 4 ，任务 3 进入任务队列，可执行任务项 = {1, 3}
     * - time = 5 ，CPU 完成任务 2 并开始执行队列中用时最短的任务 3 ，可执行任务项 = {1}
     * - time = 6 ，CPU 完成任务 3 并开始执行任务 1 ，可执行任务项 = {}
     * - time = 10 ，CPU 完成任务 1 并进入空闲状态
     * 
     * @param tasks
     * @return
     */
    public int[] getOrder(int[][] tasks) {
        /**
         * 这道题我当时看完就懵逼了，这怎么算，有开始时间，又是持续运行时间，还要时间到了加入队列，并且运行时间越短越先被处理，这怎么
         * 能在一个算法里面处理完？
         * 没办法，继续看阿东的解题思路：
         * 这题的难点在于你要同时控制三个变量(开始时间，处理时间，索引)的有序性，而且这三个变量还有优先级：
         * 首先应该考虑开始时间，因为只有时间到了开始时间，任务才会进入可执行状态
         * 其次应该考虑任务的处理时间，在所有可执行的任务重选择处理时间最短的；
         * 如果存在处理时间相同的任务，那么优先选择索引最小的。
         * 所以这题的思路是：
         * 现根据任务「开始时间」排序，维护一个时间线变量 now 来判断哪些任务到了可执行状态，然后借助一个优先级队列 pq 对「处理时间」
         * 和「索引」进行动态排序
         * 
         * 利用优先级队列动态牌是很有必要的，因为没完成一个任务，时间线 now 就要更新，进行产生新的可执行任务。
         */

        int now = 0;
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> {
            // 处理时间相同的任务，那么优先选择索引最小的
            return a[1] == b[1] ? a[2] - b[2] : a[1] - b[1];
        });
        int taskCounter = 0;
        int n = tasks.length;
        ArrayList<int[]> triples = new ArrayList<>();
        // 将任务封装成 开始时间，处理时长，索引的形式，便于排序和处理
        for (int[] task : tasks) {
            // 任务开始时间，任务处理时长，索引
            triples.add(new int[] { task[0], task[1], taskCounter++ });
        }
        // 根据开始时间排序
        Collections.sort(triples, (a, b) -> {
            return a[0] - b[0];
        });

        // 上半部分 负责推进时间
        // 时间的推进有两种形式，1、从队列中取出任务，执行任务，累加任务的时长。
        // 2、队列为空，那就把时间 推进到 当前将要被执行的时间
        int i = 0;
        List<Integer> res = new ArrayList<>();

        while (res.size() < n) {
            if (!queue.isEmpty()) {
                // 队列并非阻塞的，即使时间没有到，也能取出元素
                int[] task = queue.poll();
                // 任务执行是虚拟的, 将当前任务的索引添加到结果集中，表示执行了当前任务
                res.add(task[2]);
                // 累加时长
                now += task[1];
            } else if (i < triples.size() && triples.get(i)[0] > now) { // 队列为空，则把轮询时间置为当前任务的开始时间
                now = triples.get(i)[0];
            }

            // 负责时间到了，将任务加入队列中
            // 任务必须时间到了，才能进入队列
            for (; i < triples.size() && triples.get(i)[0] <= now; i++) {
                queue.offer(triples.get(i));
            }
        }
        int[] arr = new int[n];
        taskCounter = 0;
        for (int idx : res) {
            arr[taskCounter++] = idx;
        }
        return arr;
    }

    public static void main(String[] args) {
        HeapPriorityQueue instance = new HeapPriorityQueue();
        System.out.println(instance.frequencySort("tree"));
        System.out.println(instance.frequencySort("cccaaa"));
        System.out.println(instance.frequencySort("Aabb"));
    }
}
