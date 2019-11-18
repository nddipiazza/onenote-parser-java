package org.apache.tika.onenote;

public class FileDataStoreObject {
  GUID header;
  // uint64_t cbLength;implicit in the fileData FileChunkReference
  long reserved0;
  long reserved;
  FileChunkReference fileData = new FileChunkReference(); //points to raw data
  GUID footer; //MUST be {71FBA722-0F79-4A0B-BB13-899256426B24}.

  public GUID getHeader() {
    return header;
  }

  public FileDataStoreObject setHeader(GUID header) {
    this.header = header;
    return this;
  }

  public long getReserved0() {
    return reserved0;
  }

  public FileDataStoreObject setReserved0(long reserved0) {
    this.reserved0 = reserved0;
    return this;
  }

  public long getReserved() {
    return reserved;
  }

  public FileDataStoreObject setReserved(long reserved) {
    this.reserved = reserved;
    return this;
  }

  public FileChunkReference getFileData() {
    return fileData;
  }

  public FileDataStoreObject setFileData(FileChunkReference fileData) {
    this.fileData = fileData;
    return this;
  }

  public GUID getFooter() {
    return footer;
  }

  public FileDataStoreObject setFooter(GUID footer) {
    this.footer = footer;
    return this;
  }
}
