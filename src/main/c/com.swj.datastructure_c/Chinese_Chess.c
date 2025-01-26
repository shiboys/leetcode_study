#include <stdio.h>

//  中国象棋将帅的问题，编程之美 1.2 问题
//  要求使用一个变量来解决所有的将帅不能在同一条垂直线的问题
//  将帅都在 一个 3x3 的矩阵中
//  这道题树中有 3 个解法，还是非常清晰的。后面两种解法非常简单，但是却并不容易理解
//  第一种解法容易理解，但是代码量非常大

//  * 解法 1 的主题思想是将 A，B 对战双方的变量放在同一个变量中，怎么放哪？
//  * 对，使用位运算，使用 byte b 来表示 A和 B，因为 A，B 的取值范围都是 [1,9], 而 byte的 4 位取值范围是 [0,15], 完全能够满足要求
//  * 第一个变量在高位，也叫 left 位置，第二个变量在低位，也叫 right 位置

#define SHIFT 4
// 全量掩码
#define FULL_MASK 255
// 左掩码 左边即高4位为 1，右边即低4位为 0
#define LEFT_MASK (FULL_MASK << SHIFT)
// 右掩码，右边即低 4 位为 1
#define RIGHT_MASK (FULL_MASK >> SHIFT)
// 将 左边也就是高4位 设置为 n。就是 b 跟 右掩码进行逻辑与运算，消除高 4 位的 1，然后再将 n 左移 4 位跟与之进行逻辑或运算
#define LEFT_SET(b, n) (b = ((b & RIGHT_MASK) | (n << SHIFT)))
// 获取 b 的高 4 位的值, 必须先跟 Full_mask 进行逻辑与运算，防止 b 是高于 8 字节的 short 或者 int 甚至 long 型
// & 运算截取之后，再 >> 左移 4 位
#define LEFT_GET(b) ((b & FULL_MASK) >> SHIFT)
// RIGHT_SET 设置右边也就是 低4位设置为变量 n。先将低 4 位抹掉，然后跟 n 进行逻辑计算
#define RIGHT_SET(b, n) (b = ((b & LEFT_MASK) | n))
// RIGHT_GET 获取右边的也就是低 4 位的变量值
#define RIGHT_GET(b) (b & RIGHT_MASK)

// 将帅的 网格宽度
#define GRID_WIDTH 3

void method_with_macro()
{
    char A = 'A';
    unsigned char b;
    // 先遍历 b 字节的高位进行设置，当做 A 变量
    for (LEFT_SET(b, 1); LEFT_GET(b) <= GRID_WIDTH * GRID_WIDTH; LEFT_SET(b, LEFT_GET(b) + 1))
    {
        for (RIGHT_SET(b, 1); RIGHT_GET(b) <= GRID_WIDTH * GRID_WIDTH; RIGHT_SET(b, RIGHT_GET(b) + 1))
        {
            if (LEFT_GET(b) % GRID_WIDTH == RIGHT_GET(b) % GRID_WIDTH)
            {
                continue;
            }
            printf("A = %d, B = %d \n", LEFT_GET(b), RIGHT_GET(b));
        }
    }
}

struct position
{
    // 类型说明符 位域名:位域长度
    unsigned char a : 4;
    unsigned char b : 4;
    /* data */
} p;
// p 类型变量共占用 8 个字节，

void method_with_struct()
{
    // 使用 Struct 的方式
    for (p.a = 1; p.a <= 9; p.a++)
    {
        for (p.b = 1; p.b <= 9; p.b++)
        {
            if (p.a % 3 != p.b % 3)
            {
                printf("A = %d, B = %d\n", p.a, p.b);
            }
        }
    }
}

// 解法 3 是使用二维矩阵的方式，将 a 和 b 投射到二维矩阵，a和b 变量的取值范围都是 [1,9]
// 那么矩阵中元素的个数是 81。那么 i/9 就是行 i，i %9 就是列 j
// 那么我们只要保证 i % 3 != j % 3 就行

void metod3_rectangle()
{
    char i = 81;
    for (; i >= 1; i--)
    {
        if (i / 9 % 3 == i % 9 % 3)
        {
            continue;
        }
        printf("A = %d, B = %d \n", i / 9 + 1, i % 9 + 1);
    }
}
int main()
{
    // method_with_struct();
    metod3_rectangle();
    // printf("%c\n", 1 + A);
    return 0;
}