package org.example.ticket;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueue1 {
   static Queue<String> tickes =  new ConcurrentLinkedQueue<>();
   static{
      for (int i = 0; i < 1000; i++) {
         tickes.add("第"+i+"张票");
      }
   }

   public static void main(String[] args) {
      for (int i = 0; i < 10; i++) {
         new Thread(()->{
            while(true){
               String poll = tickes.poll();
               if (poll==null){break;}
               System.out.println(poll+Thread.currentThread().getName());
            }
         }).start();

      }
   }
}
