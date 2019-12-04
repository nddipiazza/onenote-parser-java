package org.apache.tika.onenote;

import com.google.common.collect.Sets;

import java.util.Set;

public class OneNoteTreeWalkerOptions {
  private boolean crawlAllRevisions = false;
  private boolean onlyLatestRevision = true;
  private Set<OneNotePropertyEnum> utf16PropertiesToPrint = Sets.newHashSet(OneNotePropertyEnum.ImageFilename,
      OneNotePropertyEnum.Author,
      OneNotePropertyEnum.CachedTitleString);

  public boolean isCrawlAllRevisions() {
    return crawlAllRevisions;
  }

  public OneNoteTreeWalkerOptions setCrawlAllRevisions(boolean crawlAllRevisions) {
    this.crawlAllRevisions = crawlAllRevisions;
    return this;
  }

  public boolean isOnlyLatestRevision() {
    return onlyLatestRevision;
  }

  public OneNoteTreeWalkerOptions setOnlyLatestRevision(boolean onlyLatestRevision) {
    this.onlyLatestRevision = onlyLatestRevision;
    return this;
  }

  public Set<OneNotePropertyEnum> getUtf16PropertiesToPrint() {
    return utf16PropertiesToPrint;
  }

  public OneNoteTreeWalkerOptions setUtf16PropertiesToPrint(Set<OneNotePropertyEnum> utf16PropertiesToPrint) {
    this.utf16PropertiesToPrint = utf16PropertiesToPrint;
    return this;
  }
}
