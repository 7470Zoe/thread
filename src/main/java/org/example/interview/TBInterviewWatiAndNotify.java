package org.example.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 使线程2监控线程1,当线程1中数据有5个的时候,线程2结束执行
 *
 * 曾经的面试题：（淘宝？）
 * 实现一个容器，提供两个方法，add，size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 *
 * 给lists添加volatile之后，t2能够接到通知，但是，t2线程的死循环很浪费cpu，如果不用死循环，该怎么做呢？
 *
 * 这里使用wait和notify做到，wait会释放锁，而notify不会释放锁
 * 需要注意的是，运用这种方法，必须要保证t2先执行，也就是首先让t2监听才可以
 *
 * 阅读下面的程序，并分析输出结果
 * 可以读到输出结果并不是size=5时t2退出，而是t1结束时t2才接收到通知而退出
 * 想想这是为什么？
 *
 * notify之后，t1必须释放锁，t2退出后，也必须notify，通知t1继续执行
 * 整个通信过程比较繁琐
 */
public class TBInterviewWatiAndNotify {
    //添加volatile，使t2能够得到通知
    volatile List lists = new ArrayList();

    public void add(Object o) {
        lists.add(o);
    }

    public int size() {
        return lists.size();
    }

    public static void main(String[] args) {
        final Object o = new Object();
        TBInterviewWatiAndNotify tbInterviewgood03 = new TBInterviewWatiAndNotify();
        new Thread(() -> {
            synchronized (o) {
                System.out.println("线程2启动");
                if (tbInterviewgood03.size() != 5) {
                    try {
//                        当数量不是5 的时候线程2在等待
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
//                当是5的时候
                System.out.println("t2结束");
                o.notify();
            }
        }).start();


        new Thread(() -> {
            System.out.println("线程t1启动");
            synchronized (o) {
                for (int i = 0; i < 10; i++) {
                    tbInterviewgood03.add("add" + i);
                    System.out.println("加到第" + i);
                    if (tbInterviewgood03.size() == 5) {
//                        数量为5的时候,唤醒线程2
                        o.notify();

                        try {
//                        然后让当前线程等待,从而让出锁
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

}
