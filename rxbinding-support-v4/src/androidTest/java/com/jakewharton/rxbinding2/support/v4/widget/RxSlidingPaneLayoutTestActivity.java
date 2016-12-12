package com.jakewharton.rxbinding2.support.v4.widget;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.LayoutParams;
import android.widget.FrameLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class RxSlidingPaneLayoutTestActivity extends Activity {
  SlidingPaneLayout slidingPaneLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    slidingPaneLayout = new SlidingPaneLayout(this);
    slidingPaneLayout.setId(android.R.id.primary);

    FrameLayout paneOne = new FrameLayout(this);
    LayoutParams paneOneParams = new LayoutParams(300, MATCH_PARENT);
    slidingPaneLayout.addView(paneOne, paneOneParams);

    FrameLayout paneTwo = new FrameLayout(this);
    paneTwo.setBackgroundColor(Color.WHITE);
    LayoutParams paneTwoParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    paneTwoParams.leftMargin = 50;
    slidingPaneLayout.addView(paneTwo, paneTwoParams);


    setContentView(slidingPaneLayout);
  }
}
