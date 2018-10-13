package com.jakewharton.rxbinding2.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.widget.TextSwitcher;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

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
   *
   * @deprecated Use view::setText method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> text(@NonNull TextSwitcher view) {
    checkNotNull(view, "view == null");
    return view::setText;
  }

  /**
   * An action which sets the current text property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setCurrentText method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> currentText(@NonNull TextSwitcher view) {
    checkNotNull(view, "view == null");
    return view::setCurrentText;
  }

  private RxTextSwitcher() {
    throw new AssertionError("No instances.");
  }
}
