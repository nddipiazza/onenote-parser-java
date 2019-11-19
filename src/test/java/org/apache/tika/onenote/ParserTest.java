package org.apache.tika.onenote;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ParserTest {
  private static final Logger LOG = LoggerFactory.getLogger(ParserTest.class);

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
    Parser parser = new Parser(sample1File.getAbsolutePath());
    OneNote oneNoteDoc = parser.parse();

    LOG.info("One note doc has {} child nodes at root", oneNoteDoc.root.size());
  }
}