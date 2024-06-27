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
            while (!stack.isEmpty() && (currCh = stack.peek()) > ch) {
                // 后面的字符不会再出现当前字符，当前栈顶字符不能被弹出
                if (counter[currCh] == 0) {
                    break;
                }
                // 否则当前栈顶字符可以被弹出
                inStack[stack.pop()] = false;
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

    /**
     * 使用 char[26] 来满足所有小写字母
     * 
     * @param s
     * @return
     */
    public String removeDuplicateLetters4(String s) {
        // 防重复的数组，防止重复的字符添加到栈中
        // 自然比对顺序保证有序，
        // count 保证不会对变成空的字符移出栈。
        boolean[] inStack = new boolean[26];
        // 每个字符的个数统计数组
        int[] charCount = new int[26];
        Stack<Character> stack = new Stack<>();
        char[] chs = s.toCharArray();
        for (char ch : chs) {
            charCount[ch - 'a']++;
        }
        for (char ch : chs) {
            charCount[ch - 'a']--;
            // 如果当前字符，比栈中的小，且栈中的字符后面还回有该字符串出现
            // 则弹出当前字符。
            if (inStack[ch - 'a']) {
                continue;
            }
            //循环从栈中弹出元素。
            while (!stack.isEmpty() && ch < stack.peek() && charCount[stack.peek() - 'a'] > 0) {
                inStack[stack.pop() - 'a'] = false;
            }

            inStack[ch-'a'] = true;
            stack.push(ch);
        }

        StringBuilder sb = new StringBuilder(stack.size());
        for (char ch : stack) {
            sb.append(ch);
        }
        return sb.toString();
        // while(!stack.isEmpty()) {
        // sb.append(stack.pop());
        // }
        // return sb.reverse().toString();
    }

    public static void main(String[] args) {
        String s = "bcabc";
        RemoveDuplicateChars instance = new RemoveDuplicateChars();
        System.out.println(instance.removeDuplicateLetters4(s));
        s = "cbacdcbc";
        System.out.println(instance.removeDuplicateLetters4(s));
    }

}
