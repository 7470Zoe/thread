package org.example.ThreadPool;

import java.util.concurrent.*;

public class Callable1 {
    public static void main(String[] args)  throws ExecutionException, InterruptedException{
        Callable<String> c =  new Callable(){


            @Override
            public Object call() throws Exception {
                return "hello";
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
//        submit是异步的
        Future<String> submit = executorService.submit(c);
        System.out.println(submit.get());

    }

}
