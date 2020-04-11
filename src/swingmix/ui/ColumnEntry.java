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
