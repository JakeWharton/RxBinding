package com.jakewharton.rxbinding.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public final class RxViewGroupTestActivity extends Activity {
  LinearLayout viewGroup;
  View child;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewGroup = new LinearLayout(this);
    child = new View(this);
    setContentView(viewGroup);
  }
}

