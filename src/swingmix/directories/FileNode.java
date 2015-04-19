package swingmix.directories;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * created 2009-09-13
 * @author jan
 */
public class FileNode extends DefaultMutableTreeNode implements Comparable<FileNode> {

  private static final File NULL_FILE = new File("");

  private File file;

  public FileNode(File file) {
    super();
    this.file = file;
  }

  public static FileNode newTop() {
    final FileNode top = new FileNode(NULL_FILE);
    
    FileNode node;
    for (File root : File.listRoots()) {
      node = new FileNode(root);
      node.add(FileNode.newSubDirDummy());
      top.add(node);
    }
    return top;
  }

  public static FileNode newSubDirDummy() {
    return new FileNode(NULL_FILE) {

    @Override
    public String toString() {
      return "lade Daten ...";
    }
  };
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FileNode other = (FileNode) obj;
    return compareTo(other) == 0;
  }

  @Override
  public int hashCode() {
    try {
      return file.getCanonicalPath().hashCode();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public File getFile() {
    return file;
  }

  @Override
  public String toString() {
    return file.getName().length() > 0 ? file.getName() : file.getPath();
  }

  public boolean expand() {
    removeAllChildren();
    File[] subDirs = listSubDirs();
    if (subDirs == null) {
      return true;
    }
    SortedSet<FileNode> sortedFiles = new TreeSet<>();
    for (File f : subDirs) {
      sortedFiles.add(new FileNode(f));
    }
    for (FileNode node : sortedFiles) {
      add(node);
      if (node.hasSubDirs()) {
        node.add(FileNode.newSubDirDummy());
      }
    }
    return true;
  }

  public boolean hasSubDirs() {
    return listSubDirs() != null;
  }

  @Override
  public int compareTo(FileNode other) {
    try {
      return file.getCanonicalPath().compareTo(other.file.getCanonicalPath());
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  protected File[] listSubDirs() {
    try {
      return file.listFiles(File::isDirectory);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "Error reading directory " + file.getAbsolutePath(), DirectoriesTreePanel.class.getSimpleName(), JOptionPane.WARNING_MESSAGE);
      return null;
    }
  }
}
