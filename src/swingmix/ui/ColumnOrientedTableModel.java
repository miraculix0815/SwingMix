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
