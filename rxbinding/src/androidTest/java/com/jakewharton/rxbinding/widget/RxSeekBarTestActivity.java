package com.jakewharton.rxbinding.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

@SuppressLint("Registered")
public final class RxSeekBarTestActivity extends Activity {
  SeekBar seekBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    seekBar = new SeekBar(this);
    setContentView(seekBar);
  }
}
