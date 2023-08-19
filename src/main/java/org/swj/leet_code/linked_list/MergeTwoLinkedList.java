package org.swj.leet_code.linked_list;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/08/16 11:27
 *        环形链表
 */
public class MergeTwoLinkedList {
    /**
     * leetcode 第 21 题，合并两个有序链表
     */

    ListNode mergeTwoList(ListNode head1, ListNode head2) {
        ListNode dummy = new ListNode(-1);
        ListNode p = dummy;
        ListNode p1 = head1;
        ListNode p2 = head2;

        while (p1 != null && p2 != null) {
            if (p1.val < p2.val) {
                p.next = p1;
                p1 = p1.next;
            } else {
                p.next = p2;
                p2 = p2.next;
            }
            // p 指针不断向前
            p = p.next;
        }
        if (p1 != null) {
            p.next = p1;
        } else if(p2 != null) {
            p.next = p2;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        ListNode head1 = ListNodeUtil.convertToNodeListFromArray(new int[] {1,2,4});
        ListNode head2 = ListNodeUtil.convertToNodeListFromArray(new int[] {1,3,4});
        MergeTwoLinkedList instance = new MergeTwoLinkedList();
        ListNode mergedHead = instance.mergeTwoList(head1, head2);
        ListNodeUtil.printLinkedNode(mergedHead);
    }

}
