package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jakewharton.rxbinding2.view.ViewEvent;

public abstract class RecyclerViewChildAttachStateChangeEvent extends ViewEvent<RecyclerView> {
  private final View child;

  RecyclerViewChildAttachStateChangeEvent(@NonNull RecyclerView view, @NonNull View child) {
    super(view);
    this.child = child;
  }

  /** The child from which this event occurred. */
  @NonNull public final View child() {
    return child;
  }
}
