package com.jakewharton.rxbinding2.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TabLayoutSelectionUnselectedEvent extends TabLayoutSelectionEvent {
  @CheckResult @NonNull
  public static TabLayoutSelectionUnselectedEvent create(@NonNull TabLayout view,
      @NonNull TabLayout.Tab tab) {
    return new AutoValue_TabLayoutSelectionUnselectedEvent(view, tab);
  }

  TabLayoutSelectionUnselectedEvent() {
  }
}
