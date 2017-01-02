package com.jakewharton.rxbinding2.widget;

import android.support.annotation.NonNull;
import android.widget.AdapterView;
import com.jakewharton.rxbinding2.view.ViewEvent;

public abstract class AdapterViewSelectionEvent extends ViewEvent<AdapterView<?>> {
  AdapterViewSelectionEvent(@NonNull AdapterView<?> view) {
    super(view);
  }
}
