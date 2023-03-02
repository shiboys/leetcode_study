package org.swj.leet_code.linked_list;

import java.math.BigInteger;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/03/02 20:39 力扣题目第二题 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 *     <p>请你将两个数相加，并以相同形式返回一个表示和的链表。
 *     <p>你可以假设除了数字 0 之外，这两个数都不会以 0 开头 输入：l1 = [2,4,3], l2 = [5,6,4] 输出：[7,0,8] 解释：342 + 465 = 807.
 *     输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9] 输出：[8,9,9,9,0,0,0,1]
 *
 *     字符串素组的计算，都得用到 BigInteger ？？ 哎，太卷了
 */
public class AddTwoNumbers {
  public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    BigInteger v1 = getIntegerValueFromListNode(l1);
    BigInteger v2 = getIntegerValueFromListNode(l2);
    if (v1 == null || v2 == null) {
      return null;
    }
    BigInteger v3 = v1.add(v2);

    return getListNodeFromString(String.valueOf(v3));
  }

  private BigInteger getIntegerValueFromListNode(ListNode ld) {
    StringBuilder sb = new StringBuilder();
    while (ld != null) {
      sb.append(ld.val);
      ld = ld.next;
    }
    if (sb.length() == 0) {
      return null;
    }
    return new BigInteger(sb.reverse().toString());
  }

  private ListNode getListNodeFromString(String val) {
    ListNode listNode = null;
    for (char ch : val.toCharArray()) {
      listNode = new ListNode(Integer.parseInt(String.valueOf(ch)), listNode);
    }
    return listNode;
  }

  public static class ListNode {
    int val;
    ListNode next;

    public ListNode(int val) {
      this.val = val;
    }

    public ListNode(int val, ListNode next) {
      this.val = val;
      this.next = next;
    }
  }

  public static void main(String[] args) {
    ListNode node9 = new ListNode(9,null);
    ListNode node4 = new ListNode(4,node9);
    ListNode node2 = new ListNode(2,node4);

    ListNode node99 = new ListNode(9,null);
    ListNode node44 = new ListNode(4,node99);
    ListNode node6 = new ListNode(6,node44);
    ListNode node5 = new ListNode(5,node6);
    AddTwoNumbers atn = new AddTwoNumbers();
    ListNode result = atn.addTwoNumbers(node2,node5);
    while (result != null) {
      System.out.print(result.val+",");
      result = result.next;
    }
    System.out.println();
  }
}
