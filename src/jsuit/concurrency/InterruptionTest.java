package jsuit.concurrency;

/**
 * Created thread are interrupted. Internally they handle interruption
 * differently (through InterruptedException or Thread.interrupted method)
 */
public class InterruptionTest {

  public static void main(String[] args) throws InterruptedException {

    System.out.printf("[InterruptionTest::main] Starting.%n");

    CalculationHeavyTRex calcThread = new CalculationHeavyTRex();
    SleepingTRex sleepingThread = new SleepingTRex();

    calcThread.start();
    sleepingThread.start();

    Thread.sleep(200);

    System.out.println("[InterruptionTest::main] Interrupting calculation thread.");
    calcThread.interrupt();

    Thread.sleep(200);

    System.out.println("[InterruptionTest::main] Interrupting sleeping thread.");
    sleepingThread.interrupt();

    System.out.printf("[InterruptionTest::main] I am complete.%n");
  }
}


class CalculationHeavyTRex extends Thread {

  @Override
  public synchronized void start() {
    System.out.println("[CalculationHeavyTRex::start]");
    super.start();
  }

  @Override
  public void run() {

    for (int i = 0; i < 50; i++) {

      // interruption falls to one of the loops
      for (int ii = 0; ii < 500_000; ii++) {
        Math.sin(ii);
      }
      System.out.printf("[CalculationHeavyTRex::run] %s-th run. (%s)%n", i, isInterrupted());

      for (int ii = 0; ii < 500_000; ii++) {
        Math.sin(ii);
      }
      System.out.printf("[CalculationHeavyTRex::run] %s-th run. (%s)%n", i, isInterrupted());

      if (Thread.interrupted()) {
        System.out.printf("[CalculationHeavyTRex::run] Interrupted!%n");
        return;
      }
    }

  }

}


class SleepingTRex extends Thread {

  @Override
  public synchronized void start() {
    System.out.println("[SleepingTRex::start]");
    super.start();
  }

  @Override
  public void run() {

    try {
      for (int i = 0; i < 10; i++) {
        System.out.printf("[SleepingTRex::run] %s-th run.%n", i);
        sleep(100);
      }
    } catch (InterruptedException e) {
      System.out.printf("[SleepingTRex::run] Interrupted!%n");
      return;
    }

  }

}
