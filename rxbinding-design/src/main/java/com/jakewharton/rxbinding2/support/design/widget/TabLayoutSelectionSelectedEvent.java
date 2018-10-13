package com.jakewharton.rxbinding2.support.design.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.support.design.widget.TabLayout;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TabLayoutSelectionSelectedEvent extends TabLayoutSelectionEvent {
  @CheckResult @NonNull
  public static TabLayoutSelectionSelectedEvent create(@NonNull TabLayout view,
      @NonNull TabLayout.Tab tab) {
    return new AutoValue_TabLayoutSelectionSelectedEvent(view, tab);
  }

  TabLayoutSelectionSelectedEvent() {
  }
}
