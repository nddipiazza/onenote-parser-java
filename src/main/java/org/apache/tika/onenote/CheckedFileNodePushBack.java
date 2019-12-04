package org.apache.tika.onenote;

public class CheckedFileNodePushBack {
  FileNodeList fileNodeList;
  boolean committed;

  public CheckedFileNodePushBack(FileNodeList fileNodeList) {
    committed = true;
    this.fileNodeList = fileNodeList;
    fileNodeList.children.add(new FileNode());
    committed = false;
  }

  public void commit() {
    committed = true;
  }

  public void popBackIfNotCommitted() {
    if (!committed) {
      fileNodeList.children.remove(fileNodeList.children.size()-1);
    }
  }
}
