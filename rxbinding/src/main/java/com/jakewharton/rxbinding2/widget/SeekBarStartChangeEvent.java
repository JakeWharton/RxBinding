package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SeekBar;

public final class SeekBarStartChangeEvent extends SeekBarChangeEvent {
  @CheckResult @NonNull
  public static SeekBarStartChangeEvent create(@NonNull SeekBar view) {
    return new SeekBarStartChangeEvent(view);
  }

  private SeekBarStartChangeEvent(@NonNull SeekBar view) {
    super(view);
  }

  @Override public boolean equals(Object o) {
    return o instanceof SeekBarStartChangeEvent && ((SeekBarStartChangeEvent) o).view() == view();
  }

  @Override public int hashCode() {
    return view().hashCode();
  }

  @Override public String toString() {
    return "SeekBarStartChangeEvent{view=" + view() + '}';
  }
}
