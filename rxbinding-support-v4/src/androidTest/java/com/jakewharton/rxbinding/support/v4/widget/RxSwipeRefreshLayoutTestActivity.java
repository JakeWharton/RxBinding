package com.jakewharton.rxbinding.support.v4.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

@SuppressLint("Registered")
public final class RxSwipeRefreshLayoutTestActivity extends Activity {
  SwipeRefreshLayout swipeRefreshLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    swipeRefreshLayout = new SwipeRefreshLayout(this);

    ScrollView scrollView = new ScrollView(this);
    LayoutParams scrollParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    swipeRefreshLayout.addView(scrollView, scrollParams);

    FrameLayout emptyView = new FrameLayout(this);
    LayoutParams emptyParams = new LayoutParams(MATCH_PARENT, 100000);
    scrollView.addView(emptyView, emptyParams);

    setContentView(swipeRefreshLayout);
  }
}
