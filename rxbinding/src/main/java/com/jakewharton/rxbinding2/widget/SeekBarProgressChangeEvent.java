package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SeekBar;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SeekBarProgressChangeEvent extends SeekBarChangeEvent {
  @CheckResult @NonNull
  public static SeekBarProgressChangeEvent create(@NonNull SeekBar view, int progress,
      boolean fromUser) {
    return new AutoValue_SeekBarProgressChangeEvent(view, progress, fromUser);
  }

  SeekBarProgressChangeEvent() {
  }

  public abstract int progress();
  public abstract boolean fromUser();
}
