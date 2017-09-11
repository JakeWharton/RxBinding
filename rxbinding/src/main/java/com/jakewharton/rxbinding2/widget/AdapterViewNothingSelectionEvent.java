package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AdapterView;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AdapterViewNothingSelectionEvent extends AdapterViewSelectionEvent {
  @CheckResult @NonNull
  public static AdapterViewSelectionEvent create(@NonNull AdapterView<?> view) {
    return new AutoValue_AdapterViewNothingSelectionEvent(view);
  }

  AdapterViewNothingSelectionEvent() {
  }
}
