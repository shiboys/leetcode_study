package org.swj.leet_code.linked_list;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/16 14:27
 *        链表的普通操作
 */
public class LinkedListCommonOperations {
    /**
     * 2、分割链表，leetcode 第 86 题
     * 给你一个链表的头结点 head 和一个特定值 x ，请你对链表进行分割，使得所有的小于 x 的节点都出现在 大于等于 x 的节点之前。
     * 思路：我刚开始看到这个题目，想着怎么移动一个单链表的节点到另外一个节点之前，用数组的那一套前后两个指针遍历然后交换位置，
     * 但是链表不像数组，交换位置必须有前驱结点，这样就 4 个指针了，但是链表是单链表，后面的指针没办法向数组一样通过 -- 操作向前移动
     * 顿时我就陷入了死局当中，脑子一团浆糊了，思路被锁死，想了半个小时，愣是没想出来，没办法看了阿东的解题方案，发现跟合并两个有序链表非常相似
     * 两个有序链表合并为一个链表，如果我们逆向思维，将一个链表从 x 处分割为n个有序链表，然后再把分割后的链表串在一起，不就是一个相对有序的链表吗？
     * 不就达到了题目的要求了吗？总结一句话，还是思路没有打开，链表相关操作的基础不扎实，还是的继续多刷此类的题目，将思路彻底打开，逐步消灭知识盲区
     */

    ListNode partition(ListNode head, int x) {
        ListNode dummy1 = new ListNode(-1);
        ListNode dummy2 = new ListNode(-1);
        ListNode p = head;
        ListNode p1 = dummy1, p2 = dummy2;
        while (p != null) {
            ListNode pNext = p.next;
            p.next = null; // 把原链表的 next 属性摘掉，否则会引起死循环
            if (p.val < x) { // 比目标小的，都要在目标前面。如果是 p.val > x 的判断，则会把排在目标后面的小值节点无法排在前面
                p1.next = p;
                p1 = p1.next;
            } else {
                p2.next = p;
                p2 = p2.next;
            }
            p = pNext;
        }
        // 将两个链表再串起来
        p1.next = dummy2.next;
        return dummy1.next;
    }

    /*
     * 合并 k 个链表升序链表
     * leetcode 23
     * 我在看阿东的解法之前，我之前写过外排序，使用的是归并外排序，将一个大的文件分成 n 个小的文件，
     * 先对小的文件进行内存排序，排序之后写入磁盘，写入之后，数据在每个小的子文件是有序的，剩下的就是将 n 个小的文件外排序组成一个大的有序文件。
     * 然后创建一个长度为 n 的数组
     * 同时数组的每一项需要映射一个文件的读取流，将 n 个文件的第一个要读取的数字写入该数组项，
     * 每次读取花费 O(n) 的时间读取一个最小的元素，将该元素写入总文件。
     * 但是阿东的解法时间复杂度更小，使用了使用了带优先级的二叉堆 PriorityQueue，首次将 k
     * 个链表的首元素加入堆中，然后弹出最小的元素，并将最小的元素的下一个元素重新压入二叉堆
     * 这思路的话，每次存入元素是 O(logn),获取元素是 O(1), 这也是小顶堆的一个非常有用的使用场景。
     * 所以这次我先使用小顶堆，然后再使用外排序的归并。
     * 其实将 n 个有序数组进行排序，也是这个原理
     */

    ListNode mergeKList(ListNode[] lists) {
        // 创建 辅助的二叉堆， PriorityQueue 默认小顶堆
        PriorityQueue<ListNode> queue = new PriorityQueue<>(new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.val - o2.val;
            }
        });

        for (ListNode node : lists) {
            if (node != null) { // 把每个链表的头结点加入优先级队列，加个判断更严谨
                queue.add(node);
            }
        }
        ListNode dummy = new ListNode(-1);
        ListNode p = dummy;
        ListNode node = null;
        while ((node = queue.poll()) != null) {
            ListNode nextNode = node.next;
            node.next = null;// 仍然是把 原始链表的 next 属性摘掉
            p.next = node;
            if (nextNode != null) { // 将 node 的下一个节点压入小顶堆中
                queue.add(nextNode);
            }
            p = p.next;
        }
        return dummy.next;
    }

    /*
     * 上述算法的时间复杂度是多少那？
     * 优先级队列 pq 中的元素个数最多是 k，所以一次 poll 或者 add 的时间复杂度是 O(logK)（poll 方法取走 top 1
     * 元素之后，需要将最后的元素置换过来并下沉）
     * 所有的链表节点都会被加入和弹出 queue，所有算法的整体复杂度度是 O(Nlogk), 其中 k 是链表的条数，N 是这些链表的节点总数。
     * 
     * 下面进行 leetcode 第 9 题，删除单链表的倒数第 k 个元素。
     * 从前往后寻找单链表的第 k 个节点很简单，一个 for 循环就遍历就找到了，但是如何寻找单链表的倒数第 k 个节点那？
     * 我们可能会说，假设链表有 n 各节点，倒数第 k 个节点也就是正数第 n-k+1 个节点，不也是一个 for 循环的事吗？
     * 是的，但是算法题一般只给你一个 ListNode 的头结点代表一条单链表，你不能直接得出这个链表的长度 n，而需要先遍历一遍链表算出 n 的值。
     * 然后再遍历计算第 n-k+1 个节点。
     * 也就是说，这个解法需要遍历两次链表才能到的倒数第 k 个节点
     * 那么，我们能不能只遍历一次链表，就算出倒数第 k 个节点？可以做到的，如果面试的时候被问到这道题，面试官肯定也是希望你给出只需遍历一次的链表解法。
     * 这个解法比较巧妙，假设 k=2 ，思路如下：
     * 首先，我们先让一个指针 p1 指向链表头部节点 head，然后走 k 步，此时的 p1 只需要再在 n-k 步，就能走到链表末尾的指针
     * 趁这个时候，再用一个指针 p2 指向链表的头结点 head，然后让 p1 和 p2 同时向前走，p1 走到链表末尾的空指针时前进了 n-k 步，
     * p2 也走了 n-k 步，此时p2 恰好停留在 倒数第 k 步。
     * leetcode 第 19 题
     */

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (n <= 0) {
            return head;
        }
        ListNode p1 = head;
        for (int i = 0; i < n; i++) {
            if (p1 == null) { // 超出范围了
                return head;
            }
            p1 = p1.next;
        }
        ListNode p2 = head;
        while (p1 != null && p1.next != null) {
            p1 = p1.next;
            p2 = p2.next;
        }
        if (p2 == head) {
            head = head.next;
            p2.next = null;
        } else {
            // 此时 p2 指向 k 节点前面的那个节点。倒数第 n 个节点就是正数第 n-k+1 各节点。
            ListNode nNode = p2.next;
            p2.next = nNode.next; // 将 n 节点从链表上删除
            nNode.next = null;// 摘除倒数第 n 个节点的 next 属性
        }

        return head;
    }

    /*
     * 单链表的中点，leetcode 第 876 题。
     * 问题的关键在于我们无法直接得到单链表的长度 n，常规方法也是先遍历链表计算 n，在遍历一遍得到 n/2 个节点，也就是中间节点。
     * 如果向一次遍历就得到中间节点，也需要耍点小聪明，使用「快慢指针」
     * slow 和 fast 两个指针，slow 前进一步，fast 前进两步，fast 到链表末尾，slow 就指向了链表的中间位置。
     */
    ListNode middleNode(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode p1 = head;
        ListNode p2 = head;
        while (p2 != null && p2.next != null) {
            p1 = p1.next;
            p2 = p2.next.next;
        }
        // p2.next == null ，则表明此时 p2 位于链表末尾节点，则 p1 就是中间接地那
        return p1;
    }

    /*
     * 判断链表是否包含环。leetcode 第 141 题
     * 给你一个链表的头结点 head，判断链表中是否有环。
     * 解题方法还是用上面一个的快慢指针法，如果有环，快的指针一定会跟慢的指针相遇，则此时 slow.next == fast.next
     */

    boolean hasCycle(ListNode head) {
        ListNode p1 = head;
        ListNode p2 = head;
        while (p2 != null && p2.next != null) {
            p1 = p1.next;
            p2 = p2.next.next;
            if (p1 == p2) {
                return true;
            }
        }
        return false;
    }

    /*
     * 环形链表 II，142 题，如果链表中含有环，如何计算这个环的起点
     * 我自己的解决思路：
     * 1、先判断是否是环形链表
     * 2、计算出环形的长度 k。相遇时，快的是 2k，慢的是k
     * 3、前后两个指针，第一个指针先走 k 步，然后第二个指针和第一个指针开始同步向前走，如果此时 第一个指针和第二个指针相等，则当前指针执行的节点就是环的起点
     * 阿东的解法比较巧妙
     * 1、fast 和 slow 两个指针，也是首先来判断是否是环
     * 2.1、如果不是环，则返回 null;
     * 2.2、如果是环，则将 slow 重置为指向 head，此时 slow fast 都以相同的步伐遍历链表，如果相遇则为链表起点
     * 该解法相对我的上述思路，是不用计算环的长度，
     * 其实我的思路的第三步跟阿东的 2.2 是异曲同工，当时没有意识到，就是第一个指针先走 k 步，因为 k 是链表的环的长度，此时 fast 和 slow
     * 都处于在 k 处
     * 因此如果此时将 slow 重置为指向 head，则最终相遇的节点就是环的入口
     */
    ListNode detectedFirstCycleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                break;
            }
        }
        if (fast == null || fast.next == null) {
            // 链表不是环形的
            return null;
        }

        // 此时 slow 和 fast 都是位于 k 处
        // 重置 slow 为 head，然后 slow 和 fast 同步走，则相遇的节点即为环入口
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }

        return slow;
    }

    /*
     * 两个链表是否相交，leetcode 160 题
     * 给你输入两个链表的头结点 headA 和 headB，这两个链表可能存在相交。
     * 如果相交，你的算法返回那个交点；如果没有相交，则返回 null。
     * 如下例子
     * A: a1->a2->c1->c2
     * B: b1->b2->b3->c1->c2
     * 那我们的算法应返回 c1 这个节点。
     * 这个算法直接的想法是用HashSet 记录一个链表的所有节点，然后和另一条链表对比，但这就需要额外的存储空间。
     * 如果不使用额外的存储空间，只使用两个指针，该如何做。
     * 我的思路如下：
     * 1、全部遍历链表A,同时遍历链表 B，看谁先遍历完，则那个链表较短，剩下的继续遍历至结尾则为长度之差 k。
     * 2、将长短两个指针同时重置为各自链表的 head，长的链表先走 k 步，此时 A 和 B 剩下的节点数相同，则 A 和
     * B同时开始相同步伐遍历，相遇的点则为共同链表的交点
     */

    <T> ListGenericNode<T> getIntersectionNode(ListGenericNode<T> headA, ListGenericNode<T> headB) {
        ListGenericNode<T> shortHead = headA;
        ListGenericNode<T> longHead = headB;
        if (headA == null || headB == null) {
            return null;// 没有相交点
        }
        while (shortHead.next != null && longHead.next != null) {
            shortHead = shortHead.next;
            longHead = longHead.next;
        }
        ListGenericNode<T> pNodeA = headA, pNodeB = headB;
        // 如果 shortHead 链表更长，则 pNodeA 开始迭代，A(shortHead) 链表先走
        while (shortHead.next != null) {
            pNodeA = pNodeA.next;
            shortHead = shortHead.next;
        }
        // 如果 longHead 更长，则 pNodeB 开始迭代, B(longHead) 链表先走
        while (longHead.next != null) {
            longHead = longHead.next;
            pNodeB = pNodeB.next;
        }

        // headA is done by shortHeadA point,now start to visit headB
        // and the same as headB if listB is longer than listA
        // 此时 同时 pNodeA 和 pNodeB 开始同时迭代，如果有相交点则如果相加则一定会相交
        while (pNodeA != pNodeB && pNodeA != null) {
            pNodeA = pNodeA.next;
            pNodeB = pNodeB.next;
        }
        return pNodeA;
    }

    /**
     * 这个解法是参考了阿东的解法，更加精妙，具体的解法过程请参加 md 文档
     * 
     * @param <T>
     * @param headA
     * @param headB
     * @return
     */
    <T> ListGenericNode<T> getIntersectionNodeSimplify(ListGenericNode<T> headA, ListGenericNode<T> headB) {
        ListGenericNode<T> pNodeA = headA;
        ListGenericNode<T> pNodeB = headB;
        boolean aConnnectedTob = false, bConnectedToA = false;
        while (pNodeA != null && pNodeA != pNodeB) {
            pNodeA = pNodeA.next;
            pNodeB = pNodeB.next;
            if (pNodeA == null && !aConnnectedTob) {
                pNodeA = headB; // 将 a 指针重新指向 B 链表
                aConnnectedTob = true;
            }
            if (pNodeB == null && bConnectedToA == false) {
                pNodeB = headA;// 将 b 指针重新指向 a 链表
                bConnectedToA = true;
            }
            // 最终如果有相交点，则 pNodeA == pNodeB
        }
        return pNodeA;
    }

    <T> ListGenericNode<T> getIntersectionNodeSimplify2(ListGenericNode<T> headA, ListGenericNode<T> headB) {
        ListGenericNode<T> pNodeA = headA;
        ListGenericNode<T> pNodeB = headB;
        while (pNodeA != pNodeB) {
            if (pNodeA == null) {
                pNodeA = headB; // 将 a 指针重新指向 B 链表
            } else {
                pNodeA = pNodeA.next;
            }
            if (pNodeB == null) {
                pNodeB = headA;// 将 b 指针重新指向 a 链表
            } else {
                pNodeB = pNodeB.next;
            }
            // 最终如果有相交点，则 pNodeA == pNodeB
        }
        return pNodeA;
    }

    /**
     * 移除有序链表的重复元素
     * 
     * @param head
     * @return
     */
    ListNode deleteDuplicateNode(ListNode head) {
        ListNode fast = head, slow = head;
        while (fast != null) {
            if (slow.val != fast.val) {
                slow.next = fast;
                slow = slow.next;
            }
            fast = fast.next;
        }
        // 断开与后面重复元素的连接
        slow.next = null;
        return head;
    }

    public static void main(String[] args) {
        LinkedListCommonOperations instance = new LinkedListCommonOperations();

        // ListNode head = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 4, 3,
        // 2, 5, 2 });
        // int x = 3;

        // ListNodeUtil.printLinkedNode(instance.partition(head, x));

        ListNode head1 = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 4, 5 });
        ListNode head2 = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 3, 4 });
        ListNode head3 = ListNodeUtil.convertToNodeListFromArray(new int[] { 2, 6 });

        ListNode mergedSortedHead = instance.mergeKList(new ListNode[] { head1, head2, head3 });
        ListNodeUtil.printLinkedNode(mergedSortedHead);

        ListNode removedReverseNNodeHead = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 3, 4, 5 });
        ListNode middleNode = instance.middleNode(removedReverseNNodeHead);
        System.out.println("middleNode is " + middleNode.val);
        ListNode newHead = instance.removeNthFromEnd(removedReverseNNodeHead, 2);
        ListNodeUtil.printLinkedNode(newHead);

        // testIsCycle(instance);
        testFirstCycleNode(instance);
        // testIntersectionNode(instance);
        // ListNode duplicateNode = ListNodeUtil.convertToNodeListFromArray(new int[] {
        // 0, 0, 1, 1, 1, 2, 2, 3, 3, 4 });

        // ListNode distinctHead = instance.deleteDuplicateNode(duplicateNode);
        // System.out.println("remove duplicated node:");
        // ListNodeUtil.printLinkedNode(distinctHead);

    }

    private static void testIsCycle(LinkedListCommonOperations instance) {
        ListNode cycleNodeHead = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 0, -4 });
        ListNode tailNode = cycleNodeHead;
        while (tailNode != null && tailNode.next != null) {
            tailNode = tailNode.next;
        }
        // 形成环，
        tailNode.next = cycleNodeHead.next;
        System.out.println("is cycle: " + instance.hasCycle(cycleNodeHead));
        cycleNodeHead = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2 });
        tailNode = cycleNodeHead.next;
        tailNode.next = cycleNodeHead;
        System.out.println("is cycle2: " + instance.hasCycle(cycleNodeHead));
        ListNode node1 = new ListNode(1);
        System.out.println("is cycle3: " + instance.hasCycle(node1));
    }

    static void testFirstCycleNode(LinkedListCommonOperations instance) {
        ListNode cycleNodeHead = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 3, 4 });
        ListNode jointNodeHead = ListNodeUtil.convertToNodeListFromArray(new int[] { 5, 6, 7 });
        ListNode tailNode = cycleNodeHead;
        while (tailNode != null && tailNode.next != null) {
            tailNode = tailNode.next;
        }

        ListNode joinTailNode = jointNodeHead;
        while (joinTailNode != null && joinTailNode.next != null) {
            joinTailNode = joinTailNode.next;
        }

        // 先让两个链表链起来
        tailNode.next = jointNodeHead;
        joinTailNode.next = jointNodeHead;

        ListNode firstCycleNode = instance.detectedFirstCycleNode(cycleNodeHead);
        if (firstCycleNode != null) {
            System.out.println("first cycle node is " + firstCycleNode.val);
        }
    }

    static void testIntersectionNode(LinkedListCommonOperations instance) {
        ListGenericNode<String> headC = ListNodeUtil.convertToNodeListFromArray(new String[] { "c1", "c2" });
        ListGenericNode<String> headA = ListNodeUtil.convertToNodeListFromArray(new String[] { "a1", "a2" });
        ListGenericNode<String> headB = ListNodeUtil.convertToNodeListFromArray(new String[] { "b1", "b2", "b3" });
        ListGenericNode<String> p = headA;
        while (p.next != null) {
            p = p.next;
        }
        p.next = headC;

        p = headB;
        while (p.next != null) {
            p = p.next;
        }
        p.next = headC;

        ListGenericNode<String> jointNode = instance.getIntersectionNode(headA, headB);
        if (jointNode != null) {
            System.out.println("jointNode is:" + jointNode.val);
        } else {
            System.out.println("link A is not intersected with link B。");
        }

        jointNode = instance.getIntersectionNodeSimplify(headA, headB);
        if (jointNode != null) {
            System.out.println("simple jointNode is:" + jointNode.val);
        } else {
            System.out.println("link A is not intersected with link B");
        }

        ListGenericNode<String> headA2 = ListNodeUtil.convertToNodeListFromArray(new String[] { "a1", "a2" });
        ListGenericNode<String> headB2 = ListNodeUtil.convertToNodeListFromArray(new String[] { "b1", "b2", "b3" });
        jointNode = instance.getIntersectionNodeSimplify2(headA2, headB2);
        if (jointNode != null) {
            System.out.println("simple jointNode is:" + jointNode.val);
        } else {
            System.out.println("link A2 is not intersected with link B2");
        }

        if (null != null) {
            System.out.println("null != null");
        } else if (null == null) {
            System.out.println("null == null");
        } else {
            System.out.println("null is equal to null");
        }
    }
}