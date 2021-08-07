package org.example.ThreadPool;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 用法 管理Future执行结果
 */
public class CompletableFuture1 {

    public static void main(String[] args)throws ExecutionException, InterruptedException {

        long start, end;

        /*start = System.currentTimeMillis();

        priceOfTM();
        priceOfTB();
        priceOfJD();

        end = System.currentTimeMillis();
        System.out.println("use serial method call! " + (end - start));*/

        start = System.currentTimeMillis();
        CompletableFuture<Double> TM = CompletableFuture.supplyAsync(() -> priceOfTM());
        CompletableFuture<Double> TB = CompletableFuture.supplyAsync(() -> priceOfTB());
        CompletableFuture<Double> JD = CompletableFuture.supplyAsync(() -> priceOfJD());
        CompletableFuture.allOf(TM,TB,JD).join();

        end = System.currentTimeMillis();
        System.out.println("use completable future! " + (end - start));

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static double priceOfTM() {
        delay();
        return 1.00;
    }

    private static double priceOfTB() {
        delay();
        return 2.00;
    }

    private static double priceOfJD() {
        delay();
        return 3.00;
    }

    /*private static double priceOfAmazon() {
        delay();
        throw new RuntimeException("product not exist!");
    }*/

    private static void delay() {
        int time = new Random().nextInt(500);
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("After %s sleep!\n", time);
    }

}
