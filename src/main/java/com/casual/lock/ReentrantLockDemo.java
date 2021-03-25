package com.casual.lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    private static ReentrantLock lock = new ReentrantLock(true);

    void doSth(){
        System.out.println("111");
    }

    public static void main(String[] args) {
        ReentrantLockDemo m = new ReentrantLockDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    
                }
            }).start();
        }
    }
}
