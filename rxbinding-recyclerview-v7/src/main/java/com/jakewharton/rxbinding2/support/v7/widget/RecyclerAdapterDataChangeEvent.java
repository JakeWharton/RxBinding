package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RecyclerAdapterDataChangeEvent<T extends Adapter<? extends ViewHolder>>
    extends RecyclerAdapterDataEvent<T> {
  @CheckResult @NonNull
  public static <O extends Adapter<? extends ViewHolder>> RecyclerAdapterDataEvent<O> create(
      @NonNull O adapter) {
    return new AutoValue_RecyclerAdapterDataChangeEvent(adapter);
  }

  RecyclerAdapterDataChangeEvent() {
  }

  /** The adapter from which the event occurred. */
  @NonNull public abstract T adapter();
}
