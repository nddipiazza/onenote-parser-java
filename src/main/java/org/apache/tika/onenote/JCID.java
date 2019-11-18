package org.apache.tika.onenote;

public class JCID {
  long index;
  boolean isBinary;
  boolean isPropertySet;
  boolean isGraphNode;
  boolean isFileData;
  boolean isReadOnly;

  public void loadFrom32BitIndex(long full_index) {
    index = full_index & 0xffff;
    isBinary = ((full_index >> 16) & 1) == 1;
    isPropertySet = ((full_index >> 17) & 1) == 1;
    isGraphNode = ((full_index >> 18) & 1) == 1;
    isFileData = ((full_index >> 19) & 1) == 1;
    isReadOnly = ((full_index >> 20) & 1) == 1;
    if ((full_index >> 21) != 0) {
      throw new RuntimeException("RESERVED_NONZERO");
    }
  }

  @Override
  public String toString() {
    return "JCID{" +
        "index=" + index +
        ", isBinary=" + isBinary +
        ", isPropertySet=" + isPropertySet +
        ", isGraphNode=" + isGraphNode +
        ", isFileData=" + isFileData +
        ", isReadOnly=" + isReadOnly +
        '}';
  }

  public long getIndex() {
    return index;
  }

  public JCID setIndex(long index) {
    this.index = index;
    return this;
  }

  public boolean isBinary() {
    return isBinary;
  }

  public JCID setBinary(boolean binary) {
    isBinary = binary;
    return this;
  }

  public boolean isPropertySet() {
    return isPropertySet;
  }

  public JCID setPropertySet(boolean propertySet) {
    isPropertySet = propertySet;
    return this;
  }

  public boolean isGraphNode() {
    return isGraphNode;
  }

  public JCID setGraphNode(boolean graphNode) {
    isGraphNode = graphNode;
    return this;
  }

  public boolean isFileData() {
    return isFileData;
  }

  public JCID setFileData(boolean fileData) {
    isFileData = fileData;
    return this;
  }

  public boolean isReadOnly() {
    return isReadOnly;
  }

  public JCID setReadOnly(boolean readOnly) {
    isReadOnly = readOnly;
    return this;
  }
}
