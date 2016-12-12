package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.SearchBar;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Consumer
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
   */
  @CheckResult
  @NonNull
  public static Observable<SearchBarSearchQueryEvent> searchQueryChangeEvents(
          @NonNull SearchBar view) {
    checkNotNull(view, "view == null");
    return new SearchBarSearchQueryChangeEventsOnSubscribe(view);
  }

  /**
   * Create an observable of String values for search query changes on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   */
  @CheckResult @NonNull
  public static Observable<String> searchQueryChanges(@NonNull SearchBar view) {
    checkNotNull(view, "view == null");
    return new SearchBarSearchQueryChangesOnSubscribe(view);
  }

  /**
   * An action which sets the searchQuery property of {@code view} with String values.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super String> searchQuery(@NonNull final SearchBar view) {
    checkNotNull(view, "view == null");
    return new Consumer<String>() {
      @Override public void accept(String text) throws Exception {
        view.setSearchQuery(text);
      }
    };
  }

  private RxSearchBar() {
    throw new AssertionError("No instances.");
  }
}

