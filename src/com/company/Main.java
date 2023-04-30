package com.company;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock[] forks = new ReentrantLock[5];
        for (int i = 0; i < forks.length; i++) { forks[i] = new ReentrantLock(); }

        String[] philNames = new String[]{"[1] Aurelius Augustinus", "[2] Karl Marx",
                                            "[3] René Descartes", "[4] Thomas Aquinas", "[5] Plato"};
        Thread[] philosophers = new Thread[5];
        for (int i = 0; i < philosophers.length ; i++) {
            philosophers[i] = new Thread(new Philosopher(philNames[i], forks[i % 5], forks[(i+1)%5]));
        }

        for (Thread philosopher : philosophers) {philosopher.start();}
        TimeUnit.SECONDS.sleep(30);
        System.out.println("It's finishing");
        System.out.println(Thread.getAllStackTraces().keySet());
        System.out.println(Runtime.getRuntime().availableProcessors());
        for (Thread philosopher : philosophers) { philosopher.interrupt();}
    }
}