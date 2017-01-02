package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.SearchBar;
import com.jakewharton.rxbinding2.view.ViewEvent;

public final class SearchBarSearchQueryEvent extends ViewEvent<SearchBar> {
  public enum Kind {
    CHANGED,
    SUBMITTED,
    KEYBOARD_DISMISSED
  }
  @CheckResult @NonNull
  public static SearchBarSearchQueryEvent create(@NonNull SearchBar view,
      @NonNull String searchQuery, @NonNull Kind kind) {
    return new SearchBarSearchQueryEvent(view, searchQuery, kind);
  }

  private final String searchQuery;
  private final Kind kind;

  private SearchBarSearchQueryEvent(@NonNull SearchBar view, @NonNull String searchQuery,
          @NonNull Kind kind) {
    super(view);
    this.searchQuery = searchQuery;
    this.kind = kind;
  }

  @NonNull
  public String searchQuery() {
    return searchQuery;
  }

  @NonNull
  public Kind kind() {
    return kind;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof SearchBarSearchQueryEvent)) return false;
    SearchBarSearchQueryEvent other = (SearchBarSearchQueryEvent) o;
    return other.view() == view()
        && other.searchQuery.equals(searchQuery)
        && kind == other.kind;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + searchQuery.hashCode();
    result = result * 37 + kind.hashCode();
    return result;
  }

  @Override public String toString() {
    return "SearchBarSearchQueryEvent{view="
        + view()
        + ", searchQuery="
        + searchQuery
        + ", kind="
        + kind
        + '}';
  }
}
