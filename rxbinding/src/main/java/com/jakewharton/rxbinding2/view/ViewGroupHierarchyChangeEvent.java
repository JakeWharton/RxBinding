package com.jakewharton.rxbinding2.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public abstract class ViewGroupHierarchyChangeEvent {
  ViewGroupHierarchyChangeEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract ViewGroup view();

  /** The child from which this event occurred. */
  @NonNull public abstract View child();
}
