package org.apache.tika.onenote;

import java.io.Serializable;

public class Header implements Serializable {

  GUID guidFileType;
  GUID guidFile;
  GUID guidLegacyFileVersion;
  GUID guidFileFormat;
  long ffvLastCode;
  long ffvNewestCode;
  long ffvOldestCode;
  long ffvOldestReader;
  FileChunkReference fcrLegacyFreeChunkList;
  FileChunkReference fcrLegacyTransactionLog;
  long cTransactionsInLog;
  long cbLegacyExpectedFileLength;
  long rgbPlaceholder;
  FileChunkReference fcrLegacyFileNodeListRoot;
  long cbLegacyFreeSpaceInFreeChunkList;
  long ignoredZeroA;
  long ignoredZeroB;
  long ignoredZeroC;
  long ignoredZeroD;
  GUID guidAncestor;
  long crcName;
  FileChunkReference fcrHashedChunkList;
  FileChunkReference fcrTransactionLog;
  FileChunkReference fcrFileNodeListRoot;
  FileChunkReference fcrFreeChunkList;
  long cbExpectedFileLength;
  long cbFreeSpaceInFreeChunkList;
  GUID guidFileVersion;
  long nFileVersionGeneration;
  GUID guidDenyReadFileVersion;
  long grfDebugLogFlags;
  FileChunkReference fcrDebugLogA;
  FileChunkReference fcrDebugLogB;
  long buildInfoA;
  long buildInfoB;
  long buildInfoC;
  long buildInfoD;
  byte [] reserved;

  public GUID getGuidFileType() {
    return guidFileType;
  }

  public Header setGuidFileType(GUID guidFileType) {
    this.guidFileType = guidFileType;
    return this;
  }

  public GUID getGuidFile() {
    return guidFile;
  }

  public Header setGuidFile(GUID guidFile) {
    this.guidFile = guidFile;
    return this;
  }

  public GUID getGuidLegacyFileVersion() {
    return guidLegacyFileVersion;
  }

  public Header setGuidLegacyFileVersion(GUID guidLegacyFileVersion) {
    this.guidLegacyFileVersion = guidLegacyFileVersion;
    return this;
  }

  public GUID getGuidFileFormat() {
    return guidFileFormat;
  }

  public Header setGuidFileFormat(GUID guidFileFormat) {
    this.guidFileFormat = guidFileFormat;
    return this;
  }

  public long getFfvLastCode() {
    return ffvLastCode;
  }

  public Header setFfvLastCode(long ffvLastCode) {
    this.ffvLastCode = ffvLastCode;
    return this;
  }

  public long getFfvNewestCode() {
    return ffvNewestCode;
  }

  public Header setFfvNewestCode(long ffvNewestCode) {
    this.ffvNewestCode = ffvNewestCode;
    return this;
  }

  public long getFfvOldestCode() {
    return ffvOldestCode;
  }

  public Header setFfvOldestCode(long ffvOldestCode) {
    this.ffvOldestCode = ffvOldestCode;
    return this;
  }

  public long getFfvOldestReader() {
    return ffvOldestReader;
  }

  public Header setFfvOldestReader(long ffvOldestReader) {
    this.ffvOldestReader = ffvOldestReader;
    return this;
  }

  public FileChunkReference getFcrLegacyFreeChunkList() {
    return fcrLegacyFreeChunkList;
  }

  public Header setFcrLegacyFreeChunkList(FileChunkReference fcrLegacyFreeChunkList) {
    this.fcrLegacyFreeChunkList = fcrLegacyFreeChunkList;
    return this;
  }

  public FileChunkReference getFcrLegacyTransactionLog() {
    return fcrLegacyTransactionLog;
  }

  public Header setFcrLegacyTransactionLog(FileChunkReference fcrLegacyTransactionLog) {
    this.fcrLegacyTransactionLog = fcrLegacyTransactionLog;
    return this;
  }

  public long getcTransactionsInLog() {
    return cTransactionsInLog;
  }

  public Header setcTransactionsInLog(long cTransactionsInLog) {
    this.cTransactionsInLog = cTransactionsInLog;
    return this;
  }

  public long getCbLegacyExpectedFileLength() {
    return cbLegacyExpectedFileLength;
  }

  public Header setCbLegacyExpectedFileLength(long cbLegacyExpectedFileLength) {
    this.cbLegacyExpectedFileLength = cbLegacyExpectedFileLength;
    return this;
  }

  public long getRgbPlaceholder() {
    return rgbPlaceholder;
  }

  public Header setRgbPlaceholder(long rgbPlaceholder) {
    this.rgbPlaceholder = rgbPlaceholder;
    return this;
  }

  public FileChunkReference getFcrLegacyFileNodeListRoot() {
    return fcrLegacyFileNodeListRoot;
  }

  public Header setFcrLegacyFileNodeListRoot(FileChunkReference fcrLegacyFileNodeListRoot) {
    this.fcrLegacyFileNodeListRoot = fcrLegacyFileNodeListRoot;
    return this;
  }

  public long getCbLegacyFreeSpaceInFreeChunkList() {
    return cbLegacyFreeSpaceInFreeChunkList;
  }

  public Header setCbLegacyFreeSpaceInFreeChunkList(long cbLegacyFreeSpaceInFreeChunkList) {
    this.cbLegacyFreeSpaceInFreeChunkList = cbLegacyFreeSpaceInFreeChunkList;
    return this;
  }

  public long getIgnoredZeroA() {
    return ignoredZeroA;
  }

  public Header setIgnoredZeroA(long ignoredZeroA) {
    this.ignoredZeroA = ignoredZeroA;
    return this;
  }

  public long getIgnoredZeroB() {
    return ignoredZeroB;
  }

  public Header setIgnoredZeroB(long ignoredZeroB) {
    this.ignoredZeroB = ignoredZeroB;
    return this;
  }

  public long getIgnoredZeroC() {
    return ignoredZeroC;
  }

  public Header setIgnoredZeroC(long ignoredZeroC) {
    this.ignoredZeroC = ignoredZeroC;
    return this;
  }

  public long getIgnoredZeroD() {
    return ignoredZeroD;
  }

  public Header setIgnoredZeroD(long ignoredZeroD) {
    this.ignoredZeroD = ignoredZeroD;
    return this;
  }

  public GUID getGuidAncestor() {
    return guidAncestor;
  }

  public Header setGuidAncestor(GUID guidAncestor) {
    this.guidAncestor = guidAncestor;
    return this;
  }

  public long getCrcName() {
    return crcName;
  }

  public Header setCrcName(long crcName) {
    this.crcName = crcName;
    return this;
  }

  public FileChunkReference getFcrHashedChunkList() {
    return fcrHashedChunkList;
  }

  public Header setFcrHashedChunkList(FileChunkReference fcrHashedChunkList) {
    this.fcrHashedChunkList = fcrHashedChunkList;
    return this;
  }

  public FileChunkReference getFcrTransactionLog() {
    return fcrTransactionLog;
  }

  public Header setFcrTransactionLog(FileChunkReference fcrTransactionLog) {
    this.fcrTransactionLog = fcrTransactionLog;
    return this;
  }

  public FileChunkReference getFcrFileNodeListRoot() {
    return fcrFileNodeListRoot;
  }

  public Header setFcrFileNodeListRoot(FileChunkReference fcrFileNodeListRoot) {
    this.fcrFileNodeListRoot = fcrFileNodeListRoot;
    return this;
  }

  public FileChunkReference getFcrFreeChunkList() {
    return fcrFreeChunkList;
  }

  public Header setFcrFreeChunkList(FileChunkReference fcrFreeChunkList) {
    this.fcrFreeChunkList = fcrFreeChunkList;
    return this;
  }

  public long getCbExpectedFileLength() {
    return cbExpectedFileLength;
  }

  public Header setCbExpectedFileLength(long cbExpectedFileLength) {
    this.cbExpectedFileLength = cbExpectedFileLength;
    return this;
  }

  public long getCbFreeSpaceInFreeChunkList() {
    return cbFreeSpaceInFreeChunkList;
  }

  public Header setCbFreeSpaceInFreeChunkList(long cbFreeSpaceInFreeChunkList) {
    this.cbFreeSpaceInFreeChunkList = cbFreeSpaceInFreeChunkList;
    return this;
  }

  public GUID getGuidFileVersion() {
    return guidFileVersion;
  }

  public Header setGuidFileVersion(GUID guidFileVersion) {
    this.guidFileVersion = guidFileVersion;
    return this;
  }

  public long getnFileVersionGeneration() {
    return nFileVersionGeneration;
  }

  public Header setnFileVersionGeneration(long nFileVersionGeneration) {
    this.nFileVersionGeneration = nFileVersionGeneration;
    return this;
  }

  public GUID getGuidDenyReadFileVersion() {
    return guidDenyReadFileVersion;
  }

  public Header setGuidDenyReadFileVersion(GUID guidDenyReadFileVersion) {
    this.guidDenyReadFileVersion = guidDenyReadFileVersion;
    return this;
  }

  public long getGrfDebugLogFlags() {
    return grfDebugLogFlags;
  }

  public Header setGrfDebugLogFlags(long grfDebugLogFlags) {
    this.grfDebugLogFlags = grfDebugLogFlags;
    return this;
  }

  public FileChunkReference getFcrDebugLogA() {
    return fcrDebugLogA;
  }

  public Header setFcrDebugLogA(FileChunkReference fcrDebugLogA) {
    this.fcrDebugLogA = fcrDebugLogA;
    return this;
  }

  public FileChunkReference getFcrDebugLogB() {
    return fcrDebugLogB;
  }

  public Header setFcrDebugLogB(FileChunkReference fcrDebugLogB) {
    this.fcrDebugLogB = fcrDebugLogB;
    return this;
  }

  public long getBuildInfoA() {
    return buildInfoA;
  }

  public Header setBuildInfoA(long buildInfoA) {
    this.buildInfoA = buildInfoA;
    return this;
  }

  public long getBuildInfoB() {
    return buildInfoB;
  }

  public Header setBuildInfoB(long buildInfoB) {
    this.buildInfoB = buildInfoB;
    return this;
  }

  public long getBuildInfoC() {
    return buildInfoC;
  }

  public Header setBuildInfoC(long buildInfoC) {
    this.buildInfoC = buildInfoC;
    return this;
  }

  public long getBuildInfoD() {
    return buildInfoD;
  }

  public Header setBuildInfoD(long buildInfoD) {
    this.buildInfoD = buildInfoD;
    return this;
  }

  public byte[] getReserved() {
    return reserved;
  }

  public Header setReserved(byte[] reserved) {
    this.reserved = reserved;
    return this;
  }

  @Override
  public String toString() {
    return "Header{" +
      "guidFileType=" + guidFileType +
      ", guidFile=" + guidFile +
      ", guidLegacyFileVersion=" + guidLegacyFileVersion +
      ", guidFileFormat=" + guidFileFormat +
      ", ffvLastCode=" + ffvLastCode +
      ", ffvNewestCode=" + ffvNewestCode +
      ", ffvOldestCode=" + ffvOldestCode +
      ", ffvOldestReader=" + ffvOldestReader +
      ", fcrLegacyFreeChunkList=" + fcrLegacyFreeChunkList +
      ", fcrLegacyTransactionLog=" + fcrLegacyTransactionLog +
      ", cTransactionsInLog=" + cTransactionsInLog +
      ", cbLegacyExpectedFileLength=" + cbLegacyExpectedFileLength +
      ", rgbPlaceholder=" + rgbPlaceholder +
      ", fcrLegacyFileNodeListRoot=" + fcrLegacyFileNodeListRoot +
      ", cbLegacyFreeSpaceInFreeChunkList=" + cbLegacyFreeSpaceInFreeChunkList +
      ", ignoredZeroA=" + ignoredZeroA +
      ", ignoredZeroB=" + ignoredZeroB +
      ", ignoredZeroC=" + ignoredZeroC +
      ", ignoredZeroD=" + ignoredZeroD +
      ", guidAncestor=" + guidAncestor +
      ", crcName=" + crcName +
      ", fcrHashedChunkList=" + fcrHashedChunkList +
      ", fcrTransactionLog=" + fcrTransactionLog +
      ", fcrFileNodeListRoot=" + fcrFileNodeListRoot +
      ", fcrFreeChunkList=" + fcrFreeChunkList +
      ", cbExpectedFileLength=" + cbExpectedFileLength +
      ", cbFreeSpaceInFreeChunkList=" + cbFreeSpaceInFreeChunkList +
      ", guidFileVersion=" + guidFileVersion +
      ", nFileVersionGeneration=" + nFileVersionGeneration +
      ", guidDenyReadFileVersion=" + guidDenyReadFileVersion +
      ", grfDebugLogFlags=" + grfDebugLogFlags +
      ", fcrDebugLogA=" + fcrDebugLogA +
      ", fcrDebugLogB=" + fcrDebugLogB +
      ", buildInfoA=" + buildInfoA +
      ", buildInfoB=" + buildInfoB +
      ", buildInfoC=" + buildInfoC +
      ", buildInfoD=" + buildInfoD +
      '}';
  }
}
