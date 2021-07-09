package org.example.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport想什么时候停,就什么时候停,wait()方法必须在某把锁上面
 */
public class TBInterviewbad {
    public static void main(String[] args) {
        try {
//            让当前线程睡个一段时间
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Container container = new Container();
        Input input = new Input(container);
        GetSize getSize = new GetSize(container);
        Thread thread1 = new Thread(input);
        Thread thread2 = new Thread(getSize);
        thread1.start();
        thread2.start();
    }

    public static class Container{
       volatile List<Integer> element = new ArrayList<>();
        synchronized void add(Integer a){
            element.add(a);
        }
        synchronized Integer size(){
           return element.size();
        }
    }
        public static class Input implements Runnable{
            Container container = null;
            Input( Container container){this.container=container;}
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    container.add(i);
                    System.out.println(i+"加了");
                }

            }
        }
    public static class GetSize implements Runnable{
        Container container = null;
        GetSize(Container container){this.container=container;}
        @Override
        public void run() {
            System.out.println(container.size());
            if (container.size()==5){
                System.out.println("容器中数据 有5个了");
                    LockSupport.park();
            }

        }
    }


}
