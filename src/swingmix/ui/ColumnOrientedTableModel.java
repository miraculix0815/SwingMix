/**
 *  Copyright 2009-2020 Jan Schlößin
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

import java.util.*;
import java.util.function.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 * created 11.04.2020
 * @author Jan Schlößin
 */
public abstract class ColumnOrientedTableModel<T> extends AbstractTableModel {

  private class ListChangeListener implements ListDataListener {

    private ListChangeListener() {
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
      fireTableRowsInserted(e.getIndex0(), e.getIndex1());
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
      fireTableRowsDeleted(e.getIndex0(), e.getIndex1());
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
      fireTableRowsUpdated(e.getIndex0(), e.getIndex1());
    }

  }

  private final ListModel<T> rows;
  private final List<ColumnEntry<T>> columnList = new ArrayList<>();

  public ColumnOrientedTableModel(ListModel<T> rows) {
    this.rows = rows;
    rows.addListDataListener(new ListChangeListener());
  }

  @Override
  public int getRowCount() {
    return rows.getSize();
  }

  @Override
  public int getColumnCount() {
    return columnList.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    T rowElement = rows.getElementAt(rowIndex);
    Function<T, Object> valueExtractor = columnList.get(columnIndex).getValueExtractor();
    return valueExtractor.apply(rowElement);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return columnList.get(columnIndex).getColumnClass();
  }

  @Override
  public String getColumnName(int column) {
    return columnList.get(column).getName();
  }

  protected boolean addColumn(ColumnEntry<T> e) {
    return columnList.add(e);
  }

}
