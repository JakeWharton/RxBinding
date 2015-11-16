package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SeekBar;
import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public final class RxSeekBar {
  /**
   * Create an observable of progress value changes on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<Integer> changes(@NonNull SeekBar view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SeekBarChangeOnSubscribe(view, null));
  }

  /**
   * Create an observable of progress value changes on {@code view} that were made only from the
   * user.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<Integer> userChanges(@NonNull SeekBar view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SeekBarChangeOnSubscribe(view, true));
  }

  /**
   * Create an observable of progress value changes on {@code view} that were made only from the
   * system.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<Integer> systemChanges(@NonNull SeekBar view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SeekBarChangeOnSubscribe(view, false));
  }

  /**
   * Create an observable of progress change events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<SeekBarChangeEvent> changeEvents(@NonNull SeekBar view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SeekBarChangeEventOnSubscribe(view));
  }

  private RxSeekBar() {
    throw new AssertionError("No instances.");
  }
}
