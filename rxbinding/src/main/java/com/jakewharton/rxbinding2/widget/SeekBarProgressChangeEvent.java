package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SeekBar;

public final class SeekBarProgressChangeEvent extends SeekBarChangeEvent {
  @CheckResult @NonNull
  public static SeekBarProgressChangeEvent create(@NonNull SeekBar view, int progress,
      boolean fromUser) {
    return new SeekBarProgressChangeEvent(view, progress, fromUser);
  }

  private final int progress;
  private final boolean fromUser;

  private SeekBarProgressChangeEvent(@NonNull SeekBar view, int progress, boolean fromUser) {
    super(view);
    this.progress = progress;
    this.fromUser = fromUser;
  }

  public int progress() {
    return progress;
  }

  public boolean fromUser() {
    return fromUser;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof SeekBarProgressChangeEvent)) return false;
    SeekBarProgressChangeEvent other = (SeekBarProgressChangeEvent) o;
    return other.view() == view()
        && other.progress == progress
        && other.fromUser == fromUser;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + progress;
    result = result * 37 + (fromUser ? 1 : 0);
    return result;
  }

  @Override public String toString() {
    return "SeekBarProgressChangeEvent{view="
        + view()
        + ", progress="
        + progress
        + ", fromUser="
        + fromUser
        + '}';
  }
}
