package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SearchView;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchViewQueryTextEvent {
  @CheckResult @NonNull
  public static SearchViewQueryTextEvent create(@NonNull SearchView view,
      @NonNull CharSequence queryText, boolean submitted) {
    return new AutoValue_SearchViewQueryTextEvent(view, queryText, submitted);
  }

  SearchViewQueryTextEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract SearchView view();
  @NonNull public abstract CharSequence queryText();
  public abstract boolean isSubmitted();
}
