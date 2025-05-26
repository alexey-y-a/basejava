package ru.javawebinar.basejava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private int counter;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());

        Thread thread0 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            try {
                throw new IllegalStateException("Пример исключения в thread0");
            } catch (IllegalStateException e) {
                System.err.println("Ошибка в thread0: " + e.getMessage());
            }
        });
        thread0.start();

        new Thread(() -> System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState())).start();

        System.out.println(thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("Итоговое значение counter: " + mainConcurrency.counter);

        System.out.println("\nДемонстрация deadlock:");
        DeadlockExample deadlockExample = new DeadlockExample();
        deadlockExample.demonstrateDeadlock();
    }

    private synchronized void inc() {
        counter++;
    }
}
