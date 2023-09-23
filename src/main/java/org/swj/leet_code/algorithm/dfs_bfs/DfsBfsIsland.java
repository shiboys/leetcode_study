package org.swj.leet_code.algorithm.dfs_bfs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DfsBfs 中的遍历
 * 使用 DFS 遍历二维数组，解决岛屿相关问题。
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/19 11:29
 */
public class DfsBfsIsland {
    private static int LAND = 0;
    private static int WATER = 1;

    boolean[] visited;

    /**
     * leetcode 200 题，计算岛屿的数量，最简单也是最经典的 dfs 岛屿类算法
     * 
     * @param grid
     * @return
     */
    public int numIslands(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {// 岛屿
                    count++;
                    // 将 grid[i,j] 淹没4
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }

    void dfs(char[][] grid, int i, int j) {
        int m = grid.length;
        int n = grid[0].length;
        if (i >= m || i < 0 || j >= n || j < 0) {
            return;
        }
        if (grid[i][j] == '0') { // 已经是海水了
            return;
        }
        grid[i][j] = '0';
        // 上下左右遍历
        dfs(grid, i - 1, j);
        dfs(grid, i + 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }

    void dfs(int[][] grid, int i, int j) {
        int m = grid.length;
        int n = grid[0].length;
        if (i >= m || i < 0 || j >= n || j < 0) {
            return;
        }
        if (grid[i][j] == WATER) { // 已经是海水了
            return;
        }
        grid[i][j] = WATER;
        // 上下左右遍历
        dfs(grid, i - 1, j);
        dfs(grid, i + 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }

    /**
     * leetcode 1254, 统计封闭岛屿的数量，1 是水，0 是陆地
     * 
     * @param grid
     * @return
     */
    public int closedIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        // 判断 4 周海水的情况
        for (int i = 0; i < grid.length; i++) {
            // 直接淹。第一列和最后一列
            dfs(grid, i, 0);
            dfs(grid, i, n - 1);
        }
        // 第一行和最后一行
        for (int j = 0; j < n; j++) {
            dfs(grid, 0, j);
            dfs(grid, m - 1, j);
        }
        // 遍历 grid ，剩下的岛屿都是封闭岛屿
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == LAND) {
                    count++;
                    // 淹没封闭岛屿
                    dfs(grid, i, j);
                }
            }
        }

        return count;
    }

    /**
     * 求封闭的陆地面积：其中 0 表示一个海洋单元格、1 表示一个陆地单元格。
     * 
     * @param grid
     * @return
     */
    public int numEnclaves(int[][] grid) {
        WATER = WATER ^ 1; // 跟 water 的原始定义取反
        LAND = LAND ^ 1; // land 同理
        int m = grid.length;
        int n = grid[0].length;
        // 4 条边淹没
        for (int i = 0; i < m; i++) {
            dfs(grid, i, 0);
            dfs(grid, i, n - 1);
        }
        for (int j = 0; j < n; j++) {
            dfs(grid, 0, j);
            dfs(grid, m - 1, j);
        }
        int enclaves = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == LAND) {
                    enclaves++;
                }
            }
        }
        return enclaves;
    }

    /**
     * 岛屿的最大面积，leetcode 695。
     * 岛屿 是由一些相邻的 1 (代表土地) 构成的组合，这里的「相邻」要求两个 1 必须在 水平或者竖直的四个方向上 相邻。
     * 你可以假设 grid 的四个边缘都被 0（代表水）包围着
     * 
     * @param grid
     * @return
     */
    public int maxAreaOfIsland(int[][] grid) {
        WATER = WATER ^ 1; // 跟 water 的原始定义取反
        LAND = LAND ^ 1; // land 同理
        int m = grid.length;
        int n = grid[0].length;
        int area = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == LAND) {
                    area = Math.max(area, dfsReturnVal(grid, i, j));
                }
            }
        }
        // 恢复 WATER LAND 变量
        WATER = WATER ^ 1;
        LAND = LAND ^ 1;
        return area;
    }

    public int maxAreaOfIsland2(int[][] grid) {
        WATER = WATER ^ 1; // 跟 water 的原始定义取反
        LAND = LAND ^ 1; // land 同理
        int m = grid.length;
        int n = grid[0].length;
        int area = 0;
        AtomicInteger counter = new AtomicInteger();
        used = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == LAND) {
                    counter.set(0);
                    dfsMaxArea(grid, i, j, counter);
                    area = Math.max(area, counter.get());
                }
            }
        }
        // 恢复 WATER LAND 变量
        WATER = WATER ^ 1;
        LAND = LAND ^ 1;
        return area;
    }

    int dfsReturnVal(int[][] grid, int i, int j) {
        int m = grid.length;
        int n = grid[0].length;

        if (i >= m || i < 0 || j >= n || j < 0) {
            return 0;
        }
        if (grid[i][j] == WATER) {// i，j 已经是海水
            return 0;
        }
        grid[i][j] = WATER;
        // 上下左右遍历
        return 1 +
                dfsReturnVal(grid, i - 1, j) +
                dfsReturnVal(grid, i + 1, j) +
                dfsReturnVal(grid, i, j - 1) +
                dfsReturnVal(grid, i, j + 1);
    }

    boolean[][] used;

    void dfsMaxArea(int[][] grid, int i, int j, AtomicInteger counter) {
        int m = grid.length;
        int n = grid[0].length;

        if (i >= m || i < 0 || j >= n || j < 0) {
            return;
        }
        if (grid[i][j] == WATER) {// i，j 已经是海水
            return;
        }

        grid[i][j] = WATER;
        // 上下左右遍历
        counter.incrementAndGet();

        dfsMaxArea(grid, i - 1, j, counter);
        dfsMaxArea(grid, i + 1, j, counter);
        dfsMaxArea(grid, i, j - 1, counter);
        dfsMaxArea(grid, i, j + 1, counter);
    }

    /**
     * leetcode 1905 题，统计子岛的面积。0 为水，1 是陆地
     * 
     * @param grid1
     * @param grid2
     * @return
     */
    public int countSubIslands(int[][] grid1, int[][] grid2) {
        LAND ^= 1;
        WATER ^= 1;
        int m = grid1.length;
        int n = grid1[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 如果 岛2 是陆地，岛1 的 i,j 是水，则表明grid2 的 i,j 不是 grid1 的子岛，则淹掉
                if (grid1[i][j] == WATER && grid2[i][j] == LAND) {
                    // 淹掉 grid2 的当前岛屿
                    dfs(grid2, i, j);
                }
            }
        }
        int subIslandsCount = 0;
        // grid2 剩下的岛屿就是 grid1 的子岛
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid2[i][j] == LAND) {
                    // 淹掉 grid2 的当前岛屿
                    subIslandsCount++;
                    dfs(grid2, i, j);
                }
            }
        }
        // 还原
        LAND ^= 1;
        WATER ^= 1;
        return subIslandsCount;
    }

    /**
     * 不同岛屿的数量，leetcode 694
     * @param grid
     * @return
     */
    public int numsOfDistinctIslands(int[][] grid) {
        if (grid == null || grid.length < 1) {
            return 0;
        }
        reverseWaterAndLand();
        StringBuilder content = new StringBuilder();
        Set<String> islandsSet = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == LAND) {
                    // 初始值传入一个非 遍历方向的值就行
                    content.delete(0, content.length());
                    dfsDistinctIslands(grid, i, j, content, 0);
                    islandsSet.add(content.toString());
                }
            }
        }
        reverseWaterAndLand();
        System.out.println(islandsSet);
        return islandsSet.size();
    }

    /**
     * 不同的岛屿遍历
     * 
     * @param grid
     */
    void dfsDistinctIslands(int[][] grid, int i, int j, StringBuilder sb, int direction) {
        int m = grid.length;
        int n = grid[0].length;
        if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] == WATER) {
            return;
        }
        grid[i][j] = WATER;
        sb.append(direction);
        // 上下左右 dfs 遍历。1,2,3,4 代表上下左右
        dfsDistinctIslands(grid, i - 1, j, sb, 1);
        dfsDistinctIslands(grid, i + 1, j, sb, 2);
        dfsDistinctIslands(grid, i, j - 1, sb, 3);
        dfsDistinctIslands(grid, i, j + 1, sb, 4);
        // 后续遍历位置，代表撤销操作
        sb.append(-direction);
    }

    void reverseWaterAndLand() {
        WATER = WATER ^ 1;
        LAND = LAND ^ 1;
    }

    public static void main(String[] args) {
        DfsBfsIsland instance = new DfsBfsIsland();
        // char[][] grid = new char[][] {
        // { '1', '1', '1', '1', '0' },
        // { '1', '1', '0', '1', '0' },
        // { '1', '1', '0', '0', '0' },
        // { '0', '0', '0', '0', '0' }
        // };

        // System.out.println(instance.numIslands(grid));

        int[][] grid = new int[][] {
                { 1, 1, 1, 1, 1, 1, 1, 0 }, { 1, 0, 0, 0, 0, 1, 1, 0 }, { 1, 0, 1, 0, 1, 1, 1, 0 },
                { 1, 0, 0, 0, 0, 1, 0, 1 }, { 1, 1, 1, 1, 1, 1, 1, 0 }
        };

        // System.out.println(instance.closedIsland(grid));

        // grid = new int[][] {
        // { 0, 0, 0, 0 }, { 1, 0, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 }
        // };
        // System.out.println(instance.numEnclaves(grid));

        // System.out.println(-1 ^ 2);
        // // 2 的正码为 0010，反码为 1101，因此-1^2 == ~2 == -3
        // System.out.println(Integer.toBinaryString(-1 ^ 2));

        grid = new int[][] { { 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 },
                { 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0 },
                { 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0 } };
        int[][] grid2 = Arrays.copyOf(grid, grid.length);
        int counter = 0;
        for (int[] arr : grid) {
            grid2[counter++] = Arrays.copyOf(arr, arr.length);
        }
        System.out.println(instance.maxAreaOfIsland(grid));
        System.out.println(instance.maxAreaOfIsland2(grid2));

        grid = new int[][] { { 1, 1, 1, 0, 0 }, { 0, 1, 1, 1, 1 }, { 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0 },
                { 1, 1, 0, 1, 1 } };

        grid2 = new int[][] { { 1, 1, 1, 0, 0 }, { 0, 0, 1, 1, 1 }, { 0, 1, 0, 0, 0 }, { 1, 0, 1, 1, 0 },
                { 0, 1, 0, 1, 0 } };

        System.out.println(instance.countSubIslands(grid, grid2));

        grid = new int[][] { { 1, 1, 0, 1, 1 }, { 1, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1 }, { 1, 1, 0, 1, 1 } };

        // 不同岛屿的数量
        System.out.println(instance.numsOfDistinctIslands(grid));
    }

}
