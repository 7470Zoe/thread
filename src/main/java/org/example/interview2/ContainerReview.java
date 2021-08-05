package org.example.interview2;

import jdk.nashorn.internal.ir.CallNode;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ContainerReview<T> {

    final private LinkedList<T> list = new LinkedList<T>();
    final private int Max = 10000;
    private ReentrantLock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    public void put(T t) {
        lock.lock();

        try {
            while (list.size() == Max) {
                producer.await();
            }
            list.add(t);
            consumer.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }


    }
    public T get(){
        T t = null;
        lock.lock();
        try {
            while (list.size() == 0) {
                consumer.await();
            }
             t = list.removeFirst();
            producer.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
        return t;
    }
}
