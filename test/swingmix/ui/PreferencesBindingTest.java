package swingmix.ui;

import java.util.UUID;
import java.util.prefs.*;
import javax.swing.*;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;
import static org.junit.Assert.*;
import org.junit.*;
import static swingmix.ui.PreferencesBinding.*;

/**
 * created 2011-05-10
 * @author jan
 */
public class PreferencesBindingTest {
  
  private final UUID TEST_NODE = UUID.randomUUID();
  private PreferencesBinding binding;
  
  @Before
  public void before() throws BackingStoreException {
    binding = new PreferencesBinding(TEST_NODE.toString());
  }
  
  @After
  public void after() throws BackingStoreException {
    Preferences.userRoot().node(TEST_NODE.toString()).removeNode();
  }
  
  @Test
  public void testCheckBoxRestoreFalse() {
    binding.getPreferences().putBoolean("checkBox", false);
    JCheckBox checkBox = new JCheckBox();
    checkBox.setSelected(true);
    
    binding.addBinding(checkBox, "checkBox", true);
    binding.restorePersistentValues();
    assertFalse(checkBox.isSelected());
    binding.removeBinding(checkBox);
  }
      
  @Test
  public void testCheckBoxRestoreTrue() {
    binding.getPreferences().putBoolean("checkBox", true);
    JCheckBox checkBox = new JCheckBox();
    checkBox.setSelected(false);
    
    binding.addBinding(checkBox, "checkBox", false);
    binding.restorePersistentValues();
    assertTrue(checkBox.isSelected());
    binding.removeBinding(checkBox);
  }

  @Test
  public void testCheckBoxStoreFalse() {
    binding.getPreferences().putBoolean("checkBox", true);
    JCheckBox checkBox = new JCheckBox();
    checkBox.setSelected(true);

    binding.addBinding(checkBox, "checkBox", true);
    binding.restorePersistentValues();
    checkBox.setSelected(false);
    assertFalse(binding.getPreferences().getBoolean("checkBox", true));
    binding.removeBinding(checkBox);
  }

  @Test
  public void testCheckBoxStoreTrue() {
    binding.getPreferences().putBoolean("checkBox", false);
    JCheckBox checkBox = new JCheckBox();
    checkBox.setSelected(false);

    binding.addBinding(checkBox, "checkBox", false);
    binding.restorePersistentValues();
    checkBox.setSelected(true);
    assertTrue(binding.getPreferences().getBoolean("checkBox", false));
    binding.removeBinding(checkBox);
  }
  
  @Test
  public void testTextFieldRestore() {
    binding.getPreferences().put("textField", "test text");
    JTextField textField = new JTextField();
    
    binding.addBinding(textField, "textField", "");
    binding.restorePersistentValues();
    assertEquals("test text", textField.getText());
    binding.removeBinding(textField);    
  }

  @Test
  public void testTextFieldStoreText() {
    JTextField textField = new JTextField();
    
    binding.addBinding(textField, "textField", "");
    binding.restorePersistentValues();
    textField.setText("test text");
    assertEquals("test text", binding.getPreferences().get("textField", ""));
    binding.removeBinding(textField);
  }
  
  @Test
  public void testSpinnerRestoreInteger() {
    binding.getPreferences().putByteArray("integer spinner", toByteArray(1));
    JSpinner spinner =  new JSpinner();
    
    binding.addBinding(spinner, "integer spinner", 0);
    binding.restorePersistentValues();
    assertEquals(1, spinner.getValue());
    binding.removeBinding(spinner);    
  }

  @Test
  public void testSpinnerStoreInteger() {
    JSpinner spinner = new JSpinner();
    
    binding.addBinding(spinner, "integer spinner", 0);
    spinner.setValue(1);
    assertEquals(1, 
            toObject(binding.getPreferences()
            .getByteArray("integer spinner", toByteArray(1))));
    binding.removeBinding(spinner);
  }
  
  @Test
  public void testTableModelMoveColumn() {
    String prefix = "test table";
    binding.getPreferences().remove(prefix);
    JXTable table = new JXTable(new String[][]{}, new String[] { "a", "b", "c" });
    binding.addBinding(table, prefix);
    binding.restorePersistentValues();
    
    assertEquals(3, binding.getPreferences().getInt(PreferencesBinding.toColumnCountKey(prefix), -1));
    assertEquals(-1, binding.getPreferences().getInt(PreferencesBinding.toColumnModelIndexAt(prefix, 0), -1));
    assertEquals(-1, binding.getPreferences().getInt(PreferencesBinding.toColumnModelIndexAt(prefix, 1), -1));
    table.getColumnModel().moveColumn(0, 1);
    assertEquals(1, binding.getPreferences().getInt(PreferencesBinding.toColumnModelIndexAt(prefix, 0), -1));
    assertEquals(0, binding.getPreferences().getInt(PreferencesBinding.toColumnModelIndexAt(prefix, 1), -1));
    assertEquals(2, binding.getPreferences().getInt(PreferencesBinding.toColumnModelIndexAt(prefix, 2), -1));
    binding.removeBinding(table);
    
    table = new JXTable(new String[][]{}, new String[] { "a", "b", "c" });
    binding.addBinding(table, prefix);
    assertEquals(0, table.getColumnModel().getColumn(0).getModelIndex());
    assertEquals(1, table.getColumnModel().getColumn(1).getModelIndex());
    binding.restorePersistentValues();
    assertEquals(1, table.getColumnModel().getColumn(0).getModelIndex());
    assertEquals(0, table.getColumnModel().getColumn(1).getModelIndex());
    binding.removeBinding(table);
  }
  
  @Test
  public void testTableModelColumnWidthChange() {
    String prefix = "test table";
    JXTable table = new JXTable(new String[][]{}, new String[] { "a", "b", "c" });
    binding.addBinding(table, prefix);
    binding.restorePersistentValues();

    assertEquals(-1, binding.getPreferences().getInt(PreferencesBinding.toColumnModelWidthAt(prefix, 0), -1));
    assertEquals(-1, binding.getPreferences().getInt(PreferencesBinding.toColumnModelWidthAt(prefix, 1), -1));
    table.getColumnModel().getColumn(0).setWidth(100);
    table.getColumnModel().getColumn(1).setWidth(200);
    assertEquals(100, binding.getPreferences().getInt(PreferencesBinding.toColumnModelWidthAt(prefix, 0), -1));
    assertEquals(200, binding.getPreferences().getInt(PreferencesBinding.toColumnModelWidthAt(prefix, 1), -1));
    assertEquals(-1, binding.getPreferences().getInt(PreferencesBinding.toColumnModelWidthAt(prefix, 2), -1));
    binding.removeBinding(table);

    table = new JXTable(new String[][]{}, new String[] { "a", "b", "c" });
    binding.addBinding(table, prefix);
    assertEquals(75, table.getColumnModel().getColumn(0).getPreferredWidth());
    assertEquals(75, table.getColumnModel().getColumn(1).getPreferredWidth());
    assertEquals(75, table.getColumnModel().getColumn(2).getPreferredWidth());
    binding.restorePersistentValues();
    assertEquals(100, table.getColumnModel().getColumn(0).getPreferredWidth());
    assertEquals(200, table.getColumnModel().getColumn(1).getPreferredWidth());
    assertEquals(75, table.getColumnModel().getColumn(2).getPreferredWidth());
    binding.removeBinding(table);
  }
  
  @Test
  public void testTableModelColumnNotVisible() {
    String prefix = "test table";
    JXTable table = new JXTable(new String[][]{}, new String[] { "a", "b", "c" });
    binding.addBinding(table, prefix);
    binding.restorePersistentValues();

    assertEquals(true, binding.getPreferences().getBoolean(PreferencesBinding.toColumnModelVisibleAt(prefix, 0), true));
    assertEquals(true, binding.getPreferences().getBoolean(PreferencesBinding.toColumnModelVisibleAt(prefix, 1), true));
    table.getColumnExt(1).setVisible(false);
    assertEquals(true, binding.getPreferences().getBoolean(PreferencesBinding.toColumnModelVisibleAt(prefix, 0), true));
    assertEquals(false, binding.getPreferences().getBoolean(PreferencesBinding.toColumnModelVisibleAt(prefix, 1), true));
    assertEquals(true, binding.getPreferences().getBoolean(PreferencesBinding.toColumnModelVisibleAt(prefix, 2), true));
    binding.removeBinding(table);

    table = new JXTable(new String[][]{}, new String[] { "a", "b", "c" });
    binding.addBinding(table, prefix);
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(0)).isVisible());
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(1)).isVisible());
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(2)).isVisible());
    binding.restorePersistentValues();
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(0)).isVisible());
    assertEquals(false, ((TableColumnExt) table.getColumns(true).get(1)).isVisible());
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(2)).isVisible());
    binding.removeBinding(table);
  }
  
  @Test
  public void testTableModelColumsHiddenAndMoved() {
    String prefix = "test table";
    JXTable table = new JXTable(new String[][]{}, new String[] { "a", "b", "c" });
    binding.addBinding(table, prefix);
    binding.restorePersistentValues();

    table.getColumnExt(1).setVisible(false);
    table.moveColumn(1, 0);
    binding.removeBinding(table);

    table = new JXTable(new String[][]{}, new String[] { "a", "b", "c" });
    binding.addBinding(table, prefix);
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(0)).isVisible());
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(1)).isVisible());
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(2)).isVisible());
    binding.restorePersistentValues();
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(0)).isVisible());
    assertEquals(2, table.getColumnExt(0).getModelIndex());
    assertEquals(false, ((TableColumnExt) table.getColumns(true).get(1)).isVisible());
    assertEquals(true, ((TableColumnExt) table.getColumns(true).get(2)).isVisible());
    assertEquals(0, table.getColumnExt(1).getModelIndex());
    binding.removeBinding(table);
  }
  
  @Test
  public void testTableModelBindToHiddenColumns() {
    String prefix = "test table";
    JXTable table = new JXTable(new String[][]{}, new String[] { "a", "b", "c" });
    ((TableColumnExt) table.getColumns(true).get(1)).setVisible(false);
    
    binding.addBinding(table, prefix);
    binding.restorePersistentValues();
    ((TableColumnExt) table.getColumns(true).get(1)).setVisible(true);
    assertEquals(true, binding.getPreferences().getBoolean(PreferencesBinding.toColumnModelVisibleAt(prefix, 1), false));
    binding.removeBinding(table);
  }
  
}
