package com.stealthcopter.steganography;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import java.io.File;

/**
 * Created by mat on 12/11/15.
 */
public class Steg {

  // Don't use any additional password for encryption/decryption of the data
  private final int PASS_NONE = 0;
  // Note this is really dumb encryption and can be broken easily but is better than someone being
  // able to just read the bytes straight out...
  private final int PASS_SIMPLE_XOR = 1;

  private String key = null;
  private int passmode = PASS_NONE;
  private Bitmap inBitmap = null;

  public static Steg withInput(@NonNull String filePath) {
    Steg steg = new Steg();
    steg.setInputBitmap(BitmapFactory.decodeFile(filePath));
    return steg;
  }

  public static Steg withInput(@NonNull File file) {
    Steg steg = new Steg();
    steg.setInputBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
    return steg;
  }

  public static Steg withInput(@NonNull Bitmap bitmap) {
    Steg steg = new Steg();
    steg.setInputBitmap(bitmap);
    return steg;
  }

  public static Steg withRandomInput() {
    return withRandomInput(200);
  }

  public static Steg withRandomInput(int size) {
    return withRandomInput(size, size, null);
  }

  public static Steg withRandomInput(int width, int height, @ColorInt Integer color) {
    Steg steg = new Steg();
    steg.setInputBitmap(BitmapHelper.createTestBitmap(width, height, color));
    return steg;
  }

  /**
   * @param bitmap - set the input bitmap to operate on
   */
  private void setInputBitmap(@NonNull Bitmap bitmap) {
    this.inBitmap = bitmap;
  }

  // OPTIONAL ENCRYPTION STEP

  /**
   * @param key - Additional password to encrypt the data with (or decrypt)
   */
  public Steg withPassword(@NonNull String key) {
    withPassword(key, PASS_SIMPLE_XOR);
    return this;
  }

  /**
   * @param key -  Additional password to encrypt the data with (or decrypt)
   * @param mode - Mode to use the password with, by default we'll prob
   * use something dumb and insecure
   */
  public Steg withPassword(@NonNull String key, int mode) {
    this.key = key;
    this.passmode = mode;

    // FIXME:
    if (1 == 1) {
      throw new RuntimeException("Not implemented yet");
    }

    return this;
  }

  // DECODING
  public DecodedObject decode() throws Exception {
    return new DecodedObject(BitmapEncoder.decode(inBitmap));
  }

  // ENCODING

  public EncodedObject encode(@NonNull File file) throws Exception {

    // FIXME:
    if (1 == 1) {
      throw new RuntimeException("Not implemented yet");
    }

    return encode("");
  }

  public EncodedObject encode(@NonNull String string) throws Exception {
    return encode(string.getBytes());
  }

  public EncodedObject encode(@NonNull byte[] bytes) throws Exception {

    // Check there is enough space for bitmap to be encoded into image
    if (bytes.length>bytesAvaliableInBitmap()){
      throw new IllegalArgumentException("Not enough space in bitmap to hold data (max:"+bytesAvaliableInBitmap()+")");
    }

    // Create an encoded object from our bitmap
    return new EncodedObject(BitmapEncoder.encode(inBitmap, bytes));
  }

  /**
   *
   * @return The bytes available to store in the bitmap
   */
  private int bytesAvaliableInBitmap() {
    if (inBitmap == null) return 0;
    return (inBitmap.getWidth() * inBitmap.getHeight())*3/8 - BitmapEncoder.HEADER_SIZE;
  }

}
