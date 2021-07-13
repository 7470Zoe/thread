package org.example.interview1;

import java.util.concurrent.locks.ReentrantLock;

/**
 * wait和
 * notify 唤醒在此对象监视器上等待的单个线程 必须先notify再wait才行,不然只执行一次
 *
 */
public class PrintABABMY {
    public volatile static int i = 0;
    public volatile static boolean flag = true;
    final static Object object = new Object();
    public static ReentrantLock lock = new ReentrantLock();
//    可以使用AtomicInteger来代替使用volatile修饰的int i


    public static void main(String[] args) {
        method1_2();
    }

    /**
     * 这method1虽然能执行,但是使用flag信号来控制输出显得多余
     */
    public static void method1() {
        new Thread(() -> {
            while (i < 100) {
                synchronized (object) {
                    if (flag) {
                        try {
                            i++;
                            System.out.print("A");
//                            是让当前线程等待
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        flag = false;
                    } else {
//                        是唤醒其他线程
                        object.notify();

                    }

                }
            }
        }).start();

        new Thread(() -> {
            while (i < 100) {
                synchronized (object) {
                    if (!flag) {
                        try {
                            i++;
                            System.out.print("B");
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        flag = true;
                    } else {
                        object.notify();

                    }

                }
            }
        }).start();
    }

    /**
     * 这个1_1 只用flag控制
     */
    public static void method1_1() {
        new Thread(() -> {
            while (i < 100) {
                synchronized (object) {
                    if (flag) {

                        i++;
                        System.out.print("A");
//                            是让当前线程等待
                        flag = false;
                    }

                }
            }
        }).start();

        new Thread(() -> {
            while (i < 100) {
                synchronized (object) {
                    if (!flag) {

                        i++;
                        System.out.print("B");
                        flag = true;
                    }

                }
            }
        }).start();
    }

    /**
     * 1_2使用wait和notify控制
     */
    public static void method1_2() {
        new Thread(() -> {
            while (i < 100) {
                synchronized (object) {

                    i++;
                    System.out.print("A");
//                           在执行后,唤醒其他线程
                    object.notify();
                    try {
//                        再停止当前线程
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    object.notify();//必须，否则无法停止程序 最后这个notify是必须的,执行完了要唤醒它:嘿,执行完了哦

                }
            }
        }).start();

        new Thread(() -> {
            while (i < 100) {
                synchronized (object) {
                    i++;
                    System.out.print("B");
//                            在执行后,唤醒其他线程
                    object.notify();

//                       再停止当前线程
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    object.notify();//必须，否则无法停止程序
                }
            }
        }).start();
    }


    /**
     * 使用可重入锁的时候,把unlock放在try代码块的final里
     * 这个可重入锁只是想当于一个synchronized ,得和flag配合使用
     */
    public static void method2() {
        new Thread(() -> {
            while (i < 100) {
//                被lock和unlock中间夹着的是同步代码块
                lock.lock();
                try {
                    if (flag) {
                        i++;
                        System.out.print("A");
                        flag = false;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }).start();
        new Thread(() -> {
            while (i < 100) {
                lock.lock();
                try {
                    if (!flag) {
                        i++;
                        System.out.print("B");
                        flag = true;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }).start();

    }

    /**
     * 这种会报错
     * Exception in thread "Thread-0" Exception in thread "Thread-1" java.lang.IllegalMonitorStateException
     * 	at java.lang.Object.notify(Native Method)
     * 	at org.example.interview.PrintABABMY.lambda$method2_1$8(PrintABABMY.java:189)
     * 	at java.lang.Thread.run(Thread.java:748)
     * java.lang.IllegalMonitorStateException
     * 	at java.lang.Object.notify(Native Method)
     * 	at org.example.interview.PrintABABMY.lambda$method2_1$9(PrintABABMY.java:208)
     * 	at java.lang.Thread.run(Thread.java:748)
     *
     * 	抛出该异常表明某一线程已经试图等待对象的监视器，或者试图通知其他正在等待对象的监视器，然而本身没有指定的监视器的线程。
     * 	也就是当前的线程不是此对象监视器的所有者。当前线程要锁定该对象之后，才能用锁定的对象执行这些方法，这里需要用到synchronized关键字，锁定哪个对象就用哪个对象来执行notify(), notifyAll(),wait(), wait(long), wait(long, int)操作，否则就会报IllegalMonitorStateException异常。

     要是使用wait和notify 则一定要有synchronized关键字,因为这里的object是锁,也就是监控对象
     */
    public static void method2_1() {
        new Thread(() -> {
            while (i < 100) {
//                被lock和unlock中间夹着的是同步代码块
                lock.lock();
                try {
                    object.notify();

                        i++;
                        System.out.print("A");

                } finally {
                    lock.unlock();
                }
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (i < 100) {
                lock.lock();
                try {
                    object.notify();
                        i++;
                        System.out.print("B");

                } finally {
                    lock.unlock();
                }
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
