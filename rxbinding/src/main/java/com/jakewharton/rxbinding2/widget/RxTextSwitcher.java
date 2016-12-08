package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TextSwitcher;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Consumer
 * consumers} for {@link TextSwitcher}.
 */
public final class RxTextSwitcher {
  /**
   * An action which sets the text property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> text(@NonNull final TextSwitcher view) {
    checkNotNull(view, "view == null");
    return new Consumer<CharSequence>() {
      @Override public void accept(CharSequence text) {
        view.setText(text);
      }
    };
  }

  /**
   * An action which sets the current text property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> currentText(@NonNull final TextSwitcher view) {
    checkNotNull(view, "view == null");
    return new Consumer<CharSequence>() {
      @Override public void accept(CharSequence textRes) {
        view.setCurrentText(textRes);
      }
    };
  }

  private RxTextSwitcher() {
    throw new AssertionError("No instances.");
  }
}
