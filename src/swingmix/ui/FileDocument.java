package swingmix.ui;

import java.io.File;

/**
 * created 2011-05-03
 * @author jan
 */
public class FileDocument extends SimpleTextDocument {
  public File getFile() {
    return new File(getText());
  }

  public void setFile(File file) {
    setText(file.toString());
  }
}
