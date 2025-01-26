// 打折书籍购买问题，这道题老难了，我看完一遍，书中没有给解题答案，我在网上找的，还是 C++ 版本的
// 我在网上也找到 Java 版本的了，不知道 Java 的运行效果

#include <functional>
#include <iostream>
#include <algorithm>

#define MAX_BOOKS 5
#define INF 9999
#define DISCOUNT1 1
#define DISCOUNT2 1.9
#define DISCOUNT3 2.7
#define DISCOUNT4 3.2
#define DISCOUNT5 3.75
#define BOOKPRICE 8

using namespace std;

/*
 *  使用字节的豆包 大模型问题 试了下，尼玛，给出的答案很坑爹，完全不对，都没有对应的折扣比例，没办法还是使用网上给出的 C++ 版本。
 因为 原书并没有给出答案，虽然这个 C++ 版本的，但是我觉得是实现了书中的设计思想。
*/

// 循环方式使用动态规划实现买书最省成本，但是非常难以理解
void buyBooksWithLoop()
{
    // 折扣，一本书不折扣，两本书 5% 的折扣，总的折扣就是 2-0.1=1.9，同理 3.75 = 5-（5*0.25)
    float discounts[6] = {0, 21, 1.9, 2.7, 3.2, 3.75};

    int books[6] = {0};
    int bookPrice = 8; // 书的价格
    // cout 必须使用 using namespace std 命名空间，就跟java 引入 import java.util 一样
    cout << "请输入五类中每一类书的数量：" << endl;
    for (int i = 1; i <= 5; i++)
    {
        cin >> books[i]; // 输入各类书目的数量
    }
    // 最后一个参数是比较器，跟 java 中的 Comparator 一致
    sort(books + 1, books + sizeof(books) / sizeof(int), greater<int>());
    // 存放动态规划的中间结果
    float minDisDpArr[6][6][6][6][6] = {0};

    // 存放下一次计算的 Yn,
    int yns[5] = {0};

    // 存放动态规划每次可能的状态转移方程的结果
    double tempDpArr[6] = {0};
    int y1 = 0;
    int y2 = 0;
    int y3 = 0;
    int y4 = 0;
    int y5 = 0;
    for (; y5 <= books[5]; y5++)
    {
        // 之前拷贝代码，把 for 循环中间的判断 y4 <= books[4]; 改为 y5<=books[4] 造成死循环
        for (y4 = y5; y4 <= books[4]; y4++)
        {
            for (y3 = y4; y3 <= books[3]; y3++)
            {
                for (y2 = y3; y2 <= books[2]; y2++)
                {
                    for (y1 = y2; y1 <= books[1]; y1++)
                    {
                        if (y5 > 0)
                        {
                            yns[4] = y5 - 1;
                            yns[3] = y4 - 1;
                            yns[2] = y3 - 1;
                            yns[1] = y2 - 1;
                            yns[0] = y1 - 1;
                            // 排序的目的是为了实现动态规划的函数约束 F(y1,y2,y3,y4,y5)，其中 y1>=y2>=y3>=y4>=y5
                            sort(yns, yns + 5, greater<int>());
                            tempDpArr[5] = minDisDpArr[yns[0]][yns[1]][yns[2]][yns[3]][yns[4]] + discounts[5] * bookPrice;
                        }
                        if (y4 > 0)
                        {
                            yns[4] = y5;
                            yns[3] = y4 - 1;
                            yns[2] = y3 - 1;
                            yns[1] = y2 - 1;
                            yns[0] = y1 - 1;
                            sort(yns, yns + 5, greater<int>());
                            tempDpArr[4] = minDisDpArr[yns[0]][yns[1]][yns[2]][yns[3]][yns[4]] + discounts[4] * bookPrice;
                        }
                        if (y3 > 0)
                        {
                            yns[4] = y5;
                            yns[3] = y4;
                            yns[2] = y3 - 1;
                            yns[1] = y2 - 1;
                            yns[0] = y1 - 1;
                            sort(yns, yns + 5, greater<int>());
                            tempDpArr[3] = minDisDpArr[yns[0]][yns[1]][yns[2]][yns[3]][yns[4]] + discounts[3] * bookPrice;
                        }
                        if (y2 > 0)
                        {
                            yns[4] = y5;
                            yns[3] = y4;
                            yns[2] = y3;
                            yns[1] = y2 - 1;
                            yns[0] = y1 - 1;
                            sort(yns, yns + 5, greater<int>());
                            tempDpArr[2] = minDisDpArr[yns[0]][yns[1]][yns[2]][yns[3]][yns[4]] + discounts[2] * bookPrice;
                        }
                        if (y1 > 0)
                        {
                            yns[4] = y5;
                            yns[3] = y4;
                            yns[2] = y3;
                            yns[1] = y2;
                            yns[0] = y1 - 1;
                            sort(yns, yns + 5, greater<int>());
                            tempDpArr[1] = minDisDpArr[yns[0]][yns[1]][yns[2]][yns[3]][yns[4]] + discounts[1] * bookPrice;
                        }

                        float minPrice = tempDpArr[1];
                        for (int i = 1; i <= 5; i++)
                        {
                            if ((tempDpArr[i] && tempDpArr[i] < minPrice) || minPrice == 0)
                            {
                                minPrice = tempDpArr[i];
                            }
                        }
                        minDisDpArr[y1][y2][y3][y4][y5] = minPrice;
                    }
                }
            }
        }
    }
    printf("当前购买书本可享受的最低价格为%.2f", minDisDpArr[books[1]][books[2]][books[3]][books[4]][books[5]]);
    cout << endl;
}

double min5(double r1, double r2, double r3, double r4, double r5)
{
    return min(min(min(r1, r2), min(r4, r5)), r3);
}

double buyBooksWithRecursion(int y1, int y2, int y3, int y4, int y5)
{
    if (y1 + y2 + y3 + y4 + y5 == 0) // 递归结束条件，我觉得最好是 books[i] == 0
    {
        return 0;
    }
    int arr[5] = {y1, y2, y3, y4, y5};

    // 保证 y1 >= y2 >= y3 >= y4 >= y5
    sort(arr, arr + sizeof(arr) / sizeof(arr[0]), greater<int>());
    y1 = arr[0];
    y2 = arr[1];
    y3 = arr[2];
    y4 = arr[3];
    y5 = arr[4];

    double r1 = INF, r2 = INF, r3 = INF, r4 = INF, r5 = INF;
    if (y1 > 0)
        r1 = BOOKPRICE * DISCOUNT1 + buyBooksWithRecursion(y1 - 1, y2, y3, y4, y5);
    if (y2 > 0)
        r2 = BOOKPRICE * DISCOUNT2 + buyBooksWithRecursion(y1 - 1, y2 - 1, y3, y4, y5);
    if (y3 > 0)
        r3 = BOOKPRICE * DISCOUNT3 + buyBooksWithRecursion(y1 - 1, y2 - 1, y3 - 1, y4, y5);
    if (y4 > 0)
        r4 = BOOKPRICE * DISCOUNT4 + buyBooksWithRecursion(y1 - 1, y2 - 1, y3 - 1, y4 - 1, y5);
    if (y5 > 0)
        r5 = BOOKPRICE * DISCOUNT5 + buyBooksWithRecursion(y1 - 1, y2 - 1, y3 - 1, y4 - 1, y5 - 1);

    return min5(r1, r2, r3, r4, r5);
}

// c++ 用数组，难度很大呀，生成的数组没办法像 java 一样，每次都 new 一个，
// 因为 c++ new完之后，还要 delete[] 释放内存，很麻烦。如果不 new 一个，就得在原数组上 arr[i]--，递归调用之后再 ++ 回来
// double buyBooksWithArrayRecur(int *books)
// {
//     int res = 0;
//     for (int i = 0; i < books.size(); i++)
//     {
//         res += books[i];
//     }
//     if (res == 0)
//     {
//         return 0;
//     }

//     sort(books, books + sizeof(books) / sizeof(books[0]), greater<int>());
//     double r1 = INF, r2 = INF, r3 = INF, r4 = INF, r5 = INF;
//     if (books[0] > 0)
//     {
//         r1 = BOOKPRICE * DISCOUNT1 + buyBooksWithArrayRecur(books);
//     }
// }

void useRecursion()
{
    cout << "请输入五类中每一类书(共5类)的数量：" << endl;
    int books[5] = {0};
    for (int i = 0; i < 5; i++)
    {
        cin >> books[i];
    }
    printf("当前购买书本可享受的最低价格为%.2f", buyBooksWithRecursion(books[0], books[1], books[2], books[3], books[4]));
    cout << endl;
}
int main(void)
{
    useRecursion();
}
