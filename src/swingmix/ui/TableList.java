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

import java.util.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

/**
 *
 * @param <E> Element class
 * @author jan
 */
public abstract class TableList<E> implements TableModel, List<E> {

  private List<E> l = new ArrayList<>();

  @SuppressWarnings("serial")
  private transient AbstractTableModel m = new AbstractTableModel() {

    @Override
    public int getRowCount() {
      return TableList.this.l.size();
    }

    @Override
    public int getColumnCount() {
      return TableList.this.m.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      return TableList.this.m.getValueAt(rowIndex, columnIndex);
    }
  };

  @Override
  public void addTableModelListener(TableModelListener l) {
    m.addTableModelListener(l);
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return m.isCellEditable(rowIndex, columnIndex);
  }

  @Override
  public void removeTableModelListener(TableModelListener l) {
    m.removeTableModelListener(l);
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    m.setValueAt(aValue, rowIndex, columnIndex);
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return l.toArray(a);
  }

  @Override
  public Object[] toArray() {
    return l.toArray();
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return l.subList(fromIndex, toIndex);
  }

  @Override
  public int size() {
    return l.size();
  }

  @Override
  public E set(int index, E element) {
    E oldElement = l.set(index, element);
    if (oldElement != element) {
      m.fireTableRowsUpdated(index, index);
    }
    return oldElement;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    boolean changed = l.retainAll(c);
    if (changed) {
      m.fireTableDataChanged();
    }
    return changed;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean changed = l.removeAll(c);
    if (changed) {
      m.fireTableDataChanged();
    }
    return changed;
  }

  @Override
  public E remove(int index) {
    E oldElement = l.remove(index);
    m.fireTableRowsDeleted(index, index);
    return oldElement;
  }

  @Override
  public boolean remove(Object o) {
    int index = indexOf(o);
    if (index >= 0) {
      remove(index);
    }
    return (index >= 0);
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    return l.listIterator(index);
  }

  @Override
  public ListIterator<E> listIterator() {
    return l.listIterator();
  }

  @Override
  public int lastIndexOf(Object o) {
    return l.lastIndexOf(o);
  }

  @Override
  public Iterator<E> iterator() {
    return l.iterator();
  }

  @Override
  public boolean isEmpty() {
    return l.isEmpty();
  }

  @Override
  public int indexOf(Object o) {
    return l.indexOf(o);
  }

  @Override
  public int hashCode() {
    return l.hashCode();
  }

  @Override
  public E get(int index) {
    return l.get(index);
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TableList<E> other = (TableList<E>) obj;
    if (this.l != other.l && (this.l == null || !this.l.equals(other.l))) {
      return false;
    }
    return true;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return l.containsAll(c);
  }

  @Override
  public boolean contains(Object o) {
    return l.contains(o);
  }

  @Override
  public void clear() {
    l.clear();
    m.fireTableDataChanged();
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    boolean changed = l.addAll(index, c);
    if (changed) {
      m.fireTableRowsInserted(index, index + c.size() - 1);
    }
    return changed;
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    int index = size();
    boolean changed = l.addAll(c);
    if (changed) {
      m.fireTableRowsInserted(index, index + c.size() - 1);
    }
    return changed;
  }

  @Override
  public void add(int index, E element) {
    l.add(index, element);
    m.fireTableRowsInserted(index, index);
  }

  @Override
  public boolean add(E e) {
    boolean inserted = l.add(e);
    if (inserted) {
      m.fireTableRowsInserted(l.size() - 1, l.size() - 1);
    }
    return inserted;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return m.getColumnClass(columnIndex);
  }

  @Override
  public int getRowCount() {
    return m.getRowCount();
  }

  public void update(int index) {
    m.fireTableRowsUpdated(index, index);
  }
  
}
