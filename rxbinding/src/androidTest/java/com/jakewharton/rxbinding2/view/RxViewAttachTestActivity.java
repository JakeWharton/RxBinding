package com.jakewharton.rxbinding2.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public final class RxViewAttachTestActivity extends Activity {
  FrameLayout parent;
  View child;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    parent = new FrameLayout(this);
    child = new View(this);
    setContentView(parent);
  }
}
