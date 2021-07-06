package org.example;

import java.util.ArrayList;
import java.util.List;

public class TestVolatile {
    /*volatile */int count = 0;
    public synchronized void m(){
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }

    public static void main(String[] args) {
        TestVolatile t = new TestVolatile();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(t::m,"thread-"+i));
        }
        threads.forEach((e)->e.start());
        threads.forEach((e)-> {
            try {
                //让这些线程循环join进别的线程里去执行,会让count累加起来
                e.join();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        System.out.println(t.count);
    }

    /**
     * 一共10个线程,每个线程执行一万次的count++,期望结果是得到10万,没有synchronized不会保证线程执行的原子性,得不到10万
     * volatile只能是保证线程可见性,不能保证原子性
     */
}
