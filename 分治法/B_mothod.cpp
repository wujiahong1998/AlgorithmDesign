#include <iostream>
#include <random>   //生成随机数的库
#include <ctime>

#include <windows.h>
#include <fstream>
#include <string>
#include <streambuf>
#define Range_max 100.0       //设置点集的范围
using namespace std;
class Point{
//点类
public:
    float x,y;
    void set(float x, float y){
        this->x = x;
        this->y = y;
    }
};
//获得两个点之间距离的平方
float getDistance2(Point &p1,Point &p2){
    return (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y);
}

float getAvgTime(float *ct){
    float sum = 0;
    for (int i = 0; i < 20; ++i) {
        sum += ct[i];
    }
    return sum/20;
}

void set_Points(Point *p, int length) {//初始化点集
    srand(unsigned(time(NULL)));//新建随机种子
    for (int i = 0; i < length; ++i) {
        //生成随机数进行舒初始化
        p[i].set((rand() % int(Range_max * 2000)) / Range_max - Range_max,
                 (rand() % int(Range_max * 2000)) / Range_max - Range_max);
    }
    cout << "点集初始化完成！" << endl;
}

float B_method(Point *p,int &a,int &b,int length){
    float d = getDistance2(p[0], p[1]);
    float distance2;
    for (int i = 0; i < length - 1; ++i) {
        for (int j = i + 1; j < length; ++j) {
            distance2 = getDistance2(p[i], p[j]);
            if (distance2 < d) {
                a = i;
                b = j;
                d = distance2;
            }
        }
    }
    return d;
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
    int a, b;
    float distance2 = B_method(p, a, b, 4);
    cout << "结果为：";
    cout << "           最近点对为：(" << p[a].x << "," << p[a].y << ")和(" << p[b].x << "," << p[b].y << ")" << "  ";
    cout << "最短距离为：" << sqrt(distance2) << "  "<<endl<<endl;
}

//主函数
int main(){

    Test();
    //输入需要测试的点的个数
    int N;
    while(true) {
        cout << "Please input the N:(N==0 will quit)" << endl;
        cin >> N;
        if (N == 0) break;
        //生成N个点
        cout<<"规模为"<<N<<"  ---------------以下为20组样本测试--------"<<endl;

        int i, j;
        float costTime[20];
        int l = 0;
        //开始二十次的循环，最后求平均值
        for (int k = 0; k < 20; k++) {
            Point *p = new Point[N];
            //初始化随机点
            set_Points(p,N);

            //----------------蛮力法-------------------//
            //初始化最小点对和最短距离
            clock_t start = clock();
            int a,b; //存最近点对的下标
            float distance2;
            distance2 = B_method(p,a,b,N);
            clock_t end = clock();
            float cost = end-start;

            cout << "最近点对为：(" << p[a].x << "," << p[a].y << ")和(" << p[b].x << "," << p[b].y << ")" << "  ";
            cout << "最短距离为：" << sqrt(distance2) << "  ";
            cout << "运行时间为：" << cost << "ms" << endl;
            costTime[l] = cost;
            l+=1;

        }
        float avgtime = getAvgTime(costTime);
        cout<<"--------------------------------------------------------平均运行时间为："<<avgtime<<"ms ------"<<endl<<endl;

    }

    return 0;
}
