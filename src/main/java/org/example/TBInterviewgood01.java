package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TBInterviewgood01 {
//  volatile List list=  new ArrayList();
//    Collections.synchronizedList是同步容器
volatile List list = Collections.synchronizedList(new LinkedList<>());
  public synchronized void add(Object o){
      list.add(o);
  }
  public synchronized int size(){
      return list.size();
  }

    public static void main(String[] args) {
        TBInterviewgood01 t=   new TBInterviewgood01();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                t.add("add" + i);
                System.out.println("设置了" + i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        new Thread(()->{
            boolean f=true;
            while(f){
                if(t.size()==5){
                    f=false;
                }
            }
            System.out.println("t2结束");
        },"t2").start();
    }


}
