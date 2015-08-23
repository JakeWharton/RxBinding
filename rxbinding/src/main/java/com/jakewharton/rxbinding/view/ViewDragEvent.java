package com.jakewharton.rxbinding.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.DragEvent;
import android.view.View;

/**
 * A drag event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class ViewDragEvent extends ViewEvent<View> {
  @CheckResult @NonNull
  public static ViewDragEvent create(@NonNull View view, @NonNull DragEvent dragEvent) {
    return new ViewDragEvent(view, dragEvent);
  }

  private final DragEvent dragEvent;

  private ViewDragEvent(@NonNull View view, @NonNull DragEvent dragEvent) {
    super(view);
    this.dragEvent = dragEvent;
  }

  @NonNull
  public DragEvent dragEvent() {
    return dragEvent;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewDragEvent)) return false;
    ViewDragEvent other = (ViewDragEvent) o;
    return other.view() == view() && other.dragEvent.equals(dragEvent);
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + dragEvent.hashCode();
    return result;
  }

  @Override public String toString() {
    return "ViewDragEvent{dragEvent=" + dragEvent + ", view=" + view() + '}';
  }
}
