package org.example.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TBInterviewgood02 {
  volatile List list=  new ArrayList();
//    Collections.synchronizedList是同步容器
//volatile List list = Collections.synchronizedList(new LinkedList<>());
  public  void add(Object o){
      list.add(o);
  }
  public  int size(){
      return list.size();
  }

    public static void main(String[] args) {
        TBInterviewgood02 t=   new TBInterviewgood02();
        final Object lo = new Object();
        new Thread(() -> {
            synchronized (lo){

                for (int i = 0; i < 10; i++) {
                    t.add("add" + i);
                    System.out.println("设置了" + i);
                    lo.notify();
                    if(i==5){
                        try {
                            lo.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "t1").start();

        new Thread(()->{
            synchronized (lo){
                System.out.println("t2启动");
                if(t.size()!=5){

                    try {
                        lo.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        },"t2").start();
    }


}
