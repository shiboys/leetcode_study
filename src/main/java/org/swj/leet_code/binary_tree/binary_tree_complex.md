### 构造二叉树

![前序和中序构造二叉树](../algorithm/dynamic_programming/imgs/builde_binary_tree3.png)

最终构造树的索引结果如下所示

![前序和中序构造二叉树](../algorithm/dynamic_programming/imgs/build_binary_tree4.png)

前序和后序构造二叉树

![前序和后序构造二叉树](../algorithm/dynamic_programming/imgs/build_binary_tree8.png)

![前序和后序构造二叉树](../algorithm/dynamic_programming/imgs/build_binary_tree9.png)

通过图我们可以发现 leftLength = index - postStart + 1;


### 二叉树的序列化和反序列化

总结概括，当**二叉树中节点的值不存在重复时**：

1. 如果你的序列化结果中**不包含空指针信息**，且只给出**一种**遍历顺序，那么你无法还原出唯一的一颗二叉树。
2. 如果你的序列化结果中**不包含空指针信息**，且会给出**两种**遍历顺序，那么按照前文构造二叉树所说，分为两种情况：
   2.1 如果你给出的前序和中序，或者后序和中序，那么可以还原出唯一的二叉树。
   2.2 如果你给出的是前序和后序，那么你无法还原出唯一的一颗二叉树。
3. 如果你的序列化结果中包**含空指针信息**，且你只给出**一种**遍历顺序，也要分两种情况：
   3.1 如果你给出的是前序或者后序，那么你可以还原出唯一的一颗二叉树
   3.2 如果你给出的是中序，那么你无法还原出唯一的二叉树。
 