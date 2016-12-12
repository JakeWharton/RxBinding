package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.widget.SearchBar;
import android.support.v17.leanback.widget.SearchEditText;
import android.support.v17.leanback.widget.SearchOrbView;
import com.jakewharton.rxbinding2.support.v17.leanback.R;

public final class RxSearchBarTestActivity extends Activity {
  SearchBar searchBar;
  SearchEditText searchEditText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.lb_search_fragment);

    searchBar = (SearchBar) findViewById(R.id.lb_search_bar);
    searchEditText = (SearchEditText) searchBar.findViewById(R.id.lb_search_text_editor);

    // reduce flakiness
    SearchOrbView searchOrbView = (SearchOrbView) searchBar.findViewById(R.id.lb_search_bar_speech_orb);
    searchOrbView.enableOrbColorAnimation(false);
    searchOrbView.setSoundEffectsEnabled(false);
    searchOrbView.clearAnimation();
  }
}
