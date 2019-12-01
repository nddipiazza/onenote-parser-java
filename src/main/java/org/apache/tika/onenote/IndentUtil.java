package org.apache.tika.onenote;

public class IndentUtil {
  public static String getIndent(int indentLevel) {
    String retval = "";
    for (int i = 0; i < indentLevel; ++i) {
      retval += "  ";
    }
    return retval;
  }
}
