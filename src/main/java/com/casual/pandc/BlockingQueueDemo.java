package com.casual.pandc;

import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueDemo {

    private LinkedBlockingQueue<Integer> list = new LinkedBlockingQueue<Integer>(20);

    public void produce() throws InterruptedException {
        for (; ; ) {
            System.out.println(list.size());
            list.put(1);
        }
    }

    public void consume() throws InterruptedException {
        for (; ; ) {
            System.out.println(list.size());
            list.take();
        }
    }

    static class Producer implements Runnable {
        BlockingQueueDemo worker;

        public Producer(BlockingQueueDemo worker) {
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
        BlockingQueueDemo worker;

        public Consumer(BlockingQueueDemo worker) {
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
        BlockingQueueDemo worker = new BlockingQueueDemo();
        new Thread(new BlockingQueueDemo.Producer(worker)).start();
        new Thread(new BlockingQueueDemo.Producer(worker)).start();
        new Thread(new BlockingQueueDemo.Consumer(worker)).start();
        new Thread(new BlockingQueueDemo.Consumer(worker)).start();
    }
}

