package com.jakewharton.rxbinding.support.v7.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.view.ViewEvent;

/**
 * A scroll change event on a recyclerView.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the recyclerView. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
public final class RecyclerViewScrollStateChangeEvent extends ViewEvent<RecyclerView> {
  public static RecyclerViewScrollStateChangeEvent create(@NonNull RecyclerView recyclerView, int newState) {
    return new RecyclerViewScrollStateChangeEvent(recyclerView, newState);
  }

  private final RecyclerView recyclerView;
  private final int newState;

  private RecyclerViewScrollStateChangeEvent(RecyclerView recyclerView, int newState) {
    super(recyclerView);
    this.recyclerView = recyclerView;
    this.newState = newState;
  }

  public RecyclerView recyclerView() {
    return recyclerView;
  }

  public int newState() {
    return newState;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof RecyclerViewScrollStateChangeEvent)) return false;
    RecyclerViewScrollStateChangeEvent other = (RecyclerViewScrollStateChangeEvent) o;
    return other.view() == view()
        && recyclerView.equals(other.recyclerView)
        && newState == other.newState;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + recyclerView.hashCode();
    result = result * 37 + newState;
    return result;
  }

  @Override public String toString() {
    return "RecyclerViewScrollChangeEvent{view="
        + view()
        + ", recyclerView="
        + recyclerView
        + ", newState="
        + newState
        + '}';
  }
}
