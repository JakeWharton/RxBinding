package com.jakewharton.rxbinding2.support.design.widget;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;

public abstract class TabLayoutSelectionEvent {
  TabLayoutSelectionEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract TabLayout view();
  @NonNull public abstract Tab tab();
}
