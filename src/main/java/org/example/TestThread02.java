package org.example;

/**
 * 两个方法都对b资源有修改,所以应该在涉及到修改b资源的方法上都加上同步
 */
public class TestThread02 implements Runnable {
    int b=100;
    public synchronized void m1() throws Exception{
        b=1000;
        Thread.sleep(5000);
        System.out.println("m1b=" + b);

    }
    public /*synchronized*/ void m2() throws Exception{

        Thread.sleep(2000);
        b=2000;
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
        TestThread02 testThread = new TestThread02();
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

    /** m1和m2都无synchronized的输出.都无的时候,
     *  m2b=2000
     * 2000
     * m1b=2000
     */

    /**
     * 当m1有synchronized的但m2无的时候,当m2有synchronized的但m1无的时候 都是如下的输出
     * m2b=2000
     * 2000
     * m1b=2000
     *
     *
     * 虽然m1有锁,但是m2无锁,所以此时m2可以对m1进行访问,所以在此时改了b的值,如果打印语句在睡眠语句上面那么输出的是,
     * m1有,m2无
     * m1b=1000
     * m2b=2000
     * 2000
     */

}
