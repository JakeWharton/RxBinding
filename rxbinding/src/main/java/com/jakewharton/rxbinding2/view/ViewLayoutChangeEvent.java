package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * A layout-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class ViewLayoutChangeEvent extends ViewEvent<View> {
  @CheckResult @NonNull
  public static ViewLayoutChangeEvent create(@NonNull View view, int left, int top, int right,
      int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
    return new ViewLayoutChangeEvent(view, left, top, right, bottom,
        oldLeft, oldTop, oldRight, oldBottom);
  }

  private final int left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom;

  private ViewLayoutChangeEvent(@NonNull View view, int left, int top, int right, int bottom,
      int oldLeft, int oldTop, int oldRight, int oldBottom) {
    super(view);
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.oldLeft = oldLeft;
    this.oldTop = oldTop;
    this.oldRight = oldRight;
    this.oldBottom = oldBottom;
  }

  public int left() {
    return left;
  }

  public int top() {
    return top;
  }

  public int right() {
    return right;
  }

  public int bottom() {
    return bottom;
  }

  public int oldLeft() {
    return oldLeft;
  }

  public int oldTop() {
    return oldTop;
  }

  public int oldRight() {
    return oldRight;
  }

  public int oldBottom() {
    return oldBottom;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + left;
    result = result * 37 + top;
    result = result * 37 + right;
    result = result * 37 + bottom;
    result = result * 37 + oldLeft;
    result = result * 37 + oldTop;
    result = result * 37 + oldRight;
    result = result * 37 + oldBottom;
    return result;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewLayoutChangeEvent)) return false;
    ViewLayoutChangeEvent other = (ViewLayoutChangeEvent) o;
    return other.view() == view() && other.left == left && other.top == top && other.right == right
        && other.bottom == bottom && other.oldLeft == oldLeft && other.oldTop == oldTop
        && other.oldRight == oldRight && other.oldBottom == oldBottom;
  }

  @Override public String toString() {
    return "ViewLayoutChangeEvent{left="
        + left
        + ", top="
        + top
        + ", right="
        + right
        + ", bottom="
        + bottom
        + ", oldLeft="
        + oldLeft
        + ", oldTop="
        + oldTop
        + ", oldRight="
        + oldRight
        + ", oldBottom="
        + oldBottom
        + '}';
  }
}
