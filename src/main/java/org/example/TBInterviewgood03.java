package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 使线程2监控线程1,当线程1中数据有5个的时候,线程2结束执行
 */
public class TBInterviewgood03 {
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
        TBInterviewgood03 tbInterviewgood03 = new TBInterviewgood03();
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
