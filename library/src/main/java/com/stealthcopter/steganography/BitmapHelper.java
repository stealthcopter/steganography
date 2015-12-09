package com.stealthcopter.steganography;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import java.util.Random;

/**
 * Created by mat on 16/11/15.
 */
public class BitmapHelper {

  /**
   * Create a bitmap of specific size with a random color
   *
   * @param w - width
   * @param h - height
   * @return - bitmap of random(ish) color
   */
  public static Bitmap createTestBitmap(int w, int h) {
    return createTestBitmap(w, h, null);
  }

  /**
   * Create a bitmap of specific size with a specific color
   *
   * @param w - width
   * @param h - height
   * @param color - color integer (not resource id)
   * @return - bitmap of random(ish) color
   */
  public static Bitmap createTestBitmap(int w, int h, @ColorInt Integer color) {
    Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);

    if (color == null) {
      int colors[] = new int[] { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.WHITE };
      Random rgen = new Random();
      color = colors[rgen.nextInt(colors.length - 1)];
    }

    canvas.drawColor(color);
    return bitmap;
  }
}
