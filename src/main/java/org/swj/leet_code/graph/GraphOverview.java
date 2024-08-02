package org.swj.leet_code.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/01 22:55
 */
public class GraphOverview {

    /**
     * 查找所有起点到终点路径 leetcode 797
     * 
     * @param graph
     * @param v
     * @return
     */
    List<List<Integer>> res = new LinkedList<>();

    public List<List<Integer>> allPathToTarget(int[][] graph) {
        LinkedList<Integer> pathNodeList = new LinkedList<>();
        traverse(graph, 0, pathNodeList);
        return res;
    }

    void traverse(int[][] graph, int s, LinkedList<Integer> pathNodeList) {
        // 添加节点 s 到路径
        pathNodeList.addLast(s);
        if (s == graph.length - 1) { // 遍历到尾结点
            res.add(new LinkedList<>(pathNodeList));
        }
        for (int neighbor : graph[s]) {
            traverse(graph, neighbor, pathNodeList);
        }
        // 移除路径中的 s 节点
        pathNodeList.removeLast();
    }

    public static void main(String[] args) {
        int[][] graph = new int[][] {
                new int[] { 1, 2 },
                new int[] { 3 },
                new int[] { 3 },
                new int[] {}
        };
        GraphOverview instance = new GraphOverview();
        for (List<Integer> list : instance.allPathToTarget(graph)) {
            System.out.println(list);
        }
    }

    public GraphOverview() {

    }

    // 名流的图
    boolean[][] celegrityGraph;

    public GraphOverview(int[][] edges) {
        celegrityGraph = new boolean[edges.length][];
        for (int[] edge : edges) {
            // [1,0] 表示从 0 到 1
            int from = edge[0], to = edge[1];
            celegrityGraph[from][to] = true;
        }
    }

    boolean knows(int i, int j) {
        if (i < 0 || i >= celegrityGraph.length - 1 || j < 0 || j >= celegrityGraph[0].length - 1) {
            return false;
        }
        return celegrityGraph[i][j];
    }

    /**
     * 使用暴力方式查找
     * 
     * @param n
     */
    int findCelebrityViolent(int n) {
        for (int cand = 0; cand < n; cand++) {
            int other;
            for (other = 0; other < n; other++) {
                if (cand == other)
                    continue;
                // 保证其他人都认识 cand(candidant) ，并且 cand 不认识其他人
                // 否则 kind 就不可能是名人
                if (knows(cand, other) || !knows(other, cand)) {
                    break;
                }
            }
            // other == n，说明 cand 满足名人的条件，即：跟所有人都不认识，且所有人都认识 cand
            if (other == n) {
                return cand;
            }
        }
        // 没有找到 cand
        return -1;
    }

    /**
     * 查找名人，使用 O(N) 的时间复杂度
     * 
     * @param n
     * @return
     */
    int findCelebrityON(int n) {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            stack.push(i);
        }
        // 弹出两个候选人，进行判断
        while (stack.size() >= 2) {
            int cand = stack.pop();
            int other = stack.pop();
            if (knows(cand, other) || !knows(other, cand)) {
                // cand 不是名人
                stack.push(other);
            } else {
                // other 不是名人，cand 可能是名人
                stack.push(cand);
            }
        }
        // 到这里，栈中只有一个元素了
        int cand = stack.pop();
        // 使用一个 for 循环判断是否是名人
        for (int other = 0; other < n; other++) {
            if (cand == other)
                continue;
            // cand 不是名人
            if (knows(cand, other) || !knows(other, cand)) {
                return -1;
            }
        }
        return cand;
    }

    /**
     * 省去空间复杂度 O(N) 的栈，实现时间复杂度为 O(N) 的算法
     * 
     * @param n
     * @return
     */
    int findCelebritySimplify(int n) {
        // 先假设 0 好为 名人
        int cand = 0;
        for (int other = 1; other < n; other++) {
            // cand 不是候选人，将 other 作为cand
            if (knows(cand, other) || !knows(other, cand)) {
                cand = other;
            } else {
                // other 不是候选人，cand 可能是候选人，则 do nothing。
            }
        }

        // 到这里，cand 可能是候选人，做一遍验证
        for (int other = 0; other < n; other++) {
            if (cand == other)
                continue;
            // cand 不是名人
            if (knows(cand, other) || !knows(other, cand)) {
                return -1;
            }
        }
        return cand;

    }

}
