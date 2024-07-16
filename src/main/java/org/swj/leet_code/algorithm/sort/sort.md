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
  * 3.3) a[j] < pivot： 此时我们呀哦注意到 a[j] < pivot, a[k] > pivot, a[i] == pivot , 那么我们只需要将 a[j] 放到 a[i] 上，a[i] 放到 a[k] 上。 然后 i++,k++, j-- ，k 继续扫描(注意此时 j 的扫描也结束了)

注意，当扫描结束时，i 和 j 都表示了 == pivot 的部分的起始位置和技术位置。我们只需对小于 pivot 的部分和大于 Pivot 的部分重复上述操作即可。如下图所示

![三相图3](../../../../../../resources/imgs/sort/quick-sort-3way3.png)

通过上一段的表述可知，初始化时 

我们选取最左边的元素作为 pivot, 初始化时，i=start, k = start+1, j = end


## 快速排序参考文献

https://www.cnblogs.com/nullzx/p/5880191.html
