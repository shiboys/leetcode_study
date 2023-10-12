package org.swj.leet_code.advanced_data_structure;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 贪吃蛇游戏,leetcode 353，也是 plus 会员题目
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/11 21:29
 */
public class SnakeGame {
    /**
     * 游戏的说明和解题方法请参考 markdown.md 文档
     */
    // 链表表示的蛇身
    private LinkedList<Integer> body;
    private Set<Integer> bodySet;
    private LinkedList<Integer> foodList;
    // 屏幕的高，宽
    int m, n;
    boolean alive = true;

    public SnakeGame(int width, int height, int[][] food) {
        m = height;
        n = width;
        body = new LinkedList<>();
        bodySet = new HashSet<>();
        // 初始化贪吃蛇位置
        body.add(encode(0, 0));
        bodySet.add(encode(0, 0));
        foodList = new LinkedList<>();
        for (int[] arr : food) {
            foodList.add(encode(arr[0], arr[1]));
        }
    }

    int encode(int x, int y) {
        return x * n + y;
    }

    public int move(String direction) {
        if (!alive) {
            return -1;
        }
        int startPos = body.peekFirst();
        // 获取蛇头的当前坐标
        int x = startPos / n, y = startPos % n;
        // 计算贪食蛇的下一次移动位置
        int nx = x, ny = y;
        switch (direction) {
            case "U":
                nx--;
                break;
            case "D":
                nx++;
                break;
            case "R":
                ny++;
                break;
            case "L":
                ny--;
                break;
        }
        // 越界了
        if (nx < 0 || nx >= m || ny < 0 || ny >= n) {
            alive = false;
            return -1;
        }
        //
        int newPos = encode(nx, ny);
        body.addFirst(newPos);
        // 吃到事物,
        if (!foodList.isEmpty() && newPos == foodList.peekFirst()) {
            foodList.removeFirst();
        } else { // 没有吃到事物，则删除尾巴，同时要从身体字典里面删除这个位置。
            bodySet.remove(body.removeLast());
        }
        // 贪吃蛇吃到自己身体
        if (bodySet.contains(newPos)) {
            alive = false;
            return -1;
        }
        bodySet.add(newPos);
        // 吃掉事物的个数等于自身的长度 - 1。
        return body.size() - 1;
    }



    public static void main(String[] args) {
        SnakeGame snakeGame = new SnakeGame(3, 2, new int[][] { { 1, 2 }, { 0, 1 } });
        System.out.println(snakeGame.move("R")); // 返回 0
        System.out.println(snakeGame.move("D")); // 返回 0
        System.out.println(snakeGame.move("R")); // 返回 1，蛇吃掉了第一个食物，同时第二个食物出现在 (0, 1)
        System.out.println(snakeGame.move("U")); // 返回 1
        System.out.println(snakeGame.move("L")); // 返回 2，蛇吃掉了第二个食物，没有出现更多食物
        System.out.println(snakeGame.move("U")); // 返回 -1，蛇与边界相撞，游戏结束Ô
    }
}
