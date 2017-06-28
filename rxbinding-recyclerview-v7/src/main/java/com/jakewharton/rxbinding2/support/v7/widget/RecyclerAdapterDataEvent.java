package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;

public abstract class RecyclerAdapterDataEvent<T extends Adapter<? extends ViewHolder>> {
  RecyclerAdapterDataEvent() {
  }

  /** The adapter from which the event occurred. */
  @NonNull public abstract T adapter();
}
