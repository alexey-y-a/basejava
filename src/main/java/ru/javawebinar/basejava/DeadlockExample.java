package ru.javawebinar.basejava;

public class DeadlockExample {
    private final Object lock1;
    private final Object lock2;

    public DeadlockExample() {
        this.lock1 = new Object();
        this.lock2 = new Object();
    }

    private void performAction(Object firstLock, Object secondLock, String threadName) {
        synchronized (firstLock) {
            System.out.println(threadName + ": Захватил " + firstLock + ", ждёт " + secondLock + "...");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (secondLock) {
                System.out.println(threadName + ": Захватил " + firstLock + " и " + secondLock);
            }
        }
    }

    public void demonstrateDeadlock() {
        Thread thread1 = new Thread(() -> performAction(lock1, lock2, "Поток 1"));
        Thread thread2 = new Thread(() -> performAction(lock2, lock1, "Поток 2"));

        thread1.start();
        thread2.start();
    }
}
