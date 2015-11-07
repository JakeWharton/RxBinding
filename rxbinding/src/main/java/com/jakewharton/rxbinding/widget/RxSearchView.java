package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SearchView;
import rx.Observable;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link SearchView}.
 */
public final class RxSearchView {
  /**
   * Create an observable of {@linkplain SearchViewQueryTextEvent query text events} on {@code
   * view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<SearchViewQueryTextEvent> queryTextChangeEvents(
      @NonNull SearchView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SearchViewQueryTextChangeEventsOnSubscribe(view));
  }

  /**
   * Create an observable of character sequences for query text changes on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<CharSequence> queryTextChanges(@NonNull SearchView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SearchViewQueryTextChangesOnSubscribe(view));
  }

  /**
   * An action which sets the query property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @param submit weather to submit query right after updating query text
   */
  @CheckResult @NonNull
  public static Action1<? super CharSequence> query(@NonNull final SearchView view,
      final boolean submit) {
    checkNotNull(view, "view == null");
    return new Action1<CharSequence>() {
      @Override public void call(CharSequence text) {
        view.setQuery(text, submit);
      }
    };
  }

  private RxSearchView() {
    throw new AssertionError("No instances.");
  }
}
