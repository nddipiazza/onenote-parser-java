package org.apache.tika.onenote;

import org.apache.commons.io.EndianUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class OneNotePtrTest {

  @Test
  public void deserializeVarFileChunkReference() throws Exception {
    char [] chars = new char[2];
    chars[0] = 0xe0;
    chars[1] = 0x83;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(new String(chars).getBytes());

    ByteArrayInputStream bais = new ByteArrayInputStream(new String(chars).getBytes());
    long l = EndianUtils.readSwappedShort(bais);

    System.out.println(l);
  }
}