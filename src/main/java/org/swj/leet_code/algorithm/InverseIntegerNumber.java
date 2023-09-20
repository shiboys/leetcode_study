package org.swj.leet_code.algorithm;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/03/03 16:29 
 * 整数反转 解题思路：跟 String to Int 有点类似，在循环中 除以 10 得到结果和mod，然后反转的数字 乘以10+余数
 */
public class InverseIntegerNumber {

  public int inverseNumber(int num) {
    int inverseNum = 0;
    int opNum = num;
    if (num < 0) {
      opNum = -num;
    }
    int mod;
    while (opNum > 0) {
      mod = opNum % 10;
      opNum /= 10;
      if (((Integer.MAX_VALUE - mod) / 10) < inverseNum) {
        return 0;
      }
      inverseNum = inverseNum * 10 + mod;
    }

    if (num < 0) {
      inverseNum = -inverseNum;
    }
    // 使用 long 判断，可能更好理解些。
    return inverseNum;
  }

  /**
   * 这个算法比上面灵活在不用计算 Integer.maxValue; 上面的算法其实也不需要正负来回转化
   * @param x
   * @return
   */
  public int reverse(int x) {
    long result = 0;
    int mod;
    while (x != 0) {
      mod = x % 10;
      x /= 10;
      result = result * 10 + mod;
    }
    if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
      return 0;
    }
    return (int) result;
  }

  public static void main(String[] args) {
    int i = 123;
    InverseIntegerNumber iin = new InverseIntegerNumber();
    /*System.out.println(iin.inverseNumber(i));
    i = -123;
    System.out.println(iin.inverseNumber(i));
    i = 120;
    System.out.println(iin.inverseNumber(i));
    i = 1534236469;
    System.out.println(iin.inverseNumber(i));*/
    System.out.println(iin.reverse(-123));
    System.out.println(iin.reverse(Integer.MIN_VALUE));
  }
}
