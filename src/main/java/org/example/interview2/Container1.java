package org.example.interview2;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 *  面试题：写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 *  能够支持2个生产者线程以及10个消费者线程的阻塞调用
 *
 *  同步容器,指可以多线程放入和多线程取出
 *
 *  使用wait和notify/notifyAll来实现
 */
public class Container1<T> {

    final private LinkedList<T> lists = new LinkedList<>();
    final private int MAX = 10; //最多10个元素
    private int count = 0;


    public synchronized void put(T t) {
        while(lists.size() == MAX) { //想想为什么用while而不是用if？
            try {
//                当前线程停住
                this.wait(); //effective java
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        lists.add(t);
        ++count;//因为这里有count++,所以要有synchronized
        this.notifyAll(); //通知消费者线程进行消费
    }

    public synchronized T get() {
        T t = null;
        while(lists.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        使用LinkList是因为有这个方法
        t = lists.removeFirst();
        count --;
        this.notifyAll(); //通知生产者进行生产
        return t;
    }

    public static void main(String[] args) {
        Container1<String> c = new Container1<>();
        //启动消费者线程 10个消费者线程,每个消费者消费5个T
        for(int i=0; i<10; i++) {
            new Thread(()->{
                for(int j=0; j<5; j++) {
                    System.out.println(c.get());
                }
            }, "c" + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //启动生产者线程  2个生产者线程,每个生产者生产25个T
        for(int i=0; i<2; i++) {
            new Thread(()->{
                for(int j=0; j<25; j++) {
                    c.put(Thread.currentThread().getName() + " " + j);
                }
            }, "p" + i).start();
        }
    }


}
