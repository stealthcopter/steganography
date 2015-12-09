package com.stealthcopter.steganography;

import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mat on 16/11/15.
 */
public class EncodedObject {

  private final Bitmap bitmap;

  public EncodedObject(Bitmap bitmap) {
    // The base for this encoded object is the a bitmap
    this.bitmap = bitmap;
  }

  public Bitmap intoBitmap() {
    return bitmap;
  }

  public File intoFile(String path) throws IOException {
    return intoFile(new File(path));
  }

  public File intoFile(File file) throws IOException {

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

    file.createNewFile();

    //write the bytes in file
    FileOutputStream fo = new FileOutputStream(file);
    fo.write(bytes.toByteArray());

    return file;
  }

}
