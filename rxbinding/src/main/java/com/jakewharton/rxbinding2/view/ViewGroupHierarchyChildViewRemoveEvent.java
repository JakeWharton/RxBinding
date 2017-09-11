package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.google.auto.value.AutoValue;

/**
 * A child view remove event on a {@link ViewGroup}.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class ViewGroupHierarchyChildViewRemoveEvent extends ViewGroupHierarchyChangeEvent {
  @CheckResult @NonNull
  public static ViewGroupHierarchyChildViewRemoveEvent create(@NonNull ViewGroup viewGroup,
      @NonNull View child) {
    return new AutoValue_ViewGroupHierarchyChildViewRemoveEvent(viewGroup, child);
  }

  ViewGroupHierarchyChildViewRemoveEvent() {
  }
}
