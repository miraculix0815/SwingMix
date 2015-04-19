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
