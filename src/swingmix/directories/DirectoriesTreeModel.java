package swingmix.directories;

import javax.swing.tree.*;

/**
 * created 2009-09-13
 * @author jan
 */
public class DirectoriesTreeModel extends DefaultTreeModel {

  public DirectoriesTreeModel() {
    super(FileNode.newTop());
  }

}
