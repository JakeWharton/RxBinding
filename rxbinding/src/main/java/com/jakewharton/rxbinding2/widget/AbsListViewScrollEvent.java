package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AbsListView;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AbsListViewScrollEvent {
  @CheckResult @NonNull
  public static AbsListViewScrollEvent create(AbsListView listView, int scrollState,
      int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    return new AutoValue_AbsListViewScrollEvent(listView, scrollState, firstVisibleItem,
        visibleItemCount, totalItemCount);
  }

  AbsListViewScrollEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract AbsListView view();
  public abstract int scrollState();
  public abstract int firstVisibleItem();
  public abstract int visibleItemCount();
  public abstract int totalItemCount();
}
