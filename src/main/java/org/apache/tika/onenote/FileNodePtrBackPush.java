package org.apache.tika.onenote;

import java.util.concurrent.atomic.AtomicInteger;

public class FileNodePtrBackPush {
  FileNodePtr parent;

  public FileNodePtrBackPush(FileNodePtr parent) {
    this.parent = parent;
    this.parent.offsets.add(new AtomicInteger(0));
  }

  public void inc() {
    parent.offsets.get(parent.offsets.size()-1).incrementAndGet();
  }

  public void dec() {
    parent.offsets.remove(parent.offsets.size()-1);
  }
}
