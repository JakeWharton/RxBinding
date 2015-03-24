package rx.android.view;

import android.content.Context;
import android.view.View;

/**
 * A long-click event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class ViewLongClickEvent extends ViewEvent<View> {
  public static ViewLongClickEvent create(View view, long timestamp) {
    return new ViewLongClickEvent(view, timestamp);
  }

  private ViewLongClickEvent(View view, long timestamp) {
    super(view, timestamp);
  }

  @Override public String toString() {
    return "ViewLongClickEvent{view=" + view() + ", timestamp=" + timestamp() + '}';
  }
}
