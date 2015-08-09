package com.jakewharton.rxbinding.view;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * A view attach event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
public final class ViewAttachEvent extends ViewEvent<View> {

  public static final int ATTACH = 0;
  public static final int DETACH = 1;

  @IntDef({ATTACH, DETACH})
  public @interface Kind {}

  public static ViewAttachEvent create(@NonNull View view, @Kind int kind) {
    return new ViewAttachEvent(view, kind);
  }

  @Kind private final int kind;

  private ViewAttachEvent(View view, @Kind int kind) {
    super(view);
    this.kind = kind;
  }

  @Kind public int kind() {
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
    result = result * 37 + kind();
    return result;
  }

  @Override public String toString() {
    return "ViewAttachEvent{view="
        + view()
        + ", kind="
        + (kind == ATTACH ? "ATTACH" : "DETACH")
        + '}';
  }
}
