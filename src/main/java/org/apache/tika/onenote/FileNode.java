package org.apache.tika.onenote;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileNode {
  long id; //10 bits
  long size; // 13 bits
  //base_type 0, ignore stp&cb 1, reference to data 2) reference to FileNodeList
  long baseType;
  // for ObjectSpaceManifestRoot
  // for ObjectSpaceManifestStart
  // for ObjectSpaceManifestList
  // for RevisionManifestListStart
  // ObjectGroupStartFND
  // ObjectGroupID for ObjectGroupListReferenceFND
  // RID for RevisionManifestStart4FND
  // DataSignatureGroup for RevisionManifestEndFND
  ExtendedGUID gosid;

  // only present for RevisionManfiest7FND and RevisionRoleAndContextDeclaration
  ExtendedGUID gctxid;
  GUID fileDataStoreReference;
  FileChunkReference ref;
  PropertySet propertySet;
  boolean isFileData;
  List<FileNode> children = new ArrayList<>(); // for ObjectGroupListReference

  FileNodeUnion subType = new FileNodeUnion();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileNode fileNode = (FileNode) o;
    return id == fileNode.id &&
        size == fileNode.size &&
        baseType == fileNode.baseType &&
        isFileData == fileNode.isFileData &&
        Objects.equals(gosid, fileNode.gosid) &&
        Objects.equals(gctxid, fileNode.gctxid) &&
        Objects.equals(fileDataStoreReference, fileNode.fileDataStoreReference) &&
        Objects.equals(ref, fileNode.ref) &&
        Objects.equals(propertySet, fileNode.propertySet) &&
        Objects.equals(children, fileNode.children) &&
        Objects.equals(subType, fileNode.subType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, size, baseType, gosid, gctxid, fileDataStoreReference, ref, propertySet, isFileData, children, subType);
  }

  public boolean hasGctxid() {
    return id == Constants.RevisionRoleAndContextDeclarationFND
        || id == Constants.RevisionManifestStart7FND;
  }

  public long getId() {
    return id;
  }

  public FileNode setId(long id) {
    this.id = id;
    return this;
  }

  public long getSize() {
    return size;
  }

  public FileNode setSize(long size) {
    this.size = size;
    return this;
  }

  public long getBaseType() {
    return baseType;
  }

  public FileNode setBaseType(long baseType) {
    this.baseType = baseType;
    return this;
  }

  public ExtendedGUID getGosid() {
    return gosid;
  }

  public FileNode setGosid(ExtendedGUID gosid) {
    this.gosid = gosid;
    return this;
  }

  public ExtendedGUID getGctxid() {
    return gctxid;
  }

  public FileNode setGctxid(ExtendedGUID gctxid) {
    this.gctxid = gctxid;
    return this;
  }

  public GUID getFileDataStoreReference() {
    return fileDataStoreReference;
  }

  public FileNode setFileDataStoreReference(GUID fileDataStoreReference) {
    this.fileDataStoreReference = fileDataStoreReference;
    return this;
  }

  public FileChunkReference getRef() {
    return ref;
  }

  public FileNode setRef(FileChunkReference ref) {
    this.ref = ref;
    return this;
  }

  public PropertySet getPropertySet() {
    return propertySet;
  }

  public FileNode setPropertySet(PropertySet propertySet) {
    this.propertySet = propertySet;
    return this;
  }

  public boolean isFileData() {
    return isFileData;
  }

  public FileNode setFileData(boolean fileData) {
    isFileData = fileData;
    return this;
  }

  public List<FileNode> getChildren() {
    return children;
  }

  public FileNode setChildren(List<FileNode> children) {
    this.children = children;
    return this;
  }

  public FileNodeUnion getSubType() {
    return subType;
  }

  public FileNode setSubType(FileNodeUnion subType) {
    this.subType = subType;
    return this;
  }
}
