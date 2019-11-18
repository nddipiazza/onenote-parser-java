package org.apache.tika.onenote;

import java.util.Objects;

public class PropertyID {
  long id;
  long type;
  boolean inline_bool;

  @Override
  public String toString() {
    StringBuilder retval = new StringBuilder();
    retval.append("id: ")
        .append(Properties.nameOf(id))
        .append(" (0x")
        .append(Long.toHexString(id))
        .append(") type 0x")
        .append(Long.toHexString(type))
        .append(" -- bit ")
        .append(inline_bool ? 1 : 0);
    return retval.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PropertyID that = (PropertyID) o;
    return id == that.id &&
        type == that.type &&
        inline_bool == that.inline_bool;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, inline_bool);
  }

  public long getId() {
    return id;
  }

  public PropertyID setId(long id) {
    this.id = id;
    return this;
  }

  public long getType() {
    return type;
  }

  public PropertyID setType(long type) {
    this.type = type;
    return this;
  }

  public boolean isInline_bool() {
    return inline_bool;
  }

  public PropertyID setInline_bool(boolean inline_bool) {
    this.inline_bool = inline_bool;
    return this;
  }
}
