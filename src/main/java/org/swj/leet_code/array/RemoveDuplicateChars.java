package org.swj.leet_code.array;

import java.util.Stack;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/25 14:27
 *        数组去重 leetcode 
 */
public class RemoveDuplicateChars {
    String removeDuplicateLetters(String s) {
        // 使用 256 长度的数组，来封装所有的 ascii 字符容器
        boolean[] inStack = new boolean[256];
        char[] chs = s.toCharArray();

        Stack<Character> stack = new Stack<>();
        for (char ch : chs) {
            // 如果字符存在栈中，则排重
            if (inStack[ch]) {
                continue;
            }
            // 若不存在，则插入栈顶，并标记已存在
            stack.push(ch);
            inStack[ch] = true;
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        return sb.reverse().toString();// 从 stack 中接受的字符串，需要翻转下，还原为正常顺序的字符串
    }

    String removeDupliatLetters2(String s) {
        char[] chs = s.toCharArray();
        boolean[] inStack = new boolean[256];
        Stack<Character> stack = new Stack<>();
        for (char ch : chs) {
            if (inStack[ch]) {
                continue;
            }
            while (!stack.isEmpty() && stack.peek() > ch) {
                // 将比当前字符 ch 的字符弹出来
                char currCh = stack.pop();
                inStack[currCh] = false;
            }
            stack.push(ch);
            inStack[ch] = true;
        }

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }

        return sb.reverse().toString();
    }

    public String removeDuplicateLetters3(String s) {
        char[] chs = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        boolean[] inStack = new boolean[256];
        int[] counter = new int[256];
        // 记录各个字符的数量
        for (char ch : chs) {
            counter[ch] += 1;
        }
        char currCh;
        for (char ch : chs) {
            // 先将当前字符数量 -1
            counter[ch]--;
            if (inStack[ch]) {
                continue;
            }
            while (!stack.isEmpty() && (currCh = stack.peek()) > ch && counter[currCh] > 0) {
                // 后面的字符不会再出现当前字符，当前栈顶字符不能被弹出
                // 否则当前栈顶字符可以被弹出
                stack.pop();
                inStack[currCh] = false;
            }
            stack.push(ch);
            inStack[ch] = true;
        }
        
        StringBuilder sb = new StringBuilder();
        while(!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        String s = "bcabc";
        RemoveDuplicateChars instance = new RemoveDuplicateChars();
        System.out.println(instance.removeDupliatLetters2(s));
        s="cbacdcbc";
        System.out.println(instance.removeDuplicateLetters3(s));
    }

}
