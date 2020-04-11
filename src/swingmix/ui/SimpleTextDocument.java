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

import java.util.*;
import javax.swing.event.*;
import javax.swing.event.DocumentEvent.ElementChange;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.*;

/**
 * created 2011-05-03
 * @author Jan Schlößin
 */
public class SimpleTextDocument implements Document {
  private PlainDocument doc = new PlainDocument();
  private Set<DocumentListener> myListeners = new HashSet<>();
  private DocumentListener docListener = new DocumentListener() {
    DocumentEvent withDocument(final DocumentEvent e, final Document document) {
      return new DocumentEvent() {

        @Override
        public int getOffset() {
          return e.getOffset();
        }

        @Override
        public int getLength() {
          return e.getLength();
        }

        @Override
        public Document getDocument() {
          return document;
        }

        @Override
        public EventType getType() {
          return e.getType();
        }

        @Override
        public ElementChange getChange(Element elem) {
          return e.getChange(elem);
        }
      };
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      for (DocumentListener listener : myListeners)
        listener.insertUpdate(withDocument(e, SimpleTextDocument.this));
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      for (DocumentListener listener : myListeners)
        listener.removeUpdate(withDocument(e, SimpleTextDocument.this));
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      for (DocumentListener listener : myListeners)
        listener.changedUpdate(withDocument(e, SimpleTextDocument.this));
    }
  };

  public SimpleTextDocument() {
    doc.addDocumentListener(docListener);
  }

  public String getText() {
    try {
      return getText(0, getLength());
    } catch (BadLocationException ex) {
      throw new RuntimeException(ex);
    }
  }

  public void setText(String text) {
    if (text == null)
      text = "";

    int minLen = Math.min(text.length(), getLength());
    try {
      doc.replace(0, minLen, text, null);
      if (minLen < getLength()) {
        remove(minLen, getLength() - minLen);
      }
      if (minLen < text.length()) {
        insertString(minLen, text.substring(minLen), null);
      }
    } catch (BadLocationException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void addDocumentListener(DocumentListener listener) {
    myListeners.add(listener);
  }

  @Override
  public void addUndoableEditListener(UndoableEditListener listener) {
    doc.addUndoableEditListener(listener);
  }

  @Override
  public synchronized Position createPosition(int offs) throws BadLocationException {
    return doc.createPosition(offs);
  }

  @Override
  public Element getDefaultRootElement() {
    return doc.getDefaultRootElement();
  }

  @Override
  public final Position getEndPosition() {
    return doc.getEndPosition();
  }

  @Override
  public int getLength() {
    return doc.getLength();
  }

  @Override
  public final Object getProperty(Object key) {
    return doc.getProperty(key);
  }

  @Override
  public Element[] getRootElements() {
    return doc.getRootElements();
  }

  @Override
  public final Position getStartPosition() {
    return doc.getStartPosition();
  }

  @Override
  public void getText(int offset, int length, Segment txt) throws BadLocationException {
    doc.getText(offset, length, txt);
  }

  @Override
  public String getText(int offset, int length) throws BadLocationException {
    return doc.getText(offset, length);
  }

  @Override
  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
    doc.insertString(offs, str, a);
  }

  @Override
  public final void putProperty(Object key, Object value) {
    doc.putProperty(key, value);
  }

  @Override
  public void remove(int offs, int len) throws BadLocationException {
    doc.remove(offs, len);
  }

  @Override
  public void removeDocumentListener(DocumentListener listener) {
    myListeners.remove(listener);
  }

  @Override
  public void removeUndoableEditListener(UndoableEditListener listener) {
    doc.removeUndoableEditListener(listener);
  }

  @Override
  public void render(Runnable r) {
    doc.render(r);
  }

}
