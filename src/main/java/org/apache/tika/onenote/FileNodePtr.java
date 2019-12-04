package org.apache.tika.onenote;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a list that represents how to get to the file node in the data structure.
 * First element of the nodeListPositions is the index of the FileNode at the root.
 * Next element of the nodeListPosition is the index at the next level.
 *
 * For example 0, 4, 15 would mean
 *
 * document.root.children.get(0).childFileNodeList.children.get(4).childFileNodeList.children.get(15)
 *
 */
public class FileNodePtr {
  List<Integer> nodeListPositions = new ArrayList<>();

  public FileNodePtr() {

  }

  public FileNodePtr(FileNodePtr copyFrom) {
    nodeListPositions.addAll(copyFrom.nodeListPositions);
  }

  public FileNode dereference(OneNoteDocument document) {
    if (nodeListPositions.isEmpty()) {
      return null;
    }
    if (nodeListPositions.get(0) >= document.root.children.size()) {
      throw new RuntimeException("Exceeded root child size");
    }
    FileNode cur = document.root.children.get(nodeListPositions.get(0));
    for (int i = 1, ie = nodeListPositions.size(); i < ie; ++i) {
      cur = cur.childFileNodeList.children.get(nodeListPositions.get(i));
    }
    return cur;
  }
}
