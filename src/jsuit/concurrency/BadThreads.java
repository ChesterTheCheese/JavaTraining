package jsuit.concurrency;

public class BadThreads {

  static String message;

  private static class CorrectorThread
      extends Thread {

    @Override
    public void run() {
      try {
        sleep(1000);
      } catch (InterruptedException e) {}
      // Key statement 1:
      message = "Mares do eat oats.";
    }
  }

  public static void main(String args[])
      throws InterruptedException {

    CorrectorThread thread = new CorrectorThread();
    thread.start();
    message = "Mares do not eat oats.";
    Thread.sleep(2000);
    // Key statement 2:
    System.out.println(message);
  }
}
