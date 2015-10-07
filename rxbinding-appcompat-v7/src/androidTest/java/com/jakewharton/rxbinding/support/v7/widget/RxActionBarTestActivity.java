package com.jakewharton.rxbinding.support.v7.widget;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public final class RxActionBarTestActivity extends ActionBarActivity {
  ActionBar actionBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    actionBar = getSupportActionBar();
  }
}
