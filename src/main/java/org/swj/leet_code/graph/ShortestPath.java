package org.swj.leet_code.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 最短路径问题
 * 最要是 Dijkastra 最短路径算法及其leetcode应用
 */
public class ShortestPath {

    static class TargetNodeAndTotalDistance {
        int to;
        // 所有到 start 节点 to 的路径和
        int totalSumOfDistanceTo;

        public TargetNodeAndTotalDistance(int to, int distanceTo) {
            this.to = to;
            this.totalSumOfDistanceTo = distanceTo;
        }
    }

    static class Dijkastra {
        private List<int[]>[] graph;
        PriorityQueue<TargetNodeAndTotalDistance> queue;
        // 从始发点到图中所有节点的最短路径权重和
        int[] allDistanceTo;

        private int weight(int from, int to) {
            if (graph == null) {
                return 0;
            }
            for (int[] edge : graph[from]) {
                if (edge[0] == to) {
                    return edge[1];
                }
            }
            return 0;
        }

        public int[] dijkastra(List<int[]>[] graph, int s) {
            return dijkastra(graph, s, null);
        }

        public int[] dijkastra(List<int[]>[] graph, int s, Integer target) {
            queue = new PriorityQueue<>((a, b) -> {
                return a.totalSumOfDistanceTo - b.totalSumOfDistanceTo;
            });
            allDistanceTo = new int[graph.length];
            Arrays.fill(allDistanceTo, Integer.MAX_VALUE);
            allDistanceTo[s] = 0;
            queue.offer(new TargetNodeAndTotalDistance(s, 0));
            while (!queue.isEmpty()) {
                TargetNodeAndTotalDistance ew = queue.poll();
                int currTo = ew.to;
                int totalDistanceTo = ew.totalSumOfDistanceTo;
                // 如果已经有更短的到 currTo 的路径，则 continue
                if (totalDistanceTo > allDistanceTo[currTo]) {
                    continue;
                }
                // 找到目标节点，则返回当前最短路径。为什么找到就能返回？而且一定是返回的最小路径的？
                // 因为大路径的都被上面那个 continue 给拦截了
                if (target != null && ew.to == target) {
                    return allDistanceTo;
                }

                for (int[] te : graph[currTo]) {
                    int nextTo = te[0];
                    int weight = te[1];

                    // 进行 核心的判断
                    int nextWeight = allDistanceTo[currTo] + weight;
                    if (allDistanceTo[nextTo] > nextWeight) {
                        allDistanceTo[nextTo] = nextWeight;
                        queue.offer(new TargetNodeAndTotalDistance(nextTo, nextWeight));
                    }
                }
            }
            return allDistanceTo;
        }
    }

    /**
     * leetcode 743 题
     * 返回遍历完所有节点的最长权重值
     * 
     * @param times 节点之间的延迟时间，可以抽象为边
     * @param n     节点个数
     * @param k     其实节点
     * @return
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        // 题目给的节点编号是 1-n
        List<int[]>[] graph = new LinkedList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new LinkedList<>();
        }
        for (int[] edge : times) {
            int from = edge[0];
            int to = edge[1];
            int weight = edge[2];
            graph[from].add(new int[] { to, weight });
        }
        Dijkastra dijkastra = new Dijkastra();
        int[] distanceTo = dijkastra.dijkastra(graph, k);
        int maxDis = 0;
        // 0 节点不存在
        for (int i = 1; i < distanceTo.length; i++) {
            // 节点没有被遍历到，就没有办法按照题目说的遍历完所有节点
            int dist = distanceTo[i];
            if (dist == Integer.MAX_VALUE) {
                return -1;
            }
            maxDis = Math.max(maxDis, dist);
        }
        return maxDis;
    }

    public static void main(String[] args) {
        // testBasicDijkastra();
        // testMininumEffortPath();
        testMaxProbabilityPath();
    }

    private static void testBasicDijkastra() {
        int[][] times = new int[][] {
                { 2, 1, 1 }, { 2, 3, 1 }, { 3, 4, 1 }
        };
        ShortestPath instance = new ShortestPath();
        System.out.println(instance.networkDelayTime(times, 4, 2));

        times = new int[][] {
                { 1, 2, 1 }
        };

        System.out.println(instance.networkDelayTime(times, 2, 2));

        times = new int[][] {
                { 1, 2, 1 }, { 2, 1, 3 }
        };

        System.out.println(instance.networkDelayTime(times, 2, 2));
    }

    /**
     * leetcode 1631 题
     * 找出二维矩阵从起点(0,0) 到终点 (m-1,n-1) 的路径上的最大绝对值的最小值。
     * 也是使用 Dijkastra 解法，只是将 dj 解法从最短路径和转化为最大绝对值差
     * 上一个是求累加和的最小值，本题是求最大绝对值的最小值
     */
    static class DijkastraMatrix {
        // 辅助数组，用来记录 从 startX,startY 到 currX,currY 的路径上的最大能量消耗(最大绝对值)
        // 非常类似动态规划
        int[][] maxEfforTo;
        Queue<MaxNodeAndEffort> queue;
        int m, n;

        public int minimumEfforPath(int[][] heights) {
            queue = new PriorityQueue<>((a, b) -> {
                return a.maxEffortFromStart - b.maxEffortFromStart;
            });
            // queue = new LinkedList<>();
            // 这道题用不用优先级队列都被测试用例3 证实不可以
            // 优先级队列总是把最小的给及时 poll 出来，满足相邻节点的差值的绝对值最小
            // 普通队里真的是广度优先了。
            m = heights.length;
            n = heights[0].length;
            maxEfforTo = new int[m][n];
            for (int i = 0; i < m; i++) {
                // 默认从 起始点到当前节点的最大能量消耗为 int 最大值
                Arrays.fill(maxEfforTo[i], Integer.MAX_VALUE);
            }
            // 从起点开始进行 BFS
            queue.offer(new MaxNodeAndEffort(0, 0, 0));
            maxEfforTo[0][0] = 0;

            while (!queue.isEmpty()) {
                MaxNodeAndEffort currNode = queue.poll();
                int currMaxEffort = currNode.maxEffortFromStart;
                int nodeX = currNode.nodeX;
                int nodeY = currNode.nodeY;
                // 找到了终点
                if (nodeX == m - 1 && nodeY == n - 1) {
                    return currMaxEffort;
                }
                // 已经有别的节点到 xy 节点的最小 effort，进队列了(因为maxEfforTo[i] 的设置是在进队列之前)，当前的就可以忽略
                // 如果上面的逻辑是 return maxEfforTo[nodeX][nodeY]，则不需要下面这个逻辑，因为 (x,y) 节点被弹出的时候
                // 说明 maxEfforTo[x][y] 已经是最小的，因为别的路径将它修改为最小 effort。

                // 经过测试发现，这里的 currMaxEffort 没有 != maxEfforTo[nodeX][nodeY], 这是因为
                // 优先级队列弹出的 x,y 节点的最小 effor 总是 maxEfforTo[nodeX][nodeY],
                // 而如果是 普通队列，则需要加此判断，因为普通队列是按照先进先出的顺序，陷进去的同一个位置元素的权重，可能被后进去的同一个位置的更小的权重替代
                // 因此需要增加次判断，用来防止无效的的节点进入队列
                // if (currMaxEffort != maxEfforTo[nodeX][nodeY]) {
                // System.out.println("maxEffor=" + currMaxEffort + ", pathTo=" +
                // maxEfforTo[nodeX][nodeY] + ",x="
                // + nodeX + ",y=" + nodeY);
                // }
                // if (currMaxEffort > maxEfforTo[nodeX][nodeY]) {
                // continue;
                // }

                for (NodePos pos : adj(currNode.nodeX, currNode.nodeY)) {
                    int nextX = pos.nodeX;
                    int nextY = pos.nodeY;
                    // 取所有到当前节点的 最大 effort
                    int maxNextNodeErrort = Math.max(maxEfforTo[nodeX][nodeY],
                            Math.abs(heights[nextX][nextY] - heights[currNode.nodeX][currNode.nodeY]));

                    if (maxEfforTo[nextX][nextY] > maxNextNodeErrort) {
                        // 将到 nextX,nextY 的最小 effort 的节点放入优先级队列，这样就达到了路径上取最大值
                        maxEfforTo[nextX][nextY] = maxNextNodeErrort;
                        queue.offer(new MaxNodeAndEffort(nextX, nextY, maxNextNodeErrort));
                    }
                }
            }

            return -1;
        }

        List<NodePos> adj(int currX, int currY) {
            List<NodePos> posList = new ArrayList<>();
            for (int[] pos : POS_MOVE) {
                int posX = currX + pos[0];
                int posY = currY + pos[1];
                if (posX < 0 || posX >= m || posY < 0 || posY >= n) {
                    continue;
                }
                posList.add(new NodePos(posX, posY));
            }
            return posList;
        }

        static final int[][] POS_MOVE = new int[][] {
                { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, -1 }
        };
    }

    static class NodePos {
        public int nodeX;
        public int nodeY;

        public NodePos(int nodeX, int nodeY) {
            this.nodeX = nodeX;
            this.nodeY = nodeY;
        }
    }

    static class MaxNodeAndEffort extends NodePos {

        // 从起始点到前节点的最大能量消耗值
        public int maxEffortFromStart;

        public MaxNodeAndEffort(int nodeX, int nodeY, int maxEffortFromStart) {
            super(nodeX, nodeY);
            this.maxEffortFromStart = maxEffortFromStart;
        }
    }

    static void testMininumEffortPath() {
        DijkastraMatrix instance = new DijkastraMatrix();

        int[][] heights = new int[][] { { 1, 2, 2 }, { 3, 8, 2 }, { 5, 3, 5 } };
        System.out.println(instance.minimumEfforPath(heights));

        heights = new int[][] { { 1, 2, 3 }, { 3, 8, 4 }, { 5, 3, 5 } };
        System.out.println(instance.minimumEfforPath(heights));

        heights = new int[][] {
                { 1, 2, 1, 1, 1 },
                { 1, 2, 1, 2, 1 },
                { 1, 2, 1, 2, 1 },
                { 1, 2, 1, 2, 1 },
                { 1, 1, 1, 2, 1 }
        };
        System.out.println(instance.minimumEfforPath(heights));

        heights = new int[][] { { 1, 2 }, { 3, 8 } };
        System.out.println(instance.minimumEfforPath(heights));
    }

    /**
     * 最大概率路径
     * leetcode 1514 题
     * 给你一个由 n 个节点（下标从 0 开始）组成的无向加权图，该图由一个描述边的列表组成，
     * 其中 edges[i] = [a, b] 表示连接节点 a 和 b 的一条无向边，且该边遍历成功的概率为 succProb[i] 。
     * 指定两个节点分别作为起点 start 和终点 end ，请你找出从起点到终点成功概率最大的路径，并返回其成功概率。
     * 如果不存在从 start 到 end 的路径，请 返回 0 。
     * 
     * 这道题跟上面的路径的最大绝对值的最小值是同一类型。上面求最小值，这里求最大值
     */
    static class MaxProbabilityPath {
        public double maxProbability(int n, int[][] edges, double[] succProb, int startNode, int endNode) {
            List<double[]>[] graph = buildGraph(n, edges, succProb);
            // 大顶堆，大的值在上面。
            Queue<double[]> queue = new PriorityQueue<>((a, b) -> {
                return b[1] > a[1] ? 1 : -1;
            });
            double[] distanceTo = new double[n];
            // distanceTo 数组初始化为 -1，因为概率不能可能为负值
            Arrays.fill(distanceTo, -1);
            queue.offer(new double[] { startNode, 0 });
            distanceTo[startNode] = 0;
            while (!queue.isEmpty()) {
                double[] edgeToWeight = queue.poll();
                int to = (int) edgeToWeight[0];
                if (to == endNode) {
                    return distanceTo[to];
                }
                double weight = edgeToWeight[1];

                for (double[] edge : graph[to]) {
                    int nextNode = (int) edge[0];
                    double nextMaxWeight = edge[1];
                    if(to != startNode) {
                        nextMaxWeight *= weight;
                    }
                    if (distanceTo[nextNode] < nextMaxWeight) {
                        distanceTo[nextNode] = nextMaxWeight;
                        queue.offer(new double[] { nextNode, nextMaxWeight });
                    }
                }
            }
            return 0;
        }

        List<double[]>[] buildGraph(int n, int[][] edges, double[] succProb) {
            List<double[]>[] graph = new List[n];
            for (int i = 0; i < n; i++) {
                graph[i] = new LinkedList<>();
            }
            for (int i = 0; i < edges.length; i++) {
                int[] edge = edges[i];
                int from = edge[0];
                int to = edge[1];
                double weight = succProb[i];
                // 无向边就是双向边。
                graph[from].add(new double[] { to, weight });
                graph[to].add(new double[] { from, weight });
            }
            return graph;
        }
    }

    static void testMaxProbabilityPath() {
        int n = 3;
        int[][] edges = new int[][] { { 0, 1 }, { 1, 2 }, { 0, 2 } };
        double[] succProb = new double[] { 0.5, 0.5, 0.2 };
        int startNode = 0;
        int endNode = 2;
        MaxProbabilityPath instance = new MaxProbabilityPath();
        System.out.println(instance.maxProbability(n, edges, succProb, startNode, endNode));

        succProb = new double[] { 0.5, 0.5, 0.3 };
        System.out.println(instance.maxProbability(n, edges, succProb, startNode, endNode));

        edges = new int[][] { { 0, 1 }};
        succProb = new double[] { 0.5};

        System.out.println(instance.maxProbability(n, edges, succProb, startNode, endNode));
    }

}
