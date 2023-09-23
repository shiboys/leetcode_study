package org.swj.leet_code.algorithm.dfs_bfs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 括号相关的问题
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/21 21:29
 */
public class Parentheses {

    private Stack<Character> stack = new Stack<>();
    Set<Character> parentheses = new HashSet<>(Arrays.asList('{', '[', '('));

    public boolean isValid(String s) {
        for (char c : s.toCharArray()) {
            if (parentheses.contains(c)) {
                stack.push(c);
            } else {
                if (!stack.isEmpty() && pairOfParenthes(stack.peek()) == c) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    char pairOfParenthes(char pt) {
        if (pt == '{')
            return '}';
        if (pt == '[')
            return ']';
        if (pt == '(')
            return ')';
        return (char) 0;
    }

    /**
     * leetcode 921 题，是括号有效的最少添加
     * 
     * @param s 含有括号的字符串，只包含字符 '(' 和 ')'
     * @return
     */
    public int minAddToMakeValid(String s) {
        int need = 0; // 需要左括号的数量
        int res = 0;
        for (char ch : s.toCharArray()) {
            if (ch == '(') {
                need++;
            } else { // 右括号
                need--;
                if (need == -1) {// 说明有括号数量多
                    res++; // 需要左括号
                    need = 0;
                }
            }
        }
        return need + res;
    }

    /**
     * leetcode 1541 题，平衡括号字符串的最少插入次数
     * 给你一个括号字符串 s ，它只包含字符 '(' 和 ')' 。一个括号字符串被称为平衡的当它满足：
     * 
     * 1、任何左括号 '(' 必须对应两个连续的右括号 '))' 。
     * 2、左括号 '(' 必须在对应的连续两个右括号 '))' 之前
     * 
     * @param s
     * @return
     */
    public int minInsertions(String s) {
        int need = 0;// 右括号的需求量
        int res = 0; // 左括号的需求量
        // 测试用例1 ：(())) 需要 1 个
        // ))())( 3 个
        // )))))))
        char[] chs = s.toCharArray();
        for (char ch : chs) {
            if (ch == '(') {
                need += 2;
                if (need % 2 == 1) { // need 是奇数了，这肯定不符合要求
                    // 插入一个右括号？ res 表示插入的步骤数，插入左括号不是最少的插入步骤了，因为插入左括号，need 需要 ++。
                    res++;
                    // 对右括号的需求减一。
                    need--;
                }
            } else if (ch == ')') {
                need--;
                if (need == -1) { // need 用完
                    // 增加一个 ( 括号
                    res++;
                    // 同时对 ) 括号的需求变为 。
                    need = 1;
                }
            }
        }
        return res + need;
    }

    public static void main(String[] args) {
        Parentheses instance = new Parentheses();
        System.out.println(instance.isValid("()"));
        System.out.println(instance.isValid("()[]{}"));
        System.out.println(instance.isValid("(]"));
        // System.out.println(instance.isValid("()"));

        System.out.println(instance.minAddToMakeValid("((("));
        System.out.println(instance.minAddToMakeValid("())"));
    }

}
