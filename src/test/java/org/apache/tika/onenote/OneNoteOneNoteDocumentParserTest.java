package org.apache.tika.onenote;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class OneNoteOneNoteDocumentParserTest {
  private static final Logger LOG = LoggerFactory.getLogger(OneNoteOneNoteDocumentParserTest.class);

  File sample1File;

  @Before
  public void before() throws Exception {
    sample1File = File.createTempFile("one_not_file", ".one");
    FileUtils.copyInputStreamToFile(this.getClass().getResourceAsStream("/Sample1.one"), sample1File);
  }

  @After
  public void after() {
    sample1File.delete();
  }

  @Test
  public void parse() throws Exception {
    try (FileInputStream fis = new FileInputStream(sample1File);
         FileChannel fc = fis.getChannel()) {
      OneNoteParser oneNoteParser = new OneNoteParser(fis, fc);
      OneNoteDocument oneNoteDocumentDoc = oneNoteParser.parse();

      Assert.assertEquals(5, oneNoteDocumentDoc.root.size());

      LOG.info("One note doc has {} child nodes at root", oneNoteDocumentDoc.root.size());

      Pair<Long, ExtendedGUID> roleAndContext = Pair.of(1L, ExtendedGUID.nil());
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OneNoteToText toText = new OneNoteToText(oneNoteDocumentDoc, true, fis, fc, baos, roleAndContext);

      toText.oneNoteDocumentToText();

      baos.close();

      String output = baos.toString(StandardCharsets.UTF_16LE.name());
      Assert.assertTrue("Must contain expected text output", StringUtils.contains(output, "OneNote: one place for all of your notes"));
      Assert.assertTrue("Must contain expected text output", StringUtils.contains(output, "OneNote Basics"));
    }
  }
}