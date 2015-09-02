package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * A child view add event on a viewgroup.
 */

public class ViewGroupHierarchyChildViewAddEvent extends ViewGroupHierarchyChangeEvent {
  @CheckResult @NonNull
  public static ViewGroupHierarchyChildViewAddEvent create(@NonNull ViewGroup viewGroup, View child) {
    return new ViewGroupHierarchyChildViewAddEvent(viewGroup, child);
  }

  private ViewGroupHierarchyChildViewAddEvent(@NonNull ViewGroup viewGroup, View child) {
    super(viewGroup, child);
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewGroupHierarchyChildViewAddEvent)) return false;
    ViewGroupHierarchyChildViewAddEvent other = (ViewGroupHierarchyChildViewAddEvent) o;
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
    return "ViewGroupHierarchyChildViewAddEvent{view="
        + view()
        + ", child="
        + child()
        + '}';
  }
}
