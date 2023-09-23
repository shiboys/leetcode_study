package org.swj.leet_code.algorithm.divide_merge;

import java.util.LinkedList;
import java.util.List;

/**
 * 分治法的使用
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/22 22:29
 */
public class DivideMerge {
    /**
     * leetcode 241 题，不同的增加括号的方式的最终运算结果
     * 一个分治法的典型运用，参考 merge.md 文档详细描述
     * 
     * @param expression
     * @return
     */
    public List<Integer> diffWaysToCompute(String expression) {
        return divideAndMerge(expression);
    }

    List<Integer> divideAndMerge(String expression) {
        List<Integer> res = new LinkedList<>();
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == '+' || ch == '-' || ch == '*') {
                // 分
                List<Integer> left = divideAndMerge(expression.substring(0, i));
                List<Integer> right = divideAndMerge(expression.substring(i + 1));
                // 治
                for (int a : left) {
                    for (int b : right) {
                        if (ch == '+') {
                            res.add(a + b);
                        } else if (ch == '-') {
                            res.add(a - b);
                        } else if (ch == '*') {
                            res.add(a * b);
                        }
                    }
                }
            }
        }
        // base case, 无符号，则表示是表达式中的数字
        if (res.isEmpty()) {
            res.add(Integer.parseInt(expression));
        }
        return res;
    }

    public static void main(String[] args) {
        DivideMerge instance = new DivideMerge();
        System.out.println(instance.diffWaysToCompute("2*3-4*5"));
        int val = 1<<30;
        System.out.println(val == ((Integer.MAX_VALUE >> 1) + 1));
        System.out.println(Integer.toBinaryString(val));
        System.out.println(Integer.toBinaryString((Integer.MAX_VALUE >> 1) +1));
    }
}
