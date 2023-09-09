### 最近公共祖先

#### 寻找一个元素

我们先来看一个寻找公共元素的后序遍历方法

```java
TreeNode find(TreeNode root, int val) {
    if(root == null) {
        return null;
    }
    TreeNode left = find(root.left,val);
    TreeNode right = find(root.right, val);
    // 后续位置看看 root 是不是目标节点
    if(root.val == val) {
        return root;
    }
    // root 不是目标节点，再去左右子树看看哪边找到了
    return left != null ? left : right;
}
```
这段代码相当于我么你先去左右子树查找，然后才去检查 root， 依然可以达到目的，但是效率会进一步下降。**因为这种写法必然会遍历二叉树的每一个节点**。

我们在后续位置遍历，就算是根节点就是目标节点，我们也要去左右子树遍历完所有节点才能判断出来。

最后我们改一下题目，现在不让找值为 val 的节点，而是寻找值为 val1 或者 val2 的节点，函数签名如下：

```java
TreeNode find(TreeNode root, int val1, int val2);
```

这和我们第一次实现的 find 函数基本是一样的，而且我们知道有多种写法，我们选择这样写代码：

```java
// 定义：在以 root 为根的二叉树中寻找值为 val1 或 val2 的节点
TreeNode find(TreeNode root, int val1, int val2) {
    // base case
    if (root == null) {
        return null;
    }
    // 前序位置，看看 root 是不是目标值
    if (root.val == val1 || root.val == val2) {
        return root;
    }
    // 去左右子树寻找
    TreeNode left = find(root.left, val1, val2);
    TreeNode right = find(root.right, val1, val2);
    // 后序位置，已经知道左右子树是否存在目标值
    
    return left != null ? left : right;
}
```

**为什么要写这样一个奇怪的 `find` 函数那？因为最近公共祖先系列问题的解法都是把这个函数作为框架的**。下面我们来看相关题目：

#### 二叉树的最近公共祖先

leetcode 236 题，给你输入一个**不含重复值**的二叉树，以及**存在树中的**两个节点 `p` 和 `q`，请你计算 `p` 和 `q`的最近公共祖先节点。

**如果一个节点能够在它的左右子树中分别找到 `p`和 `q`，则该节点为 `LCA` 节点**。

这就用到之前的的 `find` 函数了，只需要在后序位添加一个判断逻辑，即可改造成寻找最近公共祖先的解法代码：

详情请查看 lowestCommonAncestor 方法

leetcode 1676 题，二叉树的公共祖先 IV：

依然输入一个不包含重复值的二叉树，但这次不是 p 和 q 两个节点了，而是给你输入一个包含若干节点的列表 nodes (这些节点都存在二叉树中)，让你算这些节点的公共祖先

请参考 lowestCommonAncestor 的重载方法

**不过需要注意的是，这两道题的题目都明确告诉我们这些节点必行存在于二叉树中，如果没有这个前提条件，就需要修改代码了**。

比如leetcode 1644 题，二叉树的最近公共祖先 II

给你输入一棵**不含重复值**的二叉树，已经两个节点 `p` 和 `q`，如果 `p` 或者 `q` 不存在与树中，则返回 null，否则 返回 p 和 q 的最近公共祖先节点。

在解决标准的 lca 问题时，我们在 find  函数中有这一段前序位置的代码：

```java
// 前序位置
if(node.val == val1 || node.val == val2) {
    return node;
}
```

这段代码的逻辑是建立在 p 和 q 都是存在于二叉树上的节点，而且这段代码也刚好解决了 p 是 q 的最近公共祖先节点

但对于这道题来说，p 和 q 不一定存在于树中，所以我们不能遇到一个目标值就直接返回，而应该是对二叉树进行**完全搜索**（遍历每一个节点），如果发现 `p` 或者 `q` 不存在树中，那么久不存在 LCA 节点。

怎么解决那？回想二叉树的遍历，只有后续遍历才会先把所有节点遍历完 才处理业务逻辑

因此把上面处理前序的代码改变成处理后续的就行

leetcode 235 题，BST 中寻找最小公共祖先

给你输入一棵不包含重复值的 **二叉搜索树**, 以及**存在于树中**的两个节点 `p` 和 `q`, 请你计算 p 和 q 的最近公共祖先节点。

把之前的解法代码复制过来肯定也是可以解决这道题的，但是没有用到 BST 的 「左大右小」的性质，显然效率不是最高的。

在标准的 LCA 中，我们要在后续位置通过左右子树的搜索结果判断当前节点是不是 LCA ：

```java
TreeNode left = find(root.left, val1, val2);
TreeNode right = find(root.right, val1, val2);

// 后序位置，判断当前节点是不是 LCA 节点
if (left != null && right != null) {
    return root;
}
```

**但对于 BST 来说，根本不需要老老实实去遍历子树，由于 BST 左小右大的性质，将当前节点的值与 val1 和 val2 做对比即可判断当前节点是不是 LCA（前提得是后续遍历，因为只有后续遍历，才会形成自底向上的递归，才能获取最小公共祖先节点）。

假设 val1 < val2, 那么 `val1 <= root.val <= val2`, 则说明当前节点就是 LCA ；否则需要根据情况去左子树还是右子树中判断
如果 root.val < val1 则 去 左子树遍历，如果 root.val > val2 ，则去右子树判断。

最后一道最近公共祖先的题目，leetcode 1650 「二叉树的最近公共祖先III」，这次输入的二叉树比较特殊，包含指向父节点的指针
```java
class Node {
    int val;
    Node left;
    Node right;
    Node parent;
}
```

给你输入一棵存在于二叉树中的两个节点 `p` 和 `q`，请你返回他们的最近公共祖先。

这道题其实不是公共祖先的问题，而是单链表相交的问题，你把 `parent` 指针想象成单链表的 next 指针，题目就变成了 

给你输入两个单链表的头结点 p 和 q，这两个链表必然相交，请你返回相交点

![带父节点指针的最小公共祖先](../../algorithm/dynamic_programming/imgs/bst_with_parent_pointer5.png)

我们在前面的单链表基础操作中讲过，求两个橡胶链表的交点，具体思路就不再重复，直接给出代码，参看 