package org.apache.tika.onenote;

import java.util.ArrayList;
import java.util.List;

public class FileNodePtr {
  List<Integer> offsets = new ArrayList<>();

  public FileNodePtr() {

  }

  public FileNodePtr(FileNodePtr copyFrom) {
    offsets.addAll(copyFrom.offsets);
  }

  public FileNode dereference(OneNoteDocument document) {
    if (offsets.isEmpty()) {
      return null;
    }
    if (offsets.get(0) >= document.root.size()) {
      throw new RuntimeException("Exceeded root child size");
    }
    FileNode cur = document.root.get(offsets.get(0));
    for (int i = 1, ie = offsets.size(); i < ie; ++i) {
      cur = cur.children.get(offsets.get(i));
    }
    return cur;
  }
}
