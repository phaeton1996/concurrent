package com.casual.pandc;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class WaitAndNotify {
    //使用静态变量的lock保证不是单例模式情况下的锁的唯一性
    private static final Object lock = new Object();
    private static final List<Integer> pool = new ArrayList<Integer>();
    public void produce() throws InterruptedException {
        synchronized (lock){
            for (;;){
                while (pool.size() == 20) {
                    lock.wait();
                }
                pool.add(1);
                System.out.println("生产一个物件，list的容量为" + pool.size());
                lock.notifyAll();
            }
        }
    }

    public void consume() throws InterruptedException {
        synchronized (lock){
            for (;;){
                while (pool.size() == 0) {
                    lock.wait();
                }
                pool.remove(0);
                System.out.println("消费一个物件，list的容量为" + pool.size());
                lock.notifyAll();
            }
        }
    }

    static class Producer implements Runnable{
        WaitAndNotify worker;
        public Producer(WaitAndNotify worker) {
            this.worker = worker;
        }

        public void run() {
            try {
                worker.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class Consumer implements Runnable{
        WaitAndNotify worker;
        public Consumer(WaitAndNotify worker) {
            this.worker = worker;
        }

        public void run() {
            try {
                worker.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        WaitAndNotify worker = new WaitAndNotify();
        new Thread(new Producer(worker)).start();
        new Thread(new Producer(worker)).start();
        new Thread(new Consumer(worker)).start();
        new Thread(new Consumer(worker)).start();
    }
}
