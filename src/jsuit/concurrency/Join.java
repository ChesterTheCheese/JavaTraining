package jsuit.concurrency;

/**
 * Algorithm presenting {@link Thread#join} mechanism.
 * 
 * Two threads running for their life.
 * 
 * @author Kuba
 */
public class Join {

  public static void main(String[] args) {
    Thread joinee = new Joinee();
    Thread joiner = new Joiner(joinee);

    joinee.start();
    joiner.start();
  }

  static class Joiner extends Thread {
    Thread joinee;

    public Joiner(Thread joinee) {
      this.joinee = joinee;
    }

    @Override
    public void run() {

      for (int i = 0; i < 16; i++) {

        System.out.printf("[Join.Joiner::run] I'm running! %n");
        try {
          sleep(200);
        } catch (InterruptedException e) {}

        if (joinee.isAlive()) {
          if (Math.random() < 0.1) {
            System.out.printf("[Join.Joiner::run] I need some rest. %n");
            System.out.printf("[Join.Joiner::run] Waiting for joinee to complete his training! %n");

            try {
              joinee.join();
            } catch (InterruptedException e) {}
          }
        }
      }

      System.out.printf("[Join.Joiner::run] I've completed my training! %n");

    }

  }

  static class Joinee extends Thread {

    @Override
    public void run() {

      for (int i = 0; i < 16; i++) {
        System.out.printf("[Join.Joinee::run] \t\tI'm running! %n");
        try {
          sleep(200);
        } catch (InterruptedException e) {}
      }

      System.out.printf("[Join.Joinee::run] \t\tI've completed my training session! Can eat donuts now! %n");
    }
  }
}
