package com.jakewharton.rxbinding2.support.design.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.FrameLayout;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

public final class RxSnackbarTestActivity extends Activity {

  Snackbar snackbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    FrameLayout parent = new FrameLayout(this);
    snackbar = Snackbar.make(parent, "Hey", LENGTH_SHORT);

    setContentView(parent);
  }
}