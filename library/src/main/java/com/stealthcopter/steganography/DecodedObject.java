package com.stealthcopter.steganography;

import java.io.File;
import java.io.IOException;

/**
 * Created by mat on 16/11/15.
 */
public class DecodedObject {

  private final byte[] bytes;

  public DecodedObject(byte[] bytes) {
    // The base for this decoded class is a byte array of the decoded data
    this.bytes = bytes;
  }

  public byte[] intoByteArray() {
    return bytes;
  }

  public String intoString() {
    return new String(bytes);
  }

  public File intoFile(String path) throws IOException {
    return intoFile(new File(path));
  }

  public File intoFile(File file){
    // FIXME:
    if (1 == 1) {
      throw new RuntimeException("Not implemented yet");
    }

    return file;
  }

  // TODO: Conversion methods to convert byte arrays into other formats...

}
