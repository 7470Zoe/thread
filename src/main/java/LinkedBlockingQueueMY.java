import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LinkedBlockingQueueMY {
    static BlockingQueue<String> strQueue = new LinkedBlockingQueue<>();
    static Random r = new Random();

    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0; i < 1000; i++) {
                try {
                    strQueue.put("第几个"+i);
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"p1").start();


        for (int j = 0; j < 5; j++) {
             new Thread(()->{
                for (;;) {
                    try {
                        System.out.println(Thread.currentThread().getName()+"keke"+strQueue.take());

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"c"+j).start();
        }
    }


}
