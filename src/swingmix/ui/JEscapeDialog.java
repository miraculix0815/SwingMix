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

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**
 * based on an article By Eugene Podgorbunskikh, JavaWorld.com, 03/01/99
 *
 * @author Jan Schlößin
 */
public class JEscapeDialog extends JDialog implements ContainerListener, KeyListener {

  public JEscapeDialog(Window owner, String title) {
    super(owner, title, ModalityType.APPLICATION_MODAL);
    setLocationByPlatform(true);
    addKeyAndContainerListenerRecursively(this);
  }

  public JEscapeDialog(Window parent) {
    this(parent, null);
  }

  //The following function is recursive and is intended for internal use only. It is private to prevent anyone calling it from other classes
  //The function takes a Component as an argument and adds this Dialog as a KeyListener to it.
  //Besides it checks if the component is actually a Container and if it is, there  are 2 additional things to be done to this Container :
  //1 - add this Dialog as a ContainerListener to the Container
  //2 - call this function recursively for every child of the Container.
  private void addKeyAndContainerListenerRecursively(Component c) {
    //To be on the safe side, try to remove KeyListener first just in case it has been added before.
    //If not, it won't do any harm
    c.removeKeyListener(this);
    //Add KeyListener to the Component passed as an argument
    if (!(c instanceof JTextArea))
      c.addKeyListener(this);

    if (c instanceof Container) {
      Container cont = (Container) c;

      //To be on the safe side, try to remove ContainerListener first just in case it has been added before.
      //If not, it won't do any harm
      cont.removeContainerListener(this);
      //Add ContainerListener to the Container.
      cont.addContainerListener(this);

      //Get the Container's array of children Components.
      Component[] children = cont.getComponents();

      //For every child repeat the above operation.
      for (Component child : children)
        addKeyAndContainerListenerRecursively(child);
    }
  }

  //The following function is the same as the function above with the exception that it does exactly the opposite - removes this Dialog
  //from the listener lists of Components.
  private void removeKeyAndContainerListenerRecursively(Component c) {
    c.removeKeyListener(this);

    if (c instanceof Container) {
      Container cont = (Container) c;
      cont.removeContainerListener(this);
      Component[] children = cont.getComponents();
      for (Component child : children)
        removeKeyAndContainerListenerRecursively(child);
    }
  }

  //This function is called whenever a Component or a Container is added to another Container belonging to this Dialog
  @Override
  public void componentAdded(ContainerEvent e) {
    addKeyAndContainerListenerRecursively(e.getChild());
  }

  //This function is called whenever a Component or a Container is removed from another Container belonging to this Dialog
  @Override
  public void componentRemoved(ContainerEvent e) {
    removeKeyAndContainerListenerRecursively(e.getChild());
  }

  //This function is called whenever a Component belonging to this Dialog (or the Dialog itself) gets the KEY_PRESSED event
  @Override
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    if (code == KeyEvent.VK_ESCAPE)
      performEscapeKeyAction();

    else if (code == KeyEvent.VK_ENTER) {
      if (! isATableIsInEditMode())
        performEnterAction(e);
    }
  }

  private boolean isATableIsInEditMode() {
    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    return getTablesInComponentHierarchy(focusOwner).stream()
            .anyMatch(JTable::isEditing);
  }

  private java.util.List<JTable> getTablesInComponentHierarchy(Component focusOwner) {
    var tables = new ArrayList<JTable>();

    Component inObservation = focusOwner;
    while (inObservation != null) {
      if (inObservation instanceof JTable)
        tables.add((JTable) inObservation);

      inObservation = inObservation.getParent();
    }

    return tables;
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  public void performEnterAction(KeyEvent e) {
    //Default response to ENTER key pressed goes here
    //Redefine this function in subclasses to respond to ENTER key differently
  }

  public void performEscapeKeyAction() {
    //Key pressed is the ESCAPE key. Hide this Dialog.
    setVisible(false);
  }

}
