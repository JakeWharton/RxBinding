package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * A child view remove event on a viewgroup.
 */
public class ViewGroupHierarchyChildViewRemoveEvent extends ViewGroupHierarchyChangeEvent {
  @CheckResult @NonNull
  public static ViewGroupHierarchyChildViewRemoveEvent create(@NonNull ViewGroup viewGroup, View child) {
    return new ViewGroupHierarchyChildViewRemoveEvent(viewGroup, child);
  }

  private ViewGroupHierarchyChildViewRemoveEvent(@NonNull ViewGroup viewGroup, View child) {
    super(viewGroup, child);
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewGroupHierarchyChildViewRemoveEvent)) return false;
    ViewGroupHierarchyChildViewRemoveEvent other = (ViewGroupHierarchyChildViewRemoveEvent) o;
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
    return "ViewGroupHierarchyChildViewRemoveEvent{view="
        + view()
        + ", child="
        + child()
        + '}';
  }
}
