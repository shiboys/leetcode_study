package org.swj.leet_code.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/05 18:25
 *        Kruskal 算法的最小生成树
 * 
 */
public class Kruskal {

    /**
     * leetcode 第 1135 题，连通所有城市的最小成本(plus)
     * 使用 Kruskal 最小生成树的解法
     * Kruskal 用连通分量来解决最小生成树的问题，使用了连通分量，边就不要考虑邻接的问题，
     * 也就是不需要使用 adj(from) 或者 graph[from] 的问题，直接判断所有的边全部使用连通分量连接
     * 在一起+排除重复连通的边，就可以得到最小生成树
     * @param n
     * @param connections
     * @return
     */
    public int mimimunCost(int n, int[][] connections) {
        // 因为题中给的连通节点起始索引为 1。
        UnionFind.UF uf = new UnionFind.UF(n + 1);
        // 首先将 connections 按照权重进行从小到大排序
        Arrays.sort(connections, (a, b) -> {
            return a[2] - b[2];
        });
        int totalWeight = 0;
        for (int[] cn : connections) {
            int v = cn[0];
            int w = cn[1];
            int weight = cn[2];
            // 如果 这条边的两个节点已经在同一个连通分量上，则不需要再次连通，防止出现环
            if (uf.connected(w, v)) {
                continue;
            }
            uf.union(w, v);
            // 增加权重
            totalWeight += weight;
        }

        // 保证所有节点都以连通，
        // 理论上 uf.count() == 1 说明所有节点都被连通
        // 但是因为 0 位置没有使用，会占用一个连通位置，因此连通分量最少为 2
        return uf.count() == 2 ? totalWeight : -1;
    }

    public int minCostConnectPoints(int[][] points) {
        List<int[]> edges = new ArrayList();
        int m = points.length;
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                // 题目要求用绝对值作为权重，
                int weight = Math.abs(points[j][0] - points[i][0]) + Math.abs(points[j][1] - points[i][1]);
                // 用节点的在数组中的索引作为 UF 图中的连通节点
                edges.add(new int[] { i, j, weight });
            }
        }
        // 权重排序
        Collections.sort(edges, (a, b) -> {
            return a[2] - b[2];
        });

        // 执行 Kruskal 算法
        UF uf = new UF(m);
        int mst = 0;
        for (int[] edge : edges) {
            int v = edge[0];
            int u = edge[1];
            int weight = edge[2];
            if (uf.connected(u, v)) {
                continue;
            }
            uf.union(u, v);
            mst += weight;
        }

        return uf.count() == 1 ? mst : -1;
    }

    class UF {
        private int count;
        private int[] parent;

        public UF(int n) {
            this.count = n;
            this.parent = new int[n];
            for (int i = 0; i < n; i++) {
                this.parent[i] = i;
            }
        }

        public void union(int p, int q) {
            int rootP = findParent(p);
            int rootQ = findParent(q);

            // 这里有一个很重要的判断
            if (rootP == rootQ) { // 已经联通了，不需要再联通
                return;
            }
            System.out.println("rootP= " + rootP + ", rootQ=" + rootQ);
            parent[rootQ] = rootP;
            // 连通分量 --
            this.count--;
        }

        public boolean connected(int p, int q) {
            int rootP = findParent(p);
            int rootQ = findParent(q);
            return rootP == rootQ;
        }

        /**
         * 将查询路径上的所有父节点打平
         */
        public int findParent(int x) {
            // 记住，这里不是 while 而是 if
            if (x != parent[x]) {
                parent[x] = findParent(parent[x]);
            }
            return parent[x];
        }

        public int count() {
            return count;
        }
    }

    public static void main(String[] args) {
        int[][] distances = new int[][] {
                { 0, 0 }, { 2, 2 }, { 3, 10 }, { 5, 2 }, { 7, 0 }
        };
        Kruskal instance = new Kruskal();
        System.out.println("min weight :" + instance.minCostConnectPoints(distances));
    }
}
