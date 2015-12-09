package com.stealthcotper.steg;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.stealthcopter.steganography.BitmapHelper;
import com.stealthcopter.steganography.Steg;
import com.stealthcopter.steganography.Test;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();

        go();
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  private void go(){
    new Thread(new Runnable() {
      @Override public void run() {
        try {
          Test.runTests();
          attempEncoding();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  private void attempEncoding() throws Exception{

    String hiddenMessage = "Hello this is a hidden message";

    Bitmap bitmap = BitmapHelper.createTestBitmap(200, 200);

    Bitmap encodedBitmap = Steg.withInput(bitmap).encode(hiddenMessage).intoBitmap();

    String decodedMessage = Steg.withInput(encodedBitmap).decode().intoString();

    Log.d(getClass().getSimpleName(), "Decoded Message: " + decodedMessage);

  }

}
