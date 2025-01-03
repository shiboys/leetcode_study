package interview.jingdong;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/12/16 21:10
 */
public class ReverseNodeSample<T> {

   <T> Node<T> reverseLink(Node<T> head) {

    Node<T> p = head;
    Node<T> prev = null, next;
    while (p != null) {
      next = p.next;
      p.next = prev;
      prev=p;
      p=next;
    }
    return prev;
  }

  static class Node<T> {
    public Node<T> next;
    public T data;
    public Node(T data) {
      this.data = data;
    }
  }

  void testReverse() {
    Node<Integer> node1 = new Node<>(1);
    Node<Integer> node2 = new Node<>(2);
    Node<Integer> node3 = new Node<>(3);
    node1.next=node2;
    node2.next=node3;
    printLink(node1);
    Node<Integer> newHead = reverseLink(node1);
    printLink(newHead);
  }

  <T> void printLink(Node<T> head) {
    Node<T> p= head;
    while(p != null) {
      System.out.print(p.data+"\t");
      p=p.next;
    }
    System.out.println();
  }

  public static void main(String[] args) {
    ReverseNodeSample<Integer> instance = new ReverseNodeSample<>();
    instance.testReverse();
  }
}
