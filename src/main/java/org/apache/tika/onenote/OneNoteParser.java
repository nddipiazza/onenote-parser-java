package org.apache.tika.onenote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;

public class OneNoteParser {

  private static final Logger LOG = LoggerFactory.getLogger(OneNoteParser.class);
  private InputStream in;
  private SeekableByteChannel channel;
  public static void main(String[] args) throws Exception {
    if (args.length == 0 || args.length > 2) {
      LOG.error("USAGE: pathToOneNoteFile [pathToJsonFile]");
      System.exit(1);
    }
    try (FileInputStream fis = new FileInputStream(args[0]);
         FileChannel fc = fis.getChannel()) {
      OneNoteParser oneNoteParser = new OneNoteParser(fis, fc);
      OneNoteDocument oneNoteDocument = oneNoteParser.parse();
      ObjectMapper objectMapper = new ObjectMapper();

      if (args.length > 1) {
        String jsonOutFile = args[1];
        try (OutputStream fos = new FileOutputStream(new File(jsonOutFile))) {
          objectMapper.writerWithDefaultPrettyPrinter().writeValue(fos, oneNoteDocument.root);
        }
      } else {
        LOG.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(oneNoteDocument.root));
      }
    }
  }

  public OneNoteParser(InputStream in, SeekableByteChannel channel) {
    this.in = in;
    this.channel = channel;
  }

  public OneNoteDocument parse() throws IOException {
    OneNoteDocument oneNoteDocument = new OneNoteDocument();
    OneNotePtr oneNotePtr = new OneNotePtr(oneNoteDocument, in, channel);
    Header header = oneNotePtr.deserializeHeader();
    oneNotePtr.reposition(header.fcrFileNodeListRoot);
    FileNodePtr curPath = new FileNodePtr();
    oneNotePtr.deserializeFileNodeList(oneNoteDocument.root, curPath);

    return oneNoteDocument;
  }
}
