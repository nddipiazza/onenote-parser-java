package org.apache.tika.onenote;

public class FileNodePtrBackPush {
  FileNodePtr parent;
  public static int numAdds = 0;
  public static int numDescs = 0;

  public FileNodePtrBackPush(FileNodePtr parent) {
    this.parent = parent;
    this.parent.offsets.add(0);
    ++numAdds;
  }

  public void dec() {
    parent.offsets.remove(parent.offsets.size()-1);
    numDescs++;
  }
}
