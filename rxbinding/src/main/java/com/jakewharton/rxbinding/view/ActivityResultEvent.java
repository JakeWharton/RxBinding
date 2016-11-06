package com.jakewharton.rxbinding.view;

import android.content.Intent;
import android.support.annotation.Nullable;

public class ActivityResultEvent {

  private final int resultCode;
  private final Intent data;

  public ActivityResultEvent(int resultCode, Intent data) {
    this.resultCode = resultCode;
    this.data = data;
  }

  public int getResultCode() {
    return resultCode;
  }

  @Nullable public Intent getData() {
    return data;
  }

}
