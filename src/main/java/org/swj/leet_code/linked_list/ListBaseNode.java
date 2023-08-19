package org.swj.leet_code.linked_list;

public class ListBaseNode<T> {
    T val;
    ListBaseNode<T> next;

    public ListBaseNode(T val) {
        this.val = val;
    }

    public ListBaseNode(T val, ListBaseNode<T> next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}