package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * A view attach event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
public abstract class ViewAttachEvent {
  ViewAttachEvent() {
  }

  /** The view from which this event occurred. */
  @NonNull public abstract View view();
}
