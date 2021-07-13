package org.example.interview3;

import java.util.concurrent.locks.LockSupport;

public class A1B2_Z26MY02 {
//    自己写的自旋
    enum ReadyToRun {T1,T2};
    static char[] c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static volatile ReadyToRun r = ReadyToRun.T1;

    public static void main(String[] args) {
     new Thread(()->{
            for (int i = 1; i < 27; i++) {
                while(r!=ReadyToRun.T1){
//                    如果它不是T1,那么它一直在这循环执行,while,然后{}又没东西,相当于一直在这里等
                }
                System.out.print(i);
//                打印完之后先唤醒别的线程
                r = ReadyToRun.T2;

            }

        }).start();
        new Thread(()->{
            for (char c1 : c) {
                while(r!=ReadyToRun.T2){
//                    如果它不是T2,那么它一直在这循环执行,while,然后{}又没东西,相当于一直在这里等
                }
                System.out.print(c1);
//                打印完之后先唤醒别的线程
                r = ReadyToRun.T1;

            }
        }).start();
    }
}
