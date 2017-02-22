package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.auto.value.AutoValue;

/**
 * A layout-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class ViewLayoutChangeEvent {
  @CheckResult @NonNull
  public static ViewLayoutChangeEvent create(@NonNull View view, int left, int top, int right,
      int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
    return new AutoValue_ViewLayoutChangeEvent(view, left, top, right, bottom,
        oldLeft, oldTop, oldRight, oldBottom);
  }

  ViewLayoutChangeEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract View view();
  public abstract int left();
  public abstract int top();
  public abstract int right();
  public abstract int bottom();
  public abstract int oldLeft();
  public abstract int oldTop();
  public abstract int oldRight();
  public abstract int oldBottom();
}
