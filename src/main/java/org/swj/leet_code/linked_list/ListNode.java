package org.swj.leet_code.linked_list;

public class ListNode {

    public int val;
    public ListNode next;
    
    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
