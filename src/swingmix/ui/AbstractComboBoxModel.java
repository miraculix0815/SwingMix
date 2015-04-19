package swingmix.ui;

import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author jan
 */
public abstract class AbstractComboBoxModel
  extends AbstractListModel
  implements ComboBoxModel {

  private Object selectedItem;

  /**
   * <code>AbstractComboBoxModel</code> subclasses must call this method
   * <b>after</b>
   * one or more elements of the list change.  The changed elements
   * are specified by the closed interval index0, index1 -- the endpoints
   * are included.  Note that
   * index0 need not be less than or equal to index1.
   * 
   * @param index0 one end of the new interval
   * @param index1 the other end of the new interval
   * @see EventListenerList
   * @see DefaultListModel
   * @see AbstractListModel
   */
  public void fireContentsChanged(int index0, int index1) {
    super.fireContentsChanged(this, index0, index1);
  }

  /**
   * <code>AbstractComboBoxModel</code> subclasses must call this method
   * <b>after</b>
   * one or more elements are added to the model.  The new elements
   * are specified by a closed interval index0, index1 -- the enpoints
   * are included.  Note that
   * index0 need not be less than or equal to index1.
   * 
   * @param index0 one end of the new interval
   * @param index1 the other end of the new interval
   * @see EventListenerList
   * @see DefaultListModel
   * @see AbstractListModel
   */
  public void fireIntervalAdded(int index0, int index1) {
    super.fireIntervalAdded(this, index0, index1);
  }

  /**
   * <code>AbstractComboBoxModel</code> subclasses must call this method
   * <b>after</b> one or more elements are removed from the model. 
   * <code>index0</code> and <code>index1</code> are the end points
   * of the interval that's been removed.  Note that <code>index0</code>
   * need not be less than or equal to <code>index1</code>.
   * 
   * @param index0 one end of the removed interval,
   *               including <code>index0</code>
   * @param index1 the other end of the removed interval,
   *               including <code>index1</code>
   * @see EventListenerList
   * @see DefaultListModel
   * @see AbstractListModel
   */
  public void fireIntervalRemoved(int index0, int index1) {
    super.fireIntervalRemoved(this, index0, index1);
  }

  @Override
  public Object getSelectedItem() {
    return this.selectedItem;
  }

  @Override
  public void setSelectedItem(Object anItem) {
    if ((selectedItem != null && !selectedItem.equals( anItem )) ||
      selectedItem == null && anItem != null) {
      selectedItem = anItem;
      fireContentsChanged(-1, -1);
    }
  }

}
