package org.example;

public class TestThread01 implements Runnable {
    int b = 100;

    public synchronized void m1() throws Exception {
        b = 1000;
//        Thread.sleep(5000);
        System.out.println("m1b=" + b);

    }

    public /*synchronized */void m2() throws Exception {

//        Thread.sleep(2000);
        b = 2000;
        System.out.println("m2b=" + b);

    }

    @Override
    public void run() {
        try {
            m1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestThread01 testThread = new TestThread01();
        Thread thread = new Thread(testThread);
        thread.start();
        try {
            testThread.m2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(testThread.b);

    }
    /** m1和m2都有synchronized的输出
     * m2b=2000
     * 2000
     * m1b=1000
     */

    /** m1和m2都无synchronized的输出.都无的时候,谁最后改,那么b是谁的值
     * m2b=2000
     * 1000
     * m1b=1000
     */

    /**
     * 当只有一个有synchronized的时候,没有同步的时候,这个b是另外 一个的值
     */
}
