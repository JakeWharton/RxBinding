package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AbsListView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class AbsListViewScrollEvent extends ViewEvent<AbsListView> {

  @CheckResult @NonNull
  public static AbsListViewScrollEvent create(AbsListView listView, int scrollState,
      int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    return new AbsListViewScrollEvent(listView, scrollState, firstVisibleItem, visibleItemCount,
        totalItemCount);
  }

  private final int scrollState;
  private final int firstVisibleItem;
  private final int visibleItemCount;
  private final int totalItemCount;

  private AbsListViewScrollEvent(@NonNull AbsListView view, int scrollState, int firstVisibleItem,
      int visibleItemCount, int totalItemCount) {
    super(view);
    this.scrollState = scrollState;
    this.firstVisibleItem = firstVisibleItem;
    this.visibleItemCount = visibleItemCount;
    this.totalItemCount = totalItemCount;
  }

  public int scrollState() {
    return scrollState;
  }

  public int firstVisibleItem() {
    return firstVisibleItem;
  }

  public int visibleItemCount() {
    return visibleItemCount;
  }

  public int totalItemCount() {
    return totalItemCount;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AbsListViewScrollEvent that = (AbsListViewScrollEvent) o;

    if (scrollState != that.scrollState) return false;
    if (firstVisibleItem != that.firstVisibleItem) return false;
    if (visibleItemCount != that.visibleItemCount) return false;
    return totalItemCount == that.totalItemCount;
  }

  @Override public int hashCode() {
    int result = scrollState;
    result = 31 * result + firstVisibleItem;
    result = 31 * result + visibleItemCount;
    result = 31 * result + totalItemCount;
    return result;
  }

  @Override public String toString() {
    return "AbsListViewScrollEvent{"
        + "scrollState="
        + scrollState
        + ", firstVisibleItem="
        + firstVisibleItem
        + ", visibleItemCount="
        + visibleItemCount
        + ", totalItemCount="
        + totalItemCount
        + '}';
  }
}
