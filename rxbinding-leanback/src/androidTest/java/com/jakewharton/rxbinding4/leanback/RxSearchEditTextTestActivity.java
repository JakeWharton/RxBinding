package com.jakewharton.rxbinding4.leanback;

import android.app.Activity;
import android.os.Bundle;
import androidx.leanback.widget.SearchEditText;

public final class RxSearchEditTextTestActivity extends Activity {
  SearchEditText searchEditText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    searchEditText = new SearchEditText(this);
    setContentView(searchEditText);
  }
}
