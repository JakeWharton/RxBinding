package rx.android.widget;

import android.content.Context;
import android.widget.CompoundButton;
import rx.android.view.ViewEvent;

/**
 * A checked-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 */
public final class CompoundButtonCheckedChangeEvent extends ViewEvent<CompoundButton> {
  public static CompoundButtonCheckedChangeEvent create(CompoundButton view, long timestamp,
      boolean isChecked) {
    return new CompoundButtonCheckedChangeEvent(view, timestamp, isChecked);
  }

  private final boolean isChecked;

  private CompoundButtonCheckedChangeEvent(CompoundButton view, long timestamp, boolean isChecked) {
    super(view, timestamp);
    this.isChecked = isChecked;
  }

  public boolean isChecked() {
    return isChecked;
  }

  @Override public int hashCode() {
    int result = super.hashCode();
    result = result * 37 + (isChecked ? 1 : 0);
    return result;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof CompoundButtonCheckedChangeEvent)) return false;
    CompoundButtonCheckedChangeEvent other = (CompoundButtonCheckedChangeEvent) o;
    return super.equals(other)
        && isChecked == other.isChecked;
  }

  @Override public String toString() {
    return "CompoundButtonCheckedChangeEvent{isChecked="
        + isChecked
        + ", view="
        + view()
        + ", timestamp="
        + timestamp()
        + '}';
  }
}
