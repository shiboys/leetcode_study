package org.swj.leet_code.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 设计 Twitter 的发推功能 leetcode 355 题
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/16 23:07
 */
public class TwitterDesign {
    // 这里为了演示算法，就不在使用 AtomicInteger
    private static final AtomicInteger timestamp = new AtomicInteger();
    Map<Integer, User> userMap;

    public TwitterDesign() {
        userMap = new HashMap<>();
    }

    public void postTweet(int userId, int tweetId) {
        if (!userMap.containsKey(userId)) {
            userMap.put(userId, new User(userId));
        }
        User user = userMap.get(userId);
        user.postTweet(tweetId);
    }

    /**
     * getNewsFeed 是最复杂的一个业务方法
     * 
     * @param userId
     * @return
     */
    public List<Integer> getNewsFeed(int userId) {
        // 使用优先级队列，将 k 个关注的人的 tweet 进行排序显示输出
        if (!userMap.containsKey(userId)) {
            userMap.put(userId, new User(userId));
        }
        User currUser = userMap.get(userId);
        // 按更新时间倒叙排列
        PriorityQueue<Tweet> pq = new PriorityQueue<>((a, b) -> {
            return b.time - a.time;
        });

        for (int followeeId : currUser.followedUserIds) {
            User user = userMap.get(followeeId);
            if (user.tweetHead != null) {
                pq.add(user.tweetHead);
            }
        }

        int tweetCount = 0;
        List<Integer> res = new ArrayList<>();
        while (!pq.isEmpty()) {
            // 至多显示最近 10 条推文
            if (tweetCount++ == 10) {
                break;
            }
            Tweet twitte = pq.poll();
            res.add(twitte.tweetId);
            if (twitte.next != null) {
                pq.offer(twitte.next);
            }
        }
        return res;
    }

    public void follow(int followerId, int followeeId) {
        if (!userMap.containsKey(followerId)) {
            userMap.put(followerId, new User(followerId));
        }
        if (!userMap.containsKey(followeeId)) {
            userMap.put(followeeId, new User(followeeId));
        }
        User follower = userMap.get(followerId);
        follower.follow(followeeId);
    }

    public void unfollow(int followerId, int followeeId) {
        if (!userMap.containsKey(followerId)) {
            userMap.put(followerId, new User(followerId));
        }
        User follower = userMap.get(followerId);
        follower.unfollow(followeeId);
    }

    static class Tweet {
        private int tweetId;
        // 发推的时间
        private int time;
        // 对于 next 这个字段， 由于做了太多的基于 db 的 oop 开发和设计，从来没想到过把一个 model 存入内存的时候需要有 next 指针
        // 然后，如果熟悉 Redis 的源码，就会发现 redis 的源码中充满了链表的光辉，mysql 由于要实现写入 db
        // 文件，也是通过偏移量实现了链表的指针功能
        // 还是一句话，我离底层太远了，离开了数据库，啥也干不了。因此从数据结构开始，尝试改变
        private Tweet next;

        public Tweet() {

        }

        public Tweet(int tweetId, int time) {
            this.time = time;
            this.tweetId = tweetId;
        }
    }

    static class User {
        private int userId;
        // 我发的推文列表，这里是考验算法，是在内存中进行运算，因此用了链表的头部表示推文列表，而没有使用集合的方式
        // 相比使用集合的方式，链表头部的表示方式比 LinkedList 是对象的字节码大小更小，同时由于没有 LikedList Node 的封装，
        // 会更加节省内存。其实我看 netty 源码的时候，也注意到了 netty 使用了非常多的链表，也没有使用什么 List 集合
        private Tweet tweetHead;

        private Set<Integer> followedUserIds;

        public User(int userId) {
            this.userId = userId;
            // 自己会自动关注自己
            followedUserIds.add(userId);
        }

        public void postTweet(int tweetId) {
            Tweet twitte = new Tweet(tweetId, timestamp.incrementAndGet());
            twitte.next = tweetHead;
            // 采用头插法，将最新产生的 推文插入链表头部
            tweetHead = twitte;
        }

        public void follow(int followeeId) {
            followedUserIds.add(followeeId);
        }

        public void unfollow(int followeeId) {
            // 自己不能取关自己
            if (followeeId != userId) {
                followedUserIds.remove(followeeId);
            }
        }

    }

}
