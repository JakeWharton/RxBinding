package com.jakewharton.rxbinding2.support.v7.widget;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * A child view attach event on a {@link RecyclerView}.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class RecyclerViewChildAttachEvent extends RecyclerViewChildAttachStateChangeEvent {
  @CheckResult @NonNull
  public static RecyclerViewChildAttachEvent create(@NonNull RecyclerView view,
      @NonNull View child) {
    return new RecyclerViewChildAttachEvent(view, child);
  }

  private RecyclerViewChildAttachEvent(@NonNull RecyclerView view, @NonNull View child) {
    super(view, child);
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof RecyclerViewChildAttachEvent)) return false;
    RecyclerViewChildAttachEvent other = (RecyclerViewChildAttachEvent) o;
    return other.view() == view()
        && other.child() == child();
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + child().hashCode();
    return result;
  }

  @Override public String toString() {
    return "RecyclerViewChildAttachEvent{view="
        + view()
        + ", child="
        + child()
        + '}';
  }
}
