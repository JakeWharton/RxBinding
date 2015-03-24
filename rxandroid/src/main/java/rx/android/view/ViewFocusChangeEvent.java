package rx.android.view;

import android.content.Context;
import android.view.View;

/**
 * A focus-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class ViewFocusChangeEvent extends ViewEvent<View> {
  public static ViewFocusChangeEvent create(View view, long timestamp, boolean hasFocus) {
    return new ViewFocusChangeEvent(view, timestamp, hasFocus);
  }

  private final boolean hasFocus;

  private ViewFocusChangeEvent(View view, long timestamp, boolean hasFocus) {
    super(view, timestamp);
    this.hasFocus = hasFocus;
  }

  public boolean hasFocus() {
    return hasFocus;
  }

  @Override public int hashCode() {
    int result = super.hashCode();
    result = result * 37 + (hasFocus ? 1 : 0);
    return result;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewFocusChangeEvent)) return false;
    ViewFocusChangeEvent other = (ViewFocusChangeEvent) o;
    return super.equals(other)
        && hasFocus == other.hasFocus;
  }

  @Override public String toString() {
    return "ViewFocusChangeEvent{hasFocus="
        + hasFocus
        + ", view="
        + view()
        + ", timestamp="
        + timestamp()
        + '}';
  }
}
