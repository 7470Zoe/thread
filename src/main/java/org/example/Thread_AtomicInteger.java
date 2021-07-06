package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Thread_AtomicInteger {
    AtomicInteger count = new AtomicInteger(0);


    public void k() {
        ReentrantLock reentrantLock = new ReentrantLock();
        for (int i = 0; i < 10000; i++) {
//          这里内部用了CAS操作,无锁操作
            count.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        Thread_AtomicInteger t = new Thread_AtomicInteger();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
          /*  threads.add(new Thread(t::k, "thread-" + i));*/

            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    t.k();
                }
            })) ;
        }

        threads.forEach((e) -> e.start());
        threads.forEach((e) -> {
            try {
                //让这些线程循环join进别的线程里去执行,会让count累加起来
                e.join();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        System.out.println(t.count);
    }
}
