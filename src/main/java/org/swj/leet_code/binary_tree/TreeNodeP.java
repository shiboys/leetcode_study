package org.swj.leet_code.binary_tree;

public class TreeNodeP {

    public int val;
    public TreeNodeP left;
    public TreeNodeP right;
    public TreeNodeP parent;

    public TreeNodeP() {

    }

    public TreeNodeP(int val) {
        this(val, null, null, null);
    }

    public TreeNodeP(int val, TreeNodeP left, TreeNodeP right, TreeNodeP parent) {
        this.val = val;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }
}
