package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

public final class ViewTouchEvent extends ViewEvent<View> {
  @CheckResult @NonNull
  public static ViewTouchEvent create(@NonNull View view, @NonNull MotionEvent motionEvent) {
    return new ViewTouchEvent(view, motionEvent);
  }

  private final MotionEvent motionEvent;

  private ViewTouchEvent(@NonNull View view, @NonNull MotionEvent motionEvent) {
    super(view);
    this.motionEvent = motionEvent;
  }

  @NonNull
  public MotionEvent motionEvent() {
    return motionEvent;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewTouchEvent)) return false;
    ViewTouchEvent other = (ViewTouchEvent) o;
    return other.view() == view() && other.motionEvent.equals(motionEvent);
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + motionEvent.hashCode();
    return result;
  }

  @Override public String toString() {
    return "ViewTouchEvent{view=" + view() + ", motionEvent=" + motionEvent + '}';
  }
}
