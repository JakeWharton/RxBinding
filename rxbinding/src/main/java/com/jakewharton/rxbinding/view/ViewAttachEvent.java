package com.jakewharton.rxbinding.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * A view attach event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
public final class ViewAttachEvent extends ViewEvent<View> {

  public enum Kind {
    ATTACH, DETACH
  }

  public static ViewAttachEvent create(@NonNull View view, Kind kind) {
    return new ViewAttachEvent(view, kind);
  }

  private final Kind kind;

  private ViewAttachEvent(View view, Kind kind) {
    super(view);
    this.kind = kind;
  }

  public Kind kind() {
    return kind;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewAttachEvent)) return false;
    ViewAttachEvent other = (ViewAttachEvent) o;
    return other.view() == view()
        && kind() == other.kind();
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + kind().hashCode();
    return result;
  }

  @Override public String toString() {
    return "ViewAttachEvent{view="
        + view()
        + ", kind="
        + kind()
        + '}';
  }
}
