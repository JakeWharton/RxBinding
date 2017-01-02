package com.jakewharton.rxbinding2.support.v4.widget;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import com.jakewharton.rxbinding.support.v4.test.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class RxSwipeRefreshLayoutTestActivity extends Activity {
  SwipeRefreshLayout swipeRefreshLayout;

  private final Handler handler = new Handler(Looper.getMainLooper());
  private final Runnable stopRefreshing = new Runnable() {
    @Override public void run() {
      swipeRefreshLayout.setRefreshing(false);
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    swipeRefreshLayout = new SwipeRefreshLayout(this);
    swipeRefreshLayout.setId(R.id.swipe_refresh_layout);
    swipeRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP) {
          handler.removeCallbacks(stopRefreshing);
          handler.postDelayed(stopRefreshing, 300);
        }
        return false;
      }
    });

    ScrollView scrollView = new ScrollView(this);
    LayoutParams scrollParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    swipeRefreshLayout.addView(scrollView, scrollParams);

    FrameLayout emptyView = new FrameLayout(this);
    LayoutParams emptyParams = new LayoutParams(MATCH_PARENT, 100000);
    scrollView.addView(emptyView, emptyParams);

    setContentView(swipeRefreshLayout);
  }

  @Override protected void onPause() {
    super.onPause();
    handler.removeCallbacks(stopRefreshing);
  }
}
