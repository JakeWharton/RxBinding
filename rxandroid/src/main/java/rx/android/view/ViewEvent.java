package rx.android.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import rx.android.plugins.RxAndroidClockHook;
import rx.android.plugins.RxAndroidPlugins;

/**
 * A timestamp and target view on which an event occurred (e.g., click).
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public abstract class ViewEvent<T extends View> {
  @NonNull private final T view;
  private final long timestamp;

  protected ViewEvent(@NonNull T view, long timestamp) {
    this.view = view;
    this.timestamp = timestamp;
  }

  /** The view from which this event occurred. */
  public @NonNull T view() {
    return view;
  }

  /**
   * Timestamp (milliseconds) at which the event occurred.
   * <p>
   * This value is populated by the {@link RxAndroidClockHook} obtained from {@link
   * RxAndroidPlugins} which is a monotonically increasing clock in milliseconds and therefore is
   * only useful when comparing multiple values relative to each other.
   */
  public long timestamp() {
    return timestamp;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ViewEvent)) return false;
    ViewEvent other = (ViewEvent) o;
    return view == other.view && timestamp == other.timestamp;
  }

  @Override public int hashCode() {
    final long timestamp = this.timestamp;
    return view.hashCode() * 37 + (int) (timestamp ^ (timestamp >>> 32));
  }
}
