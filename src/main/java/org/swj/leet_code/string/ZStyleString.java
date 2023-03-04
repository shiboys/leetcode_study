package org.swj.leet_code.string;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/03/04 14:26
 */
public class ZStyleString {

  public String convert(String s, int numRows) {
    if (s == null || s.isEmpty()) {
      throw new IllegalArgumentException("s is emtpy");
    }
    String[][] convertedMatrix = getConvertedMatrix(s, numRows);

    return getPrintedString(convertedMatrix);
  }

  private String getPrintedString(String[][] convertedMatrix) {
    if (convertedMatrix == null || convertedMatrix.length < 1 || convertedMatrix[0].length < 1) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    int colLength = convertedMatrix[0].length;
    for (int i = 0; i < convertedMatrix.length; i++) {
      for (int j = 0; j < colLength; j++) {
        if (convertedMatrix[i][j] != null) {
          sb.append(convertedMatrix[i][j]);
        }
      }
    }
    return sb.toString();
  }

  public String[][] getConvertedMatrix(String s, int numRows) {
    // 根据一个 z 的元素树和行数计算列数
    int columnsCount = getColumnsCount(s.length(), numRows);
    // int zItemCount
    String[][] matrix = new String[numRows][columnsCount];
    // 列重复的间隔长度
    int columnsSpan = numRows - 1;
    // 以列为单位进行遍历
    String nextStr = null;
    int charIndex = 0;
    for (int j = 0; j < columnsCount; j++) {
      int currColIndex;
      // 防止除 0
      if (columnsSpan == 0) {
        currColIndex = 0;
      } else {
        currColIndex = j % columnsSpan;
      }

      for (int i = 0; i < numRows; i++) {
        if (currColIndex == 0) { // 满列打印
          nextStr = getNextString(s, charIndex++);
          if (nextStr == null) { // 字符串遍历结束
            return matrix;
          }
          matrix[i][j] = nextStr;
        } else {
          //其他列的情况
          // 对角线右上左下填充字符串
          if (i == (columnsSpan - currColIndex)) {
            nextStr = getNextString(s, charIndex++);
            if (nextStr == null) { // 字符串遍历结束
              return matrix;
            }
            matrix[i][j] = nextStr;
          }
        }
      }
    }
    return matrix;
  }

  private String getNextString(String s, int charIndex) {
    if (charIndex < 0 || charIndex >= s.length()) {
      return null;
    }
    return String.valueOf(s.charAt(charIndex));
  }

  private int getColumnsCount(int stringLength, int numRows) {
    // 至少 1 列。
    if (stringLength < numRows) {
      return 1;
    } else if (numRows == 1) { //如果只有 1 行那么 字符数就是列数。
      return stringLength;
    }
    // 一个 z 多少个字符
    int zItemsSize = getZItemsSize(numRows);
    // 不够整除的情况下，至少有 1 列。
    // 没有 Z的话，那就是只有一行或者一列
    // 计算有多少个 z 字形
    int zCount = getZCount(stringLength, zItemsSize, numRows);
    // 计算一个 z 字形有多少列
    int columnsCountOfOneZ = getColumnsCountOfZItem(zItemsSize);
    // z 字形是多少列 x n 个 Z 字形
    int columnsCount = columnsCountOfOneZ * zCount;
    if (stringLength % zItemsSize == 0) {
      return columnsCount;
    } else {
      int leftCharCount = stringLength - (zItemsSize * zCount);
      // 剩余不够整除的字符数来计算有多少列，使用剩余字符串数-行数 + 1 得到剩余的列数
      int leftColumn = leftCharCount > numRows ? (leftCharCount - numRows) + 1 : 1;
      return columnsCount + leftColumn;
    }

  }

  // 计算 有多少个 z
  private int getZCount(int stringLength, int zItemsSize, int numRows) {
    int zCount = 0;
    if (stringLength > zItemsSize) {
      zCount = stringLength / zItemsSize;
    } else { // 至少要有一个 z，即使不是完整的
      zCount = 1;
    }
    return zCount;
  }

  /**
   * 一个 Z 应该包含多少个字符
   *
   * @param numRows 行数
   * @return
   */
  private int getZItemsSize(int numRows) {
    return numRows * 2 - 2;
  }

  /**
   * 一个 Z 字型有多少列。应该是 zitemSize / 2, 比如 zItemSize = 6,则 columnsCount =3；
   * |    |
   * |  / |
   * | /  |
   * |    |
   *
   * @param zItemSize
   * @return
   */
  private int getColumnsCountOfZItem(int zItemSize) {
    if (zItemSize < 2) {
      return 1;
    }
    return zItemSize / 2;
  }

  public static void main(String[] args) {
    String s1 = "PAYPALISHIRING";
    ZStyleString zStyleString = new ZStyleString();
    System.out.println(zStyleString.convert("PAYPALISHIRING", 3));
    System.out.println(zStyleString.convert("PAYPALISHIRING", 4));
    System.out.println(zStyleString.convert("A", 1));
    System.out.println(zStyleString.convert("AB", 1));
    System.out.println(zStyleString.convert("AB", 2));
    System.out.println(zStyleString.convert("AB", 4));
    System.out.println(zStyleString.convert("AB", 3));
    System.out.println(zStyleString.convert("ABC", 3));
    System.out.println(zStyleString.convert("ABCDE", 4));
    System.out.println(zStyleString.convert("PAYPALISHIRING", 5));

  }
}
