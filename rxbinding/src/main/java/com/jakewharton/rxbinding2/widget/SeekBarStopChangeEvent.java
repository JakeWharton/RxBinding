package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SeekBar;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SeekBarStopChangeEvent extends SeekBarChangeEvent {
  @CheckResult @NonNull
  public static SeekBarStopChangeEvent create(@NonNull SeekBar view) {
    return new AutoValue_SeekBarStopChangeEvent(view);
  }

  SeekBarStopChangeEvent() {
  }
}
