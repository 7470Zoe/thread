package org.example;

import jdk.nashorn.internal.ir.CallNode;

public class FoodHall {
    public static void main(String[] args) {
        SyncStack ss = new SyncStack();
        ManTouProducer manTouProducer = new ManTouProducer(ss);
        LayborConsumer layborConsumer = new LayborConsumer(ss);
        new Thread(manTouProducer).start();
        new Thread(layborConsumer).start();

    }

    static class ManTou {
        int id;

        ManTou(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "馒头编号:" + id;
        }
    }

    static class SyncStack {
        int index = 0;
        ManTou arr[] = new ManTou[6];

        public synchronized void push(ManTou manTou) {
            if (index == arr.length) {
                try {

                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.notify();
            arr[index] = manTou;
            index++;
        }

        public synchronized ManTou pop() {
            if (index == 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.notify();
            index--;
            return arr[index];
        }
    }

    static class ManTouProducer implements Runnable {
        SyncStack ss = null;

        ManTouProducer(SyncStack ss) {
            this.ss = ss;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                ManTou manTou = new ManTou(i);
                ss.push(manTou);
                System.out.println("生产了" + manTou);
                try {
                    Thread.sleep((int) Math.random() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    static class LayborConsumer implements Runnable {
        SyncStack ss = null;

        LayborConsumer(SyncStack ss) {
            this.ss = ss;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                ManTou pop = null;

                pop = ss.pop();

                System.out.println("消费了" + pop);
                try {
                    Thread.sleep((int) Math.random() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}
