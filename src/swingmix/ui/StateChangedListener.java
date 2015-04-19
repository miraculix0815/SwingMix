
package swingmix.ui;

import javax.swing.event.*;

/**
 * created 17.04.2015
 * @author jan
 */
public class StateChangedListener implements DocumentListener, ChangeListener {
  private final Runnable job;

  public StateChangedListener(Runnable job) {
    this.job = job;
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    job.run();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    job.run();
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    // is to be ignored
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    job.run();
  }

}
