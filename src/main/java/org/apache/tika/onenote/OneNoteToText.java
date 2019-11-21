package org.apache.tika.onenote;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class OneNoteToText implements AutoCloseable {

  private OneNoteDocument oneNoteDocument;
  private boolean onlyLatestRevision;
  private InputStream in;
  private PrintWriter printWriter;
  private SeekableByteChannel channel;
  private Pair<Long, ExtendedGUID> roleAndContext;

  public OneNoteToText(OneNoteDocument oneNoteDocument, boolean onlyLatestRevision,
                       InputStream in, SeekableByteChannel channel, OutputStream out,
                       Pair<Long, ExtendedGUID> roleAndContext) {
    this.oneNoteDocument = oneNoteDocument;
    this.onlyLatestRevision = onlyLatestRevision;
    this.in = in;
    this.channel = channel;
    this.printWriter = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_16LE), true);
    this.roleAndContext = roleAndContext;
  }

  public void oneNoteDocumentToText() throws IOException {
    for (ExtendedGUID revisionListGuid : oneNoteDocument.revisionListOrder) {
      FileNodePtr fileNodePtr = oneNoteDocument.revisionManifestLists.get(revisionListGuid);
      walkRevision(fileNodePtr);
    }
  }

  boolean hasRevisionRole(ExtendedGUID rid, Pair<Long, ExtendedGUID> revisionRole) {
    Collection<Pair<Long, ExtendedGUID>> where = oneNoteDocument.revisionRoleMap.get(rid);
    return where.contains(revisionRole);
  }

  private void walkRevision(FileNodePtr fileNodePtr) throws IOException {
    FileNode fileNode = fileNodePtr.dereference(oneNoteDocument);
    Set<ExtendedGUID> validRevisions = new HashSet<>();
    for (int i = fileNode.children.size() - 1; i >= 0; --i) {
      FileNode child = fileNode.children.get(i);
      if (roleAndContext != null && hasRevisionRole(child.gosid, roleAndContext)) {
        validRevisions.add(child.gosid);
        if (onlyLatestRevision) {
          break;
        }
      }
    }
    boolean okGroup = false;
    for (FileNode child : fileNode.children) {
      if (child.id == Constants.RevisionManifestStart4FND ||
          child.id == Constants.RevisionManifestStart6FND ||
          child.id == Constants.RevisionManifestStart7FND) {
        okGroup = validRevisions.contains(child.gosid);
      }
      if (okGroup) {
        if ((child.id == Constants.RootObjectReference2FNDX ||
            child.id == Constants.RootObjectReference3FND) &&
            child.subType.rootObjectReference.rootObjectReferenceBase.rootRole == 1) {
          FileNodePtr childFileNodePointer = oneNoteDocument.guidToObject.get(child.gosid);
          walkFileNodePtr(childFileNodePointer);
        }
      }
    }
  }

  public void walkFileNodePtr(FileNodePtr fileNodePtr) throws IOException {
    if (fileNodePtr != null) {
      FileNode fileNode = fileNodePtr.dereference(oneNoteDocument);
      walkFileNode(fileNode);
    }
  }

  public void walkFileNode(FileNode fileNode) throws IOException {

    for (FileNode child : fileNode.children) {
      walkFileNode(child);
    }

    processPropertySet(fileNode.propertySet);
  }

  private void processPropertySet(PropertySet propertySet) throws IOException {
    for (PropertyValue propertyValue : propertySet.rgPridsData) {
      processPropertyValue(propertyValue);
    }
  }

  private void processPropertyValue(PropertyValue propertyValue) throws IOException {
    if (propertyValue.propertyID.type == 7) {
      OneNotePtr content = new OneNotePtr(oneNoteDocument, in, channel);
      content.reposition(propertyValue.rawData);
      boolean isBinary = (propertyValue.propertyID.id == (Properties.RgOutlineIndentDistance & 0xffff)) ||
          (propertyValue.propertyID.id == (Properties.NotebookManagementEntityGuid & 0xffff));
      int contentXor1 = content.size() & 1;
      if (contentXor1 == 0 && propertyValue.propertyID.id != (Properties.TextExtendedAscii & 0xffff) && !isBinary) {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        printWriter.println(new String(buf, StandardCharsets.UTF_16LE));
      }
    }


    if (propertyValue.propertyID.type > 0 && propertyValue.propertyID.type <= 6) {
      //IOUtils.write(String.format("%s%n", propertyValue.scalar), out, "UTF-8");
    } else if (propertyValue.propertyID.type == 7) {
      OneNotePtr content = new OneNotePtr(oneNoteDocument, in, channel);
      content.reposition(propertyValue.rawData);
      boolean isBinary = (propertyValue.propertyID.id
          == (Properties.RgOutlineIndentDistance & 0xffff))
          || (propertyValue.propertyID.id
          == (Properties.NotebookManagementEntityGuid & 0xffff));
      if ((content.size() & 1) == 0
          && propertyValue.propertyID.id != (Properties.TextExtendedAscii & 0xffff)
          && isBinary == false) {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        printWriter.println(new String(buf, StandardCharsets.UTF_16LE));
      } else if (isBinary == false) {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        printWriter.println(new String(buf, StandardCharsets.UTF_16LE));
      } else {
        // binary data here
      }
    } else if (propertyValue.propertyID.type == 0x9 || propertyValue.propertyID.type == 0x8
        || propertyValue.propertyID.type == 0xb || propertyValue.propertyID.type == 0xc
        || propertyValue.propertyID.type == 0xa || propertyValue.propertyID.type == 0xd) {
      for (CompactID compactID : propertyValue.compactIDs) {
        FileNodePtr childFileNodePointer = oneNoteDocument.guidToObject.get(compactID.guid);
        walkFileNodePtr(childFileNodePointer);
      }
    } else if (propertyValue.propertyID.type == 0x10 || propertyValue.propertyID.type == 0x11) {
      processPropertySet(propertyValue.propertySet);
    }
  }

  @Override
  public void close() throws Exception {
    printWriter.close();
  }
}