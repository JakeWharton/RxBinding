package com.jakewharton.rxbinding4.leanback;

import android.app.Activity;
import android.os.Bundle;
import androidx.leanback.widget.SearchBar;
import androidx.leanback.widget.SearchEditText;
import androidx.leanback.widget.SearchOrbView;

public final class RxSearchBarTestActivity extends Activity {
  SearchBar searchBar;
  SearchEditText searchEditText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.lb_search_fragment);

    searchBar = findViewById(R.id.lb_search_bar);
    searchEditText = searchBar.findViewById(R.id.lb_search_text_editor);

    // reduce flakiness
    SearchOrbView searchOrbView = searchBar.findViewById(R.id.lb_search_bar_speech_orb);
    searchOrbView.enableOrbColorAnimation(false);
    searchOrbView.setSoundEffectsEnabled(false);
    searchOrbView.clearAnimation();
  }
}
