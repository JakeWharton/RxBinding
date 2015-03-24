package rx.android.view;

import android.content.Context;
import android.view.View;

/**
 * A click event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class ViewClickEvent extends ViewEvent<View> {
  public static ViewClickEvent create(View view, long timestamp) {
    return new ViewClickEvent(view, timestamp);
  }

  private ViewClickEvent(View view, long timestamp) {
    super(view, timestamp);
  }

  @Override public String toString() {
    return "ViewClickEvent{view=" + view() + ", timestamp=" + timestamp() + '}';
  }
}
