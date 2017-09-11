package com.jakewharton.rxbinding2.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.google.auto.value.AutoValue;

/**
 * A child view add event on a {@link ViewGroup}.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class ViewGroupHierarchyChildViewAddEvent extends ViewGroupHierarchyChangeEvent {
  @CheckResult @NonNull
  public static ViewGroupHierarchyChildViewAddEvent create(@NonNull ViewGroup viewGroup,
      @NonNull View child) {
    return new AutoValue_ViewGroupHierarchyChildViewAddEvent(viewGroup, child);
  }

  ViewGroupHierarchyChildViewAddEvent() {
  }
}
