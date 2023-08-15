package swingmix.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;

/**
 * Maintenance tip - There were some tricks to getting this code
 * working:
 *
 * 1. You have to overwrite addMouseListener() to do nothing
 * 2. You have to add a mouse event on mousePressed by calling
 * super.addMouseListener()
 * 3. You have to replace the UIActionMap for the keyboard event
 * "pressed" with your own one.
 * 4. You have to remove the UIActionMap for the keyboard event
 * "released".
 * 5. You have to grab focus when the next state is entered,
 * otherwise clicking on the component won't get the focus.
 * 6. You have to use a TristateModel as a button model that
 * uses the Armed property to implement the third state and
 * does state management.
 *
 * @author Dr. Heinz M. Kabutz from http://www.javaspecialists.co.za/archive/Issue082.html
 */
public class TristateCheckBox extends JCheckBox {

  public static enum State {
    NOT_SELECTED,
    SELECTED,
    DONT_CARE;

    public State next() {
      return switch (this) {
        case NOT_SELECTED -> SELECTED;
        case SELECTED -> DONT_CARE;
        case DONT_CARE -> NOT_SELECTED;
      };
    }
  }

  private final TristateModel model = new TristateModel();

  public TristateCheckBox(String text, Icon icon, State initial) {
    super(text, icon);

    // Add a listener for when the mouse is pressed
    super.addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        grabFocus();
        model.nextState();
      }
    });

    // Reset the keyboard action map
    ActionMap map = new ActionMapUIResource();
    map.put("pressed", new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        grabFocus();
        model.nextState();
      }
    });
    map.put("released", null);
    SwingUtilities.replaceUIActionMap(this, map);

    setModel(model);
    setState(initial);
  }

  public TristateCheckBox(String text, State initial) {
    this(text, null, initial);
  }

  public TristateCheckBox(String text) {
    this(text, State.DONT_CARE);
  }

  public TristateCheckBox() {
    this(null);
  }

  /** No one may add mouse listeners, not even Swing! */
  @Override
  public void addMouseListener(MouseListener l) {
  }

  /**
   * We have to prevent the focus listener to destoy our armed and pressed state.
   * So we override the focus handler and do a simple repaint on focus gain
   * and focus lost.
   *
   * This is not relavant in a JTreeView because we can not gain focus there.
   * So this trick became necessary in a standalone application.
   *
   * @param e
   */
  @Override
  protected void processFocusEvent(FocusEvent e) {
    repaint();
  }
  /**
   * Set the new state to either SELECTED, NOT_SELECTED or
   * DONT_CARE.  If state == null, it is treated as DONT_CARE.
   */
  public void setState(State state) {
    model.setState(state);
  }

  /** Return the current state, which is determined by the
   * selection status of the model. */
  public State getState() {
    return model.getState();
  }

  @Override
  public void setSelected(boolean b) {
    setState(b ? State.SELECTED : State.NOT_SELECTED);
  }

  private class TristateModel extends ToggleButtonModel {

    private void fireActionPerformed() {
      int modifiers = 0;
      AWTEvent currentEvent = EventQueue.getCurrentEvent();
      if (currentEvent instanceof InputEvent inputEvent) {
          modifiers = inputEvent.getModifiers();
      } else if (currentEvent instanceof ActionEvent actionEvent) {
          modifiers = actionEvent.getModifiers();
      }
      fireActionPerformed(
              new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                      getActionCommand(),
                      EventQueue.getMostRecentEventTime(),
                      modifiers));
    }

    private void setState(State state) {
      var oldState = getState();
      if (state == State.NOT_SELECTED) {
        super.setArmed(false);
        setPressed(false);
        setSelected(false);
      } else if (state == State.SELECTED) {
        super.setArmed(false);
        setPressed(false);
        setSelected(true);
      } else { // either "null" or DONT_CARE
        super.setArmed(true);
        setPressed(true);
        setSelected(true);
      }

      if (getState() != oldState)
        // changing armed and pressed state originally isn't an action
        // since we are using it to encode our additional state
        // we have to fire action performed events when changing it as well
        fireActionPerformed();
    }

    /**
     * The current state is embedded in the selection / armed
     * state of the model.
     *
     * We return the SELECTED state when the checkbox is selected
     * but not armed, DONT_CARE state when the checkbox is
     * selected and armed (grey) and NOT_SELECTED when the
     * checkbox is deselected.
     */
    private State getState() {
      if (isSelected() && !isArmed()) {
        // normal black tick
        return State.SELECTED;
      } else if (isSelected() && isArmed()) {
        // don't care grey tick
        return State.DONT_CARE;
      } else {
        // normal deselected
        return State.NOT_SELECTED;
      }
    }

    /** We rotate between NOT_SELECTED, SELECTED and DONT_CARE.*/
    private void nextState() {
      setState(getState().next());
    }

    /** Filter: No one may change the armed status except us. */
    @Override
    public void setArmed(boolean b) {
    }

    /** We disable focusing on the component when it is not
     * enabled. */
    @Override
    public void setEnabled(boolean b) {
      setFocusable(b);
      super.setEnabled(b);
    }

  }

}
