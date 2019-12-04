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

/**
 * Walk the one note tree and create a Map while it goes.
 * Also writes user input text to a print writer as it parses.
 */
public class OneNoteTreeWalker implements AutoCloseable {

  private OneNoteTreeWalkerOptions options;
  private OneNoteDocument oneNoteDocument;
  private InputStream in;
  private PrintWriter printWriter;
  private SeekableByteChannel channel;
  private Pair<Long, ExtendedGUID> roleAndContext;

  /**
   * Create a one tree walker.
   * @param options The options for how to walk this tree.
   * @param oneNoteDocument The one note document we want to walk.
   * @param in The input stream pointing to the stream of one note text.
   * @param channel The seekable byte channel that accompanies the input stream. This is used extensively to
   *                reset the position of the stream parsing while the parse occurs.
   * @param out An output stream that we will use a print writer to write to.
   * @param roleAndContext The role and context value we want to use when crawling. Set this to null if you are
   *                       crawling all root file nodes, and don't care about revisions.
   */
  public OneNoteTreeWalker(OneNoteTreeWalkerOptions options, OneNoteDocument oneNoteDocument,
                           InputStream in, SeekableByteChannel channel, OutputStream out,
                           Pair<Long, ExtendedGUID> roleAndContext) {
    this.options = options;
    this.oneNoteDocument = oneNoteDocument;
    this.in = in;
    this.channel = channel;
    this.printWriter = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true);
    this.roleAndContext = roleAndContext;
  }

  /**
   * Parse the tree.
   * @return Map of the fully parsed one note document.
   * @throws IOException Can throw these when manipulating the seekable byte channel.
   */
  public Map<String, Object> walkTree() throws IOException {
    Map<String, Object> structure = new HashMap<>();
    structure.put("header", oneNoteDocument.header);
    structure.put("rootFileNodes", walkRootFileNodes());
    return structure;
  }

  /**
   * Walk the root file nodes, depending on the options will crawl revisions or the entire revision tree.
   * @return List of the root file nodes.
   * @throws IOException Can throw these when manipulating the seekable byte channel.
   */
  public List<Map<String, Object>> walkRootFileNodes() throws IOException {
    List<Map<String, Object>> res = new ArrayList<>();
    if (options.isCrawlAllFileNodesFromRoot()) {
      res.add(walkFileNodeList(oneNoteDocument.root));
    } else {
      for (ExtendedGUID revisionListGuid : oneNoteDocument.revisionListOrder) {
        Map<String, Object> structure = new HashMap<>();
        structure.put("oneNoteType", "Revision");
        structure.put("revisionListGuid", revisionListGuid.toString());
        FileNodePtr fileNodePtr = oneNoteDocument.revisionManifestLists.get(revisionListGuid);
        structure.put("fileNode", walkRevision(fileNodePtr));
        res.add(structure);
      }
    }
    return res;
  }

  /**
   * Does the revision role map have this revision role id.
   * @param rid The revision id.
   * @param revisionRole The revision role Long,GUID pair.
   * @return True if exists, false if not.
   */
  private boolean hasRevisionRole(ExtendedGUID rid, Pair<Long, ExtendedGUID> revisionRole) {
    Collection<Pair<Long, ExtendedGUID>> where = oneNoteDocument.revisionRoleMap.get(rid);
    return where.contains(revisionRole);
  }

  /**
   * Walk revisions.
   * @param fileNodePtr The file node pointer to start with.
   * @return A map of the parsed data.
   * @throws IOException Can throw these when manipulating the seekable byte channel.
   */
  private Map<String, Object> walkRevision(FileNodePtr fileNodePtr) throws IOException {
    Map<String, Object> structure = new HashMap<>();
    structure.put("oneNoteType", "FileNodePointer");
    structure.put("offsets", fileNodePtr.nodeListPositions);
    FileNode revisionFileNode = fileNodePtr.dereference(oneNoteDocument);
    structure.put("fileNodeId", revisionFileNode.id);
    if (revisionFileNode.gosid != null) {
      structure.put("gosid", revisionFileNode.gosid.toString());
    }
    structure.put("subType", revisionFileNode.subType);
    structure.put("size", revisionFileNode.size);
    structure.put("isFileData", revisionFileNode.isFileData);

    Set<ExtendedGUID> validRevisions = new HashSet<>();
    for (int i = revisionFileNode.childFileNodeList.children.size() - 1; i >= 0; --i) {
      FileNode child = revisionFileNode.childFileNodeList.children.get(i);
      if (roleAndContext != null && hasRevisionRole(child.gosid, roleAndContext)) {
        validRevisions.add(child.gosid);
        if (options.isOnlyLatestRevision()) {
          break;
        }
      }
    }
    List<Map<String, Object>> children = new ArrayList<>();
    boolean okGroup = false;
    for (FileNode child : revisionFileNode.childFileNodeList.children) {
      if (child.id == FndStructureConstants.RevisionManifestStart4FND ||
          child.id == FndStructureConstants.RevisionManifestStart6FND ||
          child.id == FndStructureConstants.RevisionManifestStart7FND) {
        okGroup = validRevisions.contains(child.gosid);
      }
      if (okGroup) {
        if ((child.id == FndStructureConstants.RootObjectReference2FNDX ||
            child.id == FndStructureConstants.RootObjectReference3FND) &&
            child.subType.rootObjectReference.rootObjectReferenceBase.rootRole == 1) {
          FileNodePtr childFileNodePointer = oneNoteDocument.guidToObject.get(child.gosid);
          children.add(walkFileNodePtr(childFileNodePointer));
        }
      }
    }
    if (!children.isEmpty()) {
      Map<String, Object> childFileNodeListMap = new HashMap<>();
      childFileNodeListMap.put("fileNodeListHeader", revisionFileNode.childFileNodeList.fileNodeListHeader);
      childFileNodeListMap.put("children", children);
      structure.put("revisionFileNodeList", childFileNodeListMap);
    }
    return structure;
  }

  /**
   * Walk the file node pointer.
   * @param fileNodePtr The file node pointer.
   * @return Returns a map of the main data.
   * @throws IOException Can throw these when manipulating the seekable byte channel.
   */
  public Map<String, Object> walkFileNodePtr(FileNodePtr fileNodePtr) throws IOException {
    if (fileNodePtr != null) {
      FileNode fileNode = fileNodePtr.dereference(oneNoteDocument);
      return walkFileNode(fileNode);
    }
    return Collections.emptyMap();
  }

  /**
   * Walk the file node list.
   * @param fileNodeList The file node list to parse.
   * @return The result.
   * @throws IOException Can throw these when manipulating the seekable byte channel.
   */
  public Map<String, Object> walkFileNodeList(FileNodeList fileNodeList) throws IOException {
    Map<String, Object> structure = new HashMap<>();
    structure.put("oneNoteType", "FileNodeList");
    structure.put("fileNodeListHeader", fileNodeList.fileNodeListHeader);
    if (!fileNodeList.children.isEmpty()) {
      List<Map<String, Object>> children = new ArrayList<>();
      for (FileNode child : fileNodeList.children) {
        children.add(walkFileNode(child));
      }
      structure.put("children", children);
    }
    return structure;
  }

  /**
   * Walk a single file node.
   * @param fileNode The file node.
   * @return Map which is result of the parsed file node.
   * @throws IOException Can throw these when manipulating the seekable byte channel.
   */
  public Map<String, Object> walkFileNode(FileNode fileNode) throws IOException {
    Map<String, Object> structure = new HashMap<>();
    structure.put("oneNoteType", "FileNode");
    structure.put("gosid", fileNode.gosid.toString());
    structure.put("size", fileNode.size);
    structure.put("fileNodeId", "0x" + Long.toHexString(fileNode.id));
    structure.put("fileNodeIdName", FndStructureConstants.nameOf(fileNode.id));
    structure.put("fileNodeBaseType", "0x" + Long.toHexString(fileNode.baseType));
    structure.put("isFileData", fileNode.isFileData);
    structure.put("idDesc", fileNode.idDesc);
    if (fileNode.childFileNodeList != null && fileNode.childFileNodeList.fileNodeListHeader != null) {
      structure.put("childFileNodeList", walkFileNodeList(fileNode.childFileNodeList));
    }
    if (fileNode.propertySet != null) {
      List<Map<String, Object>> propSet = processPropertySet(fileNode.propertySet);
      if (!propSet.isEmpty()) {
        structure.put("propertySet", propSet);
      }
    }
    if (fileNode.subType.fileDataStoreObjectReference.ref != null &&
        !FileChunkReference.nil().equals(fileNode.subType.fileDataStoreObjectReference.ref.fileData)) {
      structure.put("fileDataStoreObjectReference", walkFileDataStoreObjectReference(fileNode.subType.fileDataStoreObjectReference));
    }
    return structure;
  }

  /**
   * Walk a file data store object reference.
   * @param fileDataStoreObjectReference The file data store object reference we are parsing.
   * @return Map containing parsed content.
   * @throws IOException Can throw these when manipulating the seekable byte channel.
   */
  private Map<String, Object> walkFileDataStoreObjectReference(FileDataStoreObjectReference fileDataStoreObjectReference) throws IOException {
    Map<String, Object> structure = new HashMap<>();
    OneNotePtr content = new OneNotePtr(oneNoteDocument, in, channel);
    content.reposition(fileDataStoreObjectReference.ref.fileData);
    byte[] buf = new byte[(int)fileDataStoreObjectReference.ref.fileData.cb];
    IOUtils.readFully(in, buf);
    structure.put("fileDataStoreObjectMetadata", fileDataStoreObjectReference);
    structure.put("dataBase64", Base64.encodeBase64String(buf));
    return structure;
  }

  /**
   *
   * @param propertySet
   * @return
   * @throws IOException Can throw these when manipulating the seekable byte channel.
   */
  private List<Map<String, Object>> processPropertySet(PropertySet propertySet) throws IOException {
    List<Map<String, Object>> propValues = new ArrayList<>();
    for (PropertyValue propertyValue : propertySet.rgPridsData) {
      propValues.add(processPropertyValue(propertyValue));
    }
    return propValues;
  }

  /**
   * Is this property a binary property?
   * @param property The property.
   * @return Is it binary?
   */
  private boolean propertyIsBinary(OneNotePropertyEnum property) {
    return property == OneNotePropertyEnum.RgOutlineIndentDistance ||
        property == OneNotePropertyEnum.NotebookManagementEntityGuid ||
        property == OneNotePropertyEnum.RichEditTextUnicode;
  }

  /**
   * Process a property value and populate a map containing all the property value data.
   *
   * Parse out any relevant text and write it to the print writer as well for easy search engine parsing.
   *
   * @param propertyValue The property value we are parsing.
   * @return The map parsed by this property value.
   * @throws IOException Can throw these when manipulating the seekable byte channel.
   */
  private Map<String, Object> processPropertyValue(PropertyValue propertyValue) throws IOException {
    Map<String, Object> propMap = new HashMap<>();
    propMap.put("oneNoteType", "PropertyValue");
    propMap.put("propertyId", propertyValue.propertyId.toString());

    if (propertyValue.propertyId.type > 0 && propertyValue.propertyId.type <= 6) {
      propMap.put("scalar", propertyValue.scalar);
    } else {
      OneNotePtr content = new OneNotePtr(oneNoteDocument, in, channel);
      content.reposition(propertyValue.rawData);
      boolean isBinary = propertyIsBinary(propertyValue.propertyId.propertyEnum);
      propMap.put("isBinary", isBinary);
      if ((content.size() & 1) == 0
          && propertyValue.propertyId.propertyEnum != OneNotePropertyEnum.TextExtendedAscii
          && isBinary == false) {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        propMap.put("dataUnicode16LE", new String(buf, StandardCharsets.UTF_16LE));
        if (options.getUtf16PropertiesToPrint().contains(propertyValue.propertyId)) {
          printWriter.println(propMap.get("dataUnicode16LE"));
        }
      } else if (propertyValue.propertyId.propertyEnum == OneNotePropertyEnum.TextExtendedAscii) {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        propMap.put("dataAscii", new String(buf, StandardCharsets.US_ASCII));
        printWriter.println(propMap.get("dataAscii"));
      } else if (isBinary == false) {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        propMap.put("dataUnicode16LE", new String(buf, StandardCharsets.UTF_16LE));
        if (options.getUtf16PropertiesToPrint().contains(propertyValue.propertyId)) {
          printWriter.println(propMap.get("dataUnicode16LE"));
        }
      } else {
        byte[] buf = new byte[content.size()];
        IOUtils.readFully(in, buf);
        if (propertyValue.propertyId.propertyEnum == OneNotePropertyEnum.RichEditTextUnicode) {
          propMap.put("rtfBase64", Base64.encodeBase64String(buf));
        } else {
          propMap.put("dataB64", Base64.encodeBase64String(buf));
        }
      }
    }
    if (propertyValue.compactIDs != null) {
      List<Map<String, Object>> children = new ArrayList<>();
      for (CompactID compactID : propertyValue.compactIDs) {
        FileNodePtr childFileNodePointer = oneNoteDocument.guidToObject.get(compactID.guid);
        children.add(walkFileNodePtr(childFileNodePointer));
      }
      if (!children.isEmpty()) {
        propMap.put("children", children);
      }
    }
    if (propertyValue.propertySet != null && propertyValue.propertySet.rgPridsData != null) {
      List<Map<String, Object>> propSet = processPropertySet(propertyValue.propertySet);
      if (!propSet.isEmpty()) {
        propMap.put("propertySet", propSet);
      }
    }
    return propMap;
  }

  /**
   * Close the print writer.
   */
  @Override
  public void close() {
    printWriter.close();
  }
}