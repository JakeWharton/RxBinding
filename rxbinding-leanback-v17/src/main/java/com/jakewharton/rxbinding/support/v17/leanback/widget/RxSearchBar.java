package com.jakewharton.rxbinding.support.v17.leanback.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.SearchBar;
import rx.Observable;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link SearchBar}.
 */
public final class RxSearchBar {
  /**
   * Create an observable of {@linkplain SearchBarSearchQueryEvent search query events} on {@code
   * view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will not be emitted on subscribe.
   */
  @CheckResult
  @NonNull
  public static Observable<SearchBarSearchQueryEvent> searchQueryChangeEvents(
          @NonNull SearchBar view) {
    return Observable.create(new SearchBarSearchQueryChangeEventsOnSubscribe(view));
  }

  /**
   * Create an observable of character sequences for search query changes on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will not be emitted on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<CharSequence> searchQueryChanges(@NonNull SearchBar view) {
    return Observable.create(new SearchBarSearchQueryChangesOnSubscribe(view));
  }

  /**
   * An action which sets the searchQuery property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super CharSequence> searchQuery(@NonNull final SearchBar view) {
    return new Action1<CharSequence>() {
      @Override public void call(CharSequence text) {
        view.setSearchQuery(text.toString());
      }
    };
  }

  private RxSearchBar() {
    throw new AssertionError("No instances.");
  }
}

