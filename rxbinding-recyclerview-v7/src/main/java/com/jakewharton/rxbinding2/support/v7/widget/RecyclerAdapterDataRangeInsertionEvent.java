package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RecyclerAdapterDataRangeInsertionEvent<T extends Adapter<? extends ViewHolder>>
    extends RecyclerAdapterDataEvent<T> {
  @CheckResult @NonNull
  public static <O extends Adapter<? extends ViewHolder>> RecyclerAdapterDataEvent<O> create(
      @NonNull O adapter, int positionStart, int itemCount) {
    return new AutoValue_RecyclerAdapterDataRangeInsertionEvent(adapter, positionStart, itemCount);
  }

  RecyclerAdapterDataRangeInsertionEvent() {
  }

  @NonNull public abstract T adapter();
  public abstract int positionStart();
  public abstract int itemCount();
}
