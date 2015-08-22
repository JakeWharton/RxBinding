package com.jakewharton.rxbinding.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * A long-click event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class ViewLongClickEvent extends ViewEvent<View> {
  @CheckResult @NonNull
  public static ViewLongClickEvent create(@NonNull View view) {
    return new ViewLongClickEvent(view);
  }

  private ViewLongClickEvent(@NonNull View view) {
    super(view);
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewLongClickEvent)) return false;
    ViewLongClickEvent other = (ViewLongClickEvent) o;
    return other.view() == view();
  }

  @Override public int hashCode() {
    return view().hashCode();
  }

  @Override public String toString() {
    return "ViewLongClickEvent{view=" + view() + '}';
  }
}
