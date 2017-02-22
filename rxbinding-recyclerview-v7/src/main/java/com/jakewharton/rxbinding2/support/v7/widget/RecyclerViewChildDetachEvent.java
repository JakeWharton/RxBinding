package com.jakewharton.rxbinding2.support.v7.widget;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.auto.value.AutoValue;

/**
 * A child view detach event on a {@link RecyclerView}.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class RecyclerViewChildDetachEvent extends RecyclerViewChildAttachStateChangeEvent {
  @CheckResult @NonNull
  public static RecyclerViewChildDetachEvent create(@NonNull RecyclerView view,
      @NonNull View child) {
    return new AutoValue_RecyclerViewChildDetachEvent(view, child);
  }

  RecyclerViewChildDetachEvent() {
  }
}
