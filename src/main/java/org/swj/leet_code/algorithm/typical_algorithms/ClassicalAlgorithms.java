package org.swj.leet_code.algorithm.typical_algorithms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 经典 算法问题2
 */
public class ClassicalAlgorithms {
    /**
     * leetcode 855. 考场就座
     */
    static class ExamRoom {

        /**
         * 以 int p 为起点的线段
         */
        Map<Integer, int[]> fromDistance;
        // 以 p 为终点的 线段
        Map<Integer, int[]> toDistance;
        // 存放所有线段的容器
        TreeSet<int[]> distanceContainer;
        int limit;

        public ExamRoom(int n) {
            fromDistance = new HashMap<>();
            toDistance = new HashMap<>();
            distanceContainer = new TreeSet<>((a, b) -> {
                // 只是简单的比较距离，在距离相等的时候的逻辑没有列出来
                // return distance(a) - distance(b);
                int distanceA = distance(a);
                int distanceB = distance(b);
                // 如果距离相等，就比较索引？？这里感觉有问题
                if (distanceA == distanceB) {
                    // System.out.println("a is " + Arrays.toString(a) +", b is " +
                    // Arrays.toString(b));
                    // 这里为什么是 b[0]-a[0] ? 而不是 a[0]-b[0].
                    // 因为二叉树里面最先进入的是 [-1, N] , 然后 让如两个 [-1，0]，[0,N]， 再然后，[n-1, n] 而此时的
                    // treeMap 的源码里面 有 parent = t; cmp = cpr.compare(key, t.key); 这样两句使用到 comparator
                    // 的源码
                    // 对照源码，发现 [-1,0] 就是 t.key 也就是 传入的 b，而 key 就是新增加的 [n-1,n] 也就是 形参 a 变量
                    // 因此是 b[0]-a[0] ，表明按照 b[0] 排序，也就是 -1 排前面。
                    return b[0] - a[0];
                }
                return distanceA - distanceB;
            });
            // 先加入一个 dummy distance
            addInterval(new int[] { -1, n });
            this.limit = n;
        }

        // 计算线段的长度
        int distance(int[] interval) {
            // 不再只是简单的计算线段的距离
            // return interval[1] - interval[0] - 1;
            int from = interval[0], to = interval[1];
            int distance = 0;
            if (from == -1) {
                distance = to;
            } else if (to == this.limit) {
                distance = this.limit - 1 - from;
            } else { // 其他情况，去中点和端点之间的举例
                distance = (to - from) / 2;
            }
            return distance;
        }

        public int seat() {
            // 没有初始化数据
            if (distanceContainer.isEmpty()) {
                return -1;
            }
            int[] largestDistance = distanceContainer.last();
            int seat = 0;
            int x = largestDistance[0], y = largestDistance[1];
            if (x == -1) {
                seat = 0;
            } else if (y == this.limit) {
                seat = limit - 1;
            } else { // 中间节点
                seat = x + (y - x) / 2;
            }
            // 将最长的线段分割成 2 段
            int[] left = new int[] { x, seat };
            int[] right = new int[] { seat, y };
            removeInterval(largestDistance);
            addInterval(left);
            addInterval(right);
            return seat;
        }

        void addInterval(int[] interval) {
            int from = interval[0];
            int to = interval[1];
            fromDistance.put(from, interval);
            toDistance.put(to, interval);
            distanceContainer.add(interval);
        }

        void removeInterval(int[] interval) {
            int from = interval[0];
            int to = interval[1];
            fromDistance.remove(from);
            toDistance.remove(to);
            distanceContainer.remove(interval);
        }

        public void leave(int p) {
            int[] right = null, left = null;
            if (fromDistance.containsKey(p)) {
                right = fromDistance.get(p);
                removeInterval(right);
            }
            if (toDistance.containsKey(p)) {
                left = toDistance.get(p);
                removeInterval(left);
            }
            if (right != null && left != null) {
                // 合并为一个线段
                int[] newDistance = new int[] { left[0], right[1] };
                addInterval(newDistance);
            }
        }
    }

    /**
     * leetcode 391. 完美矩形
     * 给你一个数组 rectangles ，其中 rectangles[i] = [xi, yi, ai, bi] 表示一个坐标轴平行的矩形。
     * 这个矩形的左下顶点是 (xi, yi) ，右上顶点是 (ai, bi) 。
     * 如果所有矩形一起精确覆盖了某个矩形区域，则返回 true ；否则，返回 false 。
     * 
     * @param rectangles
     * @return
     */
    public boolean isRectangleCover(int[][] rectangles) {
        // 左下角的坐标
        int X1 = Integer.MAX_VALUE, Y1 = Integer.MAX_VALUE;
        // 右上角的坐标
        int X2 = Integer.MIN_VALUE, Y2 = Integer.MIN_VALUE;
        // 各个矩形的累计面积和
        int calcArea = 0;
        int x1, x2, y1, y2;
        Set<String> points = new HashSet<>();
        // 矩形的四个顶点
        String p1, p2, p3, p4;
        // 在坐标轴上，左下角就是 (x,y) 最小，右上角就是 (x,y) 最大。
        for (int[] rec : rectangles) {
            x1 = rec[0];
            y1 = rec[1];
            x2 = rec[2];
            y2 = rec[3];

            calcArea += (x2 - x1) * (y2 - y1);
            X1 = Math.min(X1, x1);
            Y1 = Math.min(Y1, y1);

            X2 = Math.max(X2, x2);
            Y2 = Math.max(Y2, y2);

            p1 = ptr(x1, y1);
            p2 = ptr(x1, y2);
            p3 = ptr(x2, y1);
            p4 = ptr(x2, y2);
            for (String p : Arrays.asList(p1, p2, p3, p4)) {
                if (points.contains(p)) {
                    points.remove(p);
                } else {
                    points.add(p);
                }
            }
        }
        int actualArea = (X2 - X1) * (Y2 - Y1);
        // 面积不相等，则返回 false;
        if (calcArea != actualArea) {
            return false;
        }
        // 下面判断顶点
        if (points.size() != 4) {
            return false;
        }

        return points.contains(ptr(X1, Y1)) &&
                points.contains(ptr(X1, Y2)) &&
                points.contains(ptr(X2, Y1)) &&
                points.contains(ptr(X2, Y2));
    }

    String ptr(int x, int y) {
        return String.format("%s,%s", x, y);
    }

    public static void main(String[] args) {
        ExamRoom examRoom = new ExamRoom(10);
        for (int i = 0; i < 4; i++) {
            System.out.println(examRoom.seat());
        }
        examRoom.leave(4);
        System.out.println(examRoom.seat());

        int[][] rectangles = new int[][] {{1,1,3,3},{3,1,4,2},{3,2,4,4},{1,3,2,4},{2,3,3,4}};
        ClassicalAlgorithms instance = new ClassicalAlgorithms();
        System.out.println(instance.isRectangleCover(rectangles));

        rectangles = new int[][] {{1,1,3,3},{3,1,4,2},{1,3,2,4},{2,2,4,4}};
        System.out.println(instance.isRectangleCover(rectangles));
    }
}
