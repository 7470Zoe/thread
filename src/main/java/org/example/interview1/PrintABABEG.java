package org.example.interview1;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintABABEG {
    public static int i = 1;
    public volatile static int j = 1;
    public volatile static boolean flag = false;
    public static int count = 0;

    public static Lock lock = new ReentrantLock();
    public static Condition conditionA = lock.newCondition();
    public static Condition conditionB = lock.newCondition();

    private static CountDownLatch latch = new CountDownLatch(2);
    private static AtomicInteger numA = new AtomicInteger();
    private static AtomicInteger numB = new AtomicInteger();

    private static Object block = new Object();

    public static void main(String[] args) {
        test2();
    }

    public static void test6() {
        new Thread(() -> {
            while (i < 10) {
                synchronized (block) {
                    if (flag) {
                        try {
                            block.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName()
                                + "----" + (i++));
                        flag = true;
                        block.notifyAll();
                    }
                }
            }
        }).start();

        new Thread(() -> {
            while (j < 10) {
                synchronized (block) {

                    if (!flag) {
                        try {
                            block.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName()
                                + "----" + (j++));
                        flag = false;
                        block.notifyAll();
                    }

                }
            }
        }).start();
    }



    public static void test2() {
        new Thread(() -> {
            while (i < 10) {

                lock.lock();
                try {
                    while (!flag) {
                        System.out.println(Thread.currentThread().getName()
                                + "----" + (i++));
                        flag = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();

        new Thread(() -> {
            while (j < 10) {

                lock.lock();
                try {
                    while (flag) {
                        System.out.println(Thread.currentThread().getName()
                                + "----" + (j++));
                        flag = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();
    }



    public static void test4() {

        new Thread(() -> {
            try {
                lock.lock();
                while (i < 10) {
                    if (flag) {
                        conditionA.await();
                    }
                    flag = true;
                    System.out.println(Thread.currentThread().getName() + "----"
                            + (i++));
                    conditionB.signal();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }	).start();

        new Thread(() -> {

            try {
                lock.lock();
                while (j < 10) {
                    if (!flag) {
                        conditionB.await();
                    }
                    flag = false;
                    System.out.println(Thread.currentThread().getName() + "----"
                            + (j++));
                    conditionA.signal();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }	).start();
    }



    public static void test5() {

        new Thread(() -> {
            while (numA.get() < 10) {
                if (!flag) {
                    System.out.println(Thread.currentThread().getName()
                            + "----" + (numA.incrementAndGet()));
                    flag = true;
                }
                //   latch.countDown();

            }
        }).start();

        new Thread(() -> {
            while (numB.get() < 10) {
                if (flag) {
                    System.out.println(Thread.currentThread().getName()
                            + "----" + (numB.incrementAndGet()));
                    flag = false;
                }
                //	latch.countDown();

            }
        }).start();

    }



    public static void test1() {
        new Thread(() -> {
            while (i < 10) {
                if (!flag) {
                    System.out.println(Thread.currentThread().getName()
                            + "----" + (i++));
                    flag = true;
                }
            }
        }).start();

        new Thread(() -> {
            while (j < 10) {
                if (flag) {
                    System.out.println(Thread.currentThread().getName()
                            + "----" + (-j++));
                    flag = false;
                }
            }
        }).start();
    }
}
