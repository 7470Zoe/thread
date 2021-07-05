package org.example;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Account {
    private String name;
    private Double balance;
    public synchronized void set(String name,Double balance){
        this.name=name;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.balance=balance;
    }
    public  /*synchronized*/ void getBalance(String name){
        System.out.println("此时账户余额的值是"+this.balance);
    }

    public static void main(String[] args) {
        Account account = new Account();
//        这是在主线程外另外启动 了一个线程去设置小李的账户,然后用当前线程来获取小李账户的余额
//        这两方法,上的是同一把锁
        new Thread(()->account.set("小李",100.01)).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        account.getBalance("小李");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        account.getBalance("小李");

    }
    /** set和get方法只有一个有synchronized时,输出为如下,想让两次都拿到100.01,那么给两个方法都要加锁就行了
     * 此时账户余额的值是null
     * 此时账户余额的值是100.01
     *
     * 对于访问这个资源的方法都要上锁才能数据不出错
     */
}
