package swingmix.ui;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 * created 2011-05-10
 * @author jan
 */
public class PreferencesBinding {
  
  private final Preferences preferences;
  private final Map<AbstractButton, String> buttonKeys = new HashMap<>();
  private final Map<AbstractButton, Boolean> buttonDefaults = new HashMap<>();
  
  private final Map<Document, String> textKeys = new HashMap<>();
  private final Map<Document, String> textDefaults = new HashMap<>();
  
  private final Map<SpinnerModel, String> serializableKeys = new HashMap<>();
  private final Map<SpinnerModel, Serializable> serializableDefaults = new HashMap<>();
  
  private boolean persistanceActive = false;
  
  private final ChangeListener booleanChangeListener = new ChangeListener() {
    @Override
    public void stateChanged(ChangeEvent e) {
      if (! persistanceActive)
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
      if (! persistanceActive)
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
      if (! persistanceActive)
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
    
    persistanceActive = true;
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
