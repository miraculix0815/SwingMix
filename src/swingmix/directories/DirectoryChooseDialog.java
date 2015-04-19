/*
 * DirectoryChooseDialog.java
 *
 * Created on 25.09.2009, 09:40:05
 */

package swingmix.directories;

import swingmix.ui.JChangeDialog;
import swingmix.directories.DirectoriesTreePanel.DirectoryChangedListener;
import java.io.File;

/**
 *
 * @author jan
 */
public class DirectoryChooseDialog extends JChangeDialog<DirectoryChooseDialog.FileContainer>
        implements DirectoryChangedListener {

  public static class FileContainer {
    public File file;
  }

  private File directory = null;

  /** Creates new form DirectoryChooseDialog */
  public DirectoryChooseDialog(java.awt.Frame parent) {
    super(parent);
    initComponents();
    directoriesTreePanel1.addListener(this);
  }

  @Override
  public void changed(Object source, File newDirectory) {
    directory = newDirectory;
  }

  @Override
  protected void assignToFields(DirectoryChooseDialog.FileContainer data) {
    // todo
  }

  @Override
  protected boolean collectFromFields(DirectoryChooseDialog.FileContainer data) {
    if (directory.exists()) {
      data.file = directory;
      return true;
    }
    return false;
  }

  @Override
  protected Class<DirectoryChooseDialog.FileContainer> getGenericClass() {
    return DirectoryChooseDialog.FileContainer.class;
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    directoriesTreePanel1 = new swingmix.directories.DirectoriesTreePanel();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jButton3 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    jButton1.setText("Abbrechen");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    jButton2.setText("OK");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });

    jButton3.setText("neuer Ordner ...");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(directoriesTreePanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jButton3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
            .addComponent(jButton2)
            .addGap(18, 18, 18)
            .addComponent(jButton1)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(directoriesTreePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButton1)
          .addComponent(jButton2)
          .addComponent(jButton3))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      performEnterAction(null);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
          DirectoryChooseDialog dialog = new DirectoryChooseDialog(new javax.swing.JFrame());
          dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
              System.exit(0);
            }
          });
          dialog.setVisible(true);
        });
    }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private swingmix.directories.DirectoriesTreePanel directoriesTreePanel1;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JButton jButton3;
  // End of variables declaration//GEN-END:variables

}