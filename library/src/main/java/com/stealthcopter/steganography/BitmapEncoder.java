package com.stealthcopter.steganography;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by mat on 16/11/15.
 */
public class BitmapEncoder {

  public static final int HEADER_SIZE = Long.SIZE / Byte.SIZE + 4;

  public static byte[] createHeader(long size) {
    byte[] header = new byte[HEADER_SIZE];
    int i = 0;
    header[i++] = (byte) 0x5B;
    header[i++] = (byte) 0x5B;
    for (byte b : longToBytes(size)) {
      header[i++] = b;
    }
    header[i++] = (byte) 0x5D;
    header[i++] = (byte) 0x5D;
    return header;
  }

  private static byte[] longToBytes(long x) {
    ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
    buffer.putLong(x);
    return buffer.array();
  }

  public static long bytesToLong(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
    buffer.put(bytes);
    buffer.flip();//need flip
    return buffer.getLong();
  }

  public static Bitmap encode(Bitmap inBitmap, byte[] bytes) {

    byte[] header = createHeader(bytes.length);

    // Pad the byte array by so it is divisible by 24 ( 8 bits per byte * the 3 color channels)
    // this wastes a trivial amount of space but makes code tidier.
    if (bytes.length % 24 != 0) {
      bytes = Arrays.copyOf(bytes, bytes.length + (24 - bytes.length % 24));
    }

    return encodeByteArrayIntoBitmap(inBitmap, header, bytes);
  }

  public static byte[] decode(Bitmap inBitmap) {

    byte[] header = decodeBitmapToByteArray(inBitmap, 0, HEADER_SIZE);
    int len = (int) bytesToLong(Arrays.copyOfRange(header, 2, HEADER_SIZE - 2));

    return decodeBitmapToByteArray(inBitmap, HEADER_SIZE, len);
  }

  /**
   * @param inBitmap - bitmap to hide byte array in
   * @param bytes - the bytes to hide
   * @return - copy of the bitmap with bytes hidden in
   */
  private static Bitmap encodeByteArrayIntoBitmap(Bitmap inBitmap, byte header[], byte[] bytes) {

    Bitmap outBitmap = inBitmap.copy(Bitmap.Config.ARGB_8888, true);

    int x = 0;
    int y = 0;
    int width = inBitmap.getWidth();
    int height = inBitmap.getHeight();

    int r, g, b;
    int color;

    int bufferPos = 0;
    int buffer[] = { 0, 0, 0 };

    for (int i = 0; i < header.length + bytes.length; i++) {
      // We loop over each byte and then work with each of the 8 bits

      for (int j = 0; j < 8; j++) {

        // Get the bit at position and save it in the buffer
        if (i < header.length) {
          buffer[bufferPos] = (header[i] >> j) & 1;
        } else {
          buffer[bufferPos] = (bytes[i - header.length] >> j) & 1;
        }

        // Empty the buffer (draw) into a pixel
        if (bufferPos == 2) {
          // Get original pixel color
          color = inBitmap.getPixel(x, y);

          // Split into channels
          r = Color.red(color);
          g = Color.green(color);
          b = Color.blue(color);

          // Modify the least significant bit (if needed)
          r = (r % 2 == (1 - buffer[0])) ? r + 1 : r;
          g = (g % 2 == (1 - buffer[1])) ? g + 1 : g;
          b = (b % 2 == (1 - buffer[2])) ? b + 1 : b;

          // Account for overflow
          if (r == 256) r = 254;
          if (g == 256) g = 254;
          if (b == 256) b = 254;

          // Draw pixel
          outBitmap.setPixel(x, y, Color.argb(255, r, g, b));

          // Increment to the next pixel, shifting for row if needed
          x = x + 1;
          if (x == width) {
            x = 0;
            y++;
          }
          bufferPos = 0;
        } else {
          bufferPos++;
        }
      }
    }

    return outBitmap;
  }

  /**
   * @param inBitmap - input bitmap to read data from
   * @param offset - offset to start reading from (to ignore header)
   * @param length - length of the byte array we are expecting to read
   * @return the byte array of hidden data in the bitmap
   */
  private static byte[] decodeBitmapToByteArray(Bitmap inBitmap, int offset, int length) {

    // To hold our output
    byte bytes[] = new byte[length];

    int width = inBitmap.getWidth();
    int height = inBitmap.getHeight();

    int color;

    int bitNo = 0;
    int byteNo = 0;

    int bitBuffer[] = new int[3];

    // Loop over all pixels in the image
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {

        color = inBitmap.getPixel(x, y);

        bitBuffer[0] = Color.red(color) % 2;
        bitBuffer[1] = Color.green(color) % 2;
        bitBuffer[2] = Color.blue(color) % 2;

        for (int i = 0; i < 3; i++) {

          // Ignore the bits before the offset
          if (byteNo >= offset) {
            // Set each bit from the buffer into the corresponding bit
            bytes[byteNo - offset] =
                bitBuffer[i] == 1 ? (byte) (bytes[byteNo - offset] | (1 << bitNo))
                    : (byte) (bytes[byteNo - offset] & ~(1 << bitNo));
          }

          bitNo++;
          if (bitNo == 8) {
            bitNo = 0;
            byteNo++;
          }
          if (byteNo - offset >= bytes.length) break;
        }

        // TODO: Could replace with a while loop.
        // Stop when all bytes read.
        if (byteNo - offset >= bytes.length) break;
      }
      if (byteNo - offset >= bytes.length) break;
    }

    return bytes;
  }

  public static String printBinaryString(byte[] bytes) {
    String s = "";
    for (byte b : bytes) {
      s += "" + b + ",";//Integer.toBinaryString(b);
    }
    return s;
  }
}
