package jsuit.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

/**
 * Modified Safelock class from Oracle's Java Tutorials, concurrency trail
 * 
 * https://docs.oracle.com/javase/tutorial/essential/concurrency/newlocks.html
 */
public class Safelock {

  public static void main(String[] args) throws InterruptedException {
    final Friend alphonse = new Friend("Alphonse");
    final Friend gaston = new Friend("Gaston");
    Thread t1 = new Thread(new BowLoop("1", alphonse, gaston));
    Thread t2 = new Thread(new BowLoop("2", gaston, alphonse));
    t1.start();
    t2.start();

    System.out.printf("[Safelock::main] Going to sleep now. %n");
    Thread.sleep(5000);
    System.out.printf("[Safelock::main] I've woken up! %n");
    System.out.printf("[Safelock::main] Interrupting bow loops. %n");
    t1.interrupt();
    t2.interrupt();
  }

  static class Friend {
    private final String name;
    private final Lock lock = new ReentrantLock();

    public Friend(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }

    public boolean impendingBow(Friend bower) {
      Boolean myLock = false;
      Boolean yourLock = false;
      try {
        myLock = lock.tryLock();
        yourLock = bower.lock.tryLock();
      } finally {
        if (!(myLock && yourLock)) {
          if (myLock) {
            lock.unlock();
          }
          if (yourLock) {
            bower.lock.unlock();
          }
        }
      }
      return myLock && yourLock;
    }

    public void bow(Friend bower) {
      if (impendingBow(bower)) {
        System.out.format("%s: %s has"
            + " bowed to me!%n",
            this.name, bower.getName());
        bower.bowBack(this);
        lock.unlock();
        bower.lock.unlock();
      } else {
        System.out.format("%s: %s started"
            + " to bow to me, but saw that"
            + " I was already bowing to"
            + " him.%n",
            this.name, bower.getName());
      }
    }

    public void bowBack(Friend bower) {
      System.out.format("%s: %s has" +
          " bowed back to me!%n",
          this.name, bower.getName());
    }
  }

  static class BowLoop implements Runnable {
    private Friend bower;
    private Friend bowee;
    private String name;

    public BowLoop(String name, Friend bower, Friend bowee) {
      this.name = name;
      this.bower = bower;
      this.bowee = bowee;
    }

    @Override
    public void run() {

      Random random = new Random();

      boolean shouldReturn = false;
      for (;;) {
        try {
          Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
          shouldReturn = true;
        }

        bowee.bow(bower);

        if (shouldReturn) {
          System.out.printf("[Safelock.BowLoop::run] (%s) I detected I have been interrupted! %n", this.name);
          return;
        }
      }
    }
  }

}
