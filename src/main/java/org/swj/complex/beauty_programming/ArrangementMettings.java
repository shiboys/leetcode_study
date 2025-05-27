package org.swj.complex.beauty_programming;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 高效安排见面会
 * 编程之美 1.9，提示使用最少着色方式解决。我查了不少资料，
 * 发现可以使用 dfs+回溯的方式解决，
 * 至于使用贪心算法，后面再研究
 * 题目给的思路是，把每个小组看成是一些散步的点。如果有一位同学同时对两个小组感兴趣，就在这两个小组对应的两个节点
 * 加上一条边。比如 A 同学希望参加 1、2、3 三个小组的见面会，那么就在 1、2、3 对应的节点之间加上一条边。
 * B 同学希望参加 1、3、4 三个小组的见面会，那么就在 1、3、4 对应的节点之间加上一条边。
 * 
 * 
 */
public class ArrangementMettings {

    final List<List<Integer>> graph;
    // 不同颜色数组
    final int[] colors;
    // 节点个数
    final int N;
    int minColors = Integer.MAX_VALUE;
    int pathColors = 0;

    public ArrangementMettings(int[][] edges) {
        this.graph = new ArrayList<>();
        // 第一个节点不用
        for (int i = 0; i < edges.length; i++) {
            graph.add(new ArrayList<>());
            for (int j = 0; j < edges[i].length; j++) {
                graph.get(i).add(edges[i][j]);
            }
        }
        // 节点个数
        N = edges.length - 1;
        this.colors = new int[graph.size()];
    }

    /**
     * 检查 x 节点的颜色是否可以为 color
     * 
     * @param x
     * @param color
     * @return
     */
    public boolean checkColor(int x, int color) {
        for (int i = 0; i < x; i++) {
            // 如果 x 与 i 有边，并且 i 的颜色和 x 的颜色相同
            if (graph.get(i).contains(x) && colors[i] == color) {
                return false;
            }
        }
        return true;
    }

    /**
     * 遍历 i 节点的所有边(出度)
     * 
     * @param i
     */
    public void dfs(int i) {
        if (i >= N) {
            if (minColors > pathColors) {
                minColors = pathColors;
            }
            return;
        }
        boolean found = false;
        // 尝试所有的颜色
        for (int j = 0; j < pathColors; j++) {
            if (checkColor(i, colors[j])) {
                found = true;
                int sum1 = pathColors;
                // i 位置可以防止 colors[j] ,则放置
                colors[i] = colors[j];
                dfs(i + 1);
                // 回溯
                pathColors = sum1;
            }
        }
        // 如果 i 位置没有找到合适的颜色，则增加一个颜色
        if (!found) {
            pathColors++;
            colors[i] = pathColors;
            // 求下一个节点的颜色
            dfs(i + 1);
            // pathColors--;
        }
    }

    public static void main(String[] args) {
        int[][] graph = new int[][] {
                {},
                { 2, 3, 4 },
                { 1, 3 },
                { 1, 2, 4 },
                { 1, 3 }
        };

        // int[][] graph = new int[][] {
        // {},
        // { 2, 3 },
        // { 1 },
        // { 1 }
        // };
        ArrangementMettings mettings = new ArrangementMettings(graph);
        mettings.dfs(0);
        System.out.println(mettings.minColors);

    }

}