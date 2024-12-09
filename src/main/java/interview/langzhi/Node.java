package interview.langzhi;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/12/09 14:08
 */
public class Node<T> {
  public Node<T> up;
  public Node<T> down;
  private T data;

  public Node(T data) {
    this.data = data;
  }

  public Node(Node<T> up, Node<T> down, T data) {
    this.up = up;
    this.down = down;
    this.data = data;
  }

  public Node() {

  }

  public T getData() {
    return this.data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
