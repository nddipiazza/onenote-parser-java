package org.apache.tika.onenote;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FileNodePtr {
  List<AtomicInteger> offsets = new ArrayList<>();

  public FileNodePtr() {

  }

  public FileNodePtr(FileNodePtr copyFrom) {
    offsets.addAll(copyFrom.offsets);
  }

  public FileNode dereference(OneNote document) {
    if (offsets.isEmpty()) {
      return null;
    }
    FileNode cur = document.root.get(offsets.get(0).get());
    for (int i = 1, ie = offsets.size(); i < ie; ++i) {
      cur = cur.children.get(offsets.get(i).get());
    }
    return cur;
  }
}
