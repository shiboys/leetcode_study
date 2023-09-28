package org.swj.leet_code.linked_list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 其他链表leetcode 题目
 */
public class LinkedListOther {

    /**
     * AddTwoNumbers 类中的这个解法 太 low 了，时间复杂度也非常高，空间复杂度也高，这里转换一下思路。
     * 仍然是将两个链表合并成一个链表，合并方法时将链表的相同遍历顺序节点的值相加，得到一个新链表，
     * 然后遍历新链表，将新链表的节点的值 >= 10 的节点进行转移到下一个节点
     * 
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) {
            return null;
        }
        ListNode dummp = new ListNode(-1);
        ListNode p = dummp;
        ListNode p1 = l1;
        ListNode p2 = l2;
        while (p1 != null && p2 != null) {
            p.next = new ListNode(p1.val + p2.val);
            p1 = p1.next;
            p2 = p2.next;
            p = p.next;
        }
        if (p1 != null) {
            p.next = p1;
        } else if (p2 != null) {
            p.next = p2;
        }
        p = dummp.next;

        while (p != null) {
            if (p.val >= 10) {
                handleOversizeNode(p);
            }
            p = p.next;
        }
        return dummp.next;
    }

    void handleOversizeNode(ListNode node) {
        ListNode next = node.next;
        if (next == null) {
            node.next = next = new ListNode(0);
        }
        node.val -= 10;
        next.val += 1;
    }

    /**
     * leetcode 138 题 随机链表的复制
     * 给你一个长度为 n 的链表，每个节点包含一个额外增加的随机指针 random ，该指针可以指向链表中的任何节点或空节点。
     * 构造这个链表的 深拷贝。 深拷贝应该正好由 n 个 全新 节点组成，其中每个新节点的值都设为其对应的原节点的值。
     * 新节点的 next 指针和 random
     * 指针也都应指向复制链表中的新节点，并使原链表和复制链表中的这些指针能够表示相同的链表状态。复制链表中的指针都不应指向原链表中的节点 。
     * 例如，如果原链表中有 X 和 Y 两个节点，其中 X.random --> Y 。那么在复制链表中对应的两个节点 x 和 y ，同样有 x.random
     * --> y 。
     * 返回复制链表的头节点。
     * 分析过程
     * 这道题我想了半天，也想到用 map 做辅助来做，后来一想使用 map 空间复杂度肯定高，就放弃了。
     * map 的解决方式是 借助哈希表把原始节点和克隆节点的映射存储起来，然后把克隆节点的结构连接起来即可。
     * 但是其他想法我想了半天，实在是没想起来。最后参考了别的想法，一点就会
     * 就是将每个节点深复制自己放到自己的next 指针上。然后再设置 random 属性
     * 非常类似初高中的几何做辅助线，千变万化还是那一套：
     * 链表分裂成两个，两个链表合成一个，链表在同一条链上复制自己 double 下
     * 快慢指针， 间距(前后)指针，
     * 还是自己思维狭窄了
     * 具体 例子请参考 linkedlist.md 中有关随机链表复制的介绍
     * 
     * @param head
     * @return
     */

    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        Node p = head;
        Node copyP = null;
        while (p != null) {
            copyP = new Node(p.val);
            Node pNext = p.next;
            p.next = copyP;
            copyP.next = pNext;
            p = pNext;
        }
        // 链表被 double 了
        // 设置被复制节点的 random
        p = head;
        while (p != null && p.next != null) {
            if (p.random != null) {
                p.next.random = p.random.next;
            }
            p = p.next.next;
        }
        // 切分链表
        Node oldPtr = head;
        Node newHead = head.next;
        Node newPtr = newHead;
        while (oldPtr != null && oldPtr.next != null) {
            if (oldPtr.next != null) {
                oldPtr.next = oldPtr.next.next;
                oldPtr = oldPtr.next;
            }

            if (newPtr.next != null) {
                newPtr.next = newPtr.next.next;
                newPtr = newPtr.next;
            }
        }
        return newHead;
    }

    static class Node {
        public int val;
        public Node next;
        public Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    /**
     * leetcode 82 题，删除链表中的重复元素
     * 
     * @param head
     * @return
     */
    public ListNode deleteDuplicates(ListNode head) {
        ListNode prev = head, p1 = head, p2 = head;
        boolean hasRepeated = false;
        while (p1 != null) {
            p2 = p1.next;
            hasRepeated = false;
            while (p1 != null && p2 != null && p1.val == p2.val) {
                p2 = p2.next;
                hasRepeated = true;
            }
            if (hasRepeated) {
                // p1 重新指向下一个不重复的元素
                if (p1 == head) { // 如果首节点就重复，则更新 head 节点
                    head = p2;
                }
                p1 = p2;
                // 直接删除 [p1,p2) 之间的重复元素
                prev.next = p1;
            } else { // 只有没有重复的情况下 才正常移动 prev 和 p1 指针。
                prev = p1;
                p1 = p1.next;
            }
        }
        return head;
    }

    /**
     * 删除重复节点 removeDuplicates 的递归实现
     * 递归实现，从理解角度来看更容易
     * 
     * @param head
     * @return 第一个不重复的节点
     */
    ListNode removeDuplicatesRecursively(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        if (head.next != null && head.val != head.next.val) {
            head.next = removeDuplicatesRecursively(head.next);
            return head;
        }
        while (head.next != null && head.val == head.next.val) {
            head = head.next;
        }
        // 此时 head.next 为下一个值不同的节点
        return removeDuplicatesRecursively(head.next);
    }

    /**
     * leetcode 1836，从未排序的链表中删除重复的元素
     * 输入：head = [1,2,3,2]
     * 输出：[1,3]
     * 解释：2 在链表中出现了两次，所以所有的 2 都需要被删除。删除了所有的 2 之后，我们还剩下 [1,3]。
     */
    ListNode removeDuplicates2(ListNode head) {
        // 我自己的解法，仍然是用双指针， 一前一后，用 Map<Integer,Integer> 记录重复元素的重复次数，
        // 后来发现这个想法不鲁邦，还是老老实实的地遍历两遍

        // 具体方法还是借助 虚拟节点 dummy;
        ListNode p = head;
        Map<Integer, Integer> mapCounter = new HashMap<>();
        while (p != null) {
            mapCounter.put(p.val, mapCounter.getOrDefault(p.val, 0) + 1);
            p = p.next;
        }
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        p = dummy;
        while (p != null) {
            // 删掉重复节点
            while (p.next != null && mapCounter.get(p.next.val) > 1) {
                p.next = p.next.next;
            }
            p = p.next;
        }

        return dummy.next;
    }

    /**
     * leetcode 264 题，寻找第 n 个丑数
     * 丑数 就是只包含质因数 2、3 和/或 5 的正整数
     * 分析：这道题很精妙，你看着它好像是道数学题，它实际上却是一个合并多个有序链表的问题，
     * 如果一个数 x 是丑数，那么 x*2,x*3,x*5 也一定是丑数，
     * 我们把 所有的丑数想象成一个从小到大的链表，就是这样个样子
     * 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 8 -> ...
     * 然后，我们可以把丑数分为 3 类：2的倍数，3 的倍数，5 的倍数。（1作为特殊丑数，放在开头），就像 3 个链表一样
     * 我们其实就是想把这三条「有序链表」合并在一起去重，合并的结果就是丑数的序列。最终求这个有序链表中第 n 个元素是什么。
     * 所以这里就和链表中 合并 k 条有序链表思路基本一样了。
     * 
     * @param n
     * @return
     */
    public int nthUglyNumber(int n) {
        int[] ugly = new int[n + 1];
        // 相当于 是 三个有序链表的指针
        int p2 = 1, p3 = 1, p5 = 1;
        // 相当于链表的节点的 val
        int product2 = 1, product3 = 1, product5 = 1;
        // 新链表指针
        int p = 1;
        for (; p <= n; p++) {
            int min = Math.min(product5, Math.min(product3, product2));
            ugly[p] = min;
            // 下面这种逻辑，计算丑数是错误的。

            // if (min == product2) {
            // product2 = 2 * p2;p2==7 时，但是 14 不是丑数， 因此这送算法是错误的
            // p2++;
            // }
            // if (min == product3) {
            // product3 = 3 * p3;
            // p3++;
            // }
            // if (min == product5) {
            // product5 = 5 * p5;
            // p5++;
            // }

            if (min == product2) {
                // 从之前的成员中查找，2乘以数组第二个成员，第三个成员，第四个成员，第 n 个成员
                product2 = 2 * ugly[p2];
                p2++;
            }
            if (min == product3) {
                product3 = 3 * ugly[p3]; // 同理 3 也是继续乘以第 个成员
                p3++;
            }
            if (min == product5) {
                product5 = 5 * ugly[p5];
                p5++;
            }
        }
        return ugly[n];
    }

    /**
     * leetcode 378 题
     * 有序矩阵中第 k 小的元素
     * 给你一个 nxn 的矩阵 matrix, 其中每行和每列元素均按照升序排序，找到矩阵中第 k 小的元素。
     * 请注意，它是 排序后 的第 k 小元素，而不是第 k 个不同的元素。
     * 你必须找到一个内存复杂度优于 N(n^2) 的解决方案。
     * 
     * 思路：其实这道题跟 23 合并 k 个升序链表思想一致。
     * 矩阵中的每一行都是排好序的，就好比有多条链表，你用优先级队列施展合并多条有序链表的逻辑就能找到第 k 小的元素
     * 
     * @param martix
     * @return
     */
    public int kthSmallestMatrix(int[][] matrix, int k) {
        if (k < 1) {
            return Integer.MIN_VALUE;
        }
        int n = matrix.length;
        // 使用优先级队列将每一行的第一个元素先装入队列
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> {
            return a[0] - b[0];
        });
        for (int i = 0; i < n; i++) {
            // 除了元素装入队列之外，还要装入元素所在的行，该元素再改行的索引。
            queue.offer(new int[] { matrix[i][0], i, 0 });
        }
        int target = -1;
        while (!queue.isEmpty() && k > 0) {
            int[] arr = queue.poll();
            target = arr[0];
            int rowIndex = arr[1];
            int colIndx = arr[2];
            if (colIndx < n - 1) {
                queue.offer(new int[] { matrix[rowIndex][colIndx + 1], rowIndex, colIndx + 1 });
            }
            k--;
        }
        return target;
    }

    /**
     * leetcode 373 寻找和最小的 k 对数。
     * 给定两个以 非递减顺序排列 的整数数组 nums1 和 nums2 , 以及一个整数 k 。
     * 定义一对值 (u,v)，其中第一个元素来自 nums1，第二个元素来自 nums2 。
     * 请找到和最小的 k 个数对 (u1,v1), (u2,v2) ... (uk,vk) 。
     * 
     * 示例 1:
     * 输入: nums1 = [1,7,11], nums2 = [2,4,6], k = 3
     * 输出: [1,2],[1,4],[1,6]
     * 解释: 返回序列中的前 3 对数：
     * [1,2],[1,4],[1,6],[7,2],[7,4],[11,2],[7,6],[11,4],[11,6]
     * 
     * @param nums1
     * @param nums2
     * @param k
     * @return
     */
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        Queue<int[]> queue = new PriorityQueue<>((a, b) -> {
            return a[0] + a[1] - (b[0] + b[1]);
        });
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums2.length; i++) {
            // 将 nums1[i],nums2[j], i 这几个关键信息放入队列。
            queue.offer(new int[] { nums1[0], nums2[i], 0 });
        }

        while (!queue.isEmpty() && k > 0) {
            int[] arr = queue.poll();
            res.add(Arrays.asList(arr[0], arr[1]));
            k--;
            int idx = arr[2];
            if (idx < nums1.length - 1) {
                queue.offer(new int[] { nums1[idx + 1], arr[1], idx + 1 });
            }
        }
        return res;
    }

    /**
     * leetcode 88 题，合并两个有序数组
     * 给你两个按 非递减顺序 排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n ，分别表示 nums1 和 nums2 中的元素数目。
     * 请你 合并 nums2 到 nums1 中，使合并后的数组同样按 非递减顺序 排列。
     * 
     * 注意：最终，合并后数组不应由函数返回，而是存储在数组 nums1 中。为了应对这种情况，nums1 的初始长度为 m + n，其中前 m
     * 个元素表示应合并的元素，后 n 个元素为 0 ，应忽略。nums2 的长度为 n
     * 
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void mergeTwoSortedArray(int[] nums1, int m, int[] nums2, int n) {
        /**
         * 这道题总体的解决思路，1：使用双指针，2：使用优先级队列
         * 数组由于是原地填充 nums1，为了避免覆盖 nums1 的元素，则使用倒退法
         * 
         */
        int p = m + n - 1;
        int n1 = m - 1;
        int n2 = n - 1;

        while (p >= 0 && n1 >= 0 && n2 >= 0) {
            nums1[p] = Math.max(nums1[n1], nums2[n2]);
            if (nums1[p] == nums1[n1]) {
                n1--;
            } else {
                n2--;
            }
            p--;
        }

        if (n2 >= 0) {
            nums1[p] = nums2[n2];
            n2--;
            p--;
        }
        // n1 不用管，因为本来就是更新的 nums1
    }

    /**
     * 有序数组的平方，leetcode 977 题
     * 给你一个按 非递减顺序 排序的整数数组 nums，返回 每个数字的平方 组成的新数组，要求也按 非递减顺序 排序。
     * 示例 1：
     * 
     * 输入：nums = [-4,-1,0,3,10]
     * 输出：[0,1,9,16,100]
     * 解释：平方后，数组变为 [16,1,0,9,100]
     * 排序后，数组变为 [0,1,9,16,100]
     * 
     * @param nums
     * @return
     */
    public int[] sortedSquares(int[] nums) {
        // 带负数的平方的特性是两边的平方都比中间的大，
        // 所以解决办法跟上一题基本一样，就是新数组指针从大到小递减，然后原数组前后双指针
        int left = 0, right = nums.length - 1;
        int p = right;
        int[] res = new int[right + 1];
        final int square = 2;
        while (left < right) {
            if (Math.pow(nums[left], square) > Math.pow(nums[right], square)) {
                res[p] = (int) Math.pow(nums[left], square);
                left++;
            } else {
                res[p] = (int) Math.pow(nums[right], square);
                right--;
            }
            p--;
        }
        return res;
    }

    /**
     * leetcode 360 题，有序转化数组。
     * 给你一个已经排好序的整数数组 nums 和整数 `a,b,c`。对于数组中的每一个元素 nums[i], 计算函数值 f(x)=ax2 + bx + c
     * ，请按升序返回结果数组。
     * 实例1：
     * 输入：nums = [-4,-2,2,4], a = 1, b = 3, c = 5
     * 输出：[3,9,15,33]。解释 1*(-4^2) + 3*(-4) + 5 = 9, 就是这样
     * 
     * 
     * 这道题本质并不是很难，但是细节比较多。下面一一道来
     * 上一题 977 其实就是这道题的 a=1,b=0,c=0 的特殊情况，所以这道题的关键也是在 nums 的开头和结尾设置i,j 双指针相向而行，
     * 执行合并有序数组的逻辑，只不过这里要考虑的情况更多
     * 
     * 我们中学都学过这种二次函数，图像就是一个抛物线，写个函数来表，比如函数 f
     * nums[i] 就好像坐标中的 x 轴，那么 f[nums[i]] 之间的关系就取决于抛物线的对称轴位置以及抛物线的开头方向(系数 a 的正负)。
     * 如果 nums 中的元素全部落在抛物线一侧，则这些元素本身就是有序递增或者递减的，根据开口的方向做判断可以了，比较容易处理
     * 系数 a 为正时，抛物线的开口向上，中间的元素函数求值最小
     * 系数 a 为负时，抛物线的开口朝下，中间的元素函数求值最大
     * 
     * @param nums
     * @param a
     * @param b
     * @param c
     * @return
     */
    public int[] sortTransformedArray(int[] nums, int a, int b, int c) {
        int[] res = new int[nums.length];
        int left = 0, right = nums.length - 1;
        // 因为左右指针相向而行， a > 0 时，抛物线开口朝下，越往中间，数值越小，因此新数组的指针 p 需要递减，
        // 反之，抛物线的开口朝上，越往中间值越大，p 指针需要从递增。
        int p = a > 0 ? nums.length - 1 : 0;
        while (left <= right) {
            int lv = f(nums[left], a, b, c);
            int rv = f(nums[right], a, b, c);
            if (a > 0) {
                if (lv < rv) {
                    res[p] = rv;
                    right--;
                } else {
                    res[p] = lv;
                    left++;
                }
                p--;
            } else {
                if (lv < rv) {
                    res[p] = lv;
                    left++;
                } else {
                    res[p] = rv;
                    right--;
                }
                p++;
            }
        }

        return res;
    }

    int f(int x, int a, int b, int c) {
        return a * x * x + b * x + c;
    }

    public static void main(String[] args) {
        LinkedListOther instance = new LinkedListOther();
        // testLinkedList(instance);
        System.out.println(instance.nthUglyNumber(11));

        int[][] matrix = new int[][] { { 1, 5, 9 }, { 10, 11, 13 }, { 12, 13, 15 } };
        System.out.println(instance.kthSmallestMatrix(matrix, 8));
        System.out.println(instance.kthSmallestMatrix(matrix, 6));

        int[] nums1 = { 1, 7, 11 };
        int[] nums2 = { 2, 4, 6 };
        System.out.println(instance.kSmallestPairs(nums1, nums2, 3));

        nums1 = new int[] { 1, 2, 3, 0, 0, 0 };
        nums2 = new int[] { 2, 5, 6 };
        instance.mergeTwoSortedArray(nums1, 3, nums2, 3);
        System.out.println(Arrays.toString(nums1));

        nums1 = new int[] { -4, -1, 0, 3, 10 };
        System.out.println((int) Math.pow(-4, 2));
        System.out.println(Arrays.toString(instance.sortedSquares(nums1)));

        nums1 = new int[] { -4, -2, 2, 4 };
        System.out.println(Arrays.toString(instance.sortTransformedArray(nums1, 1, 3, 5)));
    }

    private static void testLinkedList(LinkedListOther instance) {
        ListNode head1 = ListNodeUtil.convertToNodeListFromArray(new int[] { 2, 4, 3 });
        ListNode head2 = ListNodeUtil.convertToNodeListFromArray(new int[] { 5, 6, 4 });
        ListNode head = instance.addTwoNumbers(head1, head2);
        ListNodeUtil.printLinkedNode(head);

        head1 = ListNodeUtil.convertToNodeListFromArray(new int[] { 9, 9, 9, 9, 9, 9, 9 });
        head2 = ListNodeUtil.convertToNodeListFromArray(new int[] { 9, 9, 9, 9 });

        head = instance.addTwoNumbers(head1, head2);
        ListNodeUtil.printLinkedNode(head);

        // [7,null],[13,0],[11,4],[10,2],[1,0]
        // testNode(instance);

        // head1 = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 3, 3, 4, 4,
        // 5 });
        // head2 = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 1, 1, 2, 3 });
        // head = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 3, 3, 4, 4
        // });

        // ListNodeUtil.printLinkedNode(instance.removeDuplicatesRecursively(head1));
        // ListNodeUtil.printLinkedNode(instance.removeDuplicatesRecursively(head2));
        // ListNodeUtil.printLinkedNode(instance.removeDuplicatesRecursively(head));

        head1 = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 3, 2 });
        head2 = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 1, 1, 2, 3 });
        head = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 2, 2, 2, 3, 2, 4, 0, 4 });

        ListNodeUtil.printLinkedNode(instance.removeDuplicates2(head1));
        ListNodeUtil.printLinkedNode(instance.removeDuplicates2(head2));
        ListNodeUtil.printLinkedNode(instance.removeDuplicates2(head));
    }

    private static void testNode(LinkedListOther instance) {
        Node node7 = new Node(7);
        Node node13 = new Node(13);

        Node node11 = new Node(11);
        Node node10 = new Node(10);
        Node node1 = new Node(1);

        node7.next = node13;

        node13.next = node11;
        node13.random = node7;
        node11.next = node10;
        node11.random = node1;
        node10.next = node1;
        node10.random = node11;

        node1.random = node7;

        printNode(node7);

        Node newHead = instance.copyRandomList(node7);
        printNode(newHead);
    }

    static void printNode(Node head) {
        Node p = head;
        System.out.println("print copy random node");
        while (p != null) {
            System.out.print(String.format("[%s,%s],", p.val, p.random != null ? p.random.val : "null"));
            p = p.next;
        }
        System.out.println();
    }
}
