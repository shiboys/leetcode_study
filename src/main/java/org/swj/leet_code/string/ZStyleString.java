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
    // 根据一个 z 的元素数和行数计算列数
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

  /**
   * 计算列数是个技术活，我改了不下 5 次才算勉强把这个列数算对，列数算不对，就会造成最终的结果字符丢失。
   *
   * @param stringLength 字符长度
   * @param numRows      行数
   * @return
   */
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
   * 这个是我观察的，其实没有科学依据，数量包括 1 列 1 斜
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

  /**
   * 下面借他山之石，给出 3 种优化
   * 1、使用 指针移步的方式，在二维数组中移动坐标（之前我就是想这么做，但是没有找到规律）
   * 2、使用多个 StringBuilder的数组，取消纵坐标的移动，因为最终的打印结果没有列的信息
   * 3、使用一种更加巧妙的位置关系算法，来定位到元素
   */

  // 移动坐标法
  public String convert2(String s, int rowsNum) {
    if (s == null || s.isEmpty()) {
      return s;
    }

    int sLength = s.length();
    // 使用这个数组列数长度，预估的，非精准的，但是确实满足要求
    // 1 <= numRows <= 1000, 至少 1 行。
    int colLength = (sLength + 1) >> 1;
    char[][] matrixArr = new char[rowsNum][colLength];
    matrixArr[0][0] = s.charAt(0);
    int rowIdx = 0;
    int colIdx = 0;
    boolean isDown = false;
    for (int i = 1; i < sLength; i++) {
      if (rowIdx == 0) {
        // 此时横坐标指针需要向下移动
        isDown = true;
      } else if (rowIdx == rowsNum - 1) {
        // 此时已经到了二维数组的底部，需要改变移动反向
        isDown = false;
      }

      if (isDown) {
        // 如果向下移动，则纵坐标不变，横坐标增加
        rowIdx++;
      } else {
        // 向上移动
        rowIdx--;
        // colIdx 纵坐标指针总是增加，因为列数总是在增加
        colIdx++;
      }
      //找到位置，放置元素
      matrixArr[rowIdx][colIdx] = s.charAt(i);
    }

    StringBuilder stringBuilder = new StringBuilder(sLength);
    for (int i = 0; i < rowsNum; i++) {
      for (int j = 0; j < colLength; j++) {
        char currChar = matrixArr[i][j];
        if (currChar == '\0') { // 如果是空元素，则跳过
          continue;
        }
        stringBuilder.append(currChar);
      }
    }

    return stringBuilder.toString();
  }

  public String convert3(String s, int rowsNum) {
    if (s == null || s.isEmpty()) {
      return s;
    } else if (s.length() <= 1 || rowsNum <= 1 ) {
      return s;
    }

    int sLength = s.length();
    StringBuilder[] stringBuilderArray = new StringBuilder[rowsNum];
    for (int i = 0; i < rowsNum; i++) {
      stringBuilderArray[i] = new StringBuilder(sLength);
    }
    boolean isDown = false;
    int rowIdx = 0;
    stringBuilderArray[0].append(s.charAt(0));
    for (int i = 1; i < sLength; i++) {
      // rowIndex 归为0 时，则需要向下遍历
      if (rowIdx == 0) {
        isDown = true;
      } else if (rowIdx == rowsNum - 1) {
        isDown = false;
      }
      if (isDown) {
        rowIdx++;
      } else {
        rowIdx--;
      }
      // 这里使用 StringBuilder 做打平操作，就没有列的概念了
      stringBuilderArray[rowIdx].append(s.charAt(i));
    }
    // 这里使用第 1 个合并后的所有 builder
    StringBuilder resultBuilder = stringBuilderArray[0];
    for (int i = 1; i < stringBuilderArray.length; i++) {
      resultBuilder.append(stringBuilderArray[i].toString());
    }
    return resultBuilder.toString();
  }

  public String convert4(String s, int rowsNum) {
    if (s == null || s.isEmpty()) {
      return s;
    } else if (s.length() <= 1 || rowsNum <= 1) {
      return s;
    }
    int sLength = s.length();
    StringBuilder stringBuilder = new StringBuilder(s.length());
    // 一个周期的长度
    int periodLength = getZItemsSize(rowsNum);
    for (int i = 0; i < rowsNum; i++) {
      for (int j = 0; j + i < sLength; j += periodLength) {
        // 每列都有字符的列,附加每个周期的元素第 i 行元素
        stringBuilder.append(s.charAt(j + i));
        // 如果 i 不在行首或者行尾,其没有越界，第 i 个字符和 第 periodLength-i 个字符，一个 Z 字周期内有 2 个字符
        if (i != 0 && i != rowsNum - 1 && (j + periodLength - i) < sLength) {
          stringBuilder.append(s.charAt(j + (periodLength - i)));
        }
      }
    }
    return stringBuilder.toString();
  }

  public static void main(String[] args) {
    String s1 = "PAYPALISHIRING";
    ZStyleString zStyleString = new ZStyleString();
/*    System.out.println(zStyleString.convert("PAYPALISHIRING", 3));
    System.out.println(zStyleString.convert("PAYPALISHIRING", 4));
    System.out.println(zStyleString.convert("A", 1));
    System.out.println(zStyleString.convert("AB", 1));
    System.out.println(zStyleString.convert("AB", 2));
    System.out.println(zStyleString.convert("AB", 4));
    System.out.println(zStyleString.convert("AB", 3));
    System.out.println(zStyleString.convert("ABC", 3));
    */
    System.out.println(zStyleString.convert4("ABCDE", 4));
    System.out.println(zStyleString.convert4("PAYPALISHIRING", 5));

    System.out.println(Integer.numberOfLeadingZeros(1));
    System.out.println(Integer.numberOfLeadingZeros(2));

   /* System.out.println(resizeStamp(1));
    System.out.println(resizeStamp(2));*/
    System.out.println(resizeStamp(4));
    int shift=16;
    int rs;
    System.out.println((rs=resizeStamp(shift)));


    System.out.println(rs << RESIZE_STAMP_SHIFT);

    System.out.println(-3 == ~(2));
    System.out.println(~(2));

  }

  static final int resizeStamp(int n) {
    return Integer.numberOfLeadingZeros(n) | (1 << (RESIZE_STAMP_BITS - 1));

  }

  private static int RESIZE_STAMP_BITS = 16;
  private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;
}
