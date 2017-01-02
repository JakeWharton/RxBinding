package com.jakewharton.rxbinding2.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * A scroll-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link android.content.Context}.
 */
public final class ViewScrollChangeEvent extends ViewEvent<View> {
  @CheckResult @NonNull
  public static ViewScrollChangeEvent create(@NonNull View view, int scrollX, int scrollY,
      int oldScrollX, int oldScrollY) {
    return new ViewScrollChangeEvent(view, scrollX, scrollY, oldScrollX, oldScrollY);
  }

  private final int scrollX, scrollY, oldScrollX, oldScrollY;

  protected ViewScrollChangeEvent(@NonNull View view, int scrollX, int scrollY, int oldScrollX,
      int oldScrollY) {
    super(view);
    this.scrollX = scrollX;
    this.scrollY = scrollY;
    this.oldScrollX = oldScrollX;
    this.oldScrollY = oldScrollY;
  }

  public int scrollX() {
    return scrollX;
  }

  public int scrollY() {
    return scrollY;
  }

  public int oldScrollX() {
    return oldScrollX;
  }

  public int oldScrollY() {
    return oldScrollY;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + scrollX;
    result = result * 37 + scrollY;
    result = result * 37 + oldScrollX;
    result = result * 37 + oldScrollY;
    return result;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewScrollChangeEvent)) return false;
    ViewScrollChangeEvent other = (ViewScrollChangeEvent) o;
    return other.view() == view() && other.scrollX == scrollX && other.scrollY == scrollY
        && other.oldScrollX == oldScrollX && other.oldScrollY == oldScrollY;
  }

  @Override public String toString() {
    return "ViewScrollChangeEvent{scrollX=" + scrollX + ", scrollY=" + scrollY + ", oldScrollX="
        + oldScrollX + ", oldScrollY=" + oldScrollY + '}';
  }
}
