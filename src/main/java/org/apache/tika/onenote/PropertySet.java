package org.apache.tika.onenote;

import java.util.List;
import java.util.Objects;

public class PropertySet {
  List<PropertyValue> rgPridsData;

  public void print(OneNote document) {
    for (PropertyValue child : rgPridsData) {
      child.print(document);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PropertySet that = (PropertySet) o;
    return Objects.equals(rgPridsData, that.rgPridsData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rgPridsData);
  }

  public List<PropertyValue> getRgPridsData() {
    return rgPridsData;
  }

  public PropertySet setRgPridsData(List<PropertyValue> rgPridsData) {
    this.rgPridsData = rgPridsData;
    return this;
  }
}
