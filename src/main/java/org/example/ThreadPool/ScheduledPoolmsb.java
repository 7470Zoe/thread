package org.example.ThreadPool;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


//可以执行定时的任务
public class ScheduledPoolmsb {
    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
        /** 参数
         * 命令 - 要执行的任务
         * initialDelay – 延迟第一次执行的时间
         * period – 连续执行之间的时间段
         * unit – initialDelay 和 period 参数的时间单位
         */
        service.scheduleAtFixedRate(()->{
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        },
                0,
                500,
                TimeUnit.MILLISECONDS);

    }

}
