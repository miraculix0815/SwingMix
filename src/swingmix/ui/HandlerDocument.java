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
import java.io.*;
import java.util.logging.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 * created 2009-01-16
 * @author jan
 */
public class HandlerDocument extends StreamHandler implements StyledDocument {
  private StyledDocument document = new DefaultStyledDocument();

  private class DocumentStream extends OutputStream {
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    @Override
    public void write(int b) throws IOException {
      buffer.write(b);
    }

    @Override
    public void close() throws IOException {
      flush();
    }

    @Override
    public void flush() throws IOException {
      final String output = buffer.toString();
      buffer.reset();

      EventQueue.invokeLater(new Runnable() {

        @Override
        public void run() {
          try {
            insertString(getLength(), output, null);
          } catch (BadLocationException ex) {
            Logger.getLogger(HandlerDocument.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      });
    }

  }

  public HandlerDocument(Formatter formatter) {
    this();
    setFormatter(formatter);
  }

  public HandlerDocument() {
    setOutputStream(new DocumentStream());
    Logger.getLogger("").addHandler(this);
  }

  @Override
  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
    document.insertString(offs, str, a);
  }

  @Override
  public Element getDefaultRootElement() {
    return document.getDefaultRootElement();
  }

  @Override
  public void render(Runnable r) {
    document.render(r);
  }

  @Override
  public void removeUndoableEditListener(UndoableEditListener listener) {
    document.removeUndoableEditListener(listener);
  }

  @Override
  public void removeDocumentListener(DocumentListener listener) {
    document.removeDocumentListener(listener);
  }

  @Override
  public void remove(int offs, int len) throws BadLocationException {
    document.remove(offs, len);
  }

  @Override
  public final void putProperty(Object key, Object value) {
    document.putProperty(key, value);
  }

  @Override
  public void getText(int offset, int length, Segment txt) throws BadLocationException {
    document.getText(offset, length, txt);
  }

  @Override
  public String getText(int offset, int length) throws BadLocationException {
    return document.getText(offset, length);
  }

  @Override
  public final Position getStartPosition() {
    return document.getStartPosition();
  }

  @Override
  public Element[] getRootElements() {
    return document.getRootElements();
  }

  @Override
  public final Object getProperty(Object key) {
    return document.getProperty(key);
  }

  @Override
  public int getLength() {
    return document.getLength();
  }

  @Override
  public final Position getEndPosition() {
    return document.getEndPosition();
  }

  @Override
  public synchronized Position createPosition(int offs) throws BadLocationException {
    return document.createPosition(offs);
  }

  @Override
  public void addUndoableEditListener(UndoableEditListener listener) {
    document.addUndoableEditListener(listener);
  }

  @Override
  public void addDocumentListener(DocumentListener listener) {
    document.addDocumentListener(listener);
  }

  @Override
  public void setParagraphAttributes(int offset, int length, AttributeSet s, boolean replace) {
    document.setParagraphAttributes(offset, length, s, replace);
  }

  @Override
  public void setLogicalStyle(int pos, Style s) {
    document.setLogicalStyle(pos, s);
  }

  @Override
  public void setCharacterAttributes(int offset, int length, AttributeSet s, boolean replace) {
    document.setCharacterAttributes(offset, length, s, replace);
  }

  @Override
  public void removeStyle(String nm) {
    document.removeStyle(nm);
  }

  @Override
  public Style getStyle(String nm) {
    return document.getStyle(nm);
  }

  @Override
  public Element getParagraphElement(int pos) {
    return document.getParagraphElement(pos);
  }

  @Override
  public Style getLogicalStyle(int p) {
    return document.getLogicalStyle(p);
  }

  @Override
  public Color getForeground(AttributeSet attr) {
    return document.getForeground(attr);
  }

  @Override
  public Font getFont(AttributeSet attr) {
    return document.getFont(attr);
  }

  @Override
  public Element getCharacterElement(int pos) {
    return document.getCharacterElement(pos);
  }

  @Override
  public Color getBackground(AttributeSet attr) {
    return document.getBackground(attr);
  }

  @Override
  public Style addStyle(String nm, Style parent) {
    return document.addStyle(nm, parent);
  }

  @Override
  public void publish(LogRecord record) {
    super.publish(record);
    flush();
  }

}
