package org.swj.leet_code.algorithm;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/03/03 17:33 力扣第 8 题，字符串转化为整数
 */
public class StringToInt {
  public int stringToInt(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    s = s.trim();
    // 如果是空格字符串，则返回 0
    if (s.isEmpty()) {
      return 0;
    }
    long result = 0;
    char firstChar = s.charAt(0);
    boolean negative = false;
    if (firstChar < '0') {
      if (firstChar == '-') {
        negative = true;
      }
      if (firstChar == '-' || firstChar == '+') {
        s = s.substring(1);
      }
    }

    for (int i = 0, len = s.length(); i < len; i++) {
      int digit = Character.digit(s.charAt(i), 10);
      // 如果是非数字字符，则跳出，返回当前已经取得的数字
      if (digit < 0) {
        break;
      }
      result = result * 10 + digit;
      if (!negative && result > Integer.MAX_VALUE) {
        return Integer.MAX_VALUE;
      } else if (negative && result -1 > Integer.MAX_VALUE) {
        return Integer.MIN_VALUE;
      }
    }

    return (int) (negative ? -result : result);
  }

  public static void main(String[] args) {
    StringToInt sii = new StringToInt();
    System.out.println(sii.stringToInt("42"));
    System.out.println(sii.stringToInt("   -42"));
    System.out.println(sii.stringToInt("4193 with words"));
    System.out.println(sii.stringToInt("520.56"));

    System.out.println(sii.stringToInt(Integer.MAX_VALUE + "0"));
    System.out.println(sii.stringToInt(Integer.MIN_VALUE + "0"));
  }
}
