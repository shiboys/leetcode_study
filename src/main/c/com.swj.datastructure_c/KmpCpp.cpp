#include <iostream>
#include <string>
using namespace std;

/**
 * 使用 KMP 算法实现字符串匹配的 C++ 版本
 *
 */

int *getNext(const string &pattern) // 返回的 int* 表示返回的是一个指针，跟 java 中返回指针是一致的。因为 C++ 中没有数据返回的概念，所以只能使用指针来返回。坑爹呀
{                                   // string& 表示传入 string 的引用，跟 java 中传入引用是一致的。
    int *next = new int[pattern.size()];
    int prefix = 0, i = 1;
    next[0] = 0;
    while (i < pattern.size())
    {
        if (pattern[i] == pattern[prefix])
        {
            prefix++;
            // next 表示的是前后缀最长的公共子串
            next[i] = prefix;
            i++;
        }
        else if (prefix > 0)
        { // 共同前后缀当前不匹配，且 prefix 大于 0，那么 prefix 就回退到 next[prefix - 1] 的位置，继续比较。
            // 这里的 prefix 是长度，所以需要减 1
            prefix = next[prefix - 1];
        }
        else // prefix == 0, 表示没有公共子串，那么 next[i] = 0，i++
        {
            next[i] = 0;
            i++;
        }
    }
    return next;
}

int kmp(const string &text, const string &pattern)
{
    cout << "the length of pattern is " << pattern.size() << endl;
    int *next = getNext(pattern);
    int i = 0, j = 0;
    while (i < text.size() && j < pattern.size())
    {
        if (text[i] == pattern[j])
        {
            i++;
            j++;
        }
        else if (j > 0)
        {
            j = next[j - 1]; // 退到上一步的已匹配的位置
        }
        else
        { // j==0, 表示没有匹配的，i++. 最终保证只遍历一次就能找到匹配的位置
            i++;
        }
    }
    if (j == pattern.size())
    {
        return i - j;
    }
    delete[] next;
    return -1;
}

void test1()
{
    string str = "abc中文def";
    printf("字符串为：%s\n", str.data()); // data()函数回传字符串的指针，与c_str()相同

    int len;
    char str1[50];

    strcpy(str1, str.data()); // 赋值

    len = strlen(str1);
    printf("字符串的长度为（%d）\n", len);
    // 使用strlen函数获取长度

    string str2 = str;   // 也可以用string str2.assign(str)，这是string的赋值函数，不过和=没区别
    len = str2.length(); // 也可以用len = str2.size();
    printf("字符串的长度为（%d）\n", len);
    // 使用string类的长度获取函数length()

    system("pause");
}

int main()
{
    string text = "作为程序员一定学习编程之道，一定要对代码的编写有追求，不能实现就完事了。我们应该让自己写的代码更加优雅，即使这会费时费力";
    string pattern = "不能实现就完事了";
    int index = kmp(text, pattern);
    // << 这个符号，跟 java 中的 + 号是一致的。 好吧，C++ 中的解雇哦是 81，因为 C++ size()/length() 返回的事字节长度
    // 且在 mac 平台，中文字符为 3 个字节，所以这里返回的 81 跟 java 和 python 是一致的。  
    cout << "pattern 在 text 中第一次出现的位置是：" << index << endl;
    text = "abcabcababaccc";
    pattern = "ababa";
    index = kmp(text, pattern);
    cout << "pattern 在 text 中第二次出现的位置是：" << index << endl;
    test1();
    return 0;
}
