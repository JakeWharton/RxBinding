package com.jakewharton.rxbinding2.support.v7.widget;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.auto.value.AutoValue;

/**
 * A child view attach event on a {@link RecyclerView}.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
@AutoValue
public abstract class RecyclerViewChildAttachEvent extends RecyclerViewChildAttachStateChangeEvent {
  @CheckResult @NonNull
  public static RecyclerViewChildAttachEvent create(@NonNull RecyclerView view,
      @NonNull View child) {
    return new AutoValue_RecyclerViewChildAttachEvent(view, child);
  }

  RecyclerViewChildAttachEvent() {
  }
}
