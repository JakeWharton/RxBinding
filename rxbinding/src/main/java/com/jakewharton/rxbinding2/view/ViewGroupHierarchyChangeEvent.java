package com.jakewharton.rxbinding2.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewGroupHierarchyChangeEvent extends ViewEvent<ViewGroup> {
  private final View child;

  ViewGroupHierarchyChangeEvent(@NonNull ViewGroup view, View child) {
    super(view);
    this.child = child;
  }

  /** The child from which this event occurred. */
  @NonNull
  public final View child() {
    return child;
  }
}
