package com.jakewharton.rxbinding2.support.v4.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class RxNestedScrollViewTestActivity extends Activity {
  NestedScrollView nestedScrollView;
  FrameLayout emptyView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ScrollView scrollView = new ScrollView(this);
    nestedScrollView = new NestedScrollView(this);
    emptyView = new FrameLayout(this);

    LayoutParams scrollParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    LayoutParams emptyParams = new LayoutParams(50000, 50000);

    nestedScrollView.addView(emptyView, emptyParams);
    scrollView.addView(nestedScrollView, scrollParams);
    setContentView(scrollView, scrollParams);
  }
}
