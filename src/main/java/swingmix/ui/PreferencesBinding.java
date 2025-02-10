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

import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.Map.Entry;
import java.util.*;
import java.util.function.Function;
import java.util.logging.*;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javax.swing.*;
import static javax.swing.JFileChooser.*;
import javax.swing.event.*;
import javax.swing.table.TableColumn;
import javax.swing.text.*;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.*;

/**
 * created 2011-05-10
 * @author Jan Schlößin
 */
public class PreferencesBinding {

  private final Preferences preferences;
  private final Map<AbstractButton, String> buttonKeys = new HashMap<>();
  private final Map<AbstractButton, Boolean> buttonDefaults = new HashMap<>();

  private final Map<Document, String> textKeys = new HashMap<>();
  private final Map<Document, String> textDefaults = new HashMap<>();

  private final Map<SpinnerModel, String> serializableKeys = new HashMap<>();
  private final Map<SpinnerModel, Serializable> serializableDefaults = new HashMap<>();

  private final Map<TableColumnModelExt, String> tableColumnModelKeys = new HashMap<>();
  private final Map<TableColumnExt, String> tableColumnKeys = new HashMap<>();

  private final Map<JFileChooser, String> fileChooserKeys = new HashMap<>();

  private boolean persistenceActive = false;

  private final ChangeListener booleanChangeListener = new ChangeListener() {
    @Override
    public void stateChanged(ChangeEvent e) {
      if (! persistenceActive)
        return;

      if (e.getSource() instanceof AbstractButton) {
        AbstractButton abstractButton = (AbstractButton) e.getSource();
        String persistenceKey = buttonKeys.get(abstractButton);
        if (persistenceKey != null)
          preferences.putBoolean(persistenceKey, ((AbstractButton) e.getSource()).isSelected());
      }
    }
  };
  private final DocumentListener textChangeListener = new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
      if (! persistenceActive)
        return;

      String persistenceKey = textKeys.get(e.getDocument());
      if (persistenceKey != null)
        try {
          preferences.put(persistenceKey, e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (BadLocationException ex) {
          throw new RuntimeException(ex);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

    @Override
    public void changedUpdate(DocumentEvent e) { insertUpdate(e);
    }
  };

  private final ChangeListener serializableChangeListener = new ChangeListener() {
    @Override
    public void stateChanged(ChangeEvent e) {
      if (! persistenceActive)
        return;

      if (e.getSource() instanceof SpinnerModel) {
        SpinnerModel model = (SpinnerModel) e.getSource();
        String persistenceKey = serializableKeys.get(model);
        if (persistenceKey != null) {
          preferences.putByteArray(persistenceKey, toByteArray(model.getValue()));
        }
      }
    }
  };

  private final TableColumnModelListener tableColumnModelListener = new TableColumnModelListener() {
    @Override
    public void columnAdded(TableColumnModelEvent e) {
    }

    @Override
    public void columnRemoved(TableColumnModelEvent e) {
    }

    @Override
    public void columnMoved(TableColumnModelEvent e) {
      if (! persistenceActive)
        return;

      if (e.getSource() instanceof TableColumnModelExt) {
        TableColumnModelExt model = (TableColumnModelExt) e.getSource();
        Map<TableColumnExt, Integer> visiblePosition = new HashMap<>();
        List<TableColumn> allColumns = model.getColumns(false);
        for (int pos = 0; pos < allColumns.size(); pos++) {
          visiblePosition.put(((TableColumnExt) allColumns.get(pos)), pos);
        }
        Map<Integer, TableColumnExt> modelIndexMap = visiblePosition.keySet().stream()
                .collect(Collectors.toMap(TableColumnExt::getModelIndex, Function.identity()));

        String persistenceKey = tableColumnModelKeys.get(model);
        if (persistenceKey != null) {
          modelIndexMap.entrySet().stream()
                  .forEach(entry -> {
                    int modelIndex = entry.getKey();
                    int viewIndex = visiblePosition.get(entry.getValue());
                    preferences.putInt(toColumnModelIndexAt(persistenceKey, modelIndex), viewIndex);
                  });
        }
      }

    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {
    }
  };

  private final PropertyChangeListener tableColumnPropertyChangeListener = new PropertyChangeListener() {

    @Override
    public void propertyChange(PropertyChangeEvent e) {
      if (! persistenceActive)
        return;

      if (e.getSource() instanceof TableColumnExt) {
        TableColumnExt column = (TableColumnExt) e.getSource();
        String persistenceKeyPrefix = tableColumnKeys.get(column);
        if ("width".equals(e.getPropertyName()) && Integer.class.isInstance(e.getNewValue())) {
          preferences.putInt(toColumnModelWidthAt(persistenceKeyPrefix, column.getModelIndex()), (int) e.getNewValue());
        }
        if ("visible".equals(e.getPropertyName()) && Boolean.class.isInstance(e.getNewValue())) {
          preferences.putBoolean(toColumnModelVisibleAt(persistenceKeyPrefix, column.getModelIndex()), (boolean) e.getNewValue());
        }
      }
    }
  };

  static String toColumnCountKey(String persistenceKeyPrefix) {
    return persistenceKeyPrefix + ".columnCount";
  }

  static String toColumnModelIndexAt(String persistenceKeyPrefix, int modelIndex) {
    return persistenceKeyPrefix + ".columnModelIndex" + modelIndex + ".AtViewPos";
  }

  static String toColumnModelWidthAt(String persistenceKeyPrefix, int modelIndex) {
    return persistenceKeyPrefix + ".columnModelIndex" + modelIndex + ".width";
  }

  static String toColumnModelVisibleAt(String persistenceKeyPrefix, int modelIndex) {
    return persistenceKeyPrefix + ".columnModelIndex" + modelIndex + ".visible";
  }

  private final ActionListener fileChooserActionListener = new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent e) {
      if (APPROVE_SELECTION.equals(e.getActionCommand())) {
        if (e.getSource() instanceof JFileChooser jFileChooser) {
          String key = fileChooserKeys.get(jFileChooser);
          if (key != null)
            preferences.put(key, jFileChooser.getSelectedFile().toString());
        }
      }
    }

  };

  public PreferencesBinding(String path) {
    preferences = Preferences.userRoot().node(path);
  }

  public void addBinding(AbstractButton button, String persistenceKey, boolean defaultValue) {
    buttonKeys.put(button, persistenceKey);
    buttonDefaults.put(button, defaultValue);
    button.addChangeListener(booleanChangeListener);
  }

  public void removeBinding(AbstractButton button) {
    buttonKeys.remove(button);
    buttonDefaults.remove(button);
    button.removeChangeListener(booleanChangeListener);
  }

  public void addBinding(JTextComponent text, String persistenceKey, String defaultValue) {
    textKeys.put(text.getDocument(), persistenceKey);
    textDefaults.put(text.getDocument(), defaultValue);
    text.getDocument().addDocumentListener(textChangeListener);
  }

  public void removeBinding(JTextComponent text) {
    textKeys.remove(text.getDocument());
    textDefaults.remove(text.getDocument());
    text.getDocument().removeDocumentListener(textChangeListener);
  }

  /**
   * the model has to have serializable objects
   */
  public void addBinding(JSpinner spinner, String persistenceKey, Serializable defaultValue) {
    serializableKeys.put(spinner.getModel(), persistenceKey);
    serializableDefaults.put(spinner.getModel(), defaultValue);
    spinner.getModel().addChangeListener(serializableChangeListener);
  }

  public void removeBinding(JSpinner spinner) {
    serializableKeys.remove(spinner.getModel());
    serializableDefaults.remove(spinner.getModel());
    spinner.getModel().removeChangeListener(serializableChangeListener);
  }

  public void addBinding(JXTable table, String persistenceKey) {
    TableColumnModelExt model = (TableColumnModelExt) table.getColumnModel();
    tableColumnModelKeys.put(model, persistenceKey);
    model.addColumnModelListener(tableColumnModelListener);
    for (int col = 0; col < model.getColumnCount(true); col++) {
      TableColumnExt column = (TableColumnExt) model.getColumns(true).get(col);
      column.addPropertyChangeListener(tableColumnPropertyChangeListener);
      tableColumnKeys.put(column, persistenceKey);
    }
    preferences.putInt(toColumnCountKey(persistenceKey), model.getColumnCount(true));
  }

  public void removeBinding(JXTable table) {
    tableColumnModelKeys.remove((TableColumnModelExt) table.getColumnModel());
    table.getColumnModel().removeColumnModelListener(tableColumnModelListener);
    for (int col = 0; col < table.getColumnModel().getColumnCount(); col++) {
      tableColumnKeys.remove(table.getColumnModel().getColumn(col));
      table.getColumnModel().getColumn(col).removePropertyChangeListener(tableColumnPropertyChangeListener);
    }
  }

  public void addBinding(JFileChooser fileChooser, String persistenceKey) {
    fileChooserKeys.put(fileChooser, persistenceKey);
    fileChooser.addActionListener(fileChooserActionListener);
  }

  public void removeBinding(JFileChooser fileChooser) {
    fileChooserKeys.remove(fileChooser);
    fileChooser.removeActionListener(fileChooserActionListener);
  }

  /**
   * activates persistance
   */
  public void restorePersistentValues() {
    for (Map.Entry<AbstractButton, String> binding : buttonKeys.entrySet()) {
      binding.getKey().setSelected(preferences.getBoolean(binding.getValue(), buttonDefaults.get(binding.getKey())));
    }

    for (Map.Entry<Document, String> binding : textKeys.entrySet()) {
      try {
        binding.getKey().insertString(0, preferences.get(binding.getValue(), textDefaults.get(binding.getKey())), null);
      } catch (BadLocationException ex) {
        ex.printStackTrace();
      }
    }

    for (Map.Entry<SpinnerModel, String> binding : serializableKeys.entrySet()) {
      byte [] serializedObject = preferences.getByteArray(binding.getValue(),
              toByteArray(serializableDefaults.get(binding.getKey())));
      binding.getKey().setValue(toObject(serializedObject));
    }

    for (Entry<TableColumnModelExt, String> binding : tableColumnModelKeys.entrySet()) {
      String keyPrefix = binding.getValue();
      TableColumnModelExt model = binding.getKey();
      for (TableColumn tc : model.getColumns(true)) {
        TableColumnExt tableColumn = (TableColumnExt) tc;
        tableColumn.setPreferredWidth(preferences.getInt(toColumnModelWidthAt(keyPrefix, tableColumn.getModelIndex()), tableColumn.getPreferredWidth()));
        tableColumn.setVisible(preferences.getBoolean(toColumnModelVisibleAt(keyPrefix, tableColumn.getModelIndex()), tableColumn.isVisible()));
      }

      int storedColumnCount = preferences.getInt(toColumnCountKey(keyPrefix), 0);
      SortedMap<Integer, Integer> modelToView = new TreeMap<>();
      for (int col = 0; col < storedColumnCount; col++) {
        if (! ((TableColumnExt) model.getColumns(true).get(col)).isVisible())
          continue;

        modelToView.put(col, preferences.getInt(toColumnModelIndexAt(keyPrefix, col), col));
      }
      int visibleModelColumn = 0;
      for (int modelColumn = 0; modelColumn < storedColumnCount; modelColumn++) {
        if (! ((TableColumnExt) model.getColumns(true).get(modelColumn)).isVisible())
          continue;

        int viewColumn = modelToView.getOrDefault(modelColumn, modelColumn);
        if (viewColumn < visibleModelColumn)
          model.moveColumn(visibleModelColumn, viewColumn);

        visibleModelColumn++;
      }
    }

    for (var binding : fileChooserKeys.entrySet()) {
      binding.getKey().setSelectedFile(new File(preferences.get(binding.getValue(), "")));
    }

    persistenceActive = true;
  }

  Preferences getPreferences() {
    return preferences;
  }

  static byte[] toByteArray(Object value) {
    try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
         ObjectOutputStream stream = new ObjectOutputStream(byteArray)) {
      stream.writeObject(value);
      stream.flush();
      return byteArray.toByteArray();
    } catch (IOException ex) {
      Logger.getLogger(PreferencesBinding.class.getName()).log(Level.WARNING, "not serializable", ex);
      return new byte[0];
    }
  }

  static Object toObject(byte[] array) {
    try (ByteArrayInputStream byteArray = new ByteArrayInputStream(array);
         ObjectInputStream stream = new ObjectInputStream(byteArray)) {
      return stream.readObject();
    } catch (IOException | ClassNotFoundException ex) {
      Logger.getLogger(PreferencesBinding.class.getName()).log(Level.WARNING, "not deserializable", ex);
      return null;
    }
  }

}
