package com.jakewharton.rxbinding2.widget;

import androidx.annotation.NonNull;
import android.widget.AdapterView;

public abstract class AdapterViewSelectionEvent {
  AdapterViewSelectionEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract AdapterView<?> view();
}
