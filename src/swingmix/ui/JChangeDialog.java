package swingmix.ui;

import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.logging.*;
import javax.swing.*;

/**
 *
 * @author jan
 */
public abstract class JChangeDialog<T> 
  extends JEscapeDialog {

  private boolean dataNew;
  private T data;

  public JChangeDialog(Window parent) {
    super(parent);
  }
  
  /**
   * While deriving from this you have to deliver the concrete class via this
   * method since java generics erase the type information on run time in 1.6.
   * 
   * @return A Class object of T
   */
  protected abstract Class<T> getGenericClass();
  
  /**
   * Shows the dialog and returns the values changed by the user as data.
   * @param title to decorate the dialog with
   * @return the the instance with assigned data or null when the user had
   * canceled
   */
  public T getNewData(String title) {
    setTitle(title);
    this.dataNew = false;
    try {
      this.data = supplyNewInstance();
    } catch (InstantiationException | IllegalAccessException ex) {
      Logger.getLogger(JChangeDialog.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
    assignToFields(data);
    setVisible(true);
    return this.dataNew ? this.data : null;
  }

  /**
   * Creates a new instance of T using the default contructor.
   * 
   * This method is to be overwritten to use an other way to create
   * instances of T.
   * 
   * @return a new Instance of T
   * @throws InstantiationException
   * @throws IllegalAccessException 
   */
  protected T supplyNewInstance() throws InstantiationException, IllegalAccessException {
    return getGenericClass().newInstance();
  }

  /**
   * Shows the dialog and enables the user to change the data.
   * Use the returned boolean to perform any data view updates.
   * @param data which will be changed by the user
   * @param title to decorate the dialog with
   * @return true if the user changed data, false otherwise
   */
  public boolean changeData(T data, String title) {
    setTitle(title);
    this.dataNew = false;
    this.data = data;
    assignToFields(data);
    setVisible(true);
    return this.dataNew;
  }
    
  @Override
  public void performEnterAction(KeyEvent e) {
    // don't perform in multi row element
    if ((e != null) && (e.getComponent() instanceof JTextArea)) return;
    if ((e != null) && (e.getComponent() instanceof JTable)) return;

    this.dataNew = collectFromFields(this.data);
    if (this.dataNew) setVisible(false);
  }

  protected T getData() {
    return data;
  }

  /**
   * Subclasses has to overwrite this method to implement the
   * assignment from data to the visual components
   * @param data to assign to the components
   */
  protected abstract void assignToFields(T data);
  
  /**
   * Subclasses has to overwrite this method to implement
   * the collection of values from visual components to
   * data.
   * <br>
   * Don't perform any changes on the
   * (original) data before one can be sure that ervery
   * changes will be successful.
   * 
   * @param data to set on from visual components
   * @return true if the values were assigned, false otherwise
   */
  protected abstract boolean collectFromFields(T data);
  
}
