package org.apache.tika.onenote;

import java.util.List;

public class CheckedFileNodePushBack {
  List<FileNode> fileNodeList;
  boolean committed;

  public CheckedFileNodePushBack(List<FileNode> fileNodeList) {
    committed = true;
    this.fileNodeList = fileNodeList;
    fileNodeList.add(new FileNode());
    committed = false;
  }

  public void commit() {
    committed = true;
  }

  public void popBackIfNotCommitted() {
    if (!committed) {
      fileNodeList.remove(fileNodeList.size()-1);
    }
  }
}
