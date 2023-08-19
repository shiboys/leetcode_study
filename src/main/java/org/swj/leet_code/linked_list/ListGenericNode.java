package org.swj.leet_code.linked_list;

public class ListGenericNode<T> {
    T val;
    ListGenericNode<T> next;
    
    public ListGenericNode(T val) {
        this.val = val;
    }

    public ListGenericNode(T val, ListGenericNode<T> next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
