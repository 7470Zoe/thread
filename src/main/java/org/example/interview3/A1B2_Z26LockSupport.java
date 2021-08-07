package org.example.interview3;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.LockSupport;

public class A1B2_Z26LockSupport {
    static Thread t1=null,t2=null;
    static char[] c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static void main(String[] args) {
        t1 = new Thread(()->{
            for (int i = 1; i < 27; i++) {
                System.out.print(i);
//                打印完之后先唤醒别的线程
                LockSupport.unpark(t2);
                LockSupport.park();
            }

        });
        t2 = new Thread(()->{
            for (char ci : c) {
//                这个,第二个,必须这么写,打印的顺序也不能变,否则不是按要求输出的
                LockSupport.park();
                System.out.print(ci);
                LockSupport.unpark(t1);
            }
        });
        t1.start();
        t2.start();

    }
}
