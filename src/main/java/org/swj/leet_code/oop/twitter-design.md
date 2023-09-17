### 设计推特发推功能

核心功能 API 测试用例 如下：
```java
Twitter twitter = new Twitter();

twitter.postTweet(1, 5);
// 用户 1 发送了一条新推文 5

twitter.getNewsFeed(1);
// return [5]，因为自己是关注自己的

twitter.follow(1, 2);
// 用户 1 关注了用户 2

twitter.postTweet(2, 6);
// 用户2发送了一个新推文 (id = 6)

twitter.getNewsFeed(1);
// return [6, 5]
// 解释：用户 1 关注了自己和用户 2，所以返回他们的最近推文
// 而且 6 必须在 5 之前，因为 6 是最近发送的

twitter.unfollow(1, 2);
// 用户 1 取消关注了用户 2

twitter.getNewsFeed(1);
// return [5]
```

这借个 API 中大部分都很好实现，最核心的功能难点是 `getNewsFeed`, 因为返回的结果必须在时间上有序，但问题是用户的关注是动态变化的，怎么办？

**这里就涉及到到算法了**：如果我们把每个用户各自的推文存储在链表中，每个链表节点存储文章 id  和一个时间戳 time(记录发帖时间，方便比较)，而且这个链表是按 time 有序的，那么如果某个用户关注了 k 个用户，**我们就可以合并 k 个有序链表的算法合并出有序的推文列表，正确地 `getNewsFeed` 了！

而 用户 User 和 推文 Tweet 的表示，需要用简单的面向对象设计。

