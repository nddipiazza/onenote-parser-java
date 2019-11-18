package org.apache.tika.onenote;

import java.util.Arrays;
import java.util.List;

public class GUID implements Comparable<GUID> {
  int[] guid;

  @Override
  public int compareTo(GUID o) {
    return memcmp(guid, o.guid, 16);
  }

  public GUID(int[] guid) {
    this.guid = guid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GUID guid1 = (GUID) o;
    return Arrays.equals(guid, guid1.guid);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(guid);
  }

  public static int memcmp(int b1[], int b2[], int sz){
    for(int i = 0; i < sz; i++){
      if(b1[i] != b2[i]){
        if((b1[i] >= 0 && b2[i] >= 0)||(b1[i] < 0 && b2[i] < 0))
          return b1[i] - b2[i];
        if(b1[i] < 0 && b2[i] >= 0)
          return 1;
        if(b2[i] < 0 && b1[i] >=0)
          return -1;
      }
    }
    return 0;
  }

  @Override
  public String toString() {
    return "[" + Arrays.toString(guid) + "]";
  }

  public static GUID nil() {
    return new GUID(new int[16]);
  }

  public int[] getGuid() {
    return guid;
  }

  public GUID setGuid(int[] guid) {
    this.guid = guid;
    return this;
  }
}
