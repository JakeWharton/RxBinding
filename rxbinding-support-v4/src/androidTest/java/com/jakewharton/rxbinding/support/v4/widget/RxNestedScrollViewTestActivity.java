package com.jakewharton.rxbinding.support.v4.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class RxNestedScrollViewTestActivity extends Activity {
  NestedScrollView nestedScrollView;
  FrameLayout emptyView;
  FrameLayout emptyView2;
  LinearLayout linearLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    nestedScrollView = new NestedScrollView(this);
    ScrollView scrollView = new ScrollView(this);
    linearLayout = new LinearLayout(this);
    emptyView = new FrameLayout(this);
    emptyView2 = new FrameLayout(this);

    LayoutParams scrollParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    LayoutParams emptyParams = new LayoutParams(MATCH_PARENT, 5000);

    nestedScrollView.addView(emptyView, emptyParams);
    linearLayout.addView(nestedScrollView, emptyParams);
    linearLayout.addView(emptyView2, emptyParams);
    scrollView.addView(linearLayout, scrollParams);
    setContentView(scrollView);
  }
}
