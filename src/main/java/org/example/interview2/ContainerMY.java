package org.example.interview2;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 面试题：写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 * 能够支持2个生产者线程以及10个消费者线程的阻塞调用
 * <p>
 * 同步容器,指可以多线程放入和多线程取出
 * <p>
 * 使用wait和notify/notifyAll来实现
 */
public class ContainerMY<T> {
//    public volatile LinkedList<T> list = new LinkedList<T>();
    final private  LinkedList<T> list = new LinkedList<T>();
//    public volatile int count = 0;
    private  int count = 0;
//    public volatile int MAX = 10000;
    final private int MAX = 10000;
    private ReentrantLock lock = new ReentrantLock();
    private Condition consumer = lock.newCondition();
    private Condition producer = lock.newCondition();


    public void put(T t) {
        lock.lock();
        try {
            while (list.size() == MAX) {
                producer.await();
            }
            list.add(t);
//            count++;
            System.out.println(Thread.currentThread().getName()+"生产" + t+"号");
            consumer.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            lock.unlock();
        }
    }


    public T get() {
        T t =null;
        lock.lock();
        try {
            while (list.size() == 0) {

                consumer.await();
            }
            t= list.removeFirst();
//            count--;
            System.out.println(Thread.currentThread().getName()+"消费了一个"+t+"号");

            producer.signalAll();

            } catch(InterruptedException e){
                e.printStackTrace();
            }finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args) {
        ContainerMY<Integer> containerMY = new ContainerMY();
//        2个生产者
        for (int i = 0; i < 5; i++) {
                new Thread(()->{
                    for (int i1 = 0; i1 < 100; i1++) {
                        containerMY.put(i1);
                    }
                },"生产者"+i).start();
            }
//        10个消费者,每人消费3个

        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                for (int i1 = 0; i1 < 7; i1++) {
                    containerMY.get();
                }
            },"消费者"+i).start();
        }


    }
}
