package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.widget.TextView;
import com.google.auto.value.AutoValue;

/**
 * An after text-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class TextViewAfterTextChangeEvent {
  @CheckResult @NonNull
  public static TextViewAfterTextChangeEvent create(@NonNull TextView view,
      @Nullable Editable editable) {
    return new AutoValue_TextViewAfterTextChangeEvent(view, editable);
  }

  TextViewAfterTextChangeEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract TextView view();
  @Nullable public abstract Editable editable();
}
