package com.stealthcopter.steganography;

import android.graphics.Bitmap;
import android.util.Log;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by mat on 17/11/15.
 */
public class Test {

  // TODO: Move into a proper test suite

  public static void runTests() throws Exception{
    Log.e("TEST", "Test: testBitmapEncoder " + testBitmapEncoder());
    Log.e("TEST", "Test: testStegClass " + testStegClass());
    Log.e("TEST", "Test: testZipping " + testZipping());
  }

  private static boolean testBitmapEncoder(){

    String hiddenMessage = "Hello this is a secret message!";

    Bitmap bitmap = BitmapHelper.createTestBitmap(200, 200, null);
    byte[] bytes = hiddenMessage.getBytes();

    Bitmap bitmap2 = BitmapEncoder.encode(bitmap, bytes);
    byte[] returnBytes = BitmapEncoder.decode(bitmap2);

    return Arrays.equals(bytes, returnBytes);
  }

  private static boolean testStegClass() throws Exception {

    String hiddenMessage = "Hello this is a secret message!";

    Bitmap bitmap = Steg.withRandomInput().encode(hiddenMessage).intoBitmap();

    String decodedString = Steg.withInput(bitmap).decode().intoString();

    return hiddenMessage.equals(decodedString);

  }

  private static boolean testZipping() throws IOException {

    String hiddenMessage = "Secret message starts here!";
    for (int i = 0; i < 200; i++) {
      hiddenMessage+="\nLine "+i+" of our secret message";
    }

    float originalByteSize = hiddenMessage.getBytes().length;

    byte[] compressedBytes = Zipper.compress(hiddenMessage);

    float compressedByteSize = compressedBytes.length;

    String decodedMessage = Zipper.decompress(compressedBytes);

    Log.e("TEST", "Original Size: "+originalByteSize);
    Log.e("TEST", "Compressed Size: "+compressedByteSize+ " ("+String.format("%.2f",100*compressedByteSize/originalByteSize)+"%)");

    return hiddenMessage.equals(decodedMessage);

  }

}
