package com.jakewharton.rxbinding.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;

@SuppressLint("Registered")
public final class RxRatingBarTestActivity extends Activity {
  RatingBar ratingBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ratingBar = new RatingBar(this);
    ratingBar.setMax(10);
    ratingBar.setStepSize(1f);
    setContentView(ratingBar);
  }
}
