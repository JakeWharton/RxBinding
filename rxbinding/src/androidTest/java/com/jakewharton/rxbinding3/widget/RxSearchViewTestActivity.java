package com.jakewharton.rxbinding3.widget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.SearchView;

public final class RxSearchViewTestActivity extends Activity {
  SearchView searchView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    searchView = new SearchView(this);
    setContentView(searchView);
  }
}
