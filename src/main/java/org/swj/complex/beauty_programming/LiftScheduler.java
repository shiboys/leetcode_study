package org.swj.complex.beauty_programming;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

/**
 * 小飞的电梯调度算法
 * 编程之美 1.8 题
 * 题目描述：
 * 由于楼层并不太高，那么在繁忙的上下班时间，每次电梯从一层往上走的时候，我们允许电梯停在某一层。
 * 所有的乘客都从一楼上电梯，到达某层之后，电梯会停下来，所有乘客再从这里爬楼到自己的目的层。
 * 在一层的时候，每个乘客选择自己的目的层，电梯则自动计算应停的楼层。
 * 
 * 问，电梯应该停在哪一层，能够保证这次乘坐电梯的所有乘客爬楼梯的层数之和最少？
 * (其实这里面还有一个隐含的条件，就是每层的楼梯的台阶数是相等的)
 */
public class LiftScheduler {
    /**
     * 分析与解法：
     * 该问题本质上是一个优化问题。在分析问题之前，首先需要为这个问题找到一个合适的抽象模型。从问题可以看出，有两个因素会影响到最后的结果
     * 乘客的数目以及需要停的目的层数。因此，我们可以从统计到达各层的乘客数目开始分析。
     * 
     * 假设楼层总共 N 层，电梯要停在 x 层，要去第 i 层的乘客数目为 Tot[i], 那么，所爬楼梯的总数就是 sigma{Tot[i]*|i-x|}。
     * 
     * 因此我们需要找到一个整数 x, 使得 sigma{Tot[i]*|i-x|} 最小。
     * 
     */

    // 楼层总数
    final int N;

    // 每层人数。从 1 开始计数
    final int[] persons;
    int minFloorPersons = Integer.MAX_VALUE;
    int minFloor = 0;

    public LiftScheduler(int[] persons, int n) {
        this.persons = persons;
        this.N = n;
    }

    /**
     * 解法 1：
     * 可以考虑从第 1 层开始枚举 x 一直到第 N 层，然后再计算出如果电梯在第 x 层楼停的话，所有乘客需要爬多少层楼。这是一个最为直接的解法。
     * 
     */

    MinFloor minSteps() {
        MinFloor minFloor = new MinFloor(0, Integer.MAX_VALUE);
        for (int x = 1; x <= N; x++) {
            int steps = 0;
            for (int i = 1; i < x; i++) {
                steps += persons[i] * (x - i);
            }
            for (int i = x + 1; i <= N; i++) {
                steps += persons[i] * (i - x);
            }
            if (steps < minFloor.steps) {
                minFloor.steps = steps;
                minFloor.floor = x;
            }
        }
        return minFloor;
    } // 上述解法的时间复杂度为 O(n^2)，但是还是非常容易理解的。

    static class MinFloor {
        int floor;
        int steps;

        public MinFloor(int floor, int steps) {
            this.floor = floor;
            this.steps = steps;
        }

        @Override
        public String toString() {
            return String.format("min floor is %d, steps is %d", floor, steps);
        }
    }

    /**
     * 解法2：
     * 怎么才能找到更加高效的解法呢，也就是找到低于 O(N^2) 的时间复杂度呢？我们来进一步分析。
     * 假设电梯停在第 i 层，显然我们可以计算出来所有乘客需要爬楼梯的层数 Y。
     * 如果有 N1 个乘客的目的层数在第 i 层以下，有 N2 个乘客的在第 i 层，还有 N3 个乘客在第 i 层以上。
     * 这个时候，如果电梯停在第 i-1 层，所有目的地在第 i 层及以上的乘客都需要再多爬 1 层，总共需要多爬 N2+N3 层，
     * 而所有的目的地在第 i-1 层及以下的乘客都需要少爬 1 层，总共需要少爬 N1 层。因此总共需要爬的层数为
     * Y-N1+(N2+N3) = Y-(N1-N2-N3)。
     * 
     * 反之，如果电梯在 i+1 层，那么乘客总共需要爬的层数为 Y + (N1+N2-N3) 层。由此可见，当 N1>N2+N3 时，电梯在 i-1
     * 层最好，乘客走的楼梯数减少 (N1-N2-N3);
     * 而当 N1+N2<N3 时，电梯在 i+1 层最好，其他情况下，电梯停在 i 层最好。
     * 
     * 根据这个规律，我们从第一层开始考察，计算各位乘客所需要爬的楼梯的数目。然后再根据上面的策略进行调整，直到找到最佳答案
     */

    MinFloor minStepWithN() {
        MinFloor minFloor = new MinFloor(1, 0);

        int N1 = 0, N2 = persons[1], N3 = 0;
        // 计算 N3 的人数，和在第 1 层的乘客需要爬的楼梯的层数 Y
        for (int i = 2; i <= N; i++) {
            N3 += persons[i];
            // 只有 2 层及以上的乘客才需要爬楼梯
            minFloor.steps += persons[i] * (i - 1);
        }
        for (int i = 2; i <= N; i++) {
            if (N1 + N2 < N3) {
                minFloor.floor = i;
                minFloor.steps += N1 + N2 - N3;
                // 更新 N1 为 N1+N2 表示 新的 N1 为 新的 N2 及以下的所有乘客人数
                N1 += N2;
                N2 = persons[i];
                // N3 对应的人数 - 去 N2 层的乘客人数
                N3 -= persons[i];
            } else {
                break;
            }
        }
        return minFloor;
    } // 该算法的时间复杂度为 O(N)，经过测试表明，这个算法很牛逼。但是不需要考虑 从上到下，我觉得有点奇怪。经过仔细分析，
      // N1 + N2 > N3 表示电梯不需要升高了，当前层就是目标层

    public static void main(String[] args) {
        Random random = new Random();
        int[] persons = new int[7];
        for (int i = 1; i < persons.length; i++) {
            // 每层人数随机
            persons[i] = random.nextInt(10) + 1;
        }

        System.out.println("persons is " + Arrays.toString(persons));
        LiftScheduler liftScheduler = new LiftScheduler(persons, persons.length - 1);
        MinFloor minFloorComplex = liftScheduler.minSteps();
        MinFloor minFloorSimple = liftScheduler.minStepWithN();
        System.out.println("complex minfloor is " + minFloorComplex);
        System.out.println("simple minFloor is " + minFloorSimple);
        System.out.println(String.format("%020d", 100));
    }
}
