/**
 *  Copyright 2009-2015 Jan Schlößin
 * 
 *  This file is part of SwingMix.
 *
 *  SwingMix is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SwingMix is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with SwingMix.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Diese Datei ist Teil von SwingMix.
 *
 *  SwingMix ist Freie Software: Sie können es unter den Bedingungen
 *  der GNU Lesser General Public License, wie von der Free Software Foundation,
 *  Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 *  veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 *  SwingMix wird in der Hoffnung, dass es nützlich sein wird, aber
 *  OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 *  Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 *  Siehe die GNU Lesser General Public License für weitere Details.
 *
 *  Sie sollten eine Kopie der GNU Lesser General Public License zusammen mit diesem
 *  Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */

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
