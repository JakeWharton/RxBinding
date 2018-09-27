package com.jakewharton.rxbinding2.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public final class RxViewScrollTestActivity extends Activity {
  View view;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    view = new View(this);

    setContentView(view);
  }
}

