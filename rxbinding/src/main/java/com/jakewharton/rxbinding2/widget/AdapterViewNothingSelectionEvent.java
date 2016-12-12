package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AdapterView;

public final class AdapterViewNothingSelectionEvent extends AdapterViewSelectionEvent {
  @CheckResult @NonNull
  public static AdapterViewSelectionEvent create(@NonNull AdapterView<?> view) {
    return new AdapterViewNothingSelectionEvent(view);
  }

  private AdapterViewNothingSelectionEvent(@NonNull AdapterView<?> view) {
    super(view);
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof AdapterViewNothingSelectionEvent)) return false;
    AdapterViewNothingSelectionEvent other = (AdapterViewNothingSelectionEvent) o;
    return other.view() == view();
  }

  @Override public int hashCode() {
    return view().hashCode();
  }

  @Override public String toString() {
    return "AdapterViewNothingSelectionEvent{view=" + view() + '}';
  }
}
