package swingmix.ui;

import swingmix.ui.TristateCheckBox.State;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 *
 * @author Santhosh Kumar from http://www.jroller.com/santhosh/entry/jtree_with_checkboxes
 */
public class CheckTreeCellRenderer extends JPanel implements TreeCellRenderer {

  private CheckTreeSelectionModel selectionModel;
  private TreeCellRenderer delegate;
  private TristateCheckBox checkBox = new TristateCheckBox();

  public CheckTreeCellRenderer(TreeCellRenderer delegate, CheckTreeSelectionModel selectionModel) {
    this.delegate = delegate;
    this.selectionModel = selectionModel;
    setLayout(new BorderLayout());
    setOpaque(false);
    checkBox.setOpaque(false);
  }

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    Component renderer = delegate.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

    TreePath path = tree.getPathForRow(row);
    if (path != null) {
      if (selectionModel.isPathSelected(path, true)) {
        checkBox.setState(State.SELECTED);
      } else {
        checkBox.setState(selectionModel.isPartiallySelected(path) ? State.DONT_CARE : State.NOT_SELECTED);
      }
    }
    removeAll();
    add(checkBox, BorderLayout.WEST);
    add(renderer, BorderLayout.CENTER);
    return this;
  }
}