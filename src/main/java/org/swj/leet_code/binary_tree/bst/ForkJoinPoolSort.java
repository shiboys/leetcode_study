package org.swj.leet_code.binary_tree.bst;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/03/20 18:26
 */
@Slf4j
public class ForkJoinPoolSort {

  public static void main(String[] args) throws Exception {
    //打印结果 1 2 3
    testCompletableFuture1();

    //打印结果为 1
    testCompletableFuture2();

    testCompletableFuture3();
  }

  private static void testCompletableFuture1() throws Exception {
    CompletableFuture<String> base = new CompletableFuture<>();
    CompletableFuture<String> future = base
        .thenApply(s -> s + " 2")
        .thenApply(s -> s + " 3");
    base.complete("1");

    System.out.println(future.get());
    System.out.print("base.get() = ");
    System.out.println(base.get());
  }

  private static void testCompletableFuture2() throws Exception {
    CompletableFuture<String> base = new CompletableFuture<>();
    CompletableFuture<String> future = base.thenApply(s -> s + " 2").thenApply(s -> s + " 3");
    future.complete("1");
    System.out.println(future.get());
    // base.get 调用将被挂起，因为调用方没有调用 base.complete() 方法
  }

  private static void testCompletableFuture3() throws Exception {
    CompletableFuture<String> base = new CompletableFuture<>();
    CompletableFuture<String> future = base.thenApply(s -> {
      log.info(s);
      return s + " 2";
    });

    base.thenAccept(s -> log.info(s + "3-1")).thenAccept(Void -> log.info("3-2"));
    base.thenAccept(s -> log.info(s + "4-1")).thenAccept(Void -> log.info("4-2"));

    base.complete("1");
    log.info("base result : {}", base.get());
    log.info("future result:{} ", future.get());
    /**
     * 打印结果如下
     *20:52:08,147 INFO  org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort            - 14-1
     * 20:52:08,149 INFO  org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort            - 4-2
     * 20:52:08,149 INFO  org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort            - 13-1
     * 20:52:08,149 INFO  org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort            - 3-2
     * 20:52:08,149 INFO  org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort            - 1
     * 20:52:08,151 INFO  org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort            - base result : 1
     * 20:52:08,151 INFO  org.swj.leet_code.binary_tree.bst.ForkJoinPoolSort            - future result:1 2
     * 说明 CompletableFuture 确实是以栈的形式存储任务的，最后的任务，最先被执行，其内部的结构图如 md 文档所示
     */
  }

}
