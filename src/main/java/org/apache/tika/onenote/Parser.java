package org.apache.tika.onenote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class Parser {

  private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

  private String pathToOneNoteFile;

  public static void main(String[] args) throws Exception {
    if (args.length == 0 || args.length > 2) {
      LOG.error("USAGE: pathToOneNoteFile [pathToJsonFile]");
      System.exit(1);
    }
    Parser parser = new Parser(args[0]);
    OneNote oneNote = parser.parse();
    ObjectMapper objectMapper = new ObjectMapper();

    if (args.length > 1) {
      String jsonOutFile = args[1];
      try (OutputStream fos = new FileOutputStream(new File(jsonOutFile))) {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(fos, oneNote.root);
      }
    } else {
      LOG.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(oneNote.root));
    }
  }

  public Parser(String pathToOneNoteFile) {
    this.pathToOneNoteFile = pathToOneNoteFile;
  }

  public OneNote parse() throws IOException {
    try (FileInputStream in = new FileInputStream(new File(pathToOneNoteFile));
         FileChannel fc = in.getChannel()) {
      OneNote oneNote = new OneNote();
      OneNotePtr oneNotePtr = new OneNotePtr(oneNote, in, fc);
      Header header = oneNotePtr.deserializeHeader();
      oneNotePtr.reposition(header.fcrFileNodeListRoot);
      FileNodePtr curPath = new FileNodePtr();
      oneNotePtr.deserializeFileNodeList(oneNote.root, curPath);

      return oneNote;
    }
  }
}
