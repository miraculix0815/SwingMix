/**
 *  Copyright 2009-2015 Jan Schlößin
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

import javax.swing.JDialog;

/**
 * based on an article By Eugene Podgorbunskikh, JavaWorld.com, 03/01/99
 *
 * @author jan
 */
public class JEscapeDialog extends JDialog implements ContainerListener, KeyListener
{

  public JEscapeDialog(Window owner, String title)
  {
       super(owner, title, ModalityType.APPLICATION_MODAL);
       setLocationByPlatform(true);
       addKeyAndContainerListenerRecursively(this);
  }

  public JEscapeDialog(Window parent)
  {
       this(parent, null);
  }

  //The following function is recursive and is intended for internal use only. It is private to prevent anyone calling it from other classes
//The function takes a Component as an argument and adds this Dialog as a KeyListener to it.
//Besides it checks if the component is actually a Container and if it is, there  are 2 additional things to be done to this Container :
//1 - add this Dialog as a ContainerListener to the Container
//2 - call this function recursively for every child of the Container.

  private void addKeyAndContainerListenerRecursively(Component c)
  {
//To be on the safe side, try to remove KeyListener first just in case it has been added before.
//If not, it won't do any harm
       c.removeKeyListener(this);
//Add KeyListener to the Component passed as an argument
       c.addKeyListener(this);

       if(c instanceof Container){

//Component c is a Container. The following cast is safe.
            Container cont = (Container)c;

//To be on the safe side, try to remove ContainerListener first just in case it has been added before.
//If not, it won't do any harm
            cont.removeContainerListener(this);
//Add ContainerListener to the Container.
            cont.addContainerListener(this);

//Get the Container's array of children Components.
            Component[] children = cont.getComponents();

//For every child repeat the above operation.
            for(int i = 0; i < children.length; i++){
                 addKeyAndContainerListenerRecursively(children[i]);
            }
       }
  }


//The following function is the same as the function above with the exception that it does exactly the opposite - removes this Dialog
//from the listener lists of Components.

 private void removeKeyAndContainerListenerRecursively(Component c)
 {
       c.removeKeyListener(this);

       if(c instanceof Container){

            Container cont = (Container)c;

            cont.removeContainerListener(this);

            Component[] children = cont.getComponents();

            for(int i = 0; i < children.length; i++){
                 removeKeyAndContainerListenerRecursively(children[i]);
            }
       }
  }


//ContainerListener interface
/**********************************************************/

//This function is called whenever a Component or a Container is added to another Container belonging to this Dialog
  public void componentAdded(ContainerEvent e)
  {
       addKeyAndContainerListenerRecursively(e.getChild());
  }

//This function is called whenever a Component or a Container is removed from another Container belonging to this Dialog
  public void componentRemoved(ContainerEvent e)
  {
       removeKeyAndContainerListenerRecursively(e.getChild());
  }

/**********************************************************/


//KeyListener interface
/**********************************************************/
//This function is called whenever a Component belonging to this Dialog (or the Dialog itself) gets the KEY_PRESSED event
  public void keyPressed(KeyEvent e)
  {
       int code = e.getKeyCode();
       if(code == KeyEvent.VK_ESCAPE){
//Key pressed is the ESCAPE key. Hide this Dialog.
            setVisible(false);
       }
       else if(code == KeyEvent.VK_ENTER){
//Key pressed is the ENTER key. Redefine performEnterAction() in subclasses to respond to depressing the ENTER key.
            performEnterAction(e);
       }

//Insert code to process other keys here
  }

//We need the following 2 functions to complete imlementation of KeyListener
  public void keyReleased(KeyEvent e)
  {
  }

  public void keyTyped(KeyEvent e)
  {
  }

/************************************************************/

  public void performEnterAction(KeyEvent e)
  {
//Default response to ENTER key pressed goes here
//Redefine this function in subclasses to respond to ENTER key differently
  }

}
