package org.swj.leet_code.algorithm.dfs_bfs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 回溯法相关应用
 * 回溯的英文，我查了下，backTrack 和 traceBack 都可以，但是 backTrack 应该是用的更多一些，也更符合语境
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/17 21:29
 */
public class BackTrack {

    List<List<Integer>> res;
    // 记录遍历路径元素
    LinkedList<Integer> pathItems = new LinkedList<>();

    boolean[] visited;

    /**
     * nums 的所有子集。解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。
     * 示例 1：
     * 输入：nums = [1,2,3]
     * 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
     * 
     * @param nums
     * @return
     */
    public List<List<Integer>> subsets(int[] nums) {
        res = new ArrayList<>();
        trackBackSubsets(nums, 0);
        return res;
    }

    void trackBackSubsets(int[] nums, int start) {
        // 前序遍历位置
        res.add(new ArrayList<>(pathItems));

        for (int k = start; k < nums.length; k++) {
            pathItems.addLast(nums[k]);
            // 这里从 k+1 开始遍历，避免产生重复的子集
            trackBackSubsets(nums, k + 1);
            pathItems.removeLast();
        }
    }

    List<List<Integer>> combine(int n, int k) {
        int[] nums = new int[n];
        for (int i = 1; i <= n; i++) {
            nums[i - 1] = i;
        }
        res = new ArrayList<>();
        traceBackWithCombineK(nums, 0, k);
        return res;
    }

    void traceBackWithCombineK(int[] nums, int start, int k) {
        if (pathItems.size() == k) {
            res.add(new ArrayList<>(pathItems));
            return;
        }
        for (int i = start; i < nums.length; i++) {
            pathItems.add(nums[i]);
            traceBackWithCombineK(nums, i + 1, k);
            pathItems.removeLast();
        }
    }

    /**
     * leetcode 90 题 『子集 II』
     * 带有重复元素的子集问题
     * 
     * @param nums
     * @return
     */

    public List<List<Integer>> subsetsWithDp(int[] nums) {
        Arrays.sort(nums);
        res = new ArrayList<>();
        traceBackWithDp(nums, 0);
        return res;
    }

    void traceBackWithDp(int[] nums, int start) {
        res.add(new LinkedList<>(pathItems));

        for (int i = start; i < nums.length; i++) {
            // if (i >= 1 && nums[i] == nums[i - 1]) {
            // // 跳过 start 开始的遍历，因为之前已经遍历过了它
            // continue;
            // }
            // 子集和组合问题，都是从 start 开始的，而不是从什么 1 开始的
            // 上面的剪枝逻辑写错了。为什么写错，还是因为对 这个子集的问题没有弄清楚是怎么递归遍历的。
            // 从 start 到 i 的遍历，如果当前的 i 已经遍历过了，后面再遍历到 跟 i 相同的元素，肯定需要 continue 跳过的。
            // 需要根据剪枝的那张图仔细揣摩，然后对比代码就能理解该代码逻辑就是图中剪枝的实现
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            // 否则进行 回溯递归
            pathItems.add(nums[i]);
            traceBackWithDp(nums, i + 1);
            pathItems.removeLast();
        }
    }

    /**
     * leetcode 40 题，组合之和为 target 的所有元素，candidates 可能存在重复元素
     * 
     * @param candidates
     * @param target
     * @return
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        // 仍然是先排序
        Arrays.sort(candidates);
        res = new ArrayList<>();
        traceBackCombinationSum2(candidates, target, 0);
        return res;
    }

    int trackSum = 0;

    void traceBackCombinationSum2(int[] candidates, int target, int start) {
        if (trackSum == target) {
            res.add(new LinkedList<>(pathItems));
            return;
        }
        if (trackSum > target) { // 超过目标值直接结束。不加这个逻辑，会导致超时
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (i > start && candidates[i] == candidates[i - 1]) {
                continue;
            }
            trackSum += candidates[i];
            pathItems.add(candidates[i]);
            traceBackCombinationSum2(candidates, target, i + 1);
            pathItems.removeLast();
            trackSum -= candidates[i];
        }
    }

    /**
     * leetcode 47 题，含有重复元素的排列
     * 
     * @param nums
     * @return
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        res = new ArrayList<>();
        visited = new boolean[nums.length];
        Arrays.sort(nums);
        // traceBackPermuteUnique(nums);
        backTrackPermuteUnique(nums);
        return res;
    }

    void traceBackPermuteUnique(int[] nums) {
        if (pathItems.size() == nums.length) {
            res.add(new LinkedList<>(pathItems));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) {
                continue;
            }
            // 这里添加特殊的剪枝逻辑，具体为什么要用 !visited[i-1] 请看考 backTrack.md 文档描述。
            if (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) {
                continue;
            }
            pathItems.add(nums[i]);
            visited[i] = true;
            traceBackPermuteUnique(nums);
            visited[i] = false;
            pathItems.removeLast();
        }
    }

    void backTrackPermuteUnique(int[] nums) {
        if (pathItems.size() == nums.length) {
            res.add(new LinkedList<>(pathItems));
        }
        // 题目说 -10 <= nums[i] <= 10
        int prevNum = -888;

        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) {
                continue;
            }

            // 注意这里，虽然说每次递归，prevNum 都会被初始化为 -888, 但是在循环里面也被改了，
            // 如果我们忽略循环中递归函数的调用，就会发现 prevNum 确确实实记录了上一次循环的值，到当前循环中，就是上一次的值
            // 由于数组是排序的，相同的元素排列在一起，所以这个 prevNum 也能起到剪枝的作用
            // 这个剪枝逻辑想明白后，发现也是非常的容易理解，更符合我们平时的习惯和代码的直观思考
            if (nums[i] == prevNum) {
                continue;
            }
            visited[i] = true;
            // 记录这条树枝上的值
            prevNum = nums[i];
            pathItems.add(prevNum);
            backTrackPermuteUnique(nums);
            visited[i] = false;
            pathItems.removeLast();
        }
    }

    /**
     * leetcode 39 题，组合总和
     * 给你一个 无重复元素 的整数数组 candidates 和一个目标整数 target ，找出 candidates 中可以使数字和为目标数 target
     * 的 所有 不同组合 ，
     * 并以列表形式返回。你可以按 任意顺序 返回这些组合。
     * candidates 中的 同一个 数字可以 无限制重复被选取 。如果至少一个数字的被选数量不同，则两种组合是不同的。
     * 
     * 输入：candidates = [2,3,6,7], target = 7
     * 输出：[[2,2,3],[7]]
     * 
     * @param candidates
     * @param target
     * @return
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        res = new ArrayList<>();
        backTrackCombinationSum(candidates, target, 0);
        return res;
    }

    void backTrackCombinationSum(int[] candidates, int target, int start) {

        if (trackSum == target) {
            res.add(new LinkedList<>(pathItems));
            // 结束本次递归，否则会无限递归
            return;
        }
        // 超过目标和，则返回。如果有元素为 0 的，且 target 是比较大的数，这就坑爹了
        if (trackSum > target) {
            return;
        }
        // 2 <= candidates[i] <= 40

        for (int i = start; i < candidates.length; i++) {
            pathItems.add(candidates[i]);
            trackSum += candidates[i];
            backTrackCombinationSum(candidates, target, i);
            trackSum -= candidates[i];
            pathItems.removeLast();
        }
    }

    public static void main(String[] args) {
        BackTrack instance = new BackTrack();
        int[] nums = new int[] { 1, 2, 3 };
        // System.out.println(instance.subsets(nums));

        // C4_2
        System.out.println(instance.combine(4, 2));

        // C1_1
        System.out.println(instance.combine(1, 1));

        // nums = new int[] { 1, 2, 2 };
        // System.out.println(instance.subsetsWithDp(nums));

        // nums = new int[] { 2, 5, 2, 1, 2 };
        // System.out.println(instance.combinationSum2(nums, 5));

        // nums = new int[] { 10, 1, 2, 7, 6, 1, 5 };
        // System.out.println(instance.combinationSum2(nums, 8));

        // permuteUnique

        nums = new int[] { 1, 2, 2 };
        System.out.println(instance.permuteUnique(nums));

        nums = new int[] { 2, 3, 6, 7 };
        System.out.println(instance.combinationSum(nums, 7));

        System.out.println(instance.generateParenthesis(3));

        // nums = new int[] {4,3,2,3,5,2,1};
        // System.out.println(instance.canPartitionKSubsets2(nums, 4));

        nums = new int[] {2,2,2,2,3,4,5};
        System.out.println(instance.canPartitionKSubsets2(nums, 4));
    }

    /**
     * leetcode 22 题，一些列 n 对合法的小括号.
     * 解法分析请参考 backTrack.md 文档描述
     */
    public List<String> generateParenthesis(int n) {
        StringBuilder track = new StringBuilder();
        List<String> res = new LinkedList<>();
        backTrackParentheses(n, n, track, res);
        return res;
    }

    /**
     * 
     * @param left  左括号的需求量
     * @param right 右括号的需求量
     * @param track 括号字符串收集器
     * @param res
     */
    void backTrackParentheses(int left, int right, StringBuilder track, List<String> res) {
        if (right < left) { // 右括号需要的数量大于左括号，反过来就是说左括号的剩余数量多。因为从左右到，左括号的需求量不断在减少
            return;
        }
        if (left < 0 || right < 0) {
            return;
        }
        // 左右括号的匹配用完
        if (left == 0 && right == 0) {
            res.add(track.toString());
            return;
        }
        // 回溯框架
        // 尝试放左括号
        track.append('(');
        backTrackParentheses(left - 1, right, track, res);
        track.deleteCharAt(track.length() - 1);

        // 尝试放右括号
        track.append(')');
        backTrackParentheses(left, right - 1, track, res);
        track.deleteCharAt(track.length() - 1);

    }

    /**
     * 698. 划分为k个相等的子集
     * 给定一个整数数组 nums 和一个正整数 k，找出是否有可能把这个数组分成 k 个非空子集，其总和都相等。
     * 示例 1：
     * 
     * 输入： nums = [4, 3, 2, 3, 5, 2, 1], k = 4
     * 输出： True
     * 说明： 有可能将其分成 4 个子集（5），（1,4），（2,3），（2,3）等于总和。
     * 
     * @param nums
     * @param k
     * @return
     */
    public boolean canPartitionKSubsets(int[] nums, int k) {
        // 解法1 ，数字的视角
        int n = nums.length;
        if (k > n) {
            return false;
        }
        int sum = Arrays.stream(nums).sum();
        if (sum % k != 0) { // 能否整除
            return false;
        }
        int target = sum / k;
        // k 个桶
        int[] bucket = new int[k];

        // 将 nums 倒序排列
        int[] tmp = Arrays.copyOf(nums, n);
        // Arrays.sort() 带 comparator 的是 泛型 T[] 数组，不适合用 primitive type，这里有点坑爹
        Arrays.sort(tmp);
        for (int i = n - 1; i >= 0; i--) {
            nums[n - 1 - i] = tmp[i];
        }

        return backTrack(nums, bucket, 0, target);
    }

    public boolean canPartitionKSubsets2(int[] nums, int k) {
        // 解法1 ，数字的视角
        int n = nums.length;
        if (k > n) {
            return false;
        }
        int sum = Arrays.stream(nums).sum();
        if (sum % k != 0) { // 能否整除
            return false;
        }
        int target = sum / k;
 
        // 将 nums 倒序排列
        int[] tmp = Arrays.copyOf(nums, n);
        // Arrays.sort() 带 comparator 的是 泛型 T[] 数组，不适合用 primitive type，这里有点坑爹
        Arrays.sort(tmp);
        for (int i = n - 1; i >= 0; i--) {
            nums[n - 1 - i] = tmp[i];
        }
        // 使用位图技巧
        int used = 0;

        return backTrack2(k, 0, nums, 0, used, target);
    }

    boolean backTrack(int[] nums, int[] buckets, int index, int target) {
        if (index == nums.length) { // 数字递归完了
            // 判断所有桶是否都等于 target
            return Arrays.stream(buckets).allMatch(x -> x == target);
        }
        for (int i = 0, len = buckets.length; i < len; i++) {
            if (buckets[i] + nums[index] > target) { // 剪枝
                continue;
            }
            // 做选择
            buckets[i] += nums[index];
            // 回溯
            if (backTrack(nums, buckets, index + 1, target)) {
                // 这里为什么要 return true 而不是撤销选择之后再 return true。
                // 这是因为 回溯方法的前面验证逻辑，要验证所有的桶里的数字是否等于 target
                // 如果这里给撤销选择， 那永远都验证不了了。
                return true;
            }
            // 撤销选择
            buckets[i] -= nums[index];
        }
        return false;
    }

    HashMap<String, Boolean> memo = new HashMap<>();

    boolean backTrack(int k, int bucketSum, int[] nums, int start, boolean[] used, int target) {
        // base case
        if (k == 0) {
            // 所有的桶都装满了，而且桶用完了
            // 因为 target = sum/k, 同时 bucketSum == target 的时候，才递归 k-1
            return true;
        }
        String state = Arrays.toString(used);
        if (bucketSum == target) { // 当前桶已经装满，则递归下一个桶。下一个桶也是要从第一个数字开始遍历
            boolean result = backTrack(k - 1, 0, nums, 0, used, target);
            // 把当前状态装入备忘录
            memo.put(state, result);
            return result;
        }
        if (memo.containsKey(state)) {
            // 如果当前状态曾经计算过，就直接返回，不再递归穷举了。
            return memo.get(state);
        }
        // 从 start 开始探查有效的 nums[i] 装入当期那桶中
        for (int i = start; i < nums.length; i++) {
            // 仍然需要剪枝
            if (used[i]) {
                continue;
            }
            if (bucketSum + nums[i] > target) {
                // 当前桶装不下 nums[i]
                continue;
            }
            // 做选择
            used[i] = true;
            bucketSum += nums[i];
            // 回溯下一个数字是否可以装入桶中. 注意，下一个数字是 i+1, 而不是 start +1
            if (backTrack(k, bucketSum, nums, i + 1, used, target)) {
                return true;
            }
            used[i] = false;
            trackSum -= nums[i];
        }
        // 穷举了所有数字，都无法装入当前桶中。
        return false;
    }

    HashMap<Integer, Boolean> memo2 = new HashMap<>();

    boolean backTrack2(int k, int bucketSum, int[] nums, int start, int used, int target) {
        // base case
        if (k == 0) {
            return true;
        }
        if (bucketSum == target) { // 当前桶已经装满, 递归下一个
            boolean result = backTrack2(k - 1, 0, nums, 0, used, target);
            // k 的运算结果成功与否取决于 k-1 的运算结果
            memo2.put(used, result);
            return result;
        }
        if (memo2.containsKey(used)) {
            return memo2.get(used);
        }
        for (int i = start; i < nums.length; i++) {
            
            if (((used >> i) & 1) == 1) { // 表示 nums[i] 这个数字被别的桶使用了
                continue;
            }
            if (bucketSum + nums[i] > target) {
                continue;
            }
            // 选择
            bucketSum += nums[i];
            // used |= ((used >> i) ^ 1);
            used |= 1 << i; // 将第 i 位置置为 1.
            // 回溯
            if (backTrack2(k, bucketSum, nums, i + 1, used, target)) {
                return true;
            }
            // 取消选择
            bucketSum -= nums[i];
            used ^= 1 << i; // 使用 异或运算将 i 位置恢复为 0
        }
        return false;
    }

    
}
