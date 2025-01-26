package org.swj.complex.beauty_programming;

import net.openhft.affinity.AffinityLock;

import java.util.concurrent.TimeUnit;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2025/01/15 22:03
 */
public class SimulateCpu {
  static final int busyTime = 10;
  static final int idleTime = busyTime;

  public static void main(String[] args) throws InterruptedException {
    /**
     * 经过尝试，我发现，维持 50% 的 cpu 使用率对于当前多核 cpu 来说，单个线程很难实现
     * 所以，这里使用 affinity 框架实现 cpu 核心绑定
     * 但是在我的 mac 机器上完全没效果，我看网上 windows 上效果比较明显
     *
     */

    Thread thread2 = new Thread(() -> {
      try (AffinityLock lock2 = AffinityLock.acquireLock(7)) {
        while (true) {
        }
      }
    });
    try (AffinityLock lock = AffinityLock.acquireLock(6)) {

      thread2.start();

      while (true) {
      }
//        long start = System.nanoTime();
//        long busyTimeNanos = TimeUnit.NANOSECONDS.convert(busyTime, TimeUnit.MILLISECONDS);
//        while (System.nanoTime() - start <= busyTimeNanos) {
//          // busy loop
//        }
//
//        Thread.sleep(idleTime);
//        }
    }
  }
}
