package org.swj.leet_code.algorithm.dynamic_programming.basic_skill;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * @author shiweijie
 *         购买书籍的问题，之前使用 C++ 版本，嵌套了 5 个 for 循环，代码可读性很差，
 *         这次使用 递归版本，使用 java 重新编写一遍
 */
public class BuyBooks {

    static final int bookPrice = 8;
    static final double discount1 = 1;
    static final double discount2 = 1.9;
    static final double discount3 = 2.7;
    static final double discount4 = 3.2;
    static final double discount5 = 3.75;
    static final int INF = 9999;
    static final Map<List<Integer>, Double> memo = new HashMap<>();

    public double getMinBookPrice(int[] books) {
        if (books == null || books.length < 1) {
            return 0;
        }
        // Arrays.sort(books);
        // 坑爹， java 不支持 Arrays.sort 对原始类型进行倒序排列
        books = Arrays.stream(books).boxed().sorted((x, y) -> y - x).mapToInt(x -> x).toArray();
        return minBookPriceDpRecur(books);
    }

    private double minBookPriceDpRecur(int[] books) {
        if (Arrays.stream(books).sum() == 0) { // 递归出口
            return 0;
        }
        List<Integer> booksList = Arrays.stream(books).boxed().collect(Collectors.toList());
        if (memo.containsKey(booksList)) {
            return memo.get(booksList);
        }
        // 保证 y1 >= y2 >= y3 >= y4 >= y5
        // Arrays.sort(books);
        // 这里我之前出现了 2 处错误，一处是 这里我没有对 books 进行倒序排列，导致我计算的结果不对
        // 另外一处是 我没有判断 books[i] 是否为 0，导致我计算的结果不对

        // 经过一番折腾，发现之前的逻辑 books[i] > 0 ? bookPrice*books[i] + dp(xxx) : 0 是错误的
        // 正确的逻辑仍然是，如果取最小，默认值需要 +INF ，而不是 0
        // 当然，如果使用 List，而不用将 int[] 数组转来转去，性能会更高
        books = Arrays.stream(books).boxed().sorted((x, y) -> y - x).mapToInt(x -> x).toArray();
        double s1 = INF, s2 = INF, s3 = INF, s4 = INF, s5 = INF;
        if (books[0] > 0)
            s1 = bookPrice * discount1
                    + minBookPriceDpRecur(new int[] { books[0] - 1, books[1], books[2], books[3], books[4] });
        if (books[1] > 0)
            s2 = bookPrice * discount2
                    + minBookPriceDpRecur(new int[] { books[0] - 1, books[1] - 1, books[2], books[3], books[4] });
        // 对应数组个数 1 1 0 0 0
        if (books[2] > 0)
            s3 = bookPrice * discount3
                    + minBookPriceDpRecur(new int[] { books[0] - 1, books[1] - 1, books[2] - 1, books[3], books[4] });
        if (books[3] > 0)
            s4 = bookPrice * discount4
                    + minBookPriceDpRecur(
                            new int[] { books[0] - 1, books[1] - 1, books[2] - 1, books[3] - 1, books[4] });
        if (books[4] > 0)
            s5 = bookPrice * discount5
                    + minBookPriceDpRecur(
                            new int[] { books[0] - 1, books[1] - 1, books[2] - 1, books[3] - 1, books[4] - 1 });

        double minPrice = Math.min(s1, Math.min(s2, Math.min(s3, Math.min(s4, s5))));
        memo.put(booksList, minPrice);
        return minPrice;
    }

    public static void main(String[] args) {
        int[] books = { 1, 1, 2, 2, 2 };
        BuyBooks instance = new BuyBooks();
        System.out.println("最小价格是" + instance.getMinBookPrice(books));
    }
}
