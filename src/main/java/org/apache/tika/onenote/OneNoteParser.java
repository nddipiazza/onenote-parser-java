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

  /**
   * OneNote files are of format:
   *
   * The header (section 2.3.1 in MS-ONESTORE) is the first 1024 bytes of the file. It contains references to the other structures in the file as well as metadata about the file.
   * The free chunk list (section 2.3.2 in MS-ONESTORE) defines where there are free spaces in the file where data can be written.
   * The transaction log (section 2.3.3 in MS-ONESTORE) stores the state and length of each file node list (section 2.4 in MS-ONESTORE) in the file.
   * The hashed chunk list (section 2.3.4 in MS-ONESTORE) stores read-only objects in the file that can be referenced by multiple revisions (section 2.1.8 in MS-ONESTORE).
   * The root file node list (section 2.1.14 in MS-ONESTORE) is the file node list that is the root of the tree of all file node lists in the file.
   *
   * In this method we first parse the header.
   *
   * After parsing the header, this results in header.fcrFileNodeListRoot that points to the first
   * @return
   * @throws IOException
   */
  public OneNoteDocument parse() throws IOException {
    OneNoteDocument oneNoteDocument = new OneNoteDocument();
    OneNotePtr oneNotePtr = new OneNotePtr(oneNoteDocument, in, channel);
    // First parse out the header.
    oneNoteDocument.header = oneNotePtr.deserializeHeader();

    // Now that we parsed the header, the "root file node list"

    oneNotePtr.reposition(oneNoteDocument.header.fcrFileNodeListRoot);
    FileNodePtr curPath = new FileNodePtr();
    oneNotePtr.deserializeFileNodeList(oneNoteDocument.root, curPath);

    return oneNoteDocument;
  }
}
