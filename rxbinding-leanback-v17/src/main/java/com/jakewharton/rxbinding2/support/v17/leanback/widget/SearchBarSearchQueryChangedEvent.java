package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.SearchBar;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchBarSearchQueryChangedEvent extends SearchBarSearchQueryEvent {
  @CheckResult @NonNull
  public static SearchBarSearchQueryChangedEvent create(@NonNull SearchBar view,
      @NonNull String query) {
    return new AutoValue_SearchBarSearchQueryChangedEvent(view, query);
  }

  SearchBarSearchQueryChangedEvent() {
  }
}
