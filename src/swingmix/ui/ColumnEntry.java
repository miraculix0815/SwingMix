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

import java.util.function.Function;

/**
 * created 11.04.2020
 * @author Jan Schlößin
 */
public class ColumnEntry<T> {
  private final String name;
  private final Class<?> columnClass;
  private final Function<T, Object> valueExtractor;

  public ColumnEntry(String name, Class<?> columnClass, Function<T, Object> valueExtractor) {
    this.name = name;
    this.columnClass = columnClass;
    this.valueExtractor = valueExtractor;
  }

  public String getName() {
    return name;
  }

  public Class<?> getColumnClass() {
    return columnClass;
  }

  public Function<T, Object> getValueExtractor() {
    return valueExtractor;
  }

}
