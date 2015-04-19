package swingmix.ui;

import javax.swing.*;
import javax.swing.plaf.metal.*;

/**
 * created 2011-05-03
 * @author jan
 */
public abstract class NimbusPlatformFrame extends JFrame {
  public NimbusPlatformFrame() {
    tryToSetNimbus();
    setLocationByPlatform(true);
  }
  
  private static void tryToSetNimbus() {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          return;
        }
      }
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
    }
  }

}
