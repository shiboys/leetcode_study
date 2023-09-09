package org.swj.leet_code.graph;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/05 21:25
 *        Prim 算法的最小生成树
 */
public class Prim {
    // 表示 i 节点是否已经在最小生成树中，如果已经在，再次将该节点加入树中会导致树形成环。
    private boolean[] inMST;

    // 邻接表表示的图，一条边使用数组来表示 ,三元组 [from, to, weight]
    // graph[s] 表示以 s 为 起点的所有边
    List<int[]>[] graph;
    Queue<int[]> queue;
    int totalWeight;

    public Prim(List<int[]>[] graph) {
        inMST = new boolean[graph.length];
        this.graph = graph;
        queue = new PriorityQueue<>((a, b) -> {
            return a[2] - b[2];
        });
        // 不知道从哪里开始切分，就从 0 开始切分
        inMST[0] = true;
        cut(0);
        while (!queue.isEmpty()) {
            int[] edge = queue.poll();
            int to = edge[1];
            if (inMST[to]) {
                continue;
            }

            int weight = edge[2];
            totalWeight += weight;
            // 加点 to 加入后，进行新一轮的切分，会产生更多的横切边
            cut(to);
        }
    }

    private void cut(int s) {
        // queue.offer(i);
        for (int[] edge : graph[s]) {
            int to = edge[1];
            // 当前节点 s 的相邻节点 to 已经在最小生成树中，
            // 再次加入这条边会导致树成环
            if (!inMST[to]) {
                // 加入这条横切边
                queue.offer(edge);
            }
        }
    }

    public int totalWeight() {
        return totalWeight;
    }

    public boolean allConnected() {
        for (int i = 0; i < inMST.length; i++) {
            // 只要有一个节点还没进入最小生成树，就表示树没有连接所有节点。
            if (!inMST[i]) {
                return false;
            }
        }
        return true;
    }

}
