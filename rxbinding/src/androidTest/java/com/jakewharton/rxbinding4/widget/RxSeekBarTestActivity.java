package com.jakewharton.rxbinding4.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

public final class RxSeekBarTestActivity extends Activity {
  SeekBar seekBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    seekBar = new SeekBar(this);
    setContentView(seekBar);
  }
}
