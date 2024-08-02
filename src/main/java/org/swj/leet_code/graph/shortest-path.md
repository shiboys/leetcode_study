## 最短路径
### 主要说 dijkastra 最短路径

**其实，Dijkstra 可以理解成一个带 dp table（或者说备忘录）的 BFS 算法，伪码如下**：

```java
// 返回节点 from 到节点 to 之间的边的权重
int weight(int from, int to);

// 输入节点 s 返回 s 的相邻节点
List<Integer> adj(int s);

// 输入一幅图和一个起点 start，计算 start 到其他节点的最短距离
int[] dijkstra(int start, List<Integer>[] graph) {
    // 图中节点的个数
    int V = graph.length;
    // 记录最短路径的权重，你可以理解为 dp table
    // 定义：distTo[i] 的值就是节点 start 到达节点 i 的最短路径权重
    int[] distTo = new int[V];
    // 求最小值，所以 dp table 初始化为正无穷
    Arrays.fill(distTo, Integer.MAX_VALUE);
    // base case，start 到 start 的最短距离就是 0
    distTo[start] = 0;

    // 优先级队列，distFromStart 较小的排在前面
    Queue<State> pq = new PriorityQueue<>((a, b) -> {
        return a.distFromStart - b.distFromStart;
    });

    // 从起点 start 开始进行 BFS
    pq.offer(new State(start, 0));

    while (!pq.isEmpty()) {
        State curState = pq.poll();
        int curNodeID = curState.id;
        int curDistFromStart = curState.distFromStart;

        if (curDistFromStart > distTo[curNodeID]) {
            // 已经有一条更短的路径到达 curNode 节点了
            continue;
        }
        // 将 curNode 的相邻节点装入队列
        for (int nextNodeID : adj(curNodeID)) {
            // 看看从 curNode 达到 nextNode 的距离是否会更短
            int distToNextNode = distTo[curNodeID] + weight(curNodeID, nextNodeID);
            if (distTo[nextNodeID] > distToNextNode) {
                // 更新 dp table
                distTo[nextNodeID] = distToNextNode;
                // 将这个节点以及距离放入队列
                pq.offer(new State(nextNodeID, distToNextNode));
            }
        }
    }
    return distTo;
}

```

**相比于普通的 BFS 算法，我们可能会有以以下疑问**

**1、没有 `visited` 集合记录已访问的节点，所以一个节点会被访问多次，会被多次加入队列，那会不会导致队列永远不为空，造成死循环？**

**2、为什么要用优先级队列 `PriorityQueue` 而不是 `LinkedList` 实现普通队列？为什么要按照 `distFromStart` 的值来排序** ？

**3、如果我只想计算起点 `start` 都某一个终点 `end` 的最短路径，是否可以修改算法，提升一些效率**？

我们先来回答第一个问题，为什么这个算法不用 `visited ` 集合也不会死循环？

对于这个问题，我们看看算法的 offer 和 poll 的时机

`while`  循环每执行一次，都会 poll 弹出一个元素，但是如果想往队列里面插入元素，可能有很多限制了，必须满足下面这个条件：
```java
// 看看从 curNode 达到 nextNode 的距离是否会更短
if (distTo[nextNodeID] > distToNextNode) {
    // 更新 dp table
    distTo[nextNodeID] = distToNextNode;
    pq.offer(new State(nextNodeID, distToNextNode));
}
```

这也是为什么 `distanceTo` 数组可以理解为我们熟悉的 dp table, 因为这个算法逻辑就是在不断地最小化 `distanceTo` 数组中 `distanceTo[i]` 的元素值。

如果能使到达 `nextNodeId` 的距离更短，那就更新 `distanceTo[nextNodeId]` 的值，并让 `nextNodeId` 及其当前权重入队，否则的话，不允许入队。

**因为两个节点之间的最短距离(路径权重)肯定是一个确定的值，不可能无限减小下去，所以队列一定会空，队列空了之后，`distanceTo` 数组中记录的就是从 `start` 到其他节点的最短距离**。

接下来回答第二个问题，为什么用 `PriorityQueue` 而不是 `LinkedList` 实现的普通队列？

如果我们非要用普通队列，其实也没问题的，我们可以直接把 PriorityQueue 改为 LinkedList ,也能得到正确的答案，但是效率会低很多。

**Dijkastra 算法使用优先级队列，主要是为了效率上的优化，类似一种贪心算法的思路**。

因为优先级队列自动排序的性质，**每次**从队列里面拿出来的都是 `distFromStart` 值最小，所以当你**第一次**从队列中拿出终点 `end` 时，此时的 `distFromStart` 对应的值就是 `start` 到 `end` 的最短距离。

这个算法较之前的实现提前 return 了，所以效率有一定的提升。

#### 时间复杂度分析

Dijkastra 算法的时间复杂度是多少？网上查询一下，基本都说是 `O(ElogV)`, 其中 `E` 代表图中的边数，`V`代表图中的节点数

因为理想情况下，优先队列中最多装 `V` 个节点， 对优先级队列的操作次数和 `E` 成正比，所以整体的时间复杂度为 `O(ElogV)`。

不过这是理想情况，Dijkastra 算法的代码实现有很多版本，不同的编程语言或者数据结构 API 都会导致算法的时间复杂度发生一些变化。

比如本文实现的 Dijkastra 算法，使用了 Java 的 `PriorityQueue` 这个数据结构，这个容器类底层使用二叉堆实现，但没有提供通过索引操作队列中元素的 API，所以队列中会有重复的节点，最多可能有 E 个节点在队列中。

所以本文实现的 Dijkastra 算法复杂度并不是理想情况下的 `O(ElogV)` ，而是 `O(ElogE)`, 可能会大一些，因为图中的边的条数一般是大于节点的个数的。

