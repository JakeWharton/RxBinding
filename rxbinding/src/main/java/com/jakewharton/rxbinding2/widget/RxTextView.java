package com.jakewharton.rxbinding2.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.widget.TextView;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.internal.Functions;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Consumer
 * actions} for {@link TextView}.
 */
public final class RxTextView {
  /**
   * Create an observable of editor actions on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
   * observe actions. Only one observable can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<Integer> editorActions(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return editorActions(view, Functions.PREDICATE_ALWAYS_TRUE);
  }

  /**
   * Create an observable of editor actions on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
   * observe actions. Only one observable can be used for a view at a time.
   *
   * @param handled Predicate invoked each occurrence to determine the return value of the
   * underlying {@link TextView.OnEditorActionListener}.
   */
  @CheckResult @NonNull
  public static Observable<Integer> editorActions(@NonNull TextView view,
                                                  @NonNull Predicate<? super Integer> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return new TextViewEditorActionObservable(view, handled);
  }

  /**
   * Create an observable of editor action events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
   * observe actions. Only one observable can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<TextViewEditorActionEvent> editorActionEvents(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return editorActionEvents(view, Functions.PREDICATE_ALWAYS_TRUE);
  }

  /**
   * Create an observable of editor action events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
   * observe actions. Only one observable can be used for a view at a time.
   *
   * @param handled Predicate invoked each occurrence to determine the return value of the
   * underlying {@link TextView.OnEditorActionListener}.
   */
  @CheckResult @NonNull
  public static Observable<TextViewEditorActionEvent> editorActionEvents(@NonNull TextView view,
      @NonNull Predicate<? super TextViewEditorActionEvent> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return new TextViewEditorActionEventObservable(view, handled);
  }

  /**
   * Create an observable of character sequences for text changes on {@code view}.
   * <p>
   * <em>Warning:</em> Values emitted by this observable are <b>mutable</b> and owned by the host
   * {@code TextView} and thus are <b>not safe</b> to cache or delay reading (such as by observing
   * on a different thread). If you want to cache or delay reading the items emitted then you must
   * map values through a function which calls {@link String#valueOf} or
   * {@link CharSequence#toString() .toString()} to create a copy.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static InitialValueObservable<CharSequence> textChanges(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return new TextViewTextObservable(view);
  }

  /**
   * Create an observable of text change events for {@code view}.
   * <p>
   * <em>Warning:</em> Values emitted by this observable contain a <b>mutable</b>
   * {@link CharSequence} owned by the host {@code TextView} and thus are <b>not safe</b> to cache
   * or delay reading (such as by observing on a different thread). If you want to cache or delay
   * reading the items emitted then you must map values through a function which calls
   * {@link String#valueOf} or {@link CharSequence#toString() .toString()} to create a copy.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static InitialValueObservable<TextViewTextChangeEvent> textChangeEvents(
      @NonNull TextView view) {
    checkNotNull(view, "view == null");
    return new TextViewTextChangeEventObservable(view);
  }

  /**
   * Create an observable of before text change events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static InitialValueObservable<TextViewBeforeTextChangeEvent> beforeTextChangeEvents(
      @NonNull TextView view) {
    checkNotNull(view, "view == null");
    return new TextViewBeforeTextChangeEventObservable(view);
  }

  /**
   * Create an observable of after text change events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe using
   * {@link TextView#getEditableText()}.
   */
  @CheckResult @NonNull
  public static InitialValueObservable<TextViewAfterTextChangeEvent> afterTextChangeEvents(
      @NonNull TextView view) {
    checkNotNull(view, "view == null");
    return new TextViewAfterTextChangeEventObservable(view);
  }

  /**
   * An action which sets the text property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setText method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> text(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return view::setText;
  }

  /**
   * An action which sets the text property of {@code view} string resource IDs.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setText method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> textRes(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return view::setText;
  }

  /**
   * An action which sets the error property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setError method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> error(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return view::setError;
  }

  /**
   * An action which sets the error property of {@code view} string resource IDs.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setError method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> errorRes(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return textRes -> view.setError(view.getContext().getResources().getText(textRes));
  }

  /**
   * An action which sets the hint property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setHint method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> hint(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return view::setHint;
  }

  /**
   * An action which sets the hint property of {@code view} string resource IDs.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setHint method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> hintRes(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return view::setHint;
  }

  /**
   * An action which sets the color property of {@code view} with color integer.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setTextColor method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> color(@NonNull TextView view) {
    checkNotNull(view, "view == null");
    return view::setTextColor;
  }

  private RxTextView() {
    throw new AssertionError("No instances.");
  }
}
