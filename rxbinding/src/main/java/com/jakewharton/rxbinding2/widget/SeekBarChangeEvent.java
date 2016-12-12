package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.widget.SeekBar;
import com.jakewharton.rxbinding2.view.ViewEvent;

public abstract class SeekBarChangeEvent extends ViewEvent<SeekBar> {
  SeekBarChangeEvent(@NonNull SeekBar view) {
    super(view);
  }
}
