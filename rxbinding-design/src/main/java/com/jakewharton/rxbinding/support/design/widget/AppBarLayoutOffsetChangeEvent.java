package com.jakewharton.rxbinding.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class AppBarLayoutOffsetChangeEvent extends ViewEvent<AppBarLayout> {
  @CheckResult @NonNull
  public static AppBarLayoutOffsetChangeEvent create(@NonNull AppBarLayout view,
      int verticalOffset) {
    return new AppBarLayoutOffsetChangeEvent(view, verticalOffset);
  }

  private final int verticalOffset;

  private AppBarLayoutOffsetChangeEvent(@NonNull AppBarLayout view, int verticalOffset) {
    super(view);
    this.verticalOffset = verticalOffset;
  }

  public int verticalOffset() {
    return verticalOffset;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof AppBarLayoutOffsetChangeEvent)) return false;
    AppBarLayoutOffsetChangeEvent other = (AppBarLayoutOffsetChangeEvent) o;
    return view() == other.view() && verticalOffset == other.verticalOffset;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + verticalOffset;
    return result;
  }

  @Override public String toString() {
    return "AppBarLayoutOffsetChangeEvent{view=" + view() + ", verticalOffset=" + verticalOffset
        + '}';
  }
}
