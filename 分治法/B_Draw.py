import random
import math
import matplotlib.pylab as plt
import matplotlib.font_manager as fm
import numpy as np

class Point:
    def __init__(self,x,y):
        self.x = x
        self.y = y


def get_distance2(a,b):
    return (a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y)


def setPoints(n):
    p = []
    plt.ion()
    
    for i in range(n):
        p.append(Point(random.randint(0,10000)/100,random.randint(0,10000)/100))
         # 设定标题等
        plt.title("Graph")
        plt.grid(True)
        plt.xlabel("X")
        plt.ylabel("Y")
        my_x_ticks = np.arange(0 ,100, 5)
        my_y_ticks = np.arange(0, 100, 5)
        plt.xticks(my_x_ticks)
        plt.yticks(my_y_ticks)
        plt.scatter(p[i].x ,p[i].y, s=20, c='b', marker="o")
        # 暂停
        plt.pause(0.2)
    print("点集初始化完成！")
    return p


def PrintPoints(p):
    for each in p:
        print("(%.2f,%.2f)" % (each.x,each.y))

def B_method(p,len):
    d = get_distance2(p[0],p[1])
    a = 0
    b = 1
    plt.plot([p[a].x, p[b].x],[p[a].y, p[b].y], color='r')
    plt.pause(1)
    for i in range(len-1):
        for j in range(i+1,len):
            if i == 0 and j == 1:
                    continue
            distance2 = get_distance2(p[i],p[j])
            plt.plot([p[i].x, p[j].x],[p[i].y, p[j].y], color='B',ls='--')
            plt.pause(1)
            plt.plot([p[i].x, p[j].x],[p[i].y, p[j].y], color='W',alpha=0.99)
            plt.pause(1)
            if distance2 < d:
                plt.plot([p[a].x, p[b].x],[p[a].y, p[b].y], color='W',alpha=0.99)
                plt.pause(1)
                a = i
                b = j
                d = distance2
                plt.plot([p[i].x, p[j].x],[p[i].y, p[j].y], color='r')
                plt.pause(1)

    print("最近点对为：(%.2f,%.2f) 和 (%.2f,%.2f)，距离为: %.2f" % (p[a].x,p[a].y,p[b].x,p[b].y,math.sqrt(d)))

if __name__ == '__main__':
    N = int(input("请输入点集规模："))
    p = setPoints(N)

    PrintPoints(p)
    B_method(p,N)
