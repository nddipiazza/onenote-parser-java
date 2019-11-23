package org.apache.tika.onenote;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OneNoteTreeWalker implements AutoCloseable {

  private OneNoteDocument oneNoteDocument;
  private boolean onlyLatestRevision;
  private InputStream in;
  private PrintWriter printWriter;
  private SeekableByteChannel channel;
  private Pair<Long, ExtendedGUID> roleAndContext;

  public OneNoteTreeWalker(OneNoteDocument oneNoteDocument, boolean onlyLatestRevision,
                           InputStream in, SeekableByteChannel channel, OutputStream out,
                           Pair<Long, ExtendedGUID> roleAndContext) {
    this.oneNoteDocument = oneNoteDocument;
    this.onlyLatestRevision = onlyLatestRevision;
    this.in = in;
    this.channel = channel;
    this.printWriter = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_16LE), true);
    this.roleAndContext = roleAndContext;
  }

  public List<Map<String, Object>> walkTree() throws IOException {
    List<Map<String, Object>> res = new ArrayList<>();
    for (ExtendedGUID revisionListGuid : oneNoteDocument.revisionListOrder) {
      Map<String, Object> structure = new HashMap<>();
      structure.put("revisionListGuid", revisionListGuid.toString());
      FileNodePtr fileNodePtr = oneNoteDocument.revisionManifestLists.get(revisionListGuid);
      structure.put("fileNode", walkRevision(fileNodePtr));
      res.add(structure);
    }
    return res;
  }

  boolean hasRevisionRole(ExtendedGUID rid, Pair<Long, ExtendedGUID> revisionRole) {
    Collection<Pair<Long, ExtendedGUID>> where = oneNoteDocument.revisionRoleMap.get(rid);
    return where.contains(revisionRole);
  }

  private Map<String, Object> walkRevision(FileNodePtr fileNodePtr) throws IOException {
    Map<String, Object> structure = new HashMap<>();
    structure.put("offsets", fileNodePtr.offsets);
    FileNode fileNode = fileNodePtr.dereference(oneNoteDocument);
    structure.put("fileNodeId", fileNode.id);
    if (fileNode.gosid != null) {
      structure.put("gosid", fileNode.gosid.toString());
    }
    structure.put("subType", fileNode.subType);
    structure.put("size", fileNode.size);
    structure.put("isFileData", fileNode.isFileData);

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
    List<Map<String, Object>> children = new ArrayList<>();
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
          children.add(walkFileNodePtr(childFileNodePointer));
        }
      }
    }
    if (!children.isEmpty()) {
      structure.put("children", children);
    }
    return structure;
  }

  public Map<String, Object> walkFileNodePtr(FileNodePtr fileNodePtr) throws IOException {
    if (fileNodePtr != null) {
      FileNode fileNode = fileNodePtr.dereference(oneNoteDocument);
      return walkFileNode(fileNode);
    }
    return Collections.emptyMap();
  }

  public Map<String, Object> walkFileNode(FileNode fileNode) throws IOException {
    Map<String, Object> structure = new HashMap<>();
    List<Map<String, Object>> children = new ArrayList<>();
    for (FileNode child : fileNode.children) {
      children.add(walkFileNode(child));
    }
    if (!children.isEmpty()) {
      structure.put("children", children);
    }
    List<Map<String, Object>> propSet = processPropertySet(fileNode.propertySet);
    if (!propSet.isEmpty()) {
      structure.put("propertySet", propSet);
    }
    return structure;
  }

  private List<Map<String, Object>> processPropertySet(PropertySet propertySet) throws IOException {
    List<Map<String, Object>> propValues = new ArrayList<>();
    for (PropertyValue propertyValue : propertySet.rgPridsData) {
      propValues.add(processPropertyValue(propertyValue));
    }
    return propValues;
  }

  private Map<String, Object> processPropertyValue(PropertyValue propertyValue) throws IOException {
    Map<String, Object> propMap = new HashMap<>();
    propMap.put("propertyId", propertyValue.propertyId.toString());
    if (propertyValue.propertyId.type > 0 && propertyValue.propertyId.type <= 6) {
      propMap.put("scalar", propertyValue.scalar);
    } else if (propertyValue.propertyId.type == 7) {
      OneNotePtr content = new OneNotePtr(oneNoteDocument, in, channel);
      content.reposition(propertyValue.rawData);
      boolean isBinary = (propertyValue.propertyId.id
          == (Properties.RgOutlineIndentDistance & 0xffff))
          || (propertyValue.propertyId.id
          == (Properties.NotebookManagementEntityGuid & 0xffff));
      if ((content.size() & 1) == 0
          && propertyValue.propertyId.id != (Properties.TextExtendedAscii & 0xffff)
          && isBinary == false) {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        propMap.put("dataB64", Base64.encodeBase64String(buf));
        propMap.put("dataUnicode16LE", new String(buf, StandardCharsets.UTF_16LE));
      } else if (isBinary == false) {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        propMap.put("dataB64", Base64.encodeBase64String(buf));
        propMap.put("dataUnicode16LE", new String(buf, StandardCharsets.UTF_16LE));
      } else {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        propMap.put("dataB64", Base64.encodeBase64String(buf));
        propMap.put("dataUnicode16LE", new String(buf, StandardCharsets.UTF_16LE));
      }
    } else if (propertyValue.propertyId.type == 0x9 || propertyValue.propertyId.type == 0x8
        || propertyValue.propertyId.type == 0xb || propertyValue.propertyId.type == 0xc
        || propertyValue.propertyId.type == 0xa || propertyValue.propertyId.type == 0xd) {
      List<Map<String, Object>> children = new ArrayList<>();
      for (CompactID compactID : propertyValue.compactIDs) {
        FileNodePtr childFileNodePointer = oneNoteDocument.guidToObject.get(compactID.guid);
        children.add(walkFileNodePtr(childFileNodePointer));
      }
      if (!children.isEmpty()) {
        propMap.put("children", children);
      }
    } else if (propertyValue.propertyId.type == 0x10 || propertyValue.propertyId.type == 0x11) {
      List<Map<String, Object>> propSet = processPropertySet(propertyValue.propertySet);
      if (!propSet.isEmpty()) {
        propMap.put("propertySet", propSet);
      }
    }
    return propMap;
  }

  @Override
  public void close() throws Exception {
    printWriter.close();
  }
}