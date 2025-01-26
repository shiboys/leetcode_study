#include <stdio.h>
#include <unistd.h>

// 模拟 50% 的cpu 使用率。本机测试失败
void simulate_50percent_cpu()
{
    // 本机 cpu 2.8Ghz, 2.8*10^9 一个 cpu 时钟周期可以执行 2 条指令
    // 2.8*2/5 == 1.12, 然后再降低 2 个数量级
    int max = 112000000;
    for (;;)
    {
        for (int i = 0; i < max; i++)
        {
        }
        sleep(10);
    }
}

int main()
{
    // printf("1+1=%d \n", 1 + 1);
    // printf("hello,world\n");

    // int a = 128 + 32;
    // int b = 3;
    // printf("a^b is %d\n", a ^ b);
    // printf("a|b is %d\n", a | b);

    simulate_50percent_cpu();
    return 0;
}