package org.apache.tika.onenote;

public class FileNodeListHeader {
  long uintMagic;
  long FileNodeListId;
  long nFragmentSequence;

  public FileNodeListHeader(long uintMagic, long fileNodeListId, long nFragmentSequence) {
    this.uintMagic = uintMagic;
    FileNodeListId = fileNodeListId;
    this.nFragmentSequence = nFragmentSequence;
  }
}
