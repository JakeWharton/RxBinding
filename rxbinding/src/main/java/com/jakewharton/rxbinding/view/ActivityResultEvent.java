package com.jakewharton.rxbinding.view;

import android.content.Intent;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;

public class ActivityResultEvent {

  private final int resultCode;
  private final Intent data;

  @CheckResult
  public static ActivityResultEvent create(int resultCode, Intent data) {
    return new ActivityResultEvent(resultCode, data);
  }

  private ActivityResultEvent(int resultCode, Intent data) {
    this.resultCode = resultCode;
    this.data = data;
  }

  public int getResultCode() {
    return resultCode;
  }

  @Nullable public Intent getData() {
    return data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ActivityResultEvent that = (ActivityResultEvent) o;

    return resultCode == that.resultCode
        && (data != null ? data.equals(that.data) : that.data == null);
  }

  @Override public int hashCode() {
    int result = resultCode;
    result = 31 * result + (data != null ? data.hashCode() : 0);
    return result;
  }

  @Override public String toString() {
    return "ActivityResultEvent{"
        + "resultCode="
        + resultCode
        + ", data="
        + data
        + '}';
  }
}
