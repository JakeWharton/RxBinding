package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.Adapter;
import android.widget.AdapterView;
import com.jakewharton.rxbinding.internal.Functions;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link AdapterView}.
 */
public final class RxAdapterView {
  /**
   * Create an observable of the selected position of {@code view}. If nothing is selected,
   * {@link AdapterView#INVALID_POSITION} will be emitted.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Observable<Integer> itemSelections(
      @NonNull AdapterView<T> view) {
    checkNotNull(view, "view == null");
    return Observable.create(new AdapterViewItemSelectionOnSubscribe(view));
  }

  /**
   * Create an observable of selection events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Observable<AdapterViewSelectionEvent> selectionEvents(
      @NonNull AdapterView<T> view) {
    checkNotNull(view, "view == null");
    return Observable.create(new AdapterViewSelectionOnSubscribe(view));
  }

  /**
   * Create an observable of the position of item clicks for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Observable<Integer> itemClicks(
      @NonNull AdapterView<T> view) {
    checkNotNull(view, "view == null");
    return Observable.create(new AdapterViewItemClickOnSubscribe(view));
  }

  /**
   * Create an observable of the item click events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Observable<AdapterViewItemClickEvent> itemClickEvents(
      @NonNull AdapterView<T> view) {
    checkNotNull(view, "view == null");
    return Observable.create(new AdapterViewItemClickEventOnSubscribe(view));
  }

  /**
   * Create an observable of the position of item long-clicks for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Observable<Integer> itemLongClicks(
      @NonNull AdapterView<T> view) {
    checkNotNull(view, "view == null");
    return itemLongClicks(view, Functions.FUNC0_ALWAYS_TRUE);
  }

  /**
   * Create an observable of the position of item long-clicks for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @param handled Function invoked each occurrence to determine the return value of the
   * underlying {@link AdapterView.OnItemLongClickListener}.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Observable<Integer> itemLongClicks(@NonNull AdapterView<T> view,
      @NonNull Func0<Boolean> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new AdapterViewItemLongClickOnSubscribe(view, handled));
  }

  /**
   * Create an observable of the item long-click events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Observable<AdapterViewItemLongClickEvent> itemLongClickEvents(
      @NonNull AdapterView<T> view) {
    checkNotNull(view, "view == null");
    return itemLongClickEvents(view, Functions.FUNC1_ALWAYS_TRUE);
  }

  /**
   * Create an observable of the item long-click events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link AdapterView.OnItemLongClickListener}.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Observable<AdapterViewItemLongClickEvent> itemLongClickEvents(
      @NonNull AdapterView<T> view,
      @NonNull Func1<? super AdapterViewItemLongClickEvent, Boolean> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new AdapterViewItemLongClickEventOnSubscribe(view, handled));
  }

  /**
   * An action which sets the selected position of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Action1<? super Integer> selection(
      @NonNull final AdapterView<T> view) {
    checkNotNull(view, "view == null");
    return new Action1<Integer>() {
      @Override public void call(Integer position) {
        view.setSelection(position);
      }
    };
  }

  private RxAdapterView() {
    throw new AssertionError("No instances.");
  }
}
