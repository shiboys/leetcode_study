package org.swj.leet_code.linked_list;

public class ListNodeUtil {
    static ListNode convertToNodeListFromArray(int[] array) {
        if (array == null || array.length < 1) {
            return null;
        }
        ListNode dummy = new ListNode(-1);
        ListNode p = dummy;
        for (int val : array) {
            p.next = new ListNode(val);
            p = (ListNode) p.next;
        }
        return (ListNode) dummy.next;
    }

    static void printLinkedNode(ListNode head) {
        ListNode p = head;
        while (p != null) {
            System.out.print(p + "\t");
            p = (ListNode) p.next;
        }
        System.out.println();
    }


    static <T> ListGenericNode<T> convertToNodeListFromArray(T[] array) {
        if (array == null || array.length < 1) {
            return null;
        }
        ListGenericNode<T> dummy = new ListGenericNode(null);
        ListGenericNode p = dummy;
        for (T val : array) {
            p.next = new ListGenericNode(val);
            p =  p.next;
        }
        return dummy.next;
    }

    static <T> void printLinkedNode(ListGenericNode<T> head) {
        ListGenericNode<T> p = head;
        while (p != null) {
            System.out.print(p + "\t");
            p = p.next;
        }
        System.out.println();
    }
}
