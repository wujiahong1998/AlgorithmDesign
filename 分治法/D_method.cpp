//
// Created by 吴嘉鸿 on 2019/4/6.
//
#include <iostream>
#include <algorithm>
#include <ctime>
#include <cstdlib>
#include <cmath>

using namespace std;
#define INFINITE_DISTANCE 65535    // 无限大距离
#define Range_max 100.0       //设置点集的范围

class Point {
    //点类
public:
    float x, y;

    void set(float x, float y) {
        this->x = x;
        this->y = y;
    }
};

void set_Points(Point *p, int length) {//初始化点集
    srand(unsigned(time(NULL)));//新建随机种子
    for (int i = 0; i < length; ++i) {
        //生成随机数进行舒初始化
        p[i].set((rand() % int(Range_max * 2000)) / Range_max - Range_max,
                 (rand() % int(Range_max * 2000)) / Range_max - Range_max);
    }
    cout << "点集初始化完成！" << endl;
}

float get_distance2(Point &a, Point &b) {//获取两点距离的平方
    return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
}


bool compareX(Point a, Point b) {//自定义排序规则：依照结构体中的x成员变量升序排序
    return a.x < b.x;
}

bool compareY(Point a, Point b) {//自定义排序规则：依照结构体中的x成员变量升序排序
    return a.y < b.y;
}

float Solve_Closest_pair(Point p[], Point &a, Point &b, int length) {//核心函数，找出最近点对及最短距离
    float distance2;    //存放最短距离的平方
    float d1, d2;       //存放左半边和右半边最短距离平方
    Point a1, a2, b1, b2;  //存放左半边和右半边最近点对

    int i, j, k;  //计量下标

    if (length < 2) {//假如点数小于二，没有最短距离，返回无穷大距离
        return INFINITE_DISTANCE;
    } else if (length == 2) {//若点数为2，最近点对就是他们本身，最短距离就是它们之间的距离
        a = p[0];
        b = p[1];
        distance2 = get_distance2(p[0], p[1]);
    } else {//若点数大于2，采用递归实现


        sort(p, p + length, compareX);   //按x进行排序

        float mid = p[(length - 1) / 2].x;    //求出中线划分区域

        int l1 = (length) / 2;        //左半边点集长度
        int l2 = length - (length) / 2;     //右半边点集长度

        Point *p1 = new Point[l1];
        Point *p2 = new Point[l2];
        for (i = 0; i < l1; i++) p1[i] = p[i];
        for (i = 0, j = l1; j < length; j++) p2[i++] = p[j];

        d1 = Solve_Closest_pair(p1, a1, b1, l1);   //找出左半边最近点集及最短距离的平方
        d2 = Solve_Closest_pair(p2, a2, b2, l2);   //找出右半边最近点集及最短距离的平方

        if (d1 < d2) {//比较找出两边最短距离及对应点对
            distance2 = d1;
            a = a1;
            b = b1;
        } else {
            distance2 = d2;
            a = a2;
            b = b2;
        }

        //新建点数组存放距离中轴小于d的点
        Point *p3 = new Point[length];
        for (i = 0, k = 0; i < length; i++) {
            if ((p[i].x - mid) * (p[i].x - mid) <= distance2) {
                p3[k++] = p[i];
            }
        }
        //按y进行排序
        sort(p3, p3 + k, compareY);

        for (i = 0; i < k; i++) {
            if (p3[i].x - mid <= 0) {
                //只计算左边的点
                int x = 0;
                //先与相邻的右侧的上三个点比较，判断是否更新最短距离的平方及点对
                for (j = i + 1; j <= i + 3 + x && j < k; j++) {
                    if (p3[i].x - mid < 0) {//左测的点跳过不必判断
                        x++;
                        continue;
                    }
                    if (get_distance2(p3[i], p3[j]) < distance2) {
                        distance2 = get_distance2(p3[i], p3[j]);
                        a = p3[i];
                        b = p3[j];
                    }
                }
                x = 0;
                //再与相邻的右侧的下三个点比较，判断是否更新最短距离的平方及点对
                for (j = i - 1; j >= i - 3 - x && j >= 0; j--) {
                    if (p3[i].x - mid < 0) {//左测的点跳过不必判断
                        x++;
                        continue;
                    }
                    if (get_distance2(p3[i], p3[j]) < distance2) {
                        distance2 = get_distance2(p3[i], p3[j]);
                        a = p3[i];
                        b = p3[j];
                    }
                }

            }
        }
    }
    return distance2;
}

void Test() {
    cout << "<-<-<-<-<-<-测试算法的正确性->->->->->->" << endl;
    cout << "以规模为4的点集进行测试，下面是四个点的坐标：" << endl;
    Point *p = new Point[4];
    p[0].set(1, 1);
    p[1].set(5, 8);
    p[2].set(4, 6);
    p[3].set(7, 18);
    for (int i = 0; i < 4; ++i) {
        cout << "(" << p[i].x << "," << p[i].y << ")" << " ";
    }
    cout << endl;
    cout << "测试结果应该为：    最近点对为(4,6)与(5,8),最近距离为2.23607" << endl;
    Point a, b;
    float distance2 = Solve_Closest_pair(p, a, b, 4);
    cout << "结果为：";
    cout << "           最近点对为：" << "(" << a.x << "," << a.y << ")和" << "(" << b.x << "," << b.y << ")" << "    距离为："
         << sqrt(distance2) << endl << endl;
}

int main() {
    Test();
    int N;
    while (true) {
        cout << "请输入二维点集的个数(输入0停止)：";
        cin >> N;
        if (N == 0) break;
        else if (N < 2) cout << "请输入2以上的规模" << endl;
        else {
            Point a, b;    //存放最近的两个点
            float distance;  //存放点最小距离
            Point *p = new Point[N];  //生成输入规模的点集
            set_Points(p, N);    //初始化点集
            clock_t t1 = clock();
            distance = Solve_Closest_pair(p, a, b, N);
            clock_t t2 = clock();
            cout << "最近点对为：" << "(" << a.x << "," << a.y << ")和" << "(" << b.x << "," << b.y << ")" << "    距离为："
                 << sqrt(distance) << endl;
            cout<<"消耗时间为："<<t2-t1<<"ms"<<endl;
        }
    }

    return 0;
}
