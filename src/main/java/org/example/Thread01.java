package org.example;

public class Thread01 {
    public synchronized void m1(){
        System.out.println(Thread.currentThread().getName() + "m1 开始");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } System.out.println(Thread.currentThread().getName() +"m1结束执行");
    }
    public void m2(){
        System.out.println(Thread.currentThread().getName() + "m2 开始");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } System.out.println(Thread.currentThread().getName() +"m2 结束执行");

    }

    public static void main(String[] args) {
        Thread01 t = new Thread01();
        new Thread(t::m1,"这是线程1的命名").start();
        new Thread(t::m2,"这是线程22222的命名").start();
    }

    /** 输出为:
     * 这是线程1的命名m1 开始
     * 这是线程22222的命名m2 开始
     * 这是线程22222的命名m2 结束执行
     * 这是线程1的命名m1结束执行
     *
     * 这个结果表示m1不执行完,也允许m2去执行
     */
}
