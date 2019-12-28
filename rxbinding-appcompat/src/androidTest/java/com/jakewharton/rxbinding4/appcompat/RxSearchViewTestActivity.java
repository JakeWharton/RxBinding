package com.jakewharton.rxbinding4.appcompat;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

public final class RxSearchViewTestActivity extends Activity {
  SearchView searchView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    searchView = new SearchView(this);
    setContentView(searchView);
  }
}
