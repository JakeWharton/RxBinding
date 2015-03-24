package rx.android.view;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;

/**
 * A drag event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class ViewDragEvent extends ViewEvent<View> {
  public static ViewDragEvent create(View view, long timestamp, DragEvent dragEvent) {
    return new ViewDragEvent(view, timestamp, dragEvent);
  }

  private final DragEvent dragEvent;

  private ViewDragEvent(View view, long timestamp, DragEvent dragEvent) {
    super(view, timestamp);
    this.dragEvent = dragEvent;
  }

  public DragEvent dragEvent() {
    return dragEvent;
  }

  // TODO hashCode
  // TODO equals
  // TODO toString
}
