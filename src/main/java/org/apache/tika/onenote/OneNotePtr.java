package org.apache.tika.onenote;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.tika.onenote.Constants.CanRevise.ObjectDeclarationWithRefCount2FNDX;

public class OneNotePtr {

  private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

  public static final long FOOTER_CONST = 0x8BC215C38233BA4BL;
  public static final String UNKNOWN = "unknown";
  int indentLevel = 0;
  boolean quietParse = Boolean.parseBoolean(System.getProperty("org.apache.onenote.quiet", "true"));

  long offset;
  long end;

  OneNote document;
  FileChannel fc;
  InputStream in;

  public OneNotePtr(OneNote document, InputStream in, FileChannel fc) throws IOException {
    this.document = document;
    this.fc = fc;
    this.in = in;
    offset = fc.position();
    end = fc.size();
  }

  public OneNotePtr(OneNotePtr oneNotePtr) {
    this.document = oneNotePtr.document;
    this.fc = oneNotePtr.fc;
    this.in = oneNotePtr.in;
    this.offset = oneNotePtr.offset;
    this.end = oneNotePtr.end;
    this.indentLevel = oneNotePtr.indentLevel + 1;
  }

  public Header deserializeHeader() throws IOException {
    Header data = new Header();
    data.setGuidFileType(deserializeGUID())
      .setGuidFile(deserializeGUID())
      .setGuidLegacyFileVersion(deserializeGUID())
      .setGuidFileFormat(deserializeGUID())
      .setFfvLastCode(deserializeLittleEndianInt())
      .setFfvNewestCode(deserializeLittleEndianInt())
      .setFfvOldestCode(deserializeLittleEndianInt())
      .setFfvOldestReader(deserializeLittleEndianInt())
      .setFcrLegacyFreeChunkList(deserializeFileChunkReference64())
      .setFcrLegacyTransactionLog(deserializeFileChunkReference64())
      .setcTransactionsInLog(deserializeLittleEndianInt())
      .setCbExpectedFileLength(deserializeLittleEndianInt())
      .setRgbPlaceholder(deserializeLittleEndianLong())
      .setFcrLegacyFileNodeListRoot(deserializeFileChunkReference64())
      .setCbLegacyFreeSpaceInFreeChunkList(deserializeLittleEndianInt())
      .setIgnoredZeroA(deserializeLittleEndianChar())
      .setIgnoredZeroB(deserializeLittleEndianChar())
      .setIgnoredZeroC(deserializeLittleEndianChar())
      .setIgnoredZeroD(deserializeLittleEndianChar())
      .setGuidAncestor(deserializeGUID())
      .setCrcName(deserializeLittleEndianInt())
      .setFcrHashedChunkList(deserializeFileChunkReference64x32())
      .setFcrTransactionLog(deserializeFileChunkReference64x32())
      .setFcrFileNodeListRoot(deserializeFileChunkReference64x32())
      .setFcrFreeChunkList(deserializeFileChunkReference64x32())
      .setCbExpectedFileLength(deserializeLittleEndianLong())
      .setCbFreeSpaceInFreeChunkList(deserializeLittleEndianLong())
      .setGuidFileVersion(deserializeGUID())
      .setnFileVersionGeneration(deserializeLittleEndianLong())
      .setGuidDenyReadFileVersion(deserializeGUID())
      .setGrfDebugLogFlags(deserializeLittleEndianInt())
      .setFcrDebugLogA(deserializeFileChunkReference64x32())
      .setFcrDebugLogB(deserializeFileChunkReference64x32())
      .setBuildInfoA(deserializeLittleEndianInt())
      .setBuildInfoB(deserializeLittleEndianInt())
      .setBuildInfoC(deserializeLittleEndianInt())
      .setBuildInfoD(deserializeLittleEndianInt())
      .setReserved(deserializedReservedHeader());
    return data;
  }

  private GUID deserializeGUID() throws IOException {
    int[] guid = new int[16];
    for (int i = 0; i < 16; ++i) {
      guid[i] = in.read();
    }
    offset = fc.position();
    return new GUID(guid);
  }

  private byte[] deserializedReservedHeader() throws IOException {
    if (fc.position() != offset) {
      fc.position(offset);
    }
    byte [] data = new byte[728];
    IOUtils.read(in, data);
    offset = fc.position();
    return data;
  }

  private FileChunkReference deserializeFileChunkReference64() throws IOException {
    long stp = deserializeLittleEndianInt();
    long cb = deserializeLittleEndianInt();
    offset = fc.position();
    return new FileChunkReference(stp, cb);
  }

  private FileChunkReference deserializeFileChunkReference64x32() throws IOException {
    long stp = deserializeLittleEndianLong();
    long cb = deserializeLittleEndianInt();
    offset = fc.position();
    return new FileChunkReference(stp, cb);
  }

  private char deserializeLittleEndianChar() throws IOException {
    if (fc.position() != offset) {
      fc.position(offset);
    }
    char res = (char) in.read();
    ++offset;
    return res;
  }

  private long deserializeLittleEndianInt() throws IOException {
    if (fc.position() != offset) {
      fc.position(offset);
    }
    long res = EndianUtils.readSwappedUnsignedInteger(in);
    offset = fc.position();
    return res;
  }

  private long deserializeLittleEndianLong() throws IOException {
    if (fc.position() != offset) {
      fc.position(offset);
    }
    long res = EndianUtils.readSwappedLong(in);
    offset = fc.position();
    return res;
  }

  private long deserializeLittleEndianShort() throws IOException {
    if (fc.position() != offset) {
      fc.position(offset);
    }
    long res = EndianUtils.readSwappedShort(in);
    offset = fc.position();
    return res;
  }

  private String getIndent() {
    String retval = "";
    for (int i = 0; i < indentLevel; ++i) {
      retval += "  ";
    }
    return retval;
  }

  public void reposition(FileChunkReference loc) throws IOException {
    reposition(loc.stp);
    this.end = offset + loc.cb;
  }

  private void reposition(long offset) throws IOException {
    this.offset = offset;
    fc.position(offset);
  }

  public OneNotePtr deserializeFileNodeList(OneNotePtr ptr, List<FileNode> fileNodeList, FileNodePtr curPath) throws IOException {
    OneNotePtr localPtr = new OneNotePtr(document, in, fc);
    FileNodePtrBackPush bp = new FileNodePtrBackPush(curPath);
    try {
      while (true) {
        FileChunkReference next = FileChunkReference.NIL;
        deserializeFileNodeListFragment(fileNodeList, next, curPath);
        if (FileChunkReference.NIL.equals(next)) {
          break;
        }
        localPtr.reposition(next);
        ptr = localPtr;
      }
      return ptr;
    } finally {
      bp.dec();
    }
  }


  public OneNotePtr deserializeFileNodeList(List<FileNode> fileNodeList, FileNodePtr curPath) throws IOException {
    return deserializeFileNodeList(this, fileNodeList, curPath);
  }

  void deserializeFileNodeListFragment(List<FileNode> data, FileChunkReference next, FileNodePtr curPath) throws IOException {
    FileNodeListHeader fileNodeListHeader = deserializeFileNodeListHeader();
    boolean terminated = false;
    while (offset + 24 <= end) { // while there are at least 24 bytes free
      // 24 = sizeof(nextFragment) [12 bytes] + sizeof(footer) [8 bytes]
      // + 4 bytes for the FileNode header
      CheckedFileNodePushBack pushBack = new CheckedFileNodePushBack(data);
      try {
        FileNode fileNode = deserializeFileNode(data.get(data.size()-1), curPath);
        if (fileNode.id == Constants.ChunkTerminatorFND || fileNode.id == 0) {
          terminated = true;
          break;
        }
        pushBack.commit();
        assert curPath.dereference(document).equals(data.get(data.size()-1)); // is this correct? or should we be checking the pointer?
        curPath.offsets.get(curPath.offsets.size()-1).incrementAndGet();
      } finally {
        pushBack.popBackIfNotCommitted();
      }
    }
    reposition(end - 20);
    FileChunkReference nextChunkRef = deserializeFileChunkReference64x32();
    next.cb = nextChunkRef.cb;
    next.stp = nextChunkRef.stp;
    if (terminated) {
      // TODO check that next is OK
    }
    long footer = deserializeLittleEndianLong();
    if (footer != FOOTER_CONST) {
      throw new RuntimeException("Invalid footer constant. Expected " + FOOTER_CONST + " but was " + footer);
    }
  }

  private FileNode deserializeFileNode(FileNode data, FileNodePtr curPath) throws IOException {
    OneNotePtr backup = new OneNotePtr(this);
    long reserved;
    String idDesc = UNKNOWN;

    data.isFileData = false;
    data.gosid = ExtendedGUID.nil();
    long fileNodeHeader = deserializeLittleEndianInt();
    data.id = fileNodeHeader & 0x3ff;
    if (data.id == 0) {
      return data;
    }
    if (!quietParse) {
      LOG.info("{}Start Node {} ({}) - Offset={}, End={}\n", getIndent(), Constants.nameOf(data.id), data.id, offset, end);
    }
    data.size = (fileNodeHeader >> 10) & 0x1fff;
    // reset the size to only be in scope of this FileNode
    end = backup.offset + data.size;

    long stpFormat = (fileNodeHeader >> 23) & 0x3;
    long cbFormat = (fileNodeHeader >> 25) & 0x3;
    data.baseType = (fileNodeHeader >> 27) & 0xf;
    reserved = (fileNodeHeader >> 31);
    data.ref = FileChunkReference.NIL;
    if (data.baseType == 1 || data.baseType == 2) {
      data.ref = deserializeVarFileChunkReference(stpFormat, cbFormat);
    } // otherwise ignore the data ref, since we're a type 0
    if (data.baseType == 1 && !data.ref.equals(FileChunkReference.NIL)) {
      OneNotePtr content = new OneNotePtr(this);
      content.reposition(data.ref);
      // would have thrown an error if invalid.
    }
    if (data.id == Constants.ObjectGroupStartFND) {
      idDesc = "oid(group)";
      data.gosid = deserializeExtendedGUID();
      //LOG.info("{}gosid {}\n", getIndent(), data.gosid.toString().c_str());
    } else if (data.id == Constants.ObjectGroupEndFND) {
      // no data
    } else if (data.id == Constants.ObjectSpaceManifestRootFND
        || data.id == Constants.ObjectSpaceManifestListStartFND) {
      if (data.id == Constants.ObjectSpaceManifestRootFND) {
        idDesc = "gosidRoot";
      } else {
        idDesc = "gosid";
      }
      data.gosid = deserializeExtendedGUID();
      //LOG.info("{}gosid {}\n", getIndent(), data.gosid.toString().c_str());
    } else if (data.id == Constants.ObjectSpaceManifestListReferenceFND) {
      data.gosid = deserializeExtendedGUID();
      idDesc = "gosid";
      //LOG.info("{}gosid {}\n", getIndent(),data.gosid.toString().c_str());
      //children parsed in generic base_type 2 parser
    } else if (data.id == Constants.RevisionManifestListStartFND) {
      data.gosid = deserializeExtendedGUID();
      idDesc = "gosid";
      FileNodePtr parentPath = new FileNodePtr(curPath);
      parentPath.offsets.remove(parentPath.offsets.size() - 1);
      document.registerRevisionManifestList(data.gosid, parentPath);

      //LOG.info("{}gosid {}\n", getIndent(),data.gosid.toString().c_str());
      data.subType.revisionManifestListStart.nInstanceIgnored = deserializeLittleEndianInt();
    } else if (data.id == Constants.RevisionManifestStart4FND) {
      data.gosid = deserializeExtendedGUID(); // the rid
      idDesc = "rid";
      //LOG.info("{}gosid {}\n", getIndent(), data.gosid.toString().c_str());
      data.subType.revisionManifest.ridDependent = deserializeExtendedGUID(); // the rid
      if (!quietParse) {
        LOG.info("{}dependent gosid {}\n", getIndent(), data.subType.revisionManifest.ridDependent);
      }
      data.subType.revisionManifest.timeCreation = deserializeLittleEndianLong();
      data.subType.revisionManifest.revisionRole = deserializeLittleEndianInt();
      data.subType.revisionManifest.odcsDefault = deserializeLittleEndianShort();

      data.gctxid = ExtendedGUID.nil();
      document.registerRevisionManifest(data);
    } else if (data.id == Constants.RevisionManifestStart6FND
        || data.id == Constants.RevisionManifestStart7FND) {
      data.gosid = deserializeExtendedGUID(); // the rid
      idDesc = "rid";
      //LOG.info("{}gosid {}\n", getIndent(), data.gosid.toString().c_str());
      data.subType.revisionManifest.ridDependent = deserializeExtendedGUID(); // the rid
      if (!quietParse) {
        LOG.info("{}dependent gosid {}\n", getIndent(), data.subType.revisionManifest.ridDependent);
      }
      data.subType.revisionManifest.revisionRole = deserializeLittleEndianInt();
      data.subType.revisionManifest.odcsDefault = deserializeLittleEndianShort();

      data.gctxid = ExtendedGUID.nil();
      if (data.id == Constants.RevisionManifestStart7FND) {
        data.gctxid = deserializeExtendedGUID(); // the rid
      }
      document.registerAdditionalRevisionRole(data.gosid, data.subType.revisionManifest.revisionRole, data.gctxid);
      document.registerRevisionManifest(data);
    } else if (data.id == Constants.GlobalIdTableStartFNDX) {
      data.subType.globalIdTableStartFNDX.reserved = deserializeLittleEndianChar();

    } else if (data.id == Constants.GlobalIdTableEntryFNDX) {
      data.subType.globalIdTableEntryFNDX.index = deserializeLittleEndianInt();

      data.subType.globalIdTableEntryFNDX.guid = deserializeGUID();

      document.revisionMap.get(document.currentRevision).globalId.put(data.subType.globalIdTableEntryFNDX.index, data.subType.globalIdTableEntryFNDX.guid);
    } else if (data.id == Constants.GlobalIdTableEntry2FNDX) {
      data.subType.globalIdTableEntry2FNDX.indexMapFrom = deserializeLittleEndianInt();
      data.subType.globalIdTableEntry2FNDX.indexMapTo = deserializeLittleEndianInt();

      ExtendedGUID dependentRevision =
          document.revisionMap.get(document.currentRevision).dependent;
      // Get the compactId from the revisionMap's globalId map.
      GUID compactId = document.revisionMap.get(dependentRevision).globalId.get(data.subType.globalIdTableEntry2FNDX.indexMapFrom);
      if (compactId == null) {
        throw new RuntimeException("COMPACT_ID_MISSING");
      }
      document.revisionMap.get(document.currentRevision).globalId.put(data.subType.globalIdTableEntry2FNDX.indexMapTo, compactId);
    } else if (data.id == Constants.GlobalIdTableEntry3FNDX) {
      data.subType.globalIdTableEntry3FNDX.indexCopyFromStart = deserializeLittleEndianInt();

      data.subType.globalIdTableEntry3FNDX.entriesToCopy = deserializeLittleEndianInt();

      data.subType.globalIdTableEntry3FNDX.indexCopyToStart = deserializeLittleEndianInt();

      ExtendedGUID dependent_revision = document.revisionMap.get(document.currentRevision).dependent;
      for (int i = 0; i < data.subType.globalIdTableEntry3FNDX.entriesToCopy; ++i) {
        Map<Long, GUID> globalIdMap = document.revisionMap.get(dependent_revision).globalId;
        GUID compactId = globalIdMap.get(data.subType.globalIdTableEntry3FNDX.indexCopyFromStart + i);
        if (compactId == null) {
          throw new RuntimeException("COMPACT_ID_MISSING");
        }
        document.revisionMap.get(document.currentRevision).globalId.put(data.subType.globalIdTableEntry3FNDX.indexCopyToStart + i, compactId);
      }
    } else if (data.id == Constants.CanRevise.ObjectRevisionWithRefCountFNDX
        || data.id == Constants.CanRevise.ObjectRevisionWithRefCount2FNDX) {
      data.subType.objectRevisionWithRefCountFNDX.oid = deserializeCompactID(); // the oid

      if (data.id == Constants.CanRevise.ObjectRevisionWithRefCountFNDX) {
        int ref = deserializeLittleEndianChar();

        data.subType.objectRevisionWithRefCountFNDX.hasOidReferences = ref & 1;
        data.subType.objectRevisionWithRefCountFNDX.hasOsidReferences = ref & 2;
        data.subType.objectRevisionWithRefCountFNDX.cRef = (ref >> 2);
      } else {
        long ref = deserializeLittleEndianInt();

        data.subType.objectRevisionWithRefCountFNDX.hasOidReferences = ref & 1;
        data.subType.objectRevisionWithRefCountFNDX.hasOsidReferences = ref & 2;
        if ((ref >> 2) != 0) {
          throw new RuntimeException("Reserved non-zero");
        }
        data.subType.objectRevisionWithRefCountFNDX.cRef = deserializeLittleEndianInt();
      }
    } else if (data.id == Constants.RootObjectReference2FNDX) {
      data.subType.rootObjectReference.oidRoot = deserializeCompactID();

      idDesc = "oidRoot";
      data.gosid = data.subType.rootObjectReference.oidRoot.guid;
      data.subType.rootObjectReference.rootObjectReferenceBase.rootRole = deserializeLittleEndianInt();

      if (!quietParse) {
        LOG.info("{}Root role {}\n", getIndent(),
            data.subType.rootObjectReference.rootObjectReferenceBase.rootRole);
      }
    } else if (data.id == Constants.RootObjectReference3FND) {
      idDesc = "oidRoot";
      data.gosid = deserializeExtendedGUID();

      data.subType.rootObjectReference.rootObjectReferenceBase.rootRole = deserializeLittleEndianInt();

      if (!quietParse) {
        LOG.info("{}Root role {}\n", getIndent(),
            data.subType.rootObjectReference.rootObjectReferenceBase.rootRole);
      }
    } else if (data.id == Constants.RevisionRoleDeclarationFND
        || data.id == Constants.RevisionRoleAndContextDeclarationFND) {
      data.gosid = deserializeExtendedGUID();

      data.subType.revisionRoleDeclaration.revisionRole = deserializeLittleEndianInt();

      if (data.id == Constants.RevisionRoleAndContextDeclarationFND) {
        data.gctxid = deserializeExtendedGUID();

      }
      document.registerAdditionalRevisionRole(data.gosid,
          data.subType.revisionRoleDeclaration.revisionRole,
          data.gctxid);
      // FIXME: deal with ObjectDataEncryptionKey
    } else if (data.id == Constants.ObjectInfoDependencyOverridesFND) {
      OneNotePtr content = new OneNotePtr(this);
      if (!data.ref.equals(FileChunkReference.NIL)) {
        content.reposition(data.ref); // otherwise it's positioned right at this node
      }
      data.subType.objectInfoDependencyOverrides.data = content.deserializeObjectInfoDependencyOverrideData();
    } else if (data.id == Constants.FileDataStoreListReferenceFND) {
      // already processed this
    } else if (data.id == Constants.FileDataStoreObjectReferenceFND) {
      OneNotePtr fileDataStorePtr = new OneNotePtr(this);
      fileDataStorePtr.reposition(data.ref);

      data.subType.fileDataStoreObjectReference.ref = fileDataStorePtr.deserializeFileDataStoreObject();

    } else if (data.id == Constants.CanRevise.ObjectDeclarationWithRefCountFNDX
        || data.id == ObjectDeclarationWithRefCount2FNDX
        || data.id == Constants.CanRevise.ObjectDeclaration2RefCountFND
        || data.id == Constants.CanRevise.ObjectDeclaration2LargeRefCountFND
        || data.id == Constants.CanRevise.ReadOnlyObjectDeclaration2RefCountFND
        || data.id == Constants.CanRevise.ReadOnlyObjectDeclaration2LargeRefCountFND) {
      data.subType.objectDeclarationWithRefCount.body.file_data_store_reference =
          false;
      if (data.id == Constants.CanRevise.ObjectDeclarationWithRefCountFNDX
          || data.id == ObjectDeclarationWithRefCount2FNDX) {
        data.subType.objectDeclarationWithRefCount.body = deserializeObjectDeclarationWithRefCountBody();
      } else { // one of the other 4 that use the ObjectDeclaration2Body
        data.subType.objectDeclarationWithRefCount.body = deserializeObjectDeclaration2Body();
      }
      if (data.id == Constants.CanRevise.ObjectDeclarationWithRefCountFNDX
          || data.id == Constants.CanRevise.ObjectDeclaration2RefCountFND
          || data.id == Constants.CanRevise.ReadOnlyObjectDeclaration2RefCountFND) {
        long refCnt = deserializeLittleEndianChar();
        data.subType.objectDeclarationWithRefCount.cRef = refCnt;
      } else {
        data.subType.objectDeclarationWithRefCount.cRef = deserializeLittleEndianInt();
      }

      if (data.id == Constants.CanRevise.ReadOnlyObjectDeclaration2RefCountFND
          || data.id == Constants.CanRevise.ReadOnlyObjectDeclaration2LargeRefCountFND) {
        data.subType.objectDeclarationWithRefCount.readOnly.md5 = new byte[16];
        IOUtils.read(in, data.subType.objectDeclarationWithRefCount.readOnly.md5);
      }
      idDesc = "oid";
      postprocessObjectDeclarationContents(data, curPath);

      if (!quietParse) {
        LOG.info("{}  Ref Count JCID {}\n", getIndent(),
            data.subType.objectDeclarationWithRefCount.body.jcid);
      }
    } else if (data.id == Constants.CanRevise.ObjectDeclarationFileData3RefCountFND
        || data.id == Constants.CanRevise.ObjectDeclarationFileData3LargeRefCountFND) {
      data.subType.objectDeclarationWithRefCount.body.oid = deserializeCompactID();

      long jcid = deserializeLittleEndianInt();

      data.subType.objectDeclarationWithRefCount.body.jcid.loadFrom32BitIndex(jcid);

      if (data.id == Constants.CanRevise.ObjectDeclarationFileData3RefCountFND) {
        data.subType.objectDeclarationWithRefCount.cRef = deserializeLittleEndianChar();
      } else {
        data.subType.objectDeclarationWithRefCount.cRef = deserializeLittleEndianInt();
      }

      long cch = deserializeLittleEndianInt();

      if (cch > roomLeft()) { // not a valid guid
        throw new RuntimeException("SEGV");
      }

      // there is some dead code that looks like it has some purpose but
      // i'm not able to determine what. ignoring for now.
//        List<Long> u16data = Stream.generate(PropertyValue::new)
//              .limit((int)ch)
//              .collect(Collectors.toList())
//        int guidPrefix[] = { '<', 'i', 'f', 'n', 'd', 'f', '>' };
//        long prefix_len = guidPrefix.length / 4;
//        if (cch > prefix_len) {
//          // see https://github.com/dropbox/onenote-parser/blob/master/one_note_ptr.cpp#L1011
//        } else {
//          // external reference
//          String fmt = "";
//          for (int i = 0; i < u16data.size(); ++i) {
//            fmt += u16data.get(i);
//          }
//          System.err.format("Do not support external references %s\n", fmt);
//        }
    } else if (data.id == Constants.ObjectGroupListReferenceFND) {
      idDesc = "object_group_id";
      data.gosid = deserializeExtendedGUID(); // the object group id

      // the ref populates the FileNodeList children
    } else if (data.id == Constants.ObjectGroupStartFND) {
      idDesc = "object_group_id";
      data.gosid = deserializeExtendedGUID(); // the oid

    } else if (data.id == Constants.ObjectGroupEndFND) {
      // nothing to see here
    } else if (data.id == Constants.DataSignatureGroupDefinitionFND) {
      idDesc = "data_sig";
      data.gosid = deserializeExtendedGUID(); // the DataSignatureGroup

    } else if (data.id == Constants.RevisionManifestListReferenceFND) {
      document.revisionMap.putIfAbsent(document.currentRevision, new Revision());
      document.revisionMap.get(document.currentRevision).manifestList.add(curPath);
    }
    if (data.baseType == 2) {
      OneNotePtr subList = new OneNotePtr(this);
      subList.reposition(data.ref);
      subList.deserializeFileNodeList(data.children, curPath);
    }

    offset = backup.offset + data.size;
    end = backup.end;

    if (reserved != 1) {
      System.exit(1);
      throw new RuntimeException("RESERVED_NONZERO");
    }

    if (data.baseType == 1 && !(data.ref.equals(FileChunkReference.NIL))) {
      document.setAssocGuidToRef(data.gosid, data.ref);
      OneNotePtr content = new OneNotePtr(this);
      content.reposition(data.ref);
      if (data.hasGctxid()) {
        if (!quietParse) {
          LOG.info("{}gctxid {}\n", getIndent(), data.gctxid);
        }
      }
    }
    if (data.gosid.equals(ExtendedGUID.nil())) {
      if (!quietParse) {
        LOG.info("{}End Node {} ({}) - Offset={}, End={}\n", getIndent(), Constants.nameOf(data.id), (int) data.id, offset, end);
      }
    } else {
      if (!quietParse) {
        LOG.info("{}End Node {} ({}) {}:[{}] - Offset={}, End={}\n", getIndent(), Constants.nameOf(data.id), (int) data.id, idDesc,
            data.gosid, offset, end);
      }
    }
    return data;
  }

  private ObjectDeclarationWithRefCountBody deserializeObjectDeclarationWithRefCountBody() throws IOException {
    ObjectDeclarationWithRefCountBody data = new ObjectDeclarationWithRefCountBody();
    data.oid = deserializeCompactID();
    long jci_odcs_etc = deserializeLittleEndianInt();
    long reserved = deserializeLittleEndianShort();

    data.jcid.index = jci_odcs_etc & 0x3ffL;

    long must_be_zero = (jci_odcs_etc >> 10) & 0xf;
    long must_be_zeroA = ((jci_odcs_etc >> 14) & 0x3);
    data.fHasOidReferences = ((jci_odcs_etc >> 16) & 0x1) != 0;
    data.hasOsidReferences = ((jci_odcs_etc >> 17) & 0x1) != 0;
    if (jci_odcs_etc >> 18L > 0) {
      throw new RuntimeException("RESERVED_NONZERO");
    }
    if (reserved != 0 || must_be_zeroA != 0 || must_be_zero != 0) {
      throw new RuntimeException("RESERVED_NONZERO");
    }
    return data;
  }

  private ObjectDeclarationWithRefCountBody deserializeObjectDeclaration2Body() throws IOException {
    ObjectDeclarationWithRefCountBody data = new ObjectDeclarationWithRefCountBody();
    data.oid = deserializeCompactID();
    long jcid = deserializeLittleEndianInt();
    data.jcid.loadFrom32BitIndex(jcid);
    long hasRefs = deserializeLittleEndianChar();
    data.fHasOidReferences = (hasRefs & 0x1) != 0;
    data.hasOsidReferences = (hasRefs & 0x2) != 0;
    return data;
  }

  private FileDataStoreObject deserializeFileDataStoreObject() throws IOException {
    FileDataStoreObject data = new FileDataStoreObject();
    data.header = deserializeGUID();
    long len = deserializeLittleEndianLong();
    data.reserved0 = deserializeLittleEndianInt();
    data.reserved = deserializeLittleEndianLong();
    if (offset + len + 16 > end) {
      throw new RuntimeException("SEGV error");
    }
    if (data.reserved0 > 0 || data.reserved > 0) {
      throw new RuntimeException("SEGV error");
    }
    data.fileData.stp = offset;
    data.fileData.cb = len;
    offset += len;
    while ((offset & 0x7) > 0) {
      ++offset;
    }
    data.footer = deserializeGUID();
    GUID desired_footer = new GUID(new int[16]);
    int i = 0;
    desired_footer.guid[i++] = 0x22;
    desired_footer.guid[i++] = 0xa7;
    desired_footer.guid[i++] = 0xfb;
    desired_footer.guid[i++] = 0x71;

    desired_footer.guid[i++] = 0x79;
    desired_footer.guid[i++] = 0x0f;

    desired_footer.guid[i++] = 0x0b;
    desired_footer.guid[i++] = 0x4a;

    desired_footer.guid[i++] = 0xbb;
    desired_footer.guid[i++] = 0x13;
    desired_footer.guid[i++] = 0x89;
    desired_footer.guid[i++] = 0x92;
    desired_footer.guid[i++] = 0x56;
    desired_footer.guid[i++] = 0x42;
    desired_footer.guid[i++] = 0x6b;
    desired_footer.guid[i++] = 0x24;
    if (data.footer.equals(desired_footer)) {
      throw new RuntimeException("Invalid Constant");
    }
    return data;
  }

  private ObjectInfoDependencyOverrideData deserializeObjectInfoDependencyOverrideData() throws IOException {
    ObjectInfoDependencyOverrideData objectInfoDependencyOverrideData = new ObjectInfoDependencyOverrideData();
    long num_8bit_overrides = deserializeLittleEndianInt();
    long num_32bit_overrides = deserializeLittleEndianInt();
    long crc = deserializeLittleEndianInt();
    for (int i = 0; i < num_8bit_overrides; ++i) {
      int local = deserializeLittleEndianChar();
      objectInfoDependencyOverrideData.overrides1.add(local);
    }
    for (int i = 0; i < num_32bit_overrides; ++i) {
      long local = deserializeLittleEndianInt();
      objectInfoDependencyOverrideData.overrides2.add(local);
    }
    return objectInfoDependencyOverrideData;
  }

  private CompactID deserializeCompactID() throws IOException {
    CompactID compactID = new CompactID();
    compactID.n = deserializeLittleEndianChar();
    compactID.guidIndex = deserializeInt24();
    compactID.guid = ExtendedGUID.nil();
    compactID.guid.n = compactID.n;
    long index = compactID.guidIndex;
    Map<Long, GUID> globalIdMap = document.revisionMap.get(document.currentRevision).globalId;
    GUID guid = globalIdMap.get(index);
    if (guid != null) {
      compactID.guid.guid = guid;
    } else {
      throw new RuntimeException("COMPACT ID MISSING");
    }
    return compactID;
  }
  
  private long deserializeInt24() throws IOException {
    int b1 = deserializeLittleEndianChar();
    int b2 = deserializeLittleEndianChar();
    int b3 = deserializeLittleEndianChar();

    return new Int24(b1, b2, b3).value();
  }

  private ExtendedGUID deserializeExtendedGUID() throws IOException {
    GUID guid = deserializeGUID();
    long n = deserializeLittleEndianInt();
    return new ExtendedGUID(guid, n);
  }

  private FileChunkReference deserializeVarFileChunkReference(long stpFormat, long cbFormat) throws IOException {
    FileChunkReference data = new FileChunkReference(0, 0);
    long local8;
    long local16;
    long local32;
    switch (new Long(stpFormat).intValue()) {
      case 0: // 8 bytes, uncompressed
        data.stp = deserializeLittleEndianLong();
        break;
      case 1:
        local32 = deserializeLittleEndianInt();
        data.stp = local32;
        break;
      case 2:
        local16 = deserializeLittleEndianShort();
        data.stp = local16;
        data.stp <<= 3;
        break;
      case 3:
        local32 = deserializeLittleEndianInt();
        data.stp = local32;
        data.stp <<= 3;
        break;
      default:
        throw new RuntimeException("Unknown enum " + stpFormat);
    }
    switch (new Long(cbFormat).intValue()) {
      case 0: // 4 bytes, uncompressed
        local32 = deserializeLittleEndianInt();
        data.cb = local32;
        break;
      case 1: // 8 bytes, uncompressed;
        data.cb = deserializeLittleEndianLong();
        break;
      case 2: // 1 byte, compressed
        local8 = deserializeLittleEndianChar();
        data.cb = local8;
        data.cb <<= 3;
        break;
      case 3: // 2 bytes, compressed
        local16 = deserializeLittleEndianShort();
        data.cb = local16;
        data.cb <<= 3;

        break;
      default:
        throw new RuntimeException("Unknown enum " + cbFormat);
    }
    return data;
  }

  FileNodeListHeader deserializeFileNodeListHeader() throws IOException {
    long uintMagic = deserializeLittleEndianLong();
    long fileNodeListId = deserializeLittleEndianInt();
    long nFragmentSequence = deserializeLittleEndianInt();

    return new FileNodeListHeader(uintMagic, fileNodeListId, nFragmentSequence);
  }

  private void postprocessObjectDeclarationContents(FileNode data, FileNodePtr curPtr) throws IOException {
    data.gosid = data.subType.objectDeclarationWithRefCount.body.oid.guid;
    document.guidToObject.put(data.gosid, curPtr);
    if (data.subType.objectDeclarationWithRefCount.body.jcid.isPropertySet) {
      OneNotePtr objectSpacePropSetPtr = new OneNotePtr(this);
      objectSpacePropSetPtr.reposition(data.ref);
      data.subType.objectDeclarationWithRefCount.objectRef = objectSpacePropSetPtr.deserializeObjectSpaceObjectPropSet();
      ObjectStreamCounters streamCounters = new ObjectStreamCounters();
      data.propertySet = objectSpacePropSetPtr.deserializePropertySet(streamCounters, data.subType.objectDeclarationWithRefCount.objectRef);
    } else {
      if (!data.subType.objectDeclarationWithRefCount.body.jcid.isFileData) {
        throw new RuntimeException("INVALID_CONSTANT");
      }
      // this is FileData
      data.isFileData = true;
      OneNotePtr content = new OneNotePtr(this);
      content.reposition(data.ref);
      if (!quietParse) {
        LOG.info("{}Raw:", getIndent());
        content.dumpHex();
        LOG.info("\n");
      }
    }
  }

  private PropertySet deserializePropertySet(ObjectStreamCounters counters, ObjectSpaceObjectPropSet streams) throws IOException {
    PropertySet data = new PropertySet();
    long count = deserializeLittleEndianShort();
    data.rgPridsData = Stream.generate(PropertyValue::new)
        .limit((int)count)
        .collect(Collectors.toList());
    for (int i = 0; i < count; ++i) {
      data.rgPridsData.get(i).propertyID = deserializePropertyID();
      if (!quietParse) {
        LOG.info("{}Property {}\n", getIndent(), data.rgPridsData.get(i).propertyID);
      }
    }
    if (!quietParse) {
      LOG.info("{}{} elements in property set:", getIndent(), count);
    }
    for (int i = 0; i < count; ++i) {
      data.rgPridsData.set(i, deserializePropertyValueFromPropertyID(
          data.rgPridsData.get(i).propertyID, streams, counters));
    }
    if (!quietParse) {
      System.out.println();
    }
    return data;

  }

  private PropertyValue deserializePropertyValueFromPropertyID(PropertyID propertyID, ObjectSpaceObjectPropSet streams, ObjectStreamCounters counters) throws IOException {
    PropertyValue data = new PropertyValue();
    data.propertyID = propertyID;
    char val8;
    long val16;
    long val32 = 0;
    long val64;
    if (!quietParse) {
      long id = propertyID.id;
		  String retval = Properties.nameOf(id);
      LOG.info("\n{}<{}", getIndent(), retval);
    }
    ++indentLevel;
    try {
      switch ((int)propertyID.type) {
        case 0x1:
          if (!quietParse) {
            LOG.info(" [] ");
          }
          return data;
        case 0x2:
          if (!quietParse) {
            LOG.info(" PropertyID bool({})", propertyID.inline_bool);
          }
          data.scalar = propertyID.inline_bool ? 1 : 0;
          return data;
        case 0x3:
          val8 = deserializeLittleEndianChar();
          data.scalar = val8;
          if (!quietParse) {
            LOG.info(" PropertyID byte({})", data.scalar);
          }
          break;
        case 0x4:
          val16 = deserializeLittleEndianShort();
          data.scalar = val16;
          if (!quietParse) {
            LOG.info(" uint16 PropertyID short({})", data.scalar);
          }
          break;
        case 0x5:
          val32 = deserializeLittleEndianInt();
          data.scalar = val32;
          if (!quietParse) {
            LOG.info(" PropertyID int({})", data.scalar);
          }
          break;
        case 0x6:
          val64 = deserializeLittleEndianLong();
          data.scalar = val64;
          if (!quietParse) {
            LOG.info(" PropertyID long({})", data.scalar);
          }
          break;
        case 0x7:
          val32 = deserializeLittleEndianInt();
          if (!quietParse) {
            LOG.info(" raw data: ({})[", val32);
          }
        {
          data.rawData.stp = offset;
          data.rawData.cb = 0;
          if (offset + val32 > end) {
            data.rawData.cb = end - offset;
            offset = end;
            throw new RuntimeException("SEGV");
          }
          data.rawData.cb = val32;
          offset += val32;
          {
            OneNotePtr content = new OneNotePtr(this);
            content.reposition(data.rawData);
            if (!quietParse) {
              content.dumpHex();
            }
          }
        }
        if (!quietParse) {
          LOG.info("]");
        }
        break;
        case 0x9:
        case 0xb:
        case 0xd:
          val32 = deserializeLittleEndianInt();
          // fallthrough
        case 0x8:
        case 0xa:
        case 0xc:
          if (propertyID.type == 0x8 || propertyID.type == 0xa
              || propertyID.type == 0xc) {
            val32 = 1;
          }
        {
          List<CompactID> stream = streams.contextIDs.data;
          String xtype = "contextID";
          long s_count = counters.context_ids_count;
          if (propertyID.type == 0x8 || propertyID.type == 0x9) {
            stream = streams.oids.data;
            s_count = counters.oids_count;
            xtype = "OIDs";
          }
          if (propertyID.type == 0xa || propertyID.type == 0xb) {
            stream = streams.osids.data;
            s_count = counters.osids_count;
            xtype = "OSIDS";
          }
          for (int i = 0; i < val32; ++i, ++s_count) {
            int index = (int)s_count;
            if (index < stream.size()) {
              data.compactIDs.add(stream.get(index));
              if (!quietParse) {
                LOG.info(" {}[{}]", xtype,
                    data.compactIDs.get(data.compactIDs.size()-1));
              }
            } else {
              throw new RuntimeException("SEGV");
            }
          }
        }
        break;
        case 0x10:
          val32 = deserializeLittleEndianInt();
        {
          PropertyID prop_id = deserializePropertyID();
          if (!quietParse) {
            LOG.info(" UnifiedSubPropertySet {} {}", val32, prop_id);
          }
          data.propertySet.rgPridsData = Stream.generate(PropertyValue::new)
              .limit((int)val32)
              .collect(Collectors.toList());
          for (int i = 0; i < val32; ++i) {
            try {
              data.propertySet.rgPridsData.set(i, deserializePropertyValueFromPropertyID(prop_id, streams, counters));
            } catch (IOException e) {
              return data;
            }
          }
        }
        break;
        case 0x11:
          if (!quietParse) {
            LOG.info(" SubPropertySet");
          }
          data.propertySet = deserializePropertySet(counters, streams);
          break;
        default:
          throw new RuntimeException("Invalid propertyID.type: " + propertyID.type);
      }
      if (!quietParse) {
        LOG.info(">");
      }
      return data;
    } finally {
      --indentLevel;
    }
  }

  private PropertyID deserializePropertyID() throws IOException {
    PropertyID data = new PropertyID();

    long pid = deserializeLittleEndianInt();
    data.id = (pid & 0x3ffffff);
    data.type = pid >> 26 & 0x1f;
    if (data.type == 0x2) {
      data.inline_bool = ((pid >> 31) & 0x1) > 0; // set the bool value from header
    } else {
      if (((pid >> 31) & 0x1) > 0) {
        throw new RuntimeException("Reserved non-zero");
      }
    }
    return data;
  }

  private ObjectSpaceObjectPropSet deserializeObjectSpaceObjectPropSet() throws IOException {
    ObjectSpaceObjectPropSet data = new ObjectSpaceObjectPropSet();
    data.osids.extendedStreamsPresent = 0;
    data.osids.osidsStreamNotPresent = 1;
    data.contextIDs.extendedStreamsPresent = 0;
    data.contextIDs.osidsStreamNotPresent = 0;
    //uint64_t cur_offset = offset;
    //LOG.info("starting deserialization %lx(%lx) / %lx\n", offset, offset - cur_offset, end);
    data.oids = deserializeObjectSpaceObjectStreamOfOIDsOSIDsOrContextIDs();
    //LOG.info("mid deserialization %lx(%lx) / %lx\n", offset, offset - cur_offset, end);
    if (data.oids.osidsStreamNotPresent == 0) {
      data.osids = deserializeObjectSpaceObjectStreamOfOIDsOSIDsOrContextIDs();
    }
    //LOG.info("lat deserialization %lx(%lx) / %lx\n", offset, offset - cur_offset, end);
    if (data.oids.extendedStreamsPresent != 0) {
      data.contextIDs = deserializeObjectSpaceObjectStreamOfOIDsOSIDsOrContextIDs();
    }
    return data;
  }

  private ObjectSpaceObjectStreamOfOIDsOSIDsOrContextIDs deserializeObjectSpaceObjectStreamOfOIDsOSIDsOrContextIDs() throws IOException {
    ObjectSpaceObjectStreamOfOIDsOSIDsOrContextIDs data = new ObjectSpaceObjectStreamOfOIDsOSIDsOrContextIDs();
    long header = deserializeLittleEndianInt();
    data.count = header & 0xffffff;
    data.osidsStreamNotPresent = ((header >> 31) & 0x1);
    data.extendedStreamsPresent = ((header >> 30) & 0x1);
    if (!quietParse) {
      LOG.info(
          "{}Deserialized Stream Header count: {} OsidsNotPresent {} Extended {}\n",
          getIndent(), data.count,
          data.osidsStreamNotPresent,
          data.extendedStreamsPresent);
    }
    for (int i = 0; i < data.count; ++i) {
      CompactID cid;
      cid = deserializeCompactID();
      data.data.add(cid);
    }
    return data;
  }

  long roomLeft() {
    return end - offset;
  }

  public void dumpHex() {
    // TODO LOG.info the hex of data in this pointer
  }
}
