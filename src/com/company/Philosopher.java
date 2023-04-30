package com.company;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher implements Runnable {

    Random random = new Random();

    boolean running;
    String name;
    ReentrantLock leftFork;
    ReentrantLock rightFork;
    int limit;
    boolean fork_taken;
    boolean fork_right_taken;

    public Philosopher(String name,
                       ReentrantLock leftFork, ReentrantLock rightFork) {
        this.running = true;
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.limit = 0;
        this.fork_taken = false;
        this.fork_right_taken = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (!fork_taken) {
                    TimeUnit.SECONDS.sleep(random.nextInt(3, 5));
                    System.out.printf("%s is hungry", name);
                    System.out.println();
                }
                dine();
            } catch (InterruptedException e){
            }
        }
    }

    public void dine() throws InterruptedException {
        while (running) {
            if (!leftFork.isLocked()) {
                leftFork.lock();
                fork_taken = true;
                System.out.println(name + " " + "has taken left fork");
            }

            if (fork_taken) {
                if (rightFork.isLocked()) {
                    if (!fork_right_taken) {
                        System.out.println("right fork was taken from" + " " + name);
                        fork_right_taken = true;
                    }
                    TimeUnit.SECONDS.sleep(random.nextInt(1,3));
                    limit++;
                    System.out.println(limit + " " + name);
                    if (limit > random.nextInt(5,10)) {
                        leftFork.unlock();
                        fork_taken = false;
                        System.out.println(name + " " + "don't want to eat anymore due to long waiting");
                        limit = 0;
                        TimeUnit.SECONDS.sleep(5);
                    }
                } else {
                    rightFork.lock();
                    fork_right_taken = true;
                    dining();
                    rightFork.unlock();
                    leftFork.unlock();
                    fork_taken = false;
                    fork_right_taken = false;
                    limit = 0;
                    return;
                }
                return;
            }
        }
    }

    public void dining() throws InterruptedException {
        System.out.printf("%s starts eating", name);
        System.out.println();
        TimeUnit.SECONDS.sleep(random.nextInt(1, 10));
        System.out.printf("%s finishes eating and now thinking", name);
        System.out.println();
    }
}