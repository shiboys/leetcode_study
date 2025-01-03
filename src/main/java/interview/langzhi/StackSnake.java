package interview.langzhi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2024/12/17 16:17
 */
public class StackSnake {
  static final int UP_BOUND = 10000;
  StackPile<Integer> snake;
  final Random random = new Random();
  Map<Integer, Node<Integer>> nodePos = new HashMap<>();

  public StackSnake(int n) {
    if (n < 1 || n > UP_BOUND / 10) {
      throw new IllegalArgumentException("n is illegal. n should be between 1 and " + UP_BOUND / 10);
    }
    snake = new StackPile<>();
    for (int i = 0; i < n; i++) {
      Node<Integer> node = snake.push(random.nextInt(UP_BOUND));
      nodePos.put(n-i-1, node);
    }
  }

  public boolean eat(int pos) {
    if (snake.isEmpty()) {
      return true;
    }
    Node<Integer> node = nodePos.get(pos);
    if (node == null) {
      return false;
    }
    if (node == snake.head) {
      snake.pop();
      return true;
    }
    Node<Integer> prev = node.up;

    while (snake.tail != prev) {
      snake.pop2();
    }
    return false;
  }

  public static void main(String[] args) {
    StackSnake instance = new StackSnake(10);
    StackPile.printQueue(instance.snake);

    boolean result = instance.eat(5);
    StackPile.printQueue(instance.snake);
    System.out.println("eat result is " + result);
    result = instance.eat(0);
    StackPile.printQueue(instance.snake);
    System.out.println("eat result is " + result);

    Stack<Integer> stack = new Stack<>();


    ArrayList<Integer> res = new ArrayList<>(stack);
    int[] a = new int[0];
    res.toArray(a);
  }

}
