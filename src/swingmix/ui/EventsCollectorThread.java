package swingmix.ui;

import javax.swing.SwingUtilities;

/**
 * This class purpose is to collect events in a row and fire a task
 * at most once in 200 ms. This is a nice behaviour for keybord events
 * when you want a task to be executed when the user finished writing.
 * 
 * created 18.04.2015
 * @author jan
 */
public class EventsCollectorThread extends Thread {
  public static EventsCollectorThread create(Runnable event) {
    EventsCollectorThread thread = new EventsCollectorThread(event);
    thread.start();
    return thread;
  }
  
  private long nextTimeToExecute = Long.MAX_VALUE;
  private final Runnable job;

  private EventsCollectorThread(Runnable job) {
    super("events collector thread");
    setDaemon(true);
    setPriority(MIN_PRIORITY);
    this.job = job;
  }

  public void triggerFilterExecution() {
    this.nextTimeToExecute = System.currentTimeMillis() + 200;
    interrupt();
  }

  @Override
  public void run() {
    while (true)
      try {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= nextTimeToExecute) {
          SwingUtilities.invokeLater(job);
          synchronized (this) {
            wait();
          }
        } else {
          sleep(nextTimeToExecute - currentTime);
        }
      } catch (InterruptedException ex) {
      }
  }

}
