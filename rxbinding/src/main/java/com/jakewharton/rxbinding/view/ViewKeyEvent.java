package com.jakewharton.rxbinding.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;

/**
 * A key event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class ViewKeyEvent extends ViewEvent<View> {
  private final int keyCode;
  private final KeyEvent keyEvent;

  ViewKeyEvent(@NonNull View view, int keyCode, KeyEvent keyEvent) {
    super(view);
    this.keyCode = keyCode;
    this.keyEvent = keyEvent;
  }

  @NonNull public Integer keyCode() {
    return keyCode;
  }

  @NonNull public KeyEvent keyEvent() {
    return keyEvent;
  }
}
