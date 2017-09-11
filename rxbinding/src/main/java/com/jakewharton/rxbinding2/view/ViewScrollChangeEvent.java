package com.jakewharton.rxbinding2.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.auto.value.AutoValue;

/**
 * A scroll-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link android.content.Context}.
 */
@AutoValue
public abstract class ViewScrollChangeEvent {
  @CheckResult @NonNull
  public static ViewScrollChangeEvent create(@NonNull View view, int scrollX, int scrollY,
      int oldScrollX, int oldScrollY) {
    return new AutoValue_ViewScrollChangeEvent(view, scrollX, scrollY, oldScrollX, oldScrollY);
  }

  ViewScrollChangeEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract View view();
  public abstract int scrollX();
  public abstract int scrollY();
  public abstract int oldScrollX();
  public abstract int oldScrollY();
}
