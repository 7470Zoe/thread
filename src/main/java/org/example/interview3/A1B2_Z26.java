package org.example.interview3;

import java.util.concurrent.locks.LockSupport;

public class A1B2_Z26 {
    static Thread t1 = null, t2 = null;

    public static void main(String[] args) throws Exception {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        t1 = new Thread(() -> {

            for(char c : aI) {
                System.out.print(c);
                LockSupport.unpark(t2); //叫醒T2
                LockSupport.park(); //T1阻塞
            }

        }, "t1");

        t2 = new Thread(() -> {

            for(char c : aC) {
                LockSupport.unpark(t1); //叫醒t1
                LockSupport.park(); //t2阻塞
                System.out.print(c);
            }

        }, "t2");

        t1.start();
        t2.start();
    }

}
