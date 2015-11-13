package com.jakewharton.rxbinding.support.design.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

@SuppressLint("Registered")
public class RxSwipeDismissBehaviorTestActivity extends Activity {
  CoordinatorLayout parent;
  View view;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    parent = new CoordinatorLayout(this);
    view = new View(this);
    view.setLayoutParams(new CoordinatorLayout.LayoutParams(100, 100));
    parent.addView(view);

    setContentView(parent);
  }
}
