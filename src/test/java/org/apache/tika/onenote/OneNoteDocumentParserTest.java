package org.apache.tika.onenote;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
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
import java.util.Map;

public class OneNoteDocumentParserTest {
  private static final Logger LOG = LoggerFactory.getLogger(OneNoteDocumentParserTest.class);

  File sample1File;
  File example1;
  File example2;
  File example3;

  OneNoteTreeWalkerOptions options = new OneNoteTreeWalkerOptions()
      .setCrawlAllRevisions(true);

  @Before
  public void before() throws Exception {
    sample1File = File.createTempFile("one_note_file", ".one");
    FileUtils.copyInputStreamToFile(this.getClass().getResourceAsStream("/Sample1.one"), sample1File);
    example1 = File.createTempFile("one_note_file", ".one");
    FileUtils.copyInputStreamToFile(this.getClass().getResourceAsStream("/Section1SheetTitle.one"), example1);
    example2 = File.createTempFile("one_note_file", ".one");
    FileUtils.copyInputStreamToFile(this.getClass().getResourceAsStream("/Section2SheetTitle.one"), example2);
    example3 = File.createTempFile("one_note_file", ".one");
    FileUtils.copyInputStreamToFile(this.getClass().getResourceAsStream("/Section3SheetTitle.one"), example3);
  }

  @After
  public void after() {
    sample1File.delete();
    example1.delete();
    example2.delete();
    example3.delete();
  }

  /**
   * This file is when you use one-note
   * @throws Exception
   */
  @Test
  public void testSample1() throws Exception {
    try (FileInputStream fis = new FileInputStream(sample1File);
         FileChannel fc = fis.getChannel()) {
      OneNoteParser oneNoteParser = new OneNoteParser(fis, fc);
      OneNoteDocument oneNoteDocumentDoc = oneNoteParser.parse();

      Assert.assertEquals(5, oneNoteDocumentDoc.root.children.size());

      Pair<Long, ExtendedGUID> roleAndContext = Pair.of(1L, ExtendedGUID.nil());
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OneNoteTreeWalker walker = new OneNoteTreeWalker(options, oneNoteDocumentDoc, fis, fc, baos, roleAndContext);

      Map<String, Object> outputMap = walker.walkTree();

      baos.close();

      LOG.info(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writerWithDefaultPrettyPrinter().writeValueAsString(outputMap));

      String txtOut = baos.toString(StandardCharsets.UTF_8.toString());

      Assert.assertFalse("Should not include font names in the text", txtOut.contains("Calibri"));
      Assert.assertFalse("Should not include UTF-16 property values that are garbage", txtOut.contains("夂菲䈿Ǡ�"));
    }
  }

  @Test
  public void testSample2() throws Exception {
    try (FileInputStream fis = new FileInputStream(example1);
         FileChannel fc = fis.getChannel()) {
      OneNoteParser oneNoteParser = new OneNoteParser(fis, fc);
      OneNoteDocument oneNoteDocumentDoc = oneNoteParser.parse();

      Assert.assertEquals(5, oneNoteDocumentDoc.root.children.size());

      Pair<Long, ExtendedGUID> roleAndContext = Pair.of(1L, ExtendedGUID.nil());
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OneNoteTreeWalker walker = new OneNoteTreeWalker(options, oneNoteDocumentDoc, fis, fc, baos, roleAndContext);

      Map<String, Object> outputMap = walker.walkTree();

      baos.close();

      LOG.info(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writerWithDefaultPrettyPrinter().writeValueAsString(outputMap));

      String txtOut = baos.toString(StandardCharsets.UTF_8.toString());

      Assert.assertTrue(txtOut.contains("wow this is neat"));
      Assert.assertTrue(txtOut.contains("neat info about totally killin it bro"));
      Assert.assertTrue(txtOut.contains("Section1TextArea1"));
      Assert.assertTrue(txtOut.contains("Section1HeaderTitle"));
      Assert.assertTrue(txtOut.contains("Section1TextArea2"));

      Assert.assertFalse("Should not include font names in the text", txtOut.contains("Calibri"));
      Assert.assertFalse("Should not include UTF-16 property values that are garbage", txtOut.contains("夂菲䈿Ǡ�"));
    }
  }
  @Test
  public void testSample3() throws Exception {
    try (FileInputStream fis = new FileInputStream(example2);
         FileChannel fc = fis.getChannel()) {
      OneNoteParser oneNoteParser = new OneNoteParser(fis, fc);
      OneNoteDocument oneNoteDocumentDoc = oneNoteParser.parse();

      Assert.assertEquals(5, oneNoteDocumentDoc.root.children.size());

      Pair<Long, ExtendedGUID> roleAndContext = Pair.of(1L, ExtendedGUID.nil());
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OneNoteTreeWalker walker = new OneNoteTreeWalker(options, oneNoteDocumentDoc, fis, fc, baos, roleAndContext);

      Map<String, Object> outputMap = walker.walkTree();

      baos.close();

      LOG.info("Json output:");
      LOG.info(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writerWithDefaultPrettyPrinter().writeValueAsString(outputMap));
      // TODO asserts
    }
  }
  @Test
  public void testSample4() throws Exception {
    try (FileInputStream fis = new FileInputStream(example3);
         FileChannel fc = fis.getChannel()) {
      OneNoteParser oneNoteParser = new OneNoteParser(fis, fc);
      OneNoteDocument oneNoteDocumentDoc = oneNoteParser.parse();

      Assert.assertEquals(5, oneNoteDocumentDoc.root.children.size());

      Pair<Long, ExtendedGUID> roleAndContext = Pair.of(1L, ExtendedGUID.nil());
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OneNoteTreeWalker walker = new OneNoteTreeWalker(options, oneNoteDocumentDoc, fis, fc, baos, roleAndContext);

      Map<String, Object> outputMap = walker.walkTree();

      baos.close();

      LOG.info("Json output:");
      LOG.info(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writerWithDefaultPrettyPrinter().writeValueAsString(outputMap));
      // TODO asserts
    }
  }
}