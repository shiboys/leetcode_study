package org.swj.leet_code.digit;

import org.swj.leet_code.linked_list.ListNode;

/**q
 * 数字问题
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/10/16 17:07
 */
public class DigitProblems {
    public boolean isPalindrome(int x) {
        // return isPalindrome(String.valueOf(x));
        // 解法1 使用字符串
        // 解法2，使用数字
        if (x < 0) {
            return false;
        }
        int right = 1;
        long left = getHighPosOfTen(x);
        int mod = 10;
        while (left >= right) {
            if ((x / right) % mod != (x / left) % mod) {
                return false;
            }
            left /= 10;
            right *= 10;
        }
        return true;
    }

    boolean isPalindrome(String s) {
        char[] chs = s.toCharArray();
        int left = 0, right = chs.length - 1;
        while (left < right) {
            if (chs[left] != chs[right]) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    long getHighPosOfTen(int x) {
        long result = 1;
        while (result * 10 <= x) {
            result *= 10;
        }
        return result;
    }

    /**
     * 43. 字符串相乘
     * 给定两个以字符串形式表示的非负整数 num1 和 num2，返回 num1 和 num2 的乘积，它们的乘积也表示为字符串形式。
     * 
     * 注意：不能使用任何内置的 BigInteger 库或直接将输入转换为整数。
     * 
     * @param num1
     * @param num2
     * @return
     */
    public String multiply(String num1, String num2) {
        /**
         * 前提条件是不允许使用 BigInteger，也不能把输入的字符串转换成整数
         * 我之前做过用链表实现两数之和 leetcode 第 2 题，
         * 这次我就用链表是实现两数之积。
         * 思路是将 num1 中的每个字符 乘以 nums2 得到一个链表，
         * 最终形成一个链表数组，然后合并链表数组为一个链表
         * 用 StringBuilder 将链表中的数据全部取出，然后反转返回
         * 也可以递归遍历链表，用后序的方式取出链表中的元素
         */
        if (num1 == null || num1.isEmpty() || num2 == null || num2.isEmpty()) {
            throw new IllegalArgumentException();
        }
        ListNode[] heads = new ListNode[num2.length()];
        ListNode headb = getListNodeFromString(num1);
        int n = num2.length();
        for (int i = 0; i < n; i++) {
            // 获取当前字符跟 nums1 的链表的乘积后的链表
            heads[i] = getMultiplyBySingleListNode(headb, num2.charAt(i), n - 1 - i);
        }
        // 将所有的链表相加
        ListNode head = getMultiLinkedListSum(heads);
        StringBuilder sb = new StringBuilder();
        ListNode p = head;

        while (p != null) {
            sb.append(p.val);
            p = p.next;
        }
        String s = sb.reverse().toString();
        if (s != null && s.length() > 0 && s.startsWith("0")) {
            // 这里 lastIndexOf 不对，需要找到第一个 > '0' 的字符
            int idx = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) > '0') {
                    idx = i;
                    break;
                }
            }
            s = s.substring(idx);
        }
        return s;
    }

    /**
     * 字符串相乘 更优解法
     * 
     * @param num1
     * @param num2
     * @return
     */
    public String multiply2(String num1, String num2) {
        /**
         * 这个解法是参考了 阿东的解题思路，
         * 用数组装入最终的计算结果
         */
        int m = num1.length();
        int n = num2.length();
        // nums1*nums2 的数组最长结果不会超过 m*n
        int[] res = new int[m + n];
        // 从个位开始逐渐相乘
        for (int i = num1.length() - 1; i >= 0; i--) {
            for (int j = num2.length() - 1; j >= 0; j--) {
                int multi = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                // 乘积在 res 上对应的位置
                int p1 = i + j, p2 = i + j + 1;
                // 可以再看看图，61+12 =>1 +12 =13 => res[p2]=3, resp[p] += sum/10;
                int sum = multi + res[p2];
                res[p2] = sum % 10;
                res[p1] += sum / 10;
            }
        }
        int i = 0;
        while ( i<res.length && res[i] == 0) {
            i++;
        }
        StringBuilder sb = new StringBuilder();
        for (; i < res.length; i++) {
            sb.append(res[i]);
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    /**
     * 将字符串转换成链表。
     * 123=> 3-2-1
     * 
     * @param val
     * @return
     */
    private ListNode getListNodeFromString(String val) {
        ListNode listNode = null;
        char[] chs = val.toCharArray();
        for (char ch : chs) {
            listNode = new ListNode(ch - '0', listNode);
        }
        return listNode;
    }

    /**
     * 链表乘以单个字符
     * 注意 head1 的 val 不能改
     * 
     * @param head1
     * @param ch       当前乘以的字符
     * @param zeroNums 需要前面补 0 的个数
     * @return
     */
    ListNode getMultiplyBySingleListNode(ListNode head1, char ch, int zeroNums) {
        ListNode dummy = new ListNode(-1);
        ListNode p = head1;
        ListNode node = dummy;
        while (p != null) {
            node.next = new ListNode(p.val * (ch - '0'));
            p = p.next;
            node = node.next;
        }
        // 处理 >10 的字符
        node = dummy.next;
        handleOversizeNodes(node);
        for (int i = 0; i < zeroNums; i++) {
            node = new ListNode(0, node);
        }
        return node;
    }

    void handleOversizeNodes(ListNode head) {
        ListNode p = head, next;
        while (p != null) {
            if (p.val >= 10) {
                next = p.next;
                if (next == null) {
                    p.next = next = new ListNode(0);
                }
                next.val += p.val / 10;
                p.val %= 10;
            }
            p = p.next;
        }
    }

    /**
     * 将多个链表相加
     * 
     * @param heads
     * @return
     */
    ListNode getMultiLinkedListSum(ListNode[] heads) {
        if (heads == null || heads.length < 1) {
            return null;
        }
        if (heads.length == 1) {
            return heads[0];
        }
        ListNode head = heads[0];
        for (int i = 1; i < heads.length; i++) {
            addTwoNumNode(head, heads[i]);
        }
        return head;
    }

    void addTwoNumNode(ListNode head1, ListNode head2) {
        ListNode p1 = head1, p2 = head2;
        ListNode prev = p1;
        while (p1 != null && p2 != null) {
            p1.val += p2.val;
            prev = p1;
            p1 = p1.next;
            p2 = p2.next;
        }
        if (p1 == null && prev != null && p2 != null) {
            prev.next = p2;
        }
        handleOversizeNodes(head1);
    }

    /**
     * 172. 阶乘后的零
     * 给定一个整数 n ，返回 n! 结果中尾随零的数量。
     * 
     * 提示 n! = n * (n - 1) * (n - 2) * ... * 3 * 2 * 1
     * 
     * @param n
     * @return
     */
    public int trailingZeroes(int n) {
        /*
         * 解题思路请参见 digit.md 中的有关阶乘的描述
         */
        long dividor = 5;
        int res = 0;
        while (dividor <= n) {
            res += n / dividor;
            dividor *= 5;
        }
        return res;
    }

    public long trailingZeros2(long n) {
        long res = 0;
        for (long dividor = 5; dividor <= n; dividor *= 5) {
            res += n / dividor;
        }
        return res;
    }

    /**
     * 793. 阶乘函数后 K 个零。这道题被标记为 hard
     * 给定 k，找出返回能满足 f(x) = k 的非负整数 x 的数量
     * 
     * @param k
     * @return
     */
    public int preimageSizeFZF(int k) {
        /**
         * 解题思路请参见
         * digit.md 文档中的描述
         */
        // if (k == 0) {
        // return 4;
        // }
        long left = leftBound(k);
        long right = rightBound(k);
        return (int) (right - left + 1);
    }

    long leftBound(int target) {
        long left = 0;
        long right = Long.MAX_VALUE - 1;
        while (left <= right) {
            long mid = left + (right - left) / 2;
            long midVal = trailingZeros2(mid);
            if (midVal > target) {
                right = mid - 1;
            } else if (midVal < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    long rightBound(int target) {
        long left = 0;
        long right = Long.MAX_VALUE - 1;
        while (left <= right) {
            long mid = left + (right - left) / 2;
            long midVal = trailingZeros2(mid);
            if (midVal > target) {
                right = mid - 1;
            } else if (midVal < target) {
                left = mid + 1;
            } else {
                left = mid + 1;
            }
        }
        return right;
    }

    public static void main(String[] args) {
        DigitProblems instance = new DigitProblems();
        // System.out.println(instance.isPalindrome(121));
        // System.out.println(instance.isPalindrome(1874994781));
        ListNode head = instance.getListNodeFromString("123");
        // head = instance.getMultiplyBySingleListNode(head, '5', 1);
        // ListNodeUtil.printLinkedNode(head);
        // ListNode head2 = instance.getListNodeFromString("456");
        // instance.addTwoNumNode(head, head2);
        // ListNodeUtil.printLinkedNode(head);

        System.out.println(instance.multiply2("123", "456"));
        // System.out.println(instance.multiply("9", "9"));
        // System.out.println(instance.multiply("9", "99"));
        // System.out.println(instance.multiply("9133", "0"));
        // System.out.println(instance.multiply("408", "5"));

        // System.out.println(instance.trailingZeroes(5));
        // System.out.println(instance.trailingZeroes(6));
        // System.out.println(instance.trailingZeroes(24));
        // System.out.println(instance.trailingZeros2(25));
        // System.out.println(instance.trailingZeros2(125));

        System.out.println(instance.preimageSizeFZF(0));
        System.out.println(instance.preimageSizeFZF(5));
        System.out.println(instance.preimageSizeFZF(3));
    }
}
