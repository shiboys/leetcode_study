package org.swj.leet_code.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/04 14:55
 *        二分图.
 *        有关二分图的相关描述请参考 bipartite.md
 */
public class Bipartite {

    boolean isBipartite = true;
    boolean[] visited;
    boolean[] color;

    /**
     * 判断是否二分图，leetcode 785 题
     * 
     * @param graph
     * @return
     */
    public boolean isBipartite(int[][] graph) {
        int n = graph.length;
        visited = new boolean[n];
        color = new boolean[n];
        // 遍历所有节点，因为有些节点可能独立于图中没有连同
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                traverseBipartite(graph, i);
            }
        }
        return isBipartite;
    }

    void traverseBipartite(int[][] graph, int s) {
        // 如果已经判断出来不是二分图，则返回，结束遍历
        if (!isBipartite)
            return;

        visited[s] = true;
        for (int to : graph[s]) {
            if (!visited[to]) { // 跟 s 相邻的 to 节点没有访问过
                color[to] = !color[s]; // to 的颜色跟 s 相反
                traverseBipartite(graph, to);
            } else {
                // 相邻的 to 节点访问过，判断颜色是否一致
                if (color[s] == color[to]) {
                    isBipartite = false;
                    return;
                }
            }
        }

    }

    public boolean isBipartiteBFS(int[][] graph) {
        Queue<Integer> queue = new LinkedList<>();
        int n = graph.length;
        visited = new boolean[n];
        color = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                queue.offer(i);
                traverseBipartiteBFS(graph, queue);
            }
        }
        return isBipartite;
    }

    void traverseBipartiteBFS(int[][] graph, Queue<Integer> queue) {
        while (!queue.isEmpty()) {
            if (!isBipartite) { // 不是二分图，return
                return;
            }
            int cur = queue.poll();
            visited[cur] = true;
            for (int to : graph[cur]) {
                if (!visited[to]) {
                    color[to] = !color[cur];
                    queue.offer(to);
                } else {
                    if (color[cur] == color[to]) {
                        isBipartite = false;
                        return;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] graph = new int[][] {
                { 1, 2, 3 },
                { 0, 2 },
                { 0, 1, 3 },
                { 0, 2 }
        };

        Bipartite instance = new Bipartite();
        System.out.println(instance.isBipartite(graph));
        graph = new int[][] { { 1, 3 }, { 0, 2 }, { 1, 3 }, { 0, 2 } };

        instance = new Bipartite();
        System.out.println(instance.isBipartiteBFS(graph));

        instance = new Bipartite();
        int[][] dislikes = new int[][] { { 1, 2 }, { 1, 3 }, { 2, 4 } };
        System.out.println(instance.possibleBipartition(4, dislikes));

        instance = new Bipartite();
        dislikes = new int[][] { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 1, 5 } };
        System.out.println(instance.possibleBipartition(5, dislikes));

    }

    boolean possibleBipartition(int n, int[][] dislikes) {
        visited = new boolean[n + 1];
        color = new boolean[n + 1];
        List<Integer>[] graph = buildGraph(n, dislikes);
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                traversePossibleBipartition(graph, i);
            }
        }
        return isBipartite;
    }

    void traversePossibleBipartition(List<Integer>[] graph, int s) {
        if (!isBipartite) {
            return;
        }
        if (visited[s]) {
            return;
        }
        visited[s] = true;
        for (int to : graph[s]) {
            if (!visited[to]) {
                color[to] = !color[s];
                traversePossibleBipartition(graph, to);
            } else {
                if (color[s] == color[to]) { // 相邻节点的颜色一样，就不是二分图
                    isBipartite = false;
                    return;
                }
            }
        }
    }

    List<Integer>[] buildGraph(int n, int[][] dislikes) {
        List<Integer>[] graph = new List[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new LinkedList<>();
        }
        for (int[] edge : dislikes) {
            int v = edge[0];
            int w = edge[1];
            // 无向边就是双向边
            graph[v].add(w);
            graph[w].add(v);
        }
        return graph;
    }

}
