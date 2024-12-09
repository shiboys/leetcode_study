package org.swj.leet_code.data_structure_rewrite;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/08/06 14:47
 *        红黑树 4 刷
 *        首先还是红黑树的 5 特特点：
 *        1、所有节点非红即黑
 *        2、红黑树的根节点是黑色的
 *        3、红黑树的叶子节点是黑色的(Nil 节点)
 *        4、如果父节点是红色的，那么它的两个子节点必定是黑色的(这里的子节点包括 nil 节点)
 *        5、一个节点到它所有的叶子结点所经过的黑色节点个数相同(包括 nil 节点)
 */
public class RedBlackTree4<K extends Comparable<K>, V> {

    private Node<K, V> root;

    class Node<K, V> {
        public K key;
        public V val;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;
        // 节点的颜色，true 表示红色，false 表示绿色
        boolean red;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public void insert(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        int cmp = 0;
        if (root == null) {
            root = new Node<>(key, val);
            // 根节点是黑色的
            root.red = false;
            return;
        }

        Node<K, V> p = this.root, pp = null;
        while (p != null) {
            pp = p;
            cmp = p.key.compareTo(key);
            if (cmp > 0) {
                p = p.left;
            } else if (cmp < 0) {
                p = p.right;
            } else {// 相同 key 的节点
                p.val = val;
                return;
            }
        }
        // 到此处，p == null;
        p = new Node<>(key, val);
        // 除根节点之外的新节点的颜色都是红色
        p.red = true;
        p.parent = pp;
        if (cmp > 0) {
            pp.left = p;
        } else {
            pp.right = p;
        }
        //上面完成二叉搜索树的插入，下面是红黑树的平衡
        insertBalance(p);
    }

    void insertBalance(Node<K, V> x) {
        /**
         * 红黑树的平衡动作原则
         * 1、如果父节点是黑色的，则不需要平衡
         * 2、如果父节点是红色的，这需要分情况：
         * 2.1 如果叔父节点也是红色的，则 将父节点和叔父节点改为黑色，祖父节点改为红色，继续递归祖父节点
         * 2.2 如果叔父节点是黑色的，则当前节点和父节点都是红色的，需要分情况：
         * 2.2.1 如果是 LL 型的，就是父节点是祖父节点的左子节点，当前节点是其父节点的左子节点，则需要以祖父节点为旋转点，进行右旋
         * 旋转完成之后，需要进行着色，父节点颜色为黑色，祖父节点为红色
         * 2.2.2 如果是 LR 型的，就是父节点是祖父节点的左子节点，当前节点是其父节点的右子节点，就以父节点为起始点，先进行左旋，
         * 变成 LL 型，然后再进行右旋，最终保持平衡
         * 2.2.3 如果是 RR 型的，就是父节点是祖父节点的右子节点，当前节点是其父节点的右子节点，则需要以祖父节点为起点，进行旋转，旋转后进行着色，
         * 着色规则仍然是 父节点为黑色，祖父节点为红色
         * 2.2.4 如果是 RL 型的，就是父节点是祖父节点的右子节点，当前节点是其父节点的左子节点，则需要以父节点为起始点，进行右旋，变成
         * RR，然后在进行上一步的左旋+变色
         */
        // 命名像 Jdk HashMap 靠近
        // 这个判断不能少

        if (x == null) {
            return;
        }
        // 如果被递归到 root 节点，则 root 节点永远是黑色, root 可能在递归过程中被更改颜色
        // 第 4 遍写的时候，很顺利，除了这里的判断遗漏之外，都很完美
        if (x == this.root) {
            x.red = false;
        }
        if (x.parent == null) {
            return;
        }
        Node<K, V> xp = x.parent, xpp = xp.parent, xppl, xppr, uncle;
        if (!xp.red) { // 父节点黑色，不需要平衡
            return;
        }
        if (xpp == null) { // 只有两层的节点，不需要平衡
            return;
        }
        xppl = xpp.left;
        xppr = xpp.right;
        if (xp == xppl) {
            uncle = xppr;
        } else {
            uncle = xppl;
        }
        // 如果叔父节点是红色, 此时父节点和叔父节点都是红色
        if (uncle != null && uncle.red) {
            uncle.red = false;
            xp.red = false;
            xpp.red = true;
            // 递归祖父节点
            insertBalance(xpp);
        } else { // 叔父节点是黑色的
            if (xp == xppl) {
                if (x == xp.left) { // ll 型,以祖父节点为起点，开始右旋
                    rotateRight(xpp);
                    // 着色
                    xp.red = false;
                    xpp.red = true;
                } else { // lr 型, 先左旋变成 LL 型，再右旋平衡，这里先暂时递归
                    rotateLeft(xp);
                    // xp 父节点被旋转下来，变成子节点，以 xp 为新节点重新递归平衡
                    insertBalance(xp);
                }
            } else { // 父节点是祖父节点的右子节点
                if (x == xp.right) { // 当前节点是父节点的右节点，RR 型，左旋
                    rotateLeft(xpp);
                    xp.red = false;
                    xpp.red = true;
                } else { // rl 型，先对 parent 进行右旋，变成 RR 型，然后再进行左旋，进行平衡
                    rotateRight(xp);
                    // 此时 parent 被旋转下来，变成子节点，以 此时的 parent 为新节点递归调用进行左旋
                    insertBalance(xp);
                }
            }
        }
    }

    /**
     * 右旋
     * xp xp
     * | 右旋 |
     * p ---> l
     * / \ /\
     * l r ll p
     * /\ /\
     * ll lr lr r
     * 
     * @param p
     */
    void rotateRight(Node<K, V> p) {
        Node<K, V> xp = p.parent;
        Node<K, V> l = p.left, lr;
        if (l == null) {
            return;
        }
        // 右旋三部曲
        // 第一步 更改 lr
        lr = l.right;
        p.left = lr;
        if (lr != null) {
            lr.parent = p;
        }
        // 第二步更改 l 和 p 的关系
        p.parent = l;
        l.right = p;
        // 第三步更改 xp 的指针结构

        l.parent = xp;
        if (xp != null) {
            if (p == xp.left) {
                xp.left = l;
            } else {
                xp.right = l;
            }
        } else {
            // xp 为 null，说明此时 l 是 root 节点
            this.root = l;
            this.root.parent = null;
        }
    }

    /**
     * 左旋
     * xp xp
     * | 左旋 |
     * p ---> r
     * /\ /\
     * l r p rr
     * /\ /\
     * rl rr l rl
     * 
     * @param p
     */
    void rotateLeft(Node<K, V> p) {
        /**
         * 左旋三部曲
         */
        Node<K, V> xp = p.parent, r = p.right;
        if (r == null) {
            return;
        }
        Node<K, V> rl = r.left;
        // 左旋第一步 更改 rl 指针
        p.right = rl;
        if (rl != null) {
            rl.parent = p;
        }

        // 第二步,p 和 r 交换位置
        r.left = p;
        p.parent = r;
        // 第三步，跟祖父节点关联
        r.parent = xp;
        if (xp != null) {
            if (p == xp.left) {
                xp.left = r;
            } else {
                xp.right = r;
            }
        } else { // p 节点就是 root 节点
            this.root = r;
            this.root.parent = null;
        }
    }

    void traverse(Node<K, V> node) {
        if (node == null) {
            return;
        }
        // 中序遍历，顺序打印节点
        traverse(node.left);
        System.out.print(node.key + ", ");
        traverse(node.right);
    }

    int maxDepth(Node<K, V> node) {
        if (node == null) {
            return 0;
        }
        int left = maxDepth(node.left);
        int right = maxDepth(node.right);
        return Math.max(left, right) + 1;
    }

    public static void main(String[] args) {
        RedBlackTree4<Integer, Object> tree = new RedBlackTree4<>();
        Object df = new Object();
        for (int i = 10; i >= 1; i--) {
            tree.insert(i, df);
        }
        System.out.println("开始遍历红黑树");
        tree.traverse(tree.root);
        System.out.println("\ndone!");
        // 遍历没问题，就剩下层高了
        System.out.println("max depth of the tree is " + tree.maxDepth(tree.root));
    }

}
