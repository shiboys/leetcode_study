## 快速排序

### 三项快速排序

3 项快速排序法的基本思想，用 i,j,k 三个指针将数组切分成 4 部分，a[start,i-1] 表示小于 pivot1 的部分，a[i,k-1] 表示等于 pivot 的部分
a[j+1,end] 表示大于 pivot 的部分，而a[k,j] 表示未判定的元素(即不知道比 pivot 大还是比它小)。我们要注意 a[i] 始终位于等于 pivot 的部分
的第一个元素，a[i] 的左边是小于等于 pivot 的部分的第一个元素， a[i] 的左边是小于 pivot 的部分。如下图所示

![三相图1](../../../../../../resources/imgs/sort/quick-sort-3way1.png)

我们选取最左边的元素作为 pivot 元素，初始化时，i=start, k = start+1, j = end；如下图所示

![三相图2](../../../../../../resources/imgs/sort/quick-sort-3way2.png)

通过上一段的表述可知，初始化时 < pivot 的部分的元素个数为 0，等于 Pivot 部分的元素的个数为 1，大于 Pivot 部分的元素个数为0，
K 自左向右扫描直到与 j 相遇并错过(k>j)。我们扫描的目是为了逐个减少未知元素，并将每个元素按照和 pivot 的大胸关系放到不同的区间上。

在 k 的扫描过程中我们可以对 a[k] 分为如下三种情况讨论：

* (1) a[k] < pivot 交换 a[i] 和 a[k], 然后 i++,k++, k 继续扫描(符合单端分区的逻辑)
* (2) a[k] == pivot 则 k++, k 接着继续扫描
* (3) a[k] > pivot 这个时候显然 a[k] 应该放到最右端，大于 pivot 的部分。但是我们不能直接将 a[k] 与 a[j] 进行交换，因为现在 a[j] 与 pivot 的关系未知，所以这个时候我们需要将 j 从右至左右扫描，而 a[j] 与 pivot 的关系可以继续分为三种情况讨论：
  * 3.1) a[j] > pivot ，j--。j 继续扫描
  * 3.2) a[j] == pivot 交换 a[j] 和 a[k], j--, k++ 且 k 继续扫描.(注意此时 j 的扫描结束了)
  * 3.3) a[j] < pivot： 此时我们此时注意到 a[j] < pivot, a[k] > pivot, a[i] == pivot , 那么我们只需要将 a[j] 放到 a[i] 上，a[i] 放到 a[k] 上。 然后 i++,k++, j-- ，k 继续扫描(注意此时 j 的扫描也结束了)

注意，当扫描结束时，i 和 j 都表示了 == pivot 的部分的起始位置和技术位置。我们只需对小于 pivot 的部分和大于 Pivot 的部分重复上述操作即可。如下图所示

![三相图3](../../../../../../resources/imgs/sort/quick-sort-3way3.png)

通过上一段的表述可知，初始化时 

我们选取最左边的元素作为 pivot, 初始化时，i=start, k = start+1, j = end

### 双轴快速排序

双轴快速排序的算法思路和三项快速排序算法的思路基本一致，双轴快速排序算法使用两个轴，通常选取最左边的元素作为 pivot1 和最优边的元素作为 pivot 2。首先要比较这两个轴的代销，如果 pivot1 > pivot2, 则交换两个遍历，保证 pivot1 <= pivot2。 双轴快速排序同样使用 i,j,k 三个变量将数组分成 4 个部分

![双轴快排](../../../../../../resources/imgs/sort/dual-pivot-quicksort-1.png)

a[start+1,i] 是小于 pivot1 的部分，a[i+1,k-1] 是大于 pivot1 且小于 pivot2 的部分，a[j,end] 是大于 pivot2 的部分，而 A[k,j-1] 是未知部分。和三项切分的快速排序算法一样，初始化 i=0, k=i+1, j = end； k 自左向右扫描知道 k 与 j 相交为止 (k==j)。我们扫描的目的是为了逐个减少未知元素，并将每个元素按照 pivot1 和 pivot2 的大小关系放到不同的区间上去。

在 k 的扫描过程中我们可以对 a[k] 分为如下三种情况讨论：

* (1) a[k] < pivot1 i 先自增，交换 a[i] 和 a[k], 然后 k++, k 继续扫描(符合单端分区的逻辑)
* (2) a[k] >= pivot1 && a[k] <= pivot2, 则 k++, k 接着继续扫描
* (3) a[k] > pivot2 这个时候显然 a[k] 应该放到最右端大于 pivot2 的部分。但是我们不能直接将 a[k] 与下一个位置 a[--j] 进行交换，因为现在 a[--j] 与 pivot1 和 pivot2 的关系未知，所以这个时候我们需要 j 从下一个位置 --j 从右至左右扫描，而 a[--j] 与 pivot 的关系可以继续分为三种情况讨论：
  * 3.1) a[--j] > pivot2 ，j 继续扫描
  * 3.2) a[--j] >= pivot1 且 a[j] <= pivot2, 交换 a[j] 和 a[k], k++ 且 k 继续扫描.(注意此时 j 的扫描结束了)
  * 3.3) a[--j] < pivot1： 先将 i++, 此时我们注意到 a[j] < pivot1, a[k] > pivot2, pivot1 <= a[i] <= pivot2 , 那么我们只需要将 a[j] 放到 a[i] 上，a[k] 放到 a[j] 上。 然后 k++ ，k 继续扫描(注意此时 j 的扫描也结束了)

注意：

1、pivot1 与 pivot2 始终不参与 k, j 的扫描过程
2、扫描结束是，A[i] 表示了 小于 pivot1 的部分的最后一个元素， A[j] 标了大大于 pivot2 的第一个元素，这时我们只需要交换 pivot1 (start swap i)， 交换 pivot2 (j swap end) , 同时我们可以确定 A[i] 和 A[j] 的位置在后续的排序过程中不会发生变化(这一步非常重要，否则可能引起无限递归导致的栈溢出),我们只需要 对 A[start,i-1], A[i+1,j-1], A[j+1, end] 这三部分继续递归上述操作即可。

## 快速排序参考文献

https://www.cnblogs.com/nullzx/p/5880191.html
