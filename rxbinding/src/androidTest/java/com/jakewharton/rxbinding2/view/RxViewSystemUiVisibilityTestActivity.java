package com.jakewharton.rxbinding2.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public final class RxViewSystemUiVisibilityTestActivity extends Activity {
  FrameLayout root;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    root = new FrameLayout(this);
    setContentView(root);
  }
}
