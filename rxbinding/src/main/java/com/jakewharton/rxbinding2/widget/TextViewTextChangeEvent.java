package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.google.auto.value.AutoValue;

/**
 * A text-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class TextViewTextChangeEvent {
  @CheckResult @NonNull
  public static TextViewTextChangeEvent create(@NonNull TextView view, @NonNull CharSequence text,
      int start, int before, int count) {
    return new AutoValue_TextViewTextChangeEvent(view, text, start, before, count);
  }

  TextViewTextChangeEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract TextView view();
  @NonNull public abstract CharSequence text();
  public abstract int start();
  public abstract int before();
  public abstract int count();
}
