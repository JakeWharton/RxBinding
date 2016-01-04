package com.jakewharton.rxbinding.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

import com.jakewharton.rxbinding.internal.Functions;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

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

  /**
   * Create an observable of booleans representing the focus of the query text field.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   */
  @CheckResult @NonNull
  public static Observable<Boolean> queryTextFocusChange(@NonNull SearchView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SearchViewQueryTextFocusChangeOnSubscribe(view));
  }

  /**
   * Create an observable of the absolute position of the clicked item in the list of suggestions
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link SearchView#setOnSuggestionListener} to
   * observe search view events. Only one observable can be used for a search view at a time.
   */
  @CheckResult @NonNull
  public static Observable<Integer> suggestionClick(@NonNull SearchView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SearchViewSuggestionClickOnSubscribe(view,
            Functions.FUNC1_ALWAYS_TRUE));
  }

  /**
   * Create an observable of the absolute position of the clicked item in the list of suggestions
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link SearchView#setOnSuggestionListener} to
   * observe search view events. Only one observable can be used for a search view at a time.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link SearchView.OnSuggestionListener}.
   */
  @CheckResult @NonNull
  public static Observable<Integer> suggestionClick(@NonNull SearchView view,
      @NonNull Func1<? super Integer, Boolean> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new SearchViewSuggestionClickOnSubscribe(view, handled));
  }

  private RxSearchView() {
    throw new AssertionError("No instances.");
  }
}
