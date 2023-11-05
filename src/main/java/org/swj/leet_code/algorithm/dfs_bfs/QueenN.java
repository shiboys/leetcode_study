package org.swj.leet_code.algorithm.dfs_bfs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * N 皇后问题
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/17 18:29
 */
public class QueenN {

    private static final char QUEEN_SIGN = 'Q';
    private static final char DEFAULT_SIGN = '.';

    List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        char[][] board = new char[n][n];
        // 字符串数组都初始化为 [....]
        for (int i = 0; i < n; i++) {
            char[] chs = new char[n];
            Arrays.fill(chs, DEFAULT_SIGN);
            board[i] = chs;
        }
        // 进行回溯
        trackBack(board, 0, res);
        return res;
    }

    /**
     * 
     * @param board 回溯的数据源
     * @param row   从数据源的那一行开始回溯
     * @param res   结果集收集器
     */
    void trackBack(char[][] board, int row, List<List<String>> res) {
        if (row == board.length) {// 有一个成功放置 Q 的方案
            List<String> currList = new ArrayList<>(board.length);
            for (int k = 0; k < board.length; k++) {
                currList.add(new String(board[k]));
            }
            res.add(currList);
            return;
        }

        int n = board[0].length;
        // 行方向的冲突位置，是通过每行放置一个 queen 解决的
        for (int col = 0; col < n; col++) {
            if (!isValidQueenPos(board, row, col)) {
                continue;
            }
            // 进行回溯
            board[row][col] = QUEEN_SIGN;
            trackBack(board, row + 1, res);

            // 撤销选择
            board[row][col] = DEFAULT_SIGN;
        }

    }

    private boolean isValidQueenPos(char[][] board, int row, int col) {
        int n = board[0].length;
        // 检查当前列有没有冲突的的字符
        for (int i = 0; i < n; i++) {
            if (board[i][col] == QUEEN_SIGN) {
                return false;
            }
        }
        // 右上对角线的字符
        for (int i = row, j = col; i >= 0 && j < n; i--, j++)
            if (board[i][j] == QUEEN_SIGN) {
                return false;
            }
        // 左上的字符
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == QUEEN_SIGN) {
                return false;
            }
        }
        // 这里为什么不判断左下和右下对角线的元素，因为 Queen 的放置位置是从上到下，当前位置放置一个 Queue，下面的位置一定没有，不需要比较
        // 这个特性非常依赖于回溯算法的取消选择操作

        return true;
    }

    public static void main(String[] args) {
        QueenN queenN = new QueenN();
        List<List<String>> list = queenN.solveNQueens(4);
        for(List<String> subList : list) {
            System.out.println(subList);
        }
    }

}
