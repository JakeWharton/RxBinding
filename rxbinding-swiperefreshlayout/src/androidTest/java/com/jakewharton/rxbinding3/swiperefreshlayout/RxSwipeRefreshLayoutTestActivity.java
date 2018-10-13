package com.jakewharton.rxbinding3.swiperefreshlayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.jakewharton.rxbinding3.swiperefreshlayout.test.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class RxSwipeRefreshLayoutTestActivity extends Activity {
  SwipeRefreshLayout swipeRefreshLayout;

  final Handler handler = new Handler(Looper.getMainLooper());
  final Runnable stopRefreshing = new Runnable() {
    @Override public void run() {
      swipeRefreshLayout.setRefreshing(false);
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    swipeRefreshLayout = new SwipeRefreshLayout(this);
    swipeRefreshLayout.setId(R.id.swipe_refresh_layout);
    swipeRefreshLayout.setOnTouchListener((v, event) -> {
      if (event.getActionMasked() == MotionEvent.ACTION_UP) {
        handler.removeCallbacks(stopRefreshing);
        handler.postDelayed(stopRefreshing, 300);
      }
      return false;
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
