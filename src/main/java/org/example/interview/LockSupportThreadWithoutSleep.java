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
        thread = new Thread(() -> {

            if (lockSupportThread.size() != 5) {
                LockSupport.unpark(t);
                LockSupport.park();
                System.out.println("线程2监控");
            }
            System.out.println("线程2结束");

        });


        t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {

                lockSupportThread.add("i");
                System.out.println("加到了第" + i);
                if (lockSupportThread.size() == 5) {
                    LockSupport.park();
                    LockSupport.unpark(thread);
                    System.out.println("线程2启动");
                }
            }
        }
        );


        thread.start();
        t.start();
    }
/** 输出为:
 * 加到了第0
 * 加到了第1
 * 加到了第2
 * 加到了第3
 * 加到了第4
 * 线程2启动
 * 线程2监控
 * 线程2结束
 * 加到了第5
 * 加到了第6
 * 加到了第7
 * 加到了第8
 * 加到了第9
 */
}
