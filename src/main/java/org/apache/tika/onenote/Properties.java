package org.apache.tika.onenote;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Properties {

  private Properties() {
    // no-op
  }

  public static final long longLayoutTightLayout = 0x08001C00;
  public static final long longPageWidth = 0x14001C01;
  public static final long longPageHeight = 0x14001C02;
  public static final long longOutlineElementChildLevel = 0x0C001C03;
  public static final long longBold = 0x08001C04;
  public static final long longItalic = 0x08001C05;
  public static final long longUnderline = 0x08001C06;
  public static final long longStrikethrough = 0x08001C07;
  public static final long longSuperscript = 0x08001C08;
  public static final long longSubscript = 0x08001C09;
  public static final long longFont = 0x1C001C0A;
  public static final long longFontSize = 0x10001C0B;
  public static final long longFontColor = 0x14001C0C;
  public static final long longHighlight = 0x14001C0D;
  public static final long longRgOutlineIndentDistance = 0x1C001C12;
  public static final long longBodyTextAlignment = 0x0C001C13;
  public static final long longOffsetFromParentHoriz = 0x14001C14;
  public static final long longOffsetFromParentVert = 0x14001C15;
  public static final long longNumberListFormat = 0x1C001C1A;
  public static final long longLayoutMaxWidth = 0x14001C1B;
  public static final long longLayoutMaxHeight = 0x14001C1C;
  public static final long longContentChildNodesOfOutlineElement = 0x24001C1F;
  public static final long longElementChildNodesOfSection = 0x24001C20;
  public static final long longEnableHistory = 0x08001E1E;
  public static final long longRichEditTextUnicode = 0x1C001C22;
  public static final long longListNodes = 0x24001C26;
  public static final long longNotebookManagementEntityGuid = 0x1C001C30;
  public static final long longOutlineElementRTL = 0x08001C34;
  public static final long longLanguageID = 0x14001C3B;
  public static final long longLayoutAlignmentInParent = 0x14001C3E;
  public static final long longPictureContainer = 0x20001C3F;
  public static final long longPageMarginTop = 0x14001C4C;
  public static final long longPageMarginBottom = 0x14001C4D;
  public static final long longPageMarginLeft = 0x14001C4E;
  public static final long longPageMarginRight = 0x14001C4F;
  public static final long longListFont = 0x1C001C52;
  public static final long longTopologyCreationTimeStamp = 0x18001C65;
  public static final long longLayoutAlignmentSelf = 0x14001C84;
  public static final long longIsTitleTime = 0x08001C87;
  public static final long longIsBoilerText = 0x08001C88;
  public static final long longPageSize = 0x14001C8B;
  public static final long longPortraitPage = 0x08001C8E;
  public static final long longEnforceOutlineStructure = 0x08001C91;
  public static final long longEditRootRTL = 0x08001C92;
  public static final long longCannotBeSelected = 0x08001CB2;
  public static final long longIsTitleText = 0x08001CB4;
  public static final long longIsTitleDate = 0x08001CB5;
  public static final long longListRestart = 0x14001CB7;
  public static final long longIsLayoutSizeSetByUser = 0x08001CBD;
  public static final long longListSpacingMu = 0x14001CCB;
  public static final long longLayoutOutlineReservedWidth = 0x14001CDB;
  public static final long longLayoutResolveChildCollisions = 0x08001CDC;
  public static final long longIsReadOnly = 0x08001CDE;
  public static final long longLayoutMinimumOutlineWidth = 0x14001CEC;
  public static final long longLayoutCollisionPriority = 0x14001CF1;
  public static final long longCachedTitleString = 0x1C001CF3;
  public static final long longDescendantsCannotBeMoved = 0x08001CF9;
  public static final long longRichEditTextLangID = 0x10001CFE;
  public static final long longLayoutTightAlignment = 0x08001CFF;
  public static final long longCharset = 0x0C001D01;
  public static final long longCreationTimeStamp = 0x14001D09;
  public static final long longDeletable = 0x08001D0C;
  public static final long longListMSAAIndex = 0x10001D0E;
  public static final long longIsBackground = 0x08001D13;
  public static final long longIRecordMedia = 0x14001D24;
  public static final long longCachedTitleStringFromPage = 0x1C001D3C;
  public static final long longRowCount = 0x14001D57;
  public static final long longColumnCount = 0x14001D58;
  public static final long longTableBordersVisible = 0x08001D5E;
  public static final long longStructureElementChildNodes = 0x24001D5F;
  public static final long longChildGraphSpaceElementNodes = 0x2C001D63;
  public static final long longTableColumnWidths = 0x1C001D66;
  public static final long longAuthor = 0x1C001D75;
  public static final long longLastModifiedTimeStamp = 0x18001D77;
  public static final long longAuthorOriginal = 0x20001D78;
  public static final long longAuthorMostRecent = 0x20001D79;
  public static final long longLastModifiedTime = 0x14001D7A;
  public static final long longIsConflictPage = 0x08001D7C;
  public static final long longTableColumnsLocked = 0x1C001D7D;
  public static final long longSchemaRevisionInOrderToRead = 0x14001D82;
  public static final long longIsConflictObjectForRender = 0x08001D96;
  public static final long longEmbeddedFileContainer = 0x20001D9B;
  public static final long longEmbeddedFileName = 0x1C001D9C;
  public static final long longSourceFilepath = 0x1C001D9D;
  public static final long longConflictingUserName = 0x1C001D9E;
  public static final long longImageFilename = 0x1C001DD7;
  public static final long longIsConflictObjectForSelection = 0x08001DDB;
  public static final long longPageLevel = 0x14001DFF;
  public static final long longTextRunIndex = 0x1C001E12;
  public static final long longTextRunFormatting = 0x24001E13;
  public static final long longHyperlink = 0x08001E14;
  public static final long longUnderlineType = 0x0C001E15;
  public static final long longHidden = 0x08001E16;
  public static final long longHyperlinkProtected = 0x08001E19;
  public static final long longTextRunIsEmbeddedObject = 0x08001E22;
  public static final long longImageAltText = 0x1C001E58;
  public static final long longMathFormatting = 0x08003401;
  public static final long longParagraphStyle = 0x2000342C;
  public static final long longParagraphSpaceBefore = 0x1400342E;
  public static final long longParagraphSpaceAfter = 0x1400342F;
  public static final long longParagraphLineSpacingExact = 0x14003430;
  public static final long longMetaDataObjectsAboveGraphSpace = 0x24003442;
  public static final long longTextRunDataObject = 0x24003458;
  public static final long longTextRunData = 0x40003499;
  public static final long longParagraphStyleId = 0x1C00345A;
  public static final long longHasVersionPages = 0x08003462;
  public static final long longActionItemType = 0x10003463;
  public static final long longNoteTagShape = 0x10003464;
  public static final long longNoteTagHighlightColor = 0x14003465;
  public static final long longNoteTagTextColor = 0x14003466;
  public static final long longNoteTagPropertyStatus = 0x14003467;
  public static final long longNoteTagLabel = 0x1C003468;
  public static final long longNoteTagCreated = 0x1400346E;
  public static final long longNoteTagCompleted = 0x1400346F;
  public static final long longNoteTagDefinitionOid = 0x20003488;
  public static final long longNoteTagStates = 0x04003489;
  public static final long longActionItemStatus = 0x10003470;
  public static final long longActionItemSchemaVersion = 0x0C003473;
  public static final long longReadingOrderRTL = 0x08003476;
  public static final long longParagraphAlignment = 0x0C003477;
  public static final long longVersionHistoryGraphSpaceContextNodes = 0x3400347B;
  public static final long longDisplayedPageNumber = 0x14003480;
  public static final long longSectionDisplayName = 0x1C00349B;
  public static final long longNextStyle = 0x1C00348A;
  public static final long longWebPictureContainer2_3_98 = 0x200034C8;
  public static final long longImageUploadState = 0x140034CB;
  public static final long longTextExtendedAscii = 0x1C003498;
  public static final long longPictureWidth = 0x140034CD;
  public static final long longPictureHeight = 0x140034CE;
  public static final long longPageMarginOriginX = 0x14001D0F;
  public static final long longPageMarginOriginY = 0x14001D10;
  public static final long longWzHyperlinkUrl = 0x1C001E20;
  public static final long longTaskTagDueDate = 0x1400346B;

  public static String nameOf(long id) {
    String retval = "(unknown)";
    if (id == (0x08001C00 & 0xffff)) {
      retval = "LayoutTightLayout";
    }
    if (id == (0x14001C01 & 0xffff)) {
      retval = "PageWidth";
    }
    if (id == (0x14001C02 & 0xffff)) {
      retval = "PageHeight";
    }
    if (id == (0x0C001C03 & 0xffff)) {
      retval = "OutlineElementChildLevel";
    }
    if (id == (0x08001C04 & 0xffff)) {
      retval = "Bold";
    }
    if (id == (0x08001C05 & 0xffff)) {
      retval = "Italic";
    }
    if (id == (0x08001C06 & 0xffff)) {
      retval = "Underline";
    }
    if (id == (0x08001C07 & 0xffff)) {
      retval = "Strikethrough";
    }
    if (id == (0x08001C08 & 0xffff)) {
      retval = "Superscript";
    }
    if (id == (0x08001C09 & 0xffff)) {
      retval = "Subscript";
    }
    if (id == (0x1C001C0A & 0xffff)) {
      retval = "Font";
    }
    if (id == (0x10001C0B & 0xffff)) {
      retval = "FontSize";
    }
    if (id == (0x14001C0C & 0xffff)) {
      retval = "FontColor";
    }
    if (id == (0x14001C0D & 0xffff)) {
      retval = "Highlight";
    }
    if (id == (0x1C001C12 & 0xffff)) {
      retval = "RgOutlineIndentDistance";
    }
    if (id == (0x0C001C13 & 0xffff)) {
      retval = "BodyTextAlignment";
    }
    if (id == (0x14001C14 & 0xffff)) {
      retval = "OffsetFromParentHoriz";
    }
    if (id == (0x14001C15 & 0xffff)) {
      retval = "OffsetFromParentVert";
    }
    if (id == (0x1C001C1A & 0xffff)) {
      retval = "NumberListFormat";
    }
    if (id == (0x14001C1B & 0xffff)) {
      retval = "LayoutMaxWidth";
    }
    if (id == (0x14001C1C & 0xffff)) {
      retval = "LayoutMaxHeight";
    }
    if (id == (0x24001C1F & 0xffff)) {
      retval = "ContentChildNodesOfOutlineElement";
    }
    if (id == (0x24001C20 & 0xffff)) {
      retval = "ElementChildNodesOfSection";
    }
    if (id == (0x08001E1E & 0xffff)) {
      retval = "EnableHistory";
    }
    if (id == (0x1C001C22 & 0xffff)) {
      retval = "RichEditTextUnicode";
    }
    if (id == (0x24001C26 & 0xffff)) {
      retval = "ListNodes";
    }
    if (id == (0x1C001C30 & 0xffff)) {
      retval = "NotebookManagementEntityGuid";
    }
    if (id == (0x08001C34 & 0xffff)) {
      retval = "OutlineElementRTL";
    }
    if (id == (0x14001C3B & 0xffff)) {
      retval = "LanguageID";
    }
    if (id == (0x14001C3E & 0xffff)) {
      retval = "LayoutAlignmentInParent";
    }
    if (id == (0x20001C3F & 0xffff)) {
      retval = "PictureContainer";
    }
    if (id == (0x14001C4C & 0xffff)) {
      retval = "PageMarginTop";
    }
    if (id == (0x14001C4D & 0xffff)) {
      retval = "PageMarginBottom";
    }
    if (id == (0x14001C4E & 0xffff)) {
      retval = "PageMarginLeft";
    }
    if (id == (0x14001C4F & 0xffff)) {
      retval = "PageMarginRight";
    }
    if (id == (0x1C001C52 & 0xffff)) {
      retval = "ListFont";
    }
    if (id == (0x18001C65 & 0xffff)) {
      retval = "TopologyCreationTimeStamp";
    }
    if (id == (0x14001C84 & 0xffff)) {
      retval = "LayoutAlignmentSelf";
    }
    if (id == (0x08001C87 & 0xffff)) {
      retval = "IsTitleTime";
    }
    if (id == (0x08001C88 & 0xffff)) {
      retval = "IsBoilerText";
    }
    if (id == (0x14001C8B & 0xffff)) {
      retval = "PageSize";
    }
    if (id == (0x08001C8E & 0xffff)) {
      retval = "PortraitPage";
    }
    if (id == (0x08001C91 & 0xffff)) {
      retval = "EnforceOutlineStructure";
    }
    if (id == (0x08001C92 & 0xffff)) {
      retval = "EditRootRTL";
    }
    if (id == (0x08001CB2 & 0xffff)) {
      retval = "CannotBeSelected";
    }
    if (id == (0x08001CB4 & 0xffff)) {
      retval = "IsTitleText";
    }
    if (id == (0x08001CB5 & 0xffff)) {
      retval = "IsTitleDate";
    }
    if (id == (0x14001CB7 & 0xffff)) {
      retval = "ListRestart";
    }
    if (id == (0x08001CBD & 0xffff)) {
      retval = "IsLayoutSizeSetByUser";
    }
    if (id == (0x14001CCB & 0xffff)) {
      retval = "ListSpacingMu";
    }
    if (id == (0x14001CDB & 0xffff)) {
      retval = "LayoutOutlineReservedWidth";
    }
    if (id == (0x08001CDC & 0xffff)) {
      retval = "LayoutResolveChildCollisions";
    }
    if (id == (0x08001CDE & 0xffff)) {
      retval = "IsReadOnly";
    }
    if (id == (0x14001CEC & 0xffff)) {
      retval = "LayoutMinimumOutlineWidth";
    }
    if (id == (0x14001CF1 & 0xffff)) {
      retval = "LayoutCollisionPriority";
    }
    if (id == (0x1C001CF3 & 0xffff)) {
      retval = "CachedTitleString";
    }
    if (id == (0x08001CF9 & 0xffff)) {
      retval = "DescendantsCannotBeMoved";
    }
    if (id == (0x10001CFE & 0xffff)) {
      retval = "RichEditTextLangID";
    }
    if (id == (0x08001CFF & 0xffff)) {
      retval = "LayoutTightAlignment";
    }
    if (id == (0x0C001D01 & 0xffff)) {
      retval = "Charset";
    }
    if (id == (0x14001D09 & 0xffff)) {
      retval = "CreationTimeStamp";
    }
    if (id == (0x08001D0C & 0xffff)) {
      retval = "Deletable";
    }
    if (id == (0x10001D0E & 0xffff)) {
      retval = "ListMSAAIndex";
    }
    if (id == (0x08001D13 & 0xffff)) {
      retval = "IsBackground";
    }
    if (id == (0x14001D24 & 0xffff)) {
      retval = "IRecordMedia";
    }
    if (id == (0x1C001D3C & 0xffff)) {
      retval = "CachedTitleStringFromPage";
    }
    if (id == (0x14001D57 & 0xffff)) {
      retval = "RowCount";
    }
    if (id == (0x14001D58 & 0xffff)) {
      retval = "ColumnCount";
    }
    if (id == (0x08001D5E & 0xffff)) {
      retval = "TableBordersVisible";
    }
    if (id == (0x24001D5F & 0xffff)) {
      retval = "StructureElementChildNodes";
    }
    if (id == (0x2C001D63 & 0xffff)) {
      retval = "ChildGraphSpaceElementNodes";
    }
    if (id == (0x1C001D66 & 0xffff)) {
      retval = "TableColumnWidths";
    }
    if (id == (0x1C001D75 & 0xffff)) {
      retval = "Author";
    }
    if (id == (0x18001D77 & 0xffff)) {
      retval = "LastModifiedTimeStamp";
    }
    if (id == (0x20001D78 & 0xffff)) {
      retval = "AuthorOriginal";
    }
    if (id == (0x20001D79 & 0xffff)) {
      retval = "AuthorMostRecent";
    }
    if (id == (0x14001D7A & 0xffff)) {
      retval = "LastModifiedTime";
    }
    if (id == (0x08001D7C & 0xffff)) {
      retval = "IsConflictPage";
    }
    if (id == (0x1C001D7D & 0xffff)) {
      retval = "TableColumnsLocked";
    }
    if (id == (0x14001D82 & 0xffff)) {
      retval = "SchemaRevisionInOrderToRead";
    }
    if (id == (0x08001D96 & 0xffff)) {
      retval = "IsConflictObjectForRender";
    }
    if (id == (0x20001D9B & 0xffff)) {
      retval = "EmbeddedFileContainer";
    }
    if (id == (0x1C001D9C & 0xffff)) {
      retval = "EmbeddedFileName";
    }
    if (id == (0x1C001D9D & 0xffff)) {
      retval = "SourceFilepath";
    }
    if (id == (0x1C001D9E & 0xffff)) {
      retval = "ConflictingUserName";
    }
    if (id == (0x1C001DD7 & 0xffff)) {
      retval = "ImageFilename";
    }
    if (id == (0x08001DDB & 0xffff)) {
      retval = "IsConflictObjectForSelection";
    }
    if (id == (0x14001DFF & 0xffff)) {
      retval = "PageLevel";
    }
    if (id == (0x1C001E12 & 0xffff)) {
      retval = "TextRunIndex";
    }
    if (id == (0x24001E13 & 0xffff)) {
      retval = "TextRunFormatting";
    }
    if (id == (0x08001E14 & 0xffff)) {
      retval = "Hyperlink";
    }
    if (id == (0x0C001E15 & 0xffff)) {
      retval = "UnderlineType";
    }
    if (id == (0x08001E16 & 0xffff)) {
      retval = "Hidden";
    }
    if (id == (0x08001E19 & 0xffff)) {
      retval = "HyperlinkProtected";
    }
    if (id == (0x08001E22 & 0xffff)) {
      retval = "TextRunIsEmbeddedObject";
    }
    if (id == (0x1C001E58 & 0xffff)) {
      retval = "ImageAltText";
    }
    if (id == (0x08003401 & 0xffff)) {
      retval = "MathFormatting";
    }
    if (id == (0x2000342C & 0xffff)) {
      retval = "ParagraphStyle";
    }
    if (id == (0x1400342E & 0xffff)) {
      retval = "ParagraphSpaceBefore";
    }
    if (id == (0x1400342F & 0xffff)) {
      retval = "ParagraphSpaceAfter";
    }
    if (id == (0x14003430 & 0xffff)) {
      retval = "ParagraphLineSpacingExact";
    }
    if (id == (0x24003442 & 0xffff)) {
      retval = "MetaDataObjectsAboveGraphSpace";
    }
    if (id == (0x24003458 & 0xffff)) {
      retval = "TextRunDataObject";
    }
    if (id == (0x40003499 & 0xffff)) {
      retval = "TextRunData";
    }
    if (id == (0x1C00345A & 0xffff)) {
      retval = "ParagraphStyleId";
    }
    if (id == (0x08003462 & 0xffff)) {
      retval = "HasVersionPages";
    }
    if (id == (0x10003463 & 0xffff)) {
      retval = "ActionItemType";
    }
    if (id == (0x10003464 & 0xffff)) {
      retval = "NoteTagShape";
    }
    if (id == (0x14003465 & 0xffff)) {
      retval = "NoteTagHighlightColor";
    }
    if (id == (0x14003466 & 0xffff)) {
      retval = "NoteTagTextColor";
    }
    if (id == (0x14003467 & 0xffff)) {
      retval = "NoteTagPropertyStatus";
    }
    if (id == (0x1C003468 & 0xffff)) {
      retval = "NoteTagLabel";
    }
    if (id == (0x1400346E & 0xffff)) {
      retval = "NoteTagCreated";
    }
    if (id == (0x1400346F & 0xffff)) {
      retval = "NoteTagCompleted";
    }
    if (id == (0x20003488 & 0xffff)) {
      retval = "NoteTagDefinitionOid";
    }
    if (id == (0x04003489 & 0xffff)) {
      retval = "NoteTagStates";
    }
    if (id == (0x10003470 & 0xffff)) {
      retval = "ActionItemStatus";
    }
    if (id == (0x0C003473 & 0xffff)) {
      retval = "ActionItemSchemaVersion";
    }
    if (id == (0x08003476 & 0xffff)) {
      retval = "ReadingOrderRTL";
    }
    if (id == (0x0C003477 & 0xffff)) {
      retval = "ParagraphAlignment";
    }
    if (id == (0x3400347B & 0xffff)) {
      retval = "VersionHistoryGraphSpaceContextNodes";
    }
    if (id == (0x14003480 & 0xffff)) {
      retval = "DisplayedPageNumber";
    }
    if (id == (0x1C00349B & 0xffff)) {
      retval = "SectionDisplayName";
    }
    if (id == (0x1C00348A & 0xffff)) {
      retval = "NextStyle";
    }
    if (id == (0x200034C8 & 0xffff)) {
      retval = "WebPictureContainer2_3_98";
    }
    if (id == (0x140034CB & 0xffff)) {
      retval = "ImageUploadState";
    }
    if (id == (0x1C003498 & 0xffff)) {
      retval = "TextExtendedAscii";
    }
    if (id == (0x140034CD & 0xffff)) {
      retval = "PictureWidth";
    }
    if (id == (0x140034CE & 0xffff)) {
      retval = "PictureHeight";
    }
    if (id == (0x14001D0F & 0xffff)) {
      retval = "PageMarginOriginX";
    }
    if (id == (0x14001D10 & 0xffff)) {
      retval = "PageMarginOriginY";
    }
    if (id == (0x1C001E20 & 0xffff)) {
      retval = "WzHyperlinkUrl";
    }
    if (id == (0x1400346B & 0xffff)) {
      retval = "TaskTagDueDate";
    }
    return retval;
  }
}
