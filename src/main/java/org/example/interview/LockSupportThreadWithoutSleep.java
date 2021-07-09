package org.example.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


public class LockSupportThreadWithoutSleep {
    // 添加volatile，使t2能够得到通知
    volatile List lists = new ArrayList();

    public void add(Object o) {
        lists.add(o);
    }

    public int size() {
        return lists.size();
    }
    static Thread t = null, thread = null;
    public static void main(String[] args) {
        LockSupportThreadWithoutSleep lockSupportThread = new LockSupportThreadWithoutSleep();

//        两层的park和unpark不能写反了


        t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {

                lockSupportThread.add("i");
                System.out.println("加到了第" + i);
                if (lockSupportThread.size() == 5) {
//                    先把自己park上
                    LockSupport.unpark(thread);
                    LockSupport.park();
                }
            }
        }
        );
        thread = new Thread(() -> {

            LockSupport.park();

//                不等于5的时候,不要park t然后把自己park上
            System.out.println("线程2监控");

            LockSupport.unpark(t);
            System.out.println("线程2结束");

        });

        thread.start();
        t.start();
    }

}
