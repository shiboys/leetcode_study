package org.swj.leet_code.linked_list;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/17 15:27
 *        翻转链表
 */
public class ReverseLinkList {
    /**
     * 链表反转，使用递归解法，比价容易理解
     * 
     * @param head
     * @return
     */
    ListNode reverseNode(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode reverseHead = reverseNode(head.next);
        // 不能用 reverseHead，因为 reverseHead 是翻转后的头结点
        // head.next.next = head，只能在递归中使用，再循环中使用，会造成死循环。

        // 将 head 的next 属性摘除，这个目的是为了实现原链表的首节点变成尾结点后 next == null
        // 而 普通节点的 next 属性被摘除之后，下次递归后的 head.next.next = head; 会重新设置回来
        head.next.next = head;
        head.next = null;
        return reverseHead;
    }

    /**
     * 翻转链表，循环解法
     * 
     * @param head
     * @return
     */
    ListNode reverseByLoop(ListNode head) {
        ListNode p = head;
        ListNode prev = null;
        while (p != null) {
            ListNode pNext = p.next;
            p.next = prev;

            prev = p;
            p = pNext;
        }
        return prev;
    }

    ListNode successor;

    /**
     * 反转前 n 个链表节点，n 小于 链表的长度
     * 跟反转整个链表差不多。
     * 
     * @param head 头结点
     * @param n    前 n 个
     * @return 反转后链表的头结点
     */
    ListNode reverseN(ListNode head, int n) {
        if (head == null) {
            return head;
        }
        if (n == 1) {
            // 使用 successor 成员变量记录第 n+1 个节点，并作为翻转后最后一个节点的 next 连起来
            successor = head.next;
            return head;
        }
        // 以 head.next 为起点，需要反转前 n-1 个节点
        ListNode reversedHead = reverseN(head.next, n - 1);
        // head.next.nexst = head 这个是递归的用法，迭代的用法就是像上面的 node.next = prev;
        head.next.next = head;
        // 让反转后的 head 节点和 后面的节点链起来。这里有点费解，其实这一步应该是在翻转到原来的头结点也就是翻转后的尾结点处设置，
        // 但是 head 节点已经让我们改掉了。所以只能每次都将 head 的 next 指向 successor，
        // 并且在下一次的递归返回后将 next 改为正确的指向，直到最原始的 head 节点
        head.next = successor;
        return reversedHead;
    }

    /**
     * 翻转前 n 个链表的循环写法
     * 
     * @param head
     * @param n
     * @return
     */
    ListNode reverseN2(ListNode head, int n) {
        ListNode p = head;
        ListNode newSuccessor = null;
        ListNode prev = null;
        for (int i = 1; i <= n; i++) {
            if (p == null) {
                return prev;
            }
            ListNode next = p.next;
            if (i == n) {
                newSuccessor = next;
            }
            p.next = prev;
            prev = p;
            p = next;
        }
        head.next = newSuccessor;
        return prev;
    }

    ListNode reverseBetween(ListNode head, int m, int n) {
        // base case
        if (m == 1) {
            return reverseN(head, n);
        }
        // 前进到反转的起点触发 base case
        // 这点碉堡了，递归威力太大了
        head.next = reverseBetween(head.next, m - 1, n - 1);
        return head;
    }

    /**
     * 将 nodeA 与 NodeB 的前一个节点之间的所有节点反转，区间为左闭右开 [nodeA,nodeB) 也就是不包括 nodeB。
     * 
     * @param nodeA
     * @param nodeB 闭区间节点，不包含 nodeB
     * @return
     */
    ListNode reverseNodeBetweenByLoop(ListNode nodeA, ListNode nodeB) {
        ListNode p = nodeA;
        ListNode prev = null;
        while (p != nodeB && p != null) {
            ListNode nextNode = p.next;
            // 在 [m,n] 这样的区间反转，不能写下面这句代码逻辑，否则会更高 n+1 处节点的 next 指针，使链表断裂..
            // p.next.next = p;
            p.next = prev;

            prev = p;
            p = nextNode;
        }
        return prev; // 这里返回之后，其实链表也是断裂的 nodeA 和 nodeB 反转交换位置之后，nodeA作为末尾节点的 next 为 null
    }

    ListNode reverseKGroup(ListNode head, int k) {
        if (k <= 1 || head == null) {
            return head;
        }
        ListNode p = head;
        ListNode n = p;
        for (int i = 0; i < k; i++) {
            if (n == null) {// 不够 k 个, 不需要反转，则返回 head。对应着 base case
                return head;
            }
            n = n.next;
        }
        // 将 p 和 n 的前一个节点之间的所有节点交换，刚好 k 个节点
        ListNode reversedHead = reverseNodeBetweenByLoop(p, n);
        // 开始 n-1 轮的递归，新的 head 为
        // 注意 n 节点的指针并没有被改变，n 仍然指向第 k+1 个节点，所以这里可用使用 n 作为下次递归的新的起点
        // 翻转后，p 在翻转后的子链表的尾结点
        p.next = reverseKGroup(n, k);

        return reversedHead;
    }

    ListNode left;

    /**
     * leetcode 234 判断链表是否符合回文链表
     * 
     * @param head
     * @return
     */
    boolean isPalindromeLinkList(ListNode head) {
        left = head;
        // return palindromeLinkedListRecursively(head);
        return palindromeLinkedListLoop(head);
    }

    /**
     * 递归判断链表是否符合回文链表；
     * 
     * @param s
     * @param right
     * @return
     */
    boolean palindromeLinkedListRecursively(ListNode right) {
        if (right == null) {
            return true;
        }
        boolean res = palindromeLinkedListRecursively(right.next);
        // 后续遍历代码逻辑
        res = res && (left.val == right.val);
        left = left.next;
        return res;
    }

    /**
     * 循环法判断链表是否为回文联表
     * 
     * @param head
     * @return
     */
    boolean palindromeLinkedListLoop(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        if (slow == head) { // base case 判断，只有 1 个节点
            return true;
        }
        // 如果链表节点为奇数，则需要再前进一步
        if (fast != null) {
            slow = slow.next;
        }
        ListNode right = reversePalindromeList(slow);
        ListNode left = head;
        while (right != null) {
            if (left.val != right.val) {
                return false;
            }
            left = left.next;
            right = right.next;
        }
        return true;
    }

    /**
     * 反转链表的子链表
     * 
     * @param head
     * @return
     */
    ListNode reversePalindromeList(ListNode head) {
        ListNode p = head;
        ListNode prev = null;
        ListNode next = null;
        while (p != null) {
            next = p.next;

            p.next = prev;
            prev = p;
            p = next;
        }
        return prev;
    }

    /**
     * leetcode 第 61 题，旋转链表，一次旋转，将尾结点的链表翻转到头结点
     * 
     * @param head
     * @param k    旋转 k 次
     * @return
     */
    public ListNode rotateRight(ListNode head, int k) {
        if (k < 1) {
            return head;
        }
        ListNode prevTail = null;
        ListNode tail = head;
        // 初步的想法是用 Map<Node,Node> 维护一个向前的指针链表，这样在尾结点变成首节点后，新的尾结点可以从 Map 中取到

        Map<ListNode, ListNode> prevMap = new HashMap<>();
        int size = 0;
        while (tail != null) {
            prevMap.put(tail, prevTail);
            prevTail = tail;
            tail = tail.next;
            size++;
        }
        if (size == 0) {
            return head;
        }
        tail = prevTail;
        prevTail = prevMap.get(tail);
        if (k >= size) {
            System.out.println("k=" + k + ",size=" + size);
            k = k % size;
        }
        for (int i = 0; i < k; i++) {
            // 就像维护双链表一样, 维护 next 指针和 map
            // 尾结点的前一个借的 next 属性摘除
            prevTail.next = null;
            // 尾结点的 prev 指针摘除
            prevMap.put(tail, null);
            // 尾结点 next 指向 头结点
            tail.next = head;
            // 头结点的 prev 指向尾结点
            prevMap.put(head, tail);

            head = tail;
            tail = prevTail;
            prevTail = prevMap.get(prevTail);
        }
        return head;
    }

    static class RotateNode<T> {
        public T val;
        public RotateNode<T> prev;

        public RotateNode(T currNode, RotateNode<T> prevNode) {
            this.val = currNode;
            this.prev = prevNode;
        }

        public RotateNode(T currNode) {
            this.val = currNode;
        }

        public RotateNode() {

        }
    }

    public ListNode rotateRight2(ListNode head, int k) {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return head;
        }

        ListNode oldTail = head;
        int n;
        for (n = 1; oldTail.next != null; n++) {
            oldTail = oldTail.next;
        }
        // 尾结点连上头结点
        oldTail.next = head;

        ListNode newTail = head;
        // 找到第 k 个节点
        for (int i = 0; i < n - 1 - k % n; i++) {
            newTail = newTail.next;
        }
        ListNode newHead = newTail.next;
        // 断开 第 k 个节点的 next
        newTail.next = null;
        // 返回第 k 个节点
        return newHead;
    }

    public static void main(String[] args) {
        ReverseLinkList instance = new ReverseLinkList();
        ListNode head = ListNodeUtil.convertToNodeListFromArray(new int[] { 1, 2, 3, 4, 5, 6 });
        ListNode reverseHead = instance.reverseNode(head);
        ListNodeUtil.printLinkedNode(reverseHead);

        ListNode reverseHead2 = instance.reverseByLoop(reverseHead);

        if (reverseHead2 != null) {
            ListNodeUtil.printLinkedNode(reverseHead2);
        }

        // 此时 reverseHead2 已经是经过 2 次反转重新恢复原样的链表，可以继续测试

        // ListNode reversedNHead = instance.reverseN2(reverseHead2, 1);
        // // 2->1->3->4->5->6
        // ListNodeUtil.printLinkedNode(reversedNHead);
        //
        ListNode casehead = ListNodeUtil.convertToNodeListFromArray(new int[] { 0, 1, 2 });
        ListNode rotateNode = instance.rotateRight(casehead, 4);
        ListNodeUtil.printLinkedNode(rotateNode);
        // ListNode reversedBetweenHead = instance.reverseBetween(reverseHead2, 2, 4);
        // 1 4 3 2 5 6
        // ListNodeUtil.printLinkedNode(reversedBetweenHead)；

        // ListNode temp = reverseHead2;
        // int count = 0;
        // while(temp.next != null && count < 2) {
        // temp = temp.next;
        // count++;
        // }

        // ListNode kGroupNode = instance.reverseKGroup(reverseHead2, 2);
        // ListNodeUtil.printLinkedNode(kGroupNode);
        String s = "上海自来水来自海上ha";
        int[] arr = new int[s.length()];
        int count = 0;
        for (char ch : s.toCharArray()) {
            arr[count++] = (int) ch;
        }
        // ListGenericNode<String> palindromeNode =
        // ListNodeUtil.convertToNodeListFromArray(s.split(""));
        ListNode palindromeNode = ListNodeUtil.convertToNodeListFromArray(arr);
        System.out.println(instance.isPalindromeLinkList(palindromeNode));

    }
}
