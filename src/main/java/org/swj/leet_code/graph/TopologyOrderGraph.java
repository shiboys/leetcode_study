package org.swj.leet_code.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 图的拓扑排序以及有环无环检测
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/02 16:25
 */
public class TopologyOrderGraph {

    List<Integer>[] buildGraph(int numCourses, int[][] prerequisites) {
        // 有 numCourses 门课程，就有 numCourses 个节点
        List<Integer>[] graph = new LinkedList[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new LinkedList<>();
        }

        for (int[] courses : prerequisites) {
            // prerequisites 的形式 [1,0] 表示要想修课程1 必须先修课程0，也就是说选修课程的顺序必须是 从 0 到 1，
            // 我们这里建一条从 from 到 to 的边
            int from = courses[1], to = courses[0];
            // 将 to 加到 from 的目标节点集合中，形成一条边
            graph[from].add(to);
        }
        return graph;
    }

    boolean hasCycle = false;
    boolean[] visited;
    boolean[] onPath;
    Integer cycleNode = null;

    void traverse(List<Integer>[] graph, int s) {
        if (onPath[s]) {
            hasCycle = true;
        }
        // 如果发现有环，或者已经遍历过的节点，则返回
        if (hasCycle || visited[s]) {
            return;
        }
        // 前序遍历
        visited[s] = true;
        onPath[s] = true;
        // 深度优先遍历 DFS
        for (int to : graph[s]) {
            traverse(graph, to);
        }
        // 后续位置，退出 s 节点
        onPath[s] = false;
    }

    /**
     * leetcode 207 是否能完成指定的课程，通过是否成环判断
     * 
     * @param numCourses
     * @param prerequisites
     * @return
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 先构造图
        List<Integer>[] graph = buildGraph(numCourses, prerequisites);
        visited = new boolean[numCourses];
        onPath = new boolean[numCourses];

        // 对每一个节点开始 DFS 遍历，因为有些节点是独立的，跟其他节点不相连
        for (int i = 0; i < numCourses; i++) {
            traverse(graph, i);
        }
        return !hasCycle;
    }

    /**
     * leetcode 210 题，查找续完所有课程的所要学习的课程路径
     * 
     * @param numCourses
     * @param prerequisites
     * @return
     */
    int[] findOrder(int numCourses, int[][] prerequisites) {
        visited = new boolean[numCourses];
        onPath = new boolean[numCourses];
        List<Integer>[] graph = buildGraph(numCourses, prerequisites);

        // 对每一个节点进行遍历
        for (int i = 0; i < numCourses; i++) {
            traverseWithPostOrder(graph, i);
        }

        if (hasCycle) {
            return new int[] {};
        }
        // 后续遍历的结果就是拓扑排序的结果
        Collections.reverse(postOrder);

        return postOrder.stream().mapToInt(x -> x.intValue()).toArray();
    }

    // 记录后续遍历结果
    List<Integer> postOrder = new ArrayList<>();

    void traverseWithPostOrder(List<Integer>[] graph, int s) {
        if (onPath[s]) {
            hasCycle = true;
        }
        if (visited[s] || hasCycle) {
            return;
        }
        visited[s] = true;
        onPath[s] = true;
        for (int to : graph[s]) {
            traverseWithPostOrder(graph, to);
        }
        // 后序位置
        postOrder.add(s);
        onPath[s] = false;
    }

    /**
     * 使用 BFS 算法来实现是否可以修完完成指定的课程。BFS 为什么可以通过 count == numCourses 来判断课程修完，请参考
     * topology.md 文档有关 BFS 实现是否有环检测的章节。
     * 
     * @param numCourses
     * @param prerequisites
     * @return
     */
    public boolean canFinishBFS(int numCourses, int[][] prerequisites) {
        // 使用 BFS ，就不需要借助 visited,onPath 辅助数组了
        List<Integer>[] graph = buildGraph(numCourses, prerequisites);
        // 需要借助入度 indegree 数组
        int[] indegree = buildIndegree(numCourses, prerequisites);
        int count = 0;
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < indegree.length; i++) {
            // 入度为 0 的元素，最先加入队列，作为遍历的起始节点
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            // 记录 BFS 遍历路径的节点
            if (bfsCoursePath != null && bfsCoursePath.length > count) {
                bfsCoursePath[count] = cur;
            }
            // 元素个数增加
            count++;
            // 以 cur 作为起点的所有边的入度都要减一。
            for (int to : graph[cur]) {
                indegree[to]--;
                // 如果当前 to 节点的入度变为 0，则当前 to 节点需要重新进入队列
                if (indegree[to] == 0) {
                    queue.offer(to);
                }
            }
        }
        // count == numCourses 的时候，就表示图是无环的，课程所在的图是无环的图，那么久可以完成所有的课程。
        // 至于为啥 count == numCourses ，就表示无环，具体原因请参考 topology.md 文档有关 BFS 遍历无环图部分。
        return count == numCourses;
    }

    int[] buildIndegree(int numCourses, int[][] prerequisites) {
        int[] indegree = new int[numCourses];
        for (int[] courses : prerequisites) {
            // [1,0]
            int from = courses[1], to = courses[0];
            indegree[to]++;
        }
        return indegree;
    }

    int[] bfsCoursePath;

    /**
     * 使用 BFS 遍历方式来实现 课程所学路径
     * 
     * @param numCourses
     * @param prerequisites
     */
    int[] findOrderBfs(int numCourses, int[][] prerequisites) {
        bfsCoursePath = new int[numCourses];
        boolean canFinish = canFinishBFS(numCourses, prerequisites);
        if (canFinish) { // 如果无环，则返回 bfs 的遍历路径的节点
            return bfsCoursePath;
        }
        // 返回一个空数组
        return new int[] {};
    }

    /**
     * 如果存在环，则返回环中的节点，否则返回空数组
     * 
     * @param numCourses
     * @param prerequisites
     * @return
     */
    int[] findCycleNodes(int numCourses, int[][] prerequisites) {
        boolean canFinish = canFinishWithCycleNodes(numCourses, prerequisites);
        if (canFinish || cycleNode == null) { // 没有环或者没有找到环的入口点
            return new int[] {};
        }
        // 路径中的所有节点并非环中的所有节点，取环中的所有节点
        int cycleStartIndex = -1;
        for (int i=0;i < cyclePathNodeList.size();i++) {
            // 都是在路径上的，且当前节点 i 为环的入口
            if (cycleNode == cyclePathNodeList.get(i)) {
                cycleStartIndex = i; // 首次找到入口
                break;
            }
        }
        if (cycleStartIndex == -1) {
            return new int[] {};
        }
        // 最后一个节点就是最后的那个重复的入口点
        return cyclePathNodeList.subList(cycleStartIndex, cyclePathNodeList.size() - 1).stream()
                .mapToInt(x -> x.intValue()).toArray();
    }

    List<Integer> cyclePathNodeList = new ArrayList<>();

    boolean canFinishWithCycleNodes(int numCourses, int[][] prerequisites) {
        onPath = new boolean[numCourses];
        visited = new boolean[numCourses];
        List<Integer>[] graph = buildGraph(numCourses, prerequisites);
        for (int i = 0; i < numCourses; i++) {
            traverseWithCycle(graph, i);
        }
        return !hasCycle;
    }

    void traverseWithCycle(List<Integer>[] graph, int s) {
        if (onPath[s]) {
            hasCycle = true;
            // 也可以在这个地方收集所有的 onPath 为 true 的节点
            cycleNode = s;
            cyclePathNodeList.add(s);
        }
        if (visited[s] || hasCycle) {
            return;
        }
        visited[s] = true;
        onPath[s] = true;
        cyclePathNodeList.add(s);
        for (int to : graph[s]) {
            traverseWithCycle(graph, to);
        }
        // 后序位置
        onPath[s] = false;
    }

    public static void main(String[] args) {
        int[][] prerequisites = new int[][] {
                new int[] { 1, 0 }
        };
        TopologyOrderGraph instance = new TopologyOrderGraph();

        // System.out.println(instance.canFinish(2, prerequisites));
        System.out.println(instance.canFinishBFS(2, prerequisites));

        prerequisites = new int[][] {
                new int[] { 1, 0 },
                new int[] { 2, 0 },
                new int[] { 3, 1 },
                new int[] { 3, 2 }
        };

        // System.out.println(Arrays.toString(instance.findOrder(4, prerequisites)));

        System.out.println(Arrays.toString(instance.findOrderBfs(4, prerequisites)));

        prerequisites = new int[][] {
                new int[] { 1, 0 }
        };

        System.out.println("leetcode use case");
        System.out.println(Arrays.toString(instance.findOrderBfs(2, prerequisites)));

        prerequisites = new int[][] {
                new int[] { 11, 10 },
                new int[] { 12, 11 },
                new int[] { 13, 12 },
                new int[] { 11, 13 },
        };
        System.out.println(Arrays.toString(instance.findCycleNodes(4, prerequisites)));
    }

}
