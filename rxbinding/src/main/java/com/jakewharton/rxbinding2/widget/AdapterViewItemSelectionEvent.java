package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AdapterViewItemSelectionEvent extends AdapterViewSelectionEvent {
  @CheckResult @NonNull
  public static AdapterViewSelectionEvent create(@NonNull AdapterView<?> view,
      @NonNull View selectedView, int position, long id) {
    return new AutoValue_AdapterViewItemSelectionEvent(view, selectedView, position, id);
  }

  AdapterViewItemSelectionEvent() {
  }

  @NonNull public abstract View selectedView();
  public abstract int position();
  public abstract long id();
}
