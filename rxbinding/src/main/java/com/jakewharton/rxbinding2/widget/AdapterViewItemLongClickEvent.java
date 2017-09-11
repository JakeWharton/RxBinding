package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AdapterViewItemLongClickEvent {
  @CheckResult @NonNull
  public static AdapterViewItemLongClickEvent create(@NonNull AdapterView<?> view,
      @NonNull View clickedView, int position, long id) {
    return new AutoValue_AdapterViewItemLongClickEvent(view, clickedView, position, id);
  }

  AdapterViewItemLongClickEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract AdapterView<?> view();
  @NonNull public abstract View clickedView();
  public abstract int position();
  public abstract long id();
}
