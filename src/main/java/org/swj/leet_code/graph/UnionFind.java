package org.swj.leet_code.graph;

import java.util.Arrays;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/04 18:55
 *        UinonFind 算法
 *        并集查找算法
 */
public class UnionFind {

    static class UF {
        int[] parent;
        int count;

        public UF(int n) {
            parent = new int[n];
            count = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) {// 已经联通了，不需要再 union
                return;
            }
            // 两个联通分量合并成一个
            parent[rootQ] = rootP;
            count--;
        }

        public boolean connected(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            return rootP == rootQ;
        }

        // 带路径压缩的查找，将查找路径上的每一个 parent 都连通到 root
        int find(int x) {
            if (x != parent[x]) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public int count() {
            return count;
        }
    }

    public int countComponent(int n, int[][] edges) {
        UF uf = new UF(n);
        for (int[] edge : edges) {
            uf.union(edge[0], edge[1]);
        }
        // 返回连通分量个数。
        return uf.count();
    }

    /**
     * leetcode 130 题，将二维矩阵中被 X 包围的 O 替换成 X，边上的 O 不能被替换
     * 使用 Union-Find 求解
     * 
     * @param board
     */
    public void solve(char[][] board) {
        int m = board.length;
        int n = board[0].length;
        UF uf = new UF(m * n + 1);
        int dummy = m * n;

        // 将 首行尾行，首列尾列的 O 跟 dummy 相连通
        // 先联通 首列 和 尾列的 O
        // board(x,y) = x*n + y
        for (int i = 0; i < m; i++) {
            if (board[i][0] == 'O') {
                uf.union(dummy, i * n);
            }
            if (board[i][n - 1] == 'O') {
                uf.union(dummy, i * n + n - 1);
            }
        }

        // 再将首行和尾行的 O 跟 dummy 相连通
        for (int j = 0; j < n; j++) {
            if (board[0][j] == 'O') {
                uf.union(dummy, j);
            }
            if (board[m - 1][j] == 'O') {
                uf.union(dummy, (m - 1) * n + j);
            }
        }
        // 方向数组 directions 是上下左右搜索的常用手法
        int[][] directions = new int[][] { { 1, 0 }, { 0, 1 }, { 0, -1 }, { -1, 0 } };
        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (board[i][j] == 'O') {
                    // 将当前的 `O` 与周围的 'O' 进行连通
                    for (int[] ds : directions) {
                        int x = i + ds[0];
                        int y = j + ds[1];
                        if (board[x][y] == 'O') { // 之前这个判断丢了，就逻辑错误了
                            uf.union(i * n + j, x * n + y);
                        }
                    }
                }
            }
        }

        // 此时遍历 board ，所有没有跟 dummy 相连的，都可以被替换掉
        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                if (board[i][j] == 'O' && !uf.connected(dummy, i * n + j)) {
                    board[i][j] = 'X';
                }
            }
        }
    }

    /**
     * leetcode 990 等式方程的可满足性
     * 
     * @param equations
     * @return
     */
    public boolean equationsPossible(String[] equations) {
        /**
         * 1 <= equations.length <= 500
         * equations[i].length == 4
         * equations[i][0] 和 equations[i][3] 是小写字母
         * equations[i][1] 要么是 '='，要么是 '!'
         * equations[i][2] 是 '='
         */
        // 26 个字母字符
        UF uf = new UF(26);

        for (String e : equations) {
            // 先将所有 = 的连通在一起，形成最大的连通树
            if (e.charAt(1) == '=') {
                int x = e.charAt(0) - 'a';
                int y = e.charAt(3) - 'a';
                uf.union(x, y);
            }
        }
        // 再判断所有不相等的
        for (String e : equations) {
            if (e.charAt(1) == '!') {
                int x = e.charAt(0) - 'a';
                int y = e.charAt(3) - 'a';
                if (uf.connected(x, y)) { // uf 判断是相连的，但是当前的条件 e 给出的是不相连，因此这里是不满足条件的
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validTree(int n, int[][] edges) {
        UF uf = new UF(n);
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            // 如果两个节点已经在同一个连通分量里面，则会产生环
            if (uf.connected(v, u)) {
                return false;
            }
            uf.union(v, u);
        }
        // 最后保证只形成了一棵树，只有一个连通分量。
        return uf.count() == 1;
    }

    public static void main(String[] args) {
        String[] equations = new String[] {
                "a==b", "b!=c", "c==a"
        };
        UnionFind instance = new UnionFind();
        System.out.println(instance.equationsPossible(equations));

        equations = new String[] { "a==b", "b==c", "a==c" };
        System.out.println(instance.equationsPossible(equations));

        equations = new String[] { "c==c", "b==d", "x!=z" };
        System.out.println(instance.equationsPossible(equations));

        char[][] board = new char[][] {
                new char[] { 'X', 'X', 'X', 'X' },
                { 'X', 'O', 'O', 'X' },
                { 'X', 'X', 'O', 'X' },
                { 'X', 'O', 'X', 'X' }
        };

        instance.solve(board);
        for (char[] item : board) {
            System.out.println(Arrays.toString(item));
        }

        board = new char[][] {
                { 'X', 'O', 'X', 'O', 'X', 'O', 'O', 'O', 'X', 'O' },
                { 'X', 'O', 'O', 'X', 'X', 'X', 'O', 'O', 'O', 'X' },
                { 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'X', 'X' },
                { 'O', 'O', 'O', 'O', 'O', 'O', 'X', 'O', 'O', 'X' },
                { 'O', 'O', 'X', 'X', 'O', 'X', 'X', 'O', 'O', 'O' },
                { 'X', 'O', 'O', 'X', 'X', 'X', 'O', 'X', 'X', 'O' },
                { 'X', 'O', 'X', 'O', 'O', 'X', 'X', 'O', 'X', 'O' },
                { 'X', 'X', 'O', 'X', 'X', 'O', 'X', 'O', 'O', 'X' },
                { 'O', 'O', 'O', 'O', 'X', 'O', 'X', 'O', 'X', 'O' },
                { 'X', 'X', 'O', 'X', 'X', 'X', 'X', 'O', 'O', 'O' }
        };
        instance.solve(board);
        for (char[] item : board) {
            System.out.println(Arrays.toString(item));
        }
    }

}
