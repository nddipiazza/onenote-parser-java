package org.apache.tika.onenote;

public class CompactID {
  char n;
  long guidIndex; //only occupies 24 bits
  ExtendedGUID guid;

  public char getN() {
    return n;
  }

  public CompactID setN(char n) {
    this.n = n;
    return this;
  }

  public long getGuidIndex() {
    return guidIndex;
  }

  public CompactID setGuidIndex(long guidIndex) {
    this.guidIndex = guidIndex;
    return this;
  }

  public ExtendedGUID getGuid() {
    return guid;
  }

  public CompactID setGuid(ExtendedGUID guid) {
    this.guid = guid;
    return this;
  }
}
