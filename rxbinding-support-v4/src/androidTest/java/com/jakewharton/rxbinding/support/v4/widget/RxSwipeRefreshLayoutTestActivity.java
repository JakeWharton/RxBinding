package com.jakewharton.rxbinding.support.v4.widget;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class RxSwipeRefreshLayoutTestActivity extends Activity {
  SwipeRefreshLayout swipeRefreshLayout;

  private final Handler handler = new Handler(Looper.getMainLooper());
  private final Runnable stopRefreshing = new Runnable() {
    @Override public void run() {
      swipeRefreshLayout.setRefreshing(false);
      handler.postDelayed(this, 500);
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    swipeRefreshLayout = new SwipeRefreshLayout(this);
    swipeRefreshLayout.setId(1);

    ScrollView scrollView = new ScrollView(this);
    LayoutParams scrollParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    swipeRefreshLayout.addView(scrollView, scrollParams);

    FrameLayout emptyView = new FrameLayout(this);
    LayoutParams emptyParams = new LayoutParams(MATCH_PARENT, 100000);
    scrollView.addView(emptyView, emptyParams);

    setContentView(swipeRefreshLayout);

    handler.postDelayed(stopRefreshing, 500);
  }

  @Override protected void onPause() {
    super.onPause();
    handler.removeCallbacks(stopRefreshing);
  }
}
