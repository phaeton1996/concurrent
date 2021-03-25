package com.casual.pandc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {
    private static final List<Integer> pool = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();
    Condition full = lock.newCondition();
    Condition empty = lock.newCondition();

    public void produce() throws InterruptedException {
        for (; ; ) {
            lock.lock();
            try {
                while (pool.size() == 20) {
                    full.await();
                }
                pool.add(1);
                System.out.println("生产一个物件，list的容量为" + pool.size());
                empty.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public void consume() throws InterruptedException {
        for (; ; ) {
            lock.lock();
            try {
                while (pool.size() == 0) {
                    empty.await();
                }
                pool.remove(0);
                System.out.println("消费一个物件，list的容量为" + pool.size());
                full.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    static class Producer implements Runnable {
        ConditionDemo worker;

        public Producer(ConditionDemo worker) {
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

    static class Consumer implements Runnable {
        ConditionDemo worker;

        public Consumer(ConditionDemo worker) {
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
        ConditionDemo worker = new ConditionDemo();
        new Thread(new ConditionDemo.Producer(worker)).start();
        new Thread(new ConditionDemo.Producer(worker)).start();
        new Thread(new ConditionDemo.Consumer(worker)).start();
        new Thread(new ConditionDemo.Consumer(worker)).start();
    }
}
