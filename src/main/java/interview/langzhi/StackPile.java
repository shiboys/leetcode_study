package interview.langzhi;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/12/09 14:17
 */
public class StackPile<T> {

  private int size;
  Node<T> head, tail;

  public void push(T data) {
    Node<T> node = new Node<>(data);
    if (head == null) {
      head = node;
      if (tail == null) {
        tail = node;
      }
    } else {
      Node<T> oldHead = head;
      head = node;
      node.down = oldHead;

      oldHead.up = node;
    }
    size++;
  }

  public T pop() {
    if (head == null) {
      return null;
    }
    T data = head.getData();
    Node<T> next = head.down;
    head.down = null;

    head = next;
    if (head == null) {
      tail = null;
    } else {
      // remove up attribute
      head.up = null;
    }

    size--;
    return data;
  }

  public T pop2() {
    if (tail == null) {
      return null;
    }
    T data = tail.getData();
    Node<T> prev = tail.up;
    tail.up = null;

    tail = prev;
    if (prev != null) {
      prev.down = null;
    } else { // tail is null
      head = null;
    }

    size--;
    return data;
  }

  public int size() {
    return size;
  }

  public static void main(String[] args) {
    StackPile<Integer> instance = new StackPile<>();

    for (int i = 1; i <= 3; i++) {
      instance.push(i);
    }

    while (instance.size() != 0) {
      System.out.print(instance.pop() + "\t");
    }
    System.out.println();

//    printQueue(instance);
//
//    instance.pop2();
//    instance.pop();
//    instance.pop2();
//    //instance.pop();
//    printQueue(instance);
  }

  static <T> void printQueue(StackPile<T> queue) {
    Node<T> h = queue.head;
    Node<T> t = queue.tail;
    System.out.println("正序:");
    for (; h != null; h = h.down) {
      System.out.print(h.getData() + "\t");
    }
    System.out.println("\t size is " + queue.size());

    System.out.println("倒序:");
    for (; t != null; t = t.up) {
      System.out.print(t.getData() + "\t");
    }
    System.out.println("\t size is " + queue.size());
  }
}
