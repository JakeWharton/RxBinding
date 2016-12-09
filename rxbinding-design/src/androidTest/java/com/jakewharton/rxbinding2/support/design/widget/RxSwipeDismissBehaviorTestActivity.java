package com.jakewharton.rxbinding2.support.design.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

public class RxSwipeDismissBehaviorTestActivity extends Activity {
  CoordinatorLayout parent;
  View view;

  @SuppressWarnings("ResourceType")
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    parent = new CoordinatorLayout(this);
    view = new View(this);
    view.setId(1);
    view.setLayoutParams(new CoordinatorLayout.LayoutParams(100, 100));
    view.setBackgroundColor(0xFFFF0000);
    parent.addView(view);

    setContentView(parent);
  }
}
