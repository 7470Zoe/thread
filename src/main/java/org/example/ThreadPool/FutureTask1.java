package org.example.ThreadPool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureTask1 {
//    它既是feture又是runnable
    public static void main(String[] args)throws InterruptedException, ExecutionException {
        FutureTask<Integer> f =    new FutureTask<>(()->{
            TimeUnit.MILLISECONDS.sleep(500);
            return 1000;

        });

        new Thread(f).start();
        System.out.println(f.get());
    }
}
