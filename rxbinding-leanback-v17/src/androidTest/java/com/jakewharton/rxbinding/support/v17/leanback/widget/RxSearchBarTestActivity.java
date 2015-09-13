package com.jakewharton.rxbinding.support.v17.leanback.widget;

import com.jakewharton.rxbinding.support.v17.leanback.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.widget.SearchBar;
import android.support.v17.leanback.widget.SearchEditText;
import android.widget.FrameLayout;

public final class RxSearchBarTestActivity extends Activity {
  SearchBar searchBar;
  SearchEditText searchEditText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lb_search_fragment);

    FrameLayout searchFrame = (FrameLayout) findViewById(R.id.lb_search_frame);
    searchBar = (SearchBar) searchFrame.findViewById(R.id.lb_search_bar);
    searchEditText = (SearchEditText) searchBar.findViewById(R.id.lb_search_text_editor);
  }
}
