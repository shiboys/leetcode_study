package org.swj.leet_code.string;

import java.util.Arrays;

public class KMP {

    // 参考阿里云的文章
    // https://developer.aliyun.com/article/1245543?utm_content=g_1000373986

    public static int[] getNext(String pattern) {

        int[] next = new int[pattern.length()];
        int i = 0, j = -1;
        next[0] = -1;
        while (i < pattern.length() - 1) {

            if (j == -1 || pattern.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                next[i] = j;
            } else {

                j = next[j];
            }
        }
        return next;
    }

    int[] getNextReadable(String pattern) {
        int[] next = new int[pattern.length()];
        int i = 1, prefix_len = 0;
        while (i < pattern.length()) {
            // 字符匹配，长度++，继续匹配下一个字符
            if (pattern.charAt(i) == pattern.charAt(prefix_len)) {
                prefix_len++;
                next[i] = prefix_len;
                i++;
            } else if (prefix_len > 0) {
                // 字符不匹配，且 prefix_len > 0， 则 prefix_len 回退到上一个匹配的位置
                prefix_len = next[prefix_len - 1];
            } else {
                // 字符不匹配，且 prefix_len = 0， 则 next[i] = 0， i++
                next[i] = 0;
                i++;
            }
        }
        return next;
    }

    int kmpMatch(String source, String pattern) {
        int[] next = getNextReadable(pattern);
        int i = 0, j = 0;
        while (i < source.length() && j < pattern.length()) {
            if (source.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else if (j > 0) { // 不匹配
                j = next[j - 1]; // j 指针回退
            } else { // j==0
                i++; // pattern 从头开始匹配
            }
        }
        return j == pattern.length() ? i - j : -1;
    }

    public static void main(String[] args) {
        String pattern_x = "ababaca";
        KMP instance = new KMP();

        System.out.println(Arrays.toString(instance.getNext(pattern_x)));

        pattern_x += "x";
        System.out.println(Arrays.toString(instance.getNext(pattern_x)));

        String input = "作为程序员一定学习编程之道，一定要对代码的编写有追求，不能实现就完事了。我们应该让自己写的代码更加优雅，即使这会费时费力";
        String match = "不能实现就完事了";
        System.out.println("match result is " + instance.kmpMatch(input, match));
    }

}
