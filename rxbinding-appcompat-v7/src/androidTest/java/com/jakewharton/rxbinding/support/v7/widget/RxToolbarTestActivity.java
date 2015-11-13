package com.jakewharton.rxbinding.support.v7.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

@SuppressLint("Registered")
public final class RxToolbarTestActivity extends Activity {
  static final String NAVIGATION_CONTENT_DESCRIPTION = "click me!";

  Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    toolbar = new Toolbar(this);
    toolbar.setNavigationContentDescription(NAVIGATION_CONTENT_DESCRIPTION);
    toolbar.setNavigationIcon(android.R.drawable.sym_def_app_icon);
    setContentView(toolbar);
  }
}
