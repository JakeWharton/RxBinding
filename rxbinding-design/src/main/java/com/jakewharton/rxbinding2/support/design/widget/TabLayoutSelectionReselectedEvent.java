package com.jakewharton.rxbinding2.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TabLayoutSelectionReselectedEvent extends TabLayoutSelectionEvent {
  @CheckResult @NonNull
  public static TabLayoutSelectionReselectedEvent create(@NonNull TabLayout view,
      @NonNull TabLayout.Tab tab) {
    return new AutoValue_TabLayoutSelectionReselectedEvent(view, tab);
  }

  TabLayoutSelectionReselectedEvent() {
  }
}
