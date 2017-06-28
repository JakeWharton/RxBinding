package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RecyclerAdapterDataRangeChangeEvent<T extends Adapter<? extends ViewHolder>>
    extends RecyclerAdapterDataEvent<T> {
  @CheckResult @NonNull
  public static <O extends Adapter<? extends ViewHolder>> RecyclerAdapterDataEvent<O> create(
      @NonNull O adapter, int positionStart, int itemCount, @Nullable Object payload) {
    return new AutoValue_RecyclerAdapterDataRangeChangeEvent(adapter, positionStart, itemCount,
        payload);
  }

  RecyclerAdapterDataRangeChangeEvent() {
  }

  @NonNull public abstract T adapter();
  public abstract int positionStart();
  public abstract int itemCount();
  @Nullable public abstract Object payload();
}
