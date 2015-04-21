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
  public static ViewFocusChangeEvent create(View view, boolean hasFocus) {
    return new ViewFocusChangeEvent(view, hasFocus);
  }

  private final boolean hasFocus;

  private ViewFocusChangeEvent(View view, boolean hasFocus) {
    super(view);
    this.hasFocus = hasFocus;
  }

  public boolean hasFocus() {
    return hasFocus;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + (hasFocus ? 1 : 0);
    return result;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewFocusChangeEvent)) return false;
    ViewFocusChangeEvent other = (ViewFocusChangeEvent) o;
    return other.view() == view() && other.hasFocus == hasFocus;
  }

  @Override public String toString() {
    return "ViewFocusChangeEvent{hasFocus=" + hasFocus + ", view=" + view() + '}';
  }
}
