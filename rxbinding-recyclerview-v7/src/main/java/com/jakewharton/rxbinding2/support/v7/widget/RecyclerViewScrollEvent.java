package com.jakewharton.rxbinding2.support.v7.widget;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.google.auto.value.AutoValue;

/**
 * A scroll event on a recyclerView.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the recyclerView. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class RecyclerViewScrollEvent {
  @CheckResult @NonNull
  public static RecyclerViewScrollEvent create(@NonNull RecyclerView recyclerView, int dx, int dy) {
    return new AutoValue_RecyclerViewScrollEvent(recyclerView, dx, dy);
  }

  RecyclerViewScrollEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract RecyclerView view();
  public abstract int dx();
  public abstract int dy();
}
