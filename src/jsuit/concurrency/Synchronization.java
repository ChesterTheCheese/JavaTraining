package jsuit.concurrency;

public class Synchronization {

  private static final int n = 50000;

  public static void main(String[] args) {

    Synchro s1 = new Synchro();
    Synchro s2 = new Synchro();
    Synchro s3 = new Synchro();

    Thread t1 = new Thread(() -> {
      for (int i = 0; i < n; i++) {
        s1.increment1();
      }
      System.out.printf("[Synchronization::main] End.%n");
    });
    t1.start();

    Thread t2 = new Thread(() -> {
      for (int i = 0; i < n; i++) {
        s1.increment1();
      }
      System.out.printf("[Synchronization::main] End.%n");
    });
    t2.start();

    new Thread(() -> {
      for (int i = 0; i < n; i++) {
        s2.lockAndIncrement1();
      }
      System.out.printf("[Synchronization::main] End.%n");
    }).start();

    new Thread(() -> {
      for (int i = 0; i < n; i++) {
        s2.lockAndIncrement1();
      }
      System.out.printf("[Synchronization::main] End.%n");
    }).start();

    new Thread(() -> {
      for (int i = 0; i < n; i++) {
        s3.synchronizedIncrement1();
      }
      System.out.printf("[Synchronization::main] End.%n");
    }).start();

    new Thread(() -> {
      for (int i = 0; i < n; i++) {
        s3.synchronizedIncrement1();
      }
      System.out.printf("[Synchronization::main] End.%n");
    }).start();

    sleep();
    System.out.printf("[Synchronization::main] Normal incrementation:       %s (expected: %s)%n", s1.c1, 2 * n);
    System.out.printf("[Synchronization::main] Locked incrementation:       %s (expected: %s)%n", s2.c1, 2 * n);
    System.out.printf("[Synchronization::main] Synchronized incrementation: %s (expected: %s)%n", s3.c1, 2 * n);

    Counter c = new Counter();
    Thread tc1 = new CounterThread(c);
    Thread tc2 = new CounterThread(c);

    tc1.start();
    tc2.start();

    sleep();
    System.out.printf("[Synchronization::main] Counter incrementation: %s %n", c.c);

  }

  private static void sleep() {
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}


class Synchro {
  long c1 = 0;
  long c2 = 0;
  Object lock1 = new Object();
  Object lock2 = new Object();

  void increment1() {
    c1++;
  }

  void decrement1() {
    c1--;
  }

  void lockAndIncrement1() {
    synchronized (lock1) {
      c1++;
    }
  }

  synchronized void synchronizedIncrement1() {
    c1++;
  }

  void increment2() {
    c2++;
  }

  void lockAndIncrement2() {
    synchronized (lock2) {
      c2++;
    }
  }

  synchronized void synchronizedIncrement2() {
    c2++;
  }

}


class Counter {
  int c = 0;

  void increment() {
    c++;
  }

  void decrement() {
    c--;
  }

}


class CounterThread extends Thread {

  Counter c;

  public CounterThread(Counter c) {
    this.c = c;
  }

  @Override
  public void run() {
    for (int i = 0; i < 1000; i++) {
      c.increment();
    }
    System.out.printf("[CounterThread::run] End.%n");
  }
}
