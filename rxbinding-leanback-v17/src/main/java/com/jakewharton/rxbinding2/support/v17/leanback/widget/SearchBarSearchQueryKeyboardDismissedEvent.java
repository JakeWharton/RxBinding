package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.SearchBar;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchBarSearchQueryKeyboardDismissedEvent extends SearchBarSearchQueryEvent {
  @CheckResult @NonNull
  public static SearchBarSearchQueryKeyboardDismissedEvent create(@NonNull SearchBar view,
      @NonNull String query) {
    return new AutoValue_SearchBarSearchQueryKeyboardDismissedEvent(view, query);
  }

  SearchBarSearchQueryKeyboardDismissedEvent() {
  }
}
