package org.apache.tika.onenote;

import java.util.Objects;

public class FileChunkReference {

  long stp;
  long cb;

  public FileChunkReference() {

  }

  public FileChunkReference(long stp, long cb) {
    this.stp = stp;
    this.cb = cb;
  }

  public static FileChunkReference NIL = new FileChunkReference(-1L, 0L);

  @Override
  public String toString() {
    return "FileChunkReference{" +
      "stp=" + stp +
      ", cb=" + cb +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileChunkReference that = (FileChunkReference) o;
    return stp == that.stp &&
      cb == that.cb;
  }

  @Override
  public int hashCode() {
    return Objects.hash(stp, cb);
  }

  public long getStp() {
    return stp;
  }

  public FileChunkReference setStp(long stp) {
    this.stp = stp;
    return this;
  }

  public long getCb() {
    return cb;
  }

  public FileChunkReference setCb(long cb) {
    this.cb = cb;
    return this;
  }
}
