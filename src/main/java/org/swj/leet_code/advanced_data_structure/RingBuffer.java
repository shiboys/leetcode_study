package org.swj.leet_code.advanced_data_structure;

import java.util.NoSuchElementException;

/**
 * 环形字节数组
 * 
 * @author shiweijie
 * @version 1.0.0
 * @since 2023/09/23 11:29
 */
public class RingBuffer {

    // 默认字节数组大小
    private static final int DEFAULT_CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30;

    private int size;
    // 头指针
    private int read;
    // 尾指针。仍然是左开右闭的区间 [head,tail)
    private int write;

    private int mask = DEFAULT_CAPACITY - 1;

    private byte[] buffer;

    public RingBuffer() {
        buffer = new byte[DEFAULT_CAPACITY];
    }

    public RingBuffer(int size) {
        if (size <= DEFAULT_CAPACITY) {
            size = DEFAULT_CAPACITY;
        } else {
            size = ceilingToPowerOf2(size);
        }
        buffer = new byte[size];
        mask = size - 1;
    }

    private int ceilingToPowerOf2(int capacity) {
        int n = capacity - 1;
        // 无符号右移，也成为不带符号右移，如果是负数，则右移的时候用 0 填充
        n = n | (n >>> 1);
        n = n | (n >>> 2);
        n = n | (n >>> 4);
        n = n | (n >>> 8);
        n |= n >>> 16;

        return n < 0 ? 1 : (n > MAX_CAPACITY) ? MAX_CAPACITY : n + 1;
    }

    /**
     * 将当前 buffer 中的内容容读取到字节数组中
     * 
     * @param buffer
     * @return 读取成功的字节数
     */
    public int read(byte[] output) {
        if (output == null || output.length < 1) {
            return 0;
        }
        checkSize();
        int n = output.length;

        // 需要读取的自字节数，默认为 size
        int readable = size;
        // 全部读入 output
        if (size < n) {
            size = 0;
        } else {
            size -= n;
            readable = n;
        }
        // 仍然是分两种情况 --head***tail--- 和 --tail***head---
        if (read < write) {
            System.arraycopy(buffer, read, output, 0, readable);
        } else {
            if (read + readable > buffer.length) {
                int n1 = buffer.length - read;
                System.arraycopy(buffer, read, output, 0, n1);
                // 继续读取
                System.arraycopy(buffer, 0, output, n1, readable - n1);
            } else { // 读取内容不超过 buffer.length ，可一次性读取
                System.arraycopy(buffer, read, output, 0, readable);
            }
        }

        read = (read + readable) & mask;

        shrinkSize();
        return readable;
    }

    void checkSize() {
        if (size == 0) {
            throw new NoSuchElementException("buffer is empty");
        }
    }

    /**
     * 将 字节数组 out 中字节写入当前 buffer 中。
     * 
     * @param out 接收写出的字节数组
     * @return 真实写入 out 的字节个数
     */
    public int write(byte[] input) {
        if (input == null || input.length < 1) {
            return 0;
        }

        ensureCapacity(input.length);

        // 分两种情况 --head***tail--- 和 --tail***head---
        if (write >= read) { // --head***tail---
            if (write + input.length > buffer.length) {
                // 分两步存储
                int n1 = buffer.length - write;
                System.arraycopy(input, 0, buffer, write, n1);
                // 另外一部分
                System.arraycopy(input, n1, buffer, 0, input.length - n1);
            } else { // 小于等于，则直接存
                System.arraycopy(input, 0, buffer, write, input.length);
            }

        } else { // --tail***head--- 情况
            // 有 ensureCapacity 方法的保证，容量肯定够，不可能 oversize head 的位置
            System.arraycopy(input, 0, buffer, write, input.length);
        }

        write = (write + input.length) & mask;
        size += input.length;
        return input.length;
    }

    private void ensureCapacity(int requestCap) {
        if (size + requestCap <= buffer.length) {
            return;
        }
        int newSize = size + requestCap;
        newSize = ceilingToPowerOf2(newSize);
        mask = newSize - 1;
        transferElements(newSize);
    }

    private void shrinkSize() {
        if (size <= buffer.length / 4) {
            transferElements(buffer.length / 2);
        }
    }

    void transferElements(int newSize) {
        byte[] newBuffer = new byte[newSize];

        // ----head****tail** 情形
        if (read <= write) {
            System.arraycopy(buffer, read, newBuffer, 0, size);
        } else {
            // ***tail---head**** 情形
            int n1 = buffer.length - read;
            System.arraycopy(buffer, read, newBuffer, 0, n1);
            System.arraycopy(buffer, 0, newBuffer, n1, size - n1);
        }
        read = 0;
        write = size;
        buffer = newBuffer;
    }

    public static void main(String[] args) {
        char a = 'A';
        RingBuffer instance = new RingBuffer();
        // testCommonReadAndWrite(a, instance);

        // 再试试扩容
        char t = 'z';
        int length = t - a + 1;

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) (a + i));
        }
        System.out.println(sb.toString());

        byte[] bytes = new byte[10];
        for (int i = 0; i < 10; i++) {
            bytes[i] = (byte) (a + i);
        }

        // 先写入 10 个
        instance.write(bytes);

        // 读取 9 个
        byte[] readBytes = new byte[9];
        instance.read(readBytes);

        for (byte b : readBytes) {
            System.out.print((char) b + " ");
        }
        System.out.println();

        // 再把剩下所有字节写入，触发扩容
        a += 10;
        length -= 10;
        bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) (a + i);
        }

        instance.write(bytes);
        // 再读取 length-1 个字节，就只剩下 2 个字节，这时候应该会触发缩容
        readBytes = new byte[length - 1];
        instance.read(readBytes);
        for (byte b : readBytes) {
            System.out.print((char) b + " ");
        }
        System.out.println();

        System.out.println("size now is " + instance.size + ", and bytes array's length is " + instance.buffer.length);
    }

    private static void testCommonReadAndWrite(char a, RingBuffer instance) {
        StringBuilder sb = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            sb.append((char) (a + i));
        }

        System.out.println(sb.toString());

        byte[] bytes = new byte[10];
        for (int i = 0; i < 10; i++) {
            bytes[i] = (byte) (a + i);
        }

        instance.write(bytes);
        // 读取 9 个
        byte[] readBytes = new byte[9];
        instance.read(readBytes);

        for (byte b : readBytes) {
            System.out.print((char) b + " ");
        }
        System.out.println();

        // 再写入 10 个字节 此时，write 指针就发生了折返，此时 write < read 指针
        a += 10;
        for (int i = 0; i < 10; i++) {
            bytes[i] = (byte) (a + i);
        }
        instance.write(bytes);

        // 再读取 8 个字节，
        readBytes = new byte[8];
        instance.read(readBytes);
        for (byte b : readBytes) {
            System.out.print((char) b + " ");
        }
        System.out.println();
    }
}
