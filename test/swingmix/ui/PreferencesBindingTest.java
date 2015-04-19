package swingmix.ui;

import swingmix.ui.PreferencesBinding;
import java.util.UUID;
import java.util.prefs.*;
import javax.swing.*;
import org.junit.*;
import static org.junit.Assert.*;
import static swingmix.ui.PreferencesBinding.*;

/**
 * created 2011-05-10
 * @author jan
 */
public class PreferencesBindingTest {
  
  private static final UUID TEST_NODE = UUID.randomUUID();
  private static final PreferencesBinding BINDING = new PreferencesBinding(TEST_NODE.toString());
  
  @AfterClass
  public static void afterClass() throws BackingStoreException {
    Preferences.userRoot().node(TEST_NODE.toString()).removeNode();
  }
  
  @Test
  public void testCheckBoxRestoreFalse() {
    BINDING.getPreferences().putBoolean("checkBox", false);
    JCheckBox checkBox = new JCheckBox();
    checkBox.setSelected(true);
    
    BINDING.addBinding(checkBox, "checkBox", true);
    BINDING.restorePersistentValues();
    assertFalse(checkBox.isSelected());
    BINDING.removeBinding(checkBox);
  }
      
  @Test
  public void testCheckBoxRestoreTrue() {
    BINDING.getPreferences().putBoolean("checkBox", true);
    JCheckBox checkBox = new JCheckBox();
    checkBox.setSelected(false);
    
    BINDING.addBinding(checkBox, "checkBox", false);
    BINDING.restorePersistentValues();
    assertTrue(checkBox.isSelected());
    BINDING.removeBinding(checkBox);
  }

  @Test
  public void testCheckBoxStoreFalse() {
    BINDING.getPreferences().putBoolean("checkBox", true);
    JCheckBox checkBox = new JCheckBox();
    checkBox.setSelected(true);

    BINDING.addBinding(checkBox, "checkBox", true);
    checkBox.setSelected(false);
    assertFalse(BINDING.getPreferences().getBoolean("checkBox", true));
    BINDING.removeBinding(checkBox);
  }

  @Test
  public void testCheckBoxStoreTrue() {
    BINDING.getPreferences().putBoolean("checkBox", false);
    JCheckBox checkBox = new JCheckBox();
    checkBox.setSelected(false);

    BINDING.addBinding(checkBox, "checkBox", false);
    checkBox.setSelected(true);
    assertTrue(BINDING.getPreferences().getBoolean("checkBox", false));
    BINDING.removeBinding(checkBox);
  }
  
  @Test
  public void testTextFieldRestore() {
    BINDING.getPreferences().put("textField", "test text");
    JTextField textField = new JTextField();
    
    BINDING.addBinding(textField, "textField", "");
    BINDING.restorePersistentValues();
    assertEquals("test text", textField.getText());
    BINDING.removeBinding(textField);    
  }

  @Test
  public void testTextFieldStoreText() {
    BINDING.getPreferences().remove("textField");
    JTextField textField = new JTextField();
    
    BINDING.addBinding(textField, "textField", "");
    textField.setText("test text");
    assertEquals("test text", BINDING.getPreferences().get("textField", ""));
    BINDING.removeBinding(textField);
  }
  
  @Test
  public void testSpinnerRestoreInteger() {
    BINDING.getPreferences().putByteArray("integer spinner", toByteArray(1));
    JSpinner spinner =  new JSpinner();
    
    BINDING.addBinding(spinner, "integer spinner", 0);
    BINDING.restorePersistentValues();
    assertEquals(1, spinner.getValue());
    BINDING.removeBinding(spinner);    
  }

  @Test
  public void testSpinnerStoreInteger() {
    BINDING.getPreferences().remove("integer spinner");
    JSpinner spinner =  new JSpinner();
    
    BINDING.addBinding(spinner, "integer spinner", 0);
    spinner.setValue(1);
    assertEquals(1, 
            toObject(BINDING.getPreferences()
            .getByteArray("integer spinner", toByteArray(1))));
    BINDING.removeBinding(spinner);
  }
  
}
